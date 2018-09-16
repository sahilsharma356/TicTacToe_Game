
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.FlowLayout;
import java.net.URL;
 // Import all libraries 
/**
 * A class modelling a tic-tac-toe (noughts and crosses, Xs and Os) game.
 * 
 * @author Sahil Sharma
 * @version December 03, 2017
 */

public class TicTacToe implements ActionListener
{
    public static final String PLAYER_X = "X"; // player using "X"
    public static final String PLAYER_O = "O"; // player using "O"
    public static final String EMPTY = " ";  // empty cell
    public static final String TIE = "T"; // game ended in a tie

    private String player;   // current player (PLAYER_X or PLAYER_O)

    private String winner;   // winner: PLAYER_X, PLAYER_O, TIE, EMPTY = in progress

    private int numFreeSquares; // number of squares still free

    private JButton board[][]; // 3x3 array representing the board

    private ImageIcon img_x; // Image for X
    private ImageIcon img_y; // Image for O
    private ImageIcon img_blank; // Default Image

    private JMenuItem newGame; // menu button for a new game
    private JMenuItem quitItem; //menu button for quitting 

    private JLabel gameUpdate; // updates for game 

    AudioClip event; 

    /** 
     * Constructs a new Tic-Tac-Toe board and initializes all fields. 
     */
    public TicTacToe()
    {
        board = new JButton[3][3]; // 3X3 JButtons
        JFrame frame = new JFrame("TIC-TAC-TOE"); // Create a frame for the GUI
        frame.setBounds(100, 100, 550, 550);  

        frame.getContentPane().setLayout(new BorderLayout()); // Set the layout of the content pane to a BorderLayout

        JPanel panel = new JPanel(); // Create a new panel
        panel.setLayout(new GridLayout(3, 3)); // make panel a 3x3 grid layout 
        frame.getContentPane().add(panel, BorderLayout.CENTER); // add panel to pane 

        JMenuBar menubar = new JMenuBar();
        frame.setJMenuBar(menubar);

        JMenu fileMenu = new JMenu("Options"); // create a menu
        menubar.add(fileMenu); // and add to our menu bar

        newGame = new JMenuItem("New Game"); // create a menu item called "Quit"
        fileMenu.add(newGame); // and add to our menu

        quitItem = new JMenuItem("Quit"); // create a menu item called "Quit"
        fileMenu.add(quitItem); // and add to our menu

        gameUpdate = new JLabel("Game in Progress: X's turn"); // Player X always starts first
        frame.getContentPane().add(gameUpdate, BorderLayout.SOUTH);

        final int SHORTCUT_MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(); // add shortcuts to save typing
        newGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, SHORTCUT_MASK));
        quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, SHORTCUT_MASK));

        newGame.addActionListener(this); // add action listeners to commands 
        quitItem.addActionListener(new ActionListener() // create an anonymous inner class
            { // start of anonymous subclass of ActionListener
                // this allows us to put the code for this action here  
                public void actionPerformed(ActionEvent event)
                {
                    System.exit(0); // quit
                }
            } // end of anonymous subclass
        );

        //loop through the grid and initialize each square to a JButton, adding to the panel and adding action listener. 
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                board[i][j] = new JButton();
                panel.add(board[i][j]);
                board[i][j].addActionListener(this); 
            }
        }
        
        img_x = new ImageIcon("x.png"); //import image for X  
        img_y = new ImageIcon("o.png"); //import image for O
        img_blank = new ImageIcon("white.jpeg"); // Import blank image 
        clearBoard(); // clear board 
        frame.setVisible(true);  
    }

    /**
     * Sets everything up for a new game.  Marks all squares in the Tic Tac Toe board as empty,
     * and indicates no winner yet, 9 free squares and the current player is player X.
     */
    private void clearBoard()
    {
        // loops through the grid and returns all buttons to default state. 
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j].setIcon(img_blank);
                board[i][j].setEnabled(true); // make the buttons available for next game 
            }
        }
        gameUpdate.setText("Game in Progress: X's turn"); // X starts game

        winner = EMPTY;
        numFreeSquares = 9;
        player = PLAYER_X;     // Player X always has the first turn.
    }

    /**
     * Plays one game of Tic Tac Toe.
     * 
     * @param row , pass in the row number
     * @param col , pass in the col number 
     */
    public void playGame(int row, int col)
    {

        numFreeSquares--; // decrese the number of squares available 

        // see if the game is over
        if (haveWinner(row,col)) {
            winner = player; // must be the player who just went
            endGame();
        }
        else if (numFreeSquares==0) {
            winner = TIE;
            // board is full so it's a tie
            endGame();
        }


        if (winner!=EMPTY) {
            endGame(); // disable all buttons
            // print winner
            String string = "Game over: ";
            if (winner == PLAYER_X) {
                string += "X wins";
            } else if (winner == PLAYER_O) {
                string += "O wins";
            } else if (winner == TIE) {
                string += "Tied game";
            }   
            gameUpdate.setText(string);
        } else {
            // change to other player (game continues)
            if (player==PLAYER_X) {
                player=PLAYER_O;
                gameUpdate.setText("Game in progress: O's turn");
            } else {
                player=PLAYER_X;
                gameUpdate.setText("Game in progress: X's turn");
            }
        }

    } 

    /**
     * Returns true if filling the given square gives us a winner, and false
     * otherwise.
     *
     * @param int row of square just set
     * @param int col of square just set
     * 
     * @return true if we have a winner, false otherwise
     */
    private boolean haveWinner(int row, int col) 
    {
        // unless at least 5 squares have been filled, we don't need to go any further
        // (the earliest we can have a winner is after player X's 3rd move).

        if (numFreeSquares>4) return false;

        // Note: We don't need to check all rows, columns, and diagonals, only those
        // that contain the latest filled square.  We know that we have a winner 
        // if all 3 squares are the same, as they can't all be blank (as the latest
        // filled square is one of them).

        // check row "row"
        if ( board[row][0].getIcon().equals(board[row][1].getIcon()) && 
        board[row][0].getIcon().equals(board[row][2].getIcon())) {
            return true;
        }

        // check column "col"
        if ( board[0][col].getIcon().equals(board[1][col].getIcon()) &&
        board[0][col].getIcon().equals(board[2][col].getIcon()) ) return true;

        // if row=col check one diagonal
        if (row==col)
            if ( board[0][0].getIcon().equals(board[1][1].getIcon()) &&
            board[0][0].getIcon().equals(board[2][2].getIcon()) ) return true;

        // if row=2-col check other diagonal
        if (row==2-col)
            if ( board[0][2].getIcon().equals(board[1][1].getIcon()) &&
            board[0][2].getIcon().equals(board[2][0].getIcon()) ) return true;

        // no winner yet
        return false;
    }

    /** This action listener is called when the user clicks on 
     * any of the GUI's buttons. 
     * 
     * @param e , pass in the event
     */
    public void actionPerformed(ActionEvent e)
    {
        Object o = e.getSource(); // get the action 

        // see if it's a JButton
        if (o instanceof JButton) {
            JButton button = (JButton)o;// typecast o to JButton
            int row = 0;
            int col = 0;

            if (button.getIcon() == img_blank){ // If button not already clicked 
                URL urlClick = TicTacToe.class.getResource("scream.wav"); // surprise sound :) volume warning 
                event = Applet.newAudioClip(urlClick);
                event.play(); // play once
                
                // Get the row and col of the button that was pressed 
                for(int i = 0; i < 3; i++){
                    for(int j = 0; j < 3; j++){
                        if(board[i][j].equals(button)){
                            row = i; 
                            col = j; 
                        }
                    }
                }
                

                // Set the player image depending on whose turn it is 
                switch(player) {
                    case PLAYER_X :
                    button.setIcon(img_x);
                    break; // optional
                    case PLAYER_O :
                    button.setIcon(img_y); 
                    break; // optionalbutton.setText(player);

                }
                //haveWinner(row,col);
                playGame(row,col);
            }
        }
        else{
            JMenuItem item = (JMenuItem)o; //Typecast o to a JMenuItem. 
            // If new game command is selected from the menu 
            if(item == newGame){ 
                clearBoard(); // clear board. 
            }
        }
    }

    /**
     * Disables all the buttons in the grid essentially ending the game until reset
     */
    public void endGame()
    {
        if(numFreeSquares == 0) return; 

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                board[i][j].setEnabled(false); 
            }
        }
    }
}

