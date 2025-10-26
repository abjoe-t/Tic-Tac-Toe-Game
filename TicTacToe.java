import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * A complete, self-contained Tic Tac Toe game in Java using Swing for the GUI.
 *
 * This single class handles all game logic and graphical user interface:
 * - Creating the game window and board
 * - Handling player clicks on the board
 * - Displaying game status (e.g., "Player X's turn", "Player O wins!")
 * - Checking for win conditions and draws
 * - Providing a "New Game" button
 */
public class TicTacToe extends JFrame implements ActionListener {

    // The 3x3 logical game board. ' ' (space) represents an empty cell.
    private char[][] board;
    // The current player, 'X' or 'O'. 'X' always starts.
    private char currentPlayer;
    
    // GUI Components
    private JButton[][] buttons; // 3x3 grid of buttons for the board
    private JLabel statusLabel;  // Displays game status messages
    private JButton resetButton; // Button to start a new game

    // A nice, large font for the 'X' and 'O' on the buttons
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 100);

    /**
     * Constructor: Initializes the game logic and sets up the GUI.
     */
    public TicTacToe() {
        // Initialize game logic variables
        board = new char[3][3];
        buttons = new JButton[3][3];
        currentPlayer = 'X';

        // Set up the main window (JFrame)
        setTitle("Tic Tac Toe - Java Swing");
        setSize(450, 550); // Set window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit app when window closes
        setLocationRelativeTo(null); // Center the window on the screen
        setLayout(new BorderLayout()); // Use BorderLayout for main layout

        // Create and add the status label (Top)
        statusLabel = new JLabel("Player X's turn", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(statusLabel, BorderLayout.NORTH);

        // Create the button panel for the 3x3 grid (Center)
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(3, 3));
        add(boardPanel, BorderLayout.CENTER);

        // Create the 3x3 grid of buttons
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton(""); // Create button
                buttons[i][j].setFont(BUTTON_FONT); // Set the large font
                buttons[i][j].setFocusable(false); // Remove dotted border on click
                buttons[i][j].addActionListener(this); // Add this class as the event listener
                boardPanel.add(buttons[i][j]); // Add button to the grid panel
            }
        }

        // Create the "New Game" button (Bottom)
        resetButton = new JButton("New Game");
        resetButton.setFont(new Font("Arial", Font.PLAIN, 20));
        resetButton.addActionListener(this); // Also handled by this class
        add(resetButton, BorderLayout.SOUTH);

        // Initialize the game board for the first game
        initializeGame();
    }

    /**
     * Resets the game to its initial state.
     * Clears the logical board and resets the text and state of all buttons.
     */
    public void initializeGame() {
        currentPlayer = 'X';
        statusLabel.setText("Player X's turn");
        statusLabel.setForeground(Color.BLACK);

        // Loop through all cells
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' '; // Clear logical board
                buttons[i][j].setText(""); // Clear button text
                buttons[i][j].setEnabled(true); // Re-enable the button
                buttons[i][j].setBackground(null); // Reset background color
            }
        }
    }

    /**
     * This method is called whenever a button is clicked.
     * It handles all game logic based on which button was pressed.
     * @param e The ActionEvent object containing details about the click.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Check if the "New Game" button was clicked
        if (e.getSource() == resetButton) {
            initializeGame();
            return; // Stop processing, game is reset
        }

        // Find which grid button was clicked
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (e.getSource() == buttons[i][j]) {
                    // Check if the cell is already taken or game is over
                    if (board[i][j] == ' ') {
                        
                        // 1. Make the move
                        board[i][j] = currentPlayer; // Update logical board
                        buttons[i][j].setText(String.valueOf(currentPlayer)); // Update button text
                        buttons[i][j].setEnabled(false); // Disable button
                        
                        // Set color for X and O
                        if (currentPlayer == 'X') {
                            buttons[i][j].setForeground(Color.RED);
                        } else {
                            buttons[i][j].setForeground(Color.BLUE);
                        }

                        // 2. Check for a winner
                        if (isWinner()) {
                            statusLabel.setText("Player " + currentPlayer + " wins! Congratulations!");
                            statusLabel.setForeground(Color.GREEN);
                            disableAllButtons();
                        }
                        // 3. Check for a draw
                        else if (isBoardFull()) {
                            statusLabel.setText("The game is a draw!");
                            statusLabel.setForeground(Color.ORANGE);
                        }
                        // 4. If no win or draw, switch to the other player
                        else {
                            switchPlayer();
                            statusLabel.setText("Player " + currentPlayer + "'s turn");
                        }
                    }
                    return; // Move processing is done, exit the method
                }
            }
        }
    }
    
    /**
     * Disables all 9 grid buttons, typically after a win or draw.
     */
    private void disableAllButtons() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setEnabled(false);
            }
        }
    }

    /**
     * Switches the current player from 'X' to 'O' or from 'O' to 'X'.
     */
    private void switchPlayer() {
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
    }

    /**
     * Checks if the board is full (i.e., no ' ' cells are left).
     * @return true if the board is full (draw), false otherwise.
     */
    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    return false; // Found an empty cell, board is not full
                }
            }
        }
        return true; // No empty cells found
    }

    /**
     * Checks if the current player has won the game.
     * @return true if the current player has 3 in a row, column, or diagonal.
     */
    private boolean isWinner() {
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == currentPlayer && board[i][1] == currentPlayer && board[i][2] == currentPlayer) {
                return true;
            }
        }
        // Check columns
        for (int j = 0; j < 3; j++) {
            if (board[0][j] == currentPlayer && board[1][j] == currentPlayer && board[2][j] == currentPlayer) {
                return true;
            }
        }
        // Check diagonals
        if (board[0][0] == currentPlayer && board[1][1] == currentPlayer && board[2][2] == currentPlayer) {
            return true;
        }
        if (board[0][2] == currentPlayer && board[1][1] == currentPlayer && board[2][0] == currentPlayer) {
            return true;
        }
        // No win condition met
        return false;
    }

    /**
     * The main method. This is the entry point of the program.
     * It creates and shows the game window on the Event Dispatch Thread (EDT).
     */
    public static void main(String[] args) {
        // Use SwingUtilities.invokeLater to ensure all GUI code runs
        // on the Event Dispatch Thread (EDT), which is Swing's standard practice.
        SwingUtilities.invokeLater(() -> {
            // Create a new game instance
            TicTacToe game = new TicTacToe();
            // Make the game window visible
            game.setVisible(true);
        });
    }
}

