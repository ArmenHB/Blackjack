package ui;

import model.*;

import model.Event;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;


//represents the blackjack game
public class BlackjackGame extends JFrame {
    private static final String JSON_STORE = "./data/deck.json";
    private static final String JSON_STOREPLAYA = "./data/playerHand.json";
    private DealerHand dealer;
    private Deck gameDeck;
    private PlayerHand player;
    private final JsonWriter jsonWriter;
    private final JsonWriter jsonWriterP;
    private final JsonReader jsonReader;
    private final JsonReader jsonReaderP;
    private final int width = 1000;
    private final int height = 800;
    private JFrame frame = new JFrame("BlackJack");
    private JPanel panel = new JPanel();
    private JTextArea west;
    private JPanel south;
    private JPanel north;

    //EFFECTS: starts the game
    public BlackjackGame() {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonWriterP = new JsonWriter(JSON_STOREPLAYA);
        jsonReader = new JsonReader(JSON_STORE);
        jsonReaderP = new JsonReader(JSON_STOREPLAYA);
        initializeGraphics();
        String logOut = "";
        Iterator<Event> log = EventLog.getInstance().iterator();
        for (Iterator<Event> it = log; it.hasNext(); ) {
            Event e = it.next();
            logOut = logOut + "\n" + e.getDescription();
        }
        System.out.println(logOut);

    }

    //creates the frame
    public void initializeGraphics() {
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(width, height));
        frame.setLocationRelativeTo(null);

        initializePanel();

        int choice = JOptionPane.showOptionDialog(
                this,
                "Would you like to start a new game or load a previous session?",
                "A voice booms as you enter the casino...",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{"New Game", "Load Game"},
                "New Game");

        if (choice == JOptionPane.YES_OPTION) {
            startGame();
        } else if (choice == JOptionPane.NO_OPTION) {
            loadInit();
        }
    }

    //EFFECTS: sets up all of the panels that will be on the frame
    private void initializePanel() {
        Color golden = new Color(250,228,27);
        Color mine = new Color(6, 85, 53);
        panel.setLayout(new BorderLayout());
        panel.setBackground(mine);

        west = new JTextArea("Dealer Hand Value: 0 \nl\nl\nl Player Hand Value: 0"
                + ("\nl\nl \nl") + "Cards Remaining in Deck:  0\nl\nl\nl Your Bank Account: $1000");
        JPanel table = new JPanel();
        south = new JPanel();
        north = new JPanel();


        west.setBackground(golden);
        south.setBackground(mine);
        north.setBackground(mine);
        table.setBackground(mine);
        north.setSize(width, 100);
        south.setSize(width, 100);

        south.setLayout(new GridLayout(1,0));
        north.setLayout(new GridLayout(1,0));

        panel.add(table, BorderLayout.CENTER);
        panel.add(west, BorderLayout.WEST);
        panel.add(south, BorderLayout.SOUTH);
        panel.add(north, BorderLayout.NORTH);

        frame.add(panel);
        frame.setVisible(true);
    }

    //EFFECTS: helper for creating message pop=ups
    private void showMessageDialog(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Message",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    //MODIFIES: this
    //EFFECTS processes user input (Taken from teller app)
    private void startGame() {
        int initNum = 0;
        boolean properInit = false;
        boolean goodJob = false;

        while (!properInit) {
            String deckNo = JOptionPane.showInputDialog("Please enter a non-negative number to choose number of decks");
            try {
                if (Integer.valueOf(deckNo) > 0) {
                    initNum = (Integer.valueOf(deckNo));
                    properInit = true;
                } else if (Integer.valueOf(deckNo) == 0) {
                    showMessageDialog("Congratulations! You have chosen to not gamble. YOU WIN!");
                    properInit = true;
                    goodJob = true;
                } else if (Integer.valueOf(deckNo) < 0) {
                    showMessageDialog("Please choose a NON-NEGATIVE integer >:(");
                }
            } catch (NumberFormatException e) {
                showMessageDialog("ERROR: Please be careful and only enter Numeric Values (e.g. '12')");
            }
        }
        if (!goodJob) {
            init(initNum);
        }
    }

    //EFFECTS: initialize game with loaded JSON
    public void loadInit() {
        dealer = new DealerHand();
        player = new PlayerHand();
        gameDeck = new Deck();
        loadGame();
        gameTime();
    }

    //REQUIRES: needs user input i
    //MODIFIES: this
    //EFFECTS: initializes fields and creates proper number of decks
    public void init(int i) {
        dealer = new DealerHand();
        player = new PlayerHand();
        gameDeck = new Deck();
        gameDeck.initialDeck(i);
        blankGUI();
        gameTime();
    }

    //EFFECTS updates the text in the GUI, boolean for whether or not to reveal the dealer's cards.
    public void updateGUI(boolean dealerReveal) {
        String bruh;
        if (dealer.getInPlay().get(0).getName() == "Ace") {
            bruh = "11";
        } else {
            bruh = Integer.toString(dealer.getInPlay().get(0).getValue());
        }

        if (!dealerReveal) {
            west.setText("Dealer Hand Value: " + bruh
                    + "\nl\nl\nl Player Hand Value: " + showValue(player.getValue(), player.getInPlay())
                    + ("\nl\nl\nl") + "Cards Remaining in Deck: " + gameDeck.getDeck().size()
                    + ("\nl\nl\nl") + "Your bank: $" + player.getBank());
        } else {
            west.setText("Dealer Hand Value: " + showValue(dealer.getValue(), dealer.getInPlay())
                    + "\nl\nl\nl Player Hand Value: " + showValue(player.getValue(), player.getInPlay())
                    + ("\nl\nl\nl") + "Cards Remaining in Deck: " + gameDeck.getDeck().size()
                    + ("\nl\nl\nl") + "Your bank: $" + player.getBank());
        }

    }

    //EFFECTS: resets the GUI after a turn
    public void blankGUI() {
        west.setText("Dealer Hand Value: 0 \nl\nl\nl Player Hand Value: 0"
                + ("\nl\nl \nl") + "Cards Remaining in Deck: " + gameDeck.getDeck().size()
                + "\nl\nl\nl Your Bank Account: $" + player.getBank());

        south.removeAll();
        north.removeAll();
    }

    //EFFECTS: entrance to gameplay loop, you can quit, see bank, or deal
    public void gameTime() {
        boolean gameOver = false;
        while (!gameOver) {
            if (player.getBank() == 0) {
                showMessageDialog("You ran out of money! You should probably get better before you lose more...");
                gameOver = true;
            } else {
                int choice = getChoice();
                if (choice == JOptionPane.YES_OPTION) {
                    showMessageDialog("You have $" + player.getBank() + " in the bank!");
                } else if (choice == JOptionPane.NO_OPTION) {
                    startTurn();
                } else if (choice == JOptionPane.CANCEL_OPTION) {
                    quitMenu();
                    gameOver = true;
                }
            }
        }
    }

    //EFFECTS: Done for refactoring purposes. returns the choice of the player
    private int getChoice() {
        int choice = JOptionPane.showOptionDialog(
                this,
                "Click 'bank' to see your funds, 'deal' to play, and 'quit' to quit.",
                "New Hand!",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{"Bank", "Deal", "Quit"},
                "Bank");
        return choice;
    }

    //EFFECTS: Brings up the menu you see when you try to quit
    public void quitMenu() {
        int choice = JOptionPane.showOptionDialog(
                this,
                "Would you like to Save and Quit, Quit without saving, or go back to the table?",
                "Are you sure?",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{"Quit Without Saving", "Save & Quit", "Back"},
                "Bank");

        if ((choice == JOptionPane.YES_OPTION)) { //Idea from tellerApp
            showMessageDialog(("Thanks for playing! You ended things with $" + player.getBank() + " in your account."));
        } else if (choice == JOptionPane.NO_OPTION) {
            player.clearHand();
            saveGame();
            showMessageDialog(("See you soon! You ended things with $" + player.getBank() + " in your account."));
        } else if (choice == JOptionPane.CANCEL_OPTION) {
            gameTime();
        }
    }

    //EFFECTS: make your bet and see the first two cards that you are dealt.
    public void startTurn() {
        boolean betMade = false;
        String intCommand = "0";

        while (!betMade) {
            intCommand = JOptionPane.showInputDialog("Please place your bet!");

            try {
                if (Integer.parseInt(intCommand) <= 0) {
                    showMessageDialog("Don't be silly, choose an actual bet...");
                } else if (Integer.parseInt(intCommand) > player.getBank()) {
                    showMessageDialog("You don't have enough money! Choose a smaller bet...");
                } else {
                    betMade = true;
                }
            } catch (NumberFormatException e) {
                showMessageDialog("ERROR: Please be careful and only enter Numeric Values (e.g. '12')");
            }
        }

        playerCommands(Integer.parseInt(intCommand));
    }

    //EFFECTS: main gameplay loop where players can hit, stand, or double
    public void playerCommands(int i) {
        boolean turnState = true;
        player.deal(gameDeck, i);
        dealerDraw();
        showEveryP(player.getInPlay());
        updateGUI(false);
        while (turnState && (player.getValue() <= 21)) {
            int choice = playerChoice();
            if (choice == JOptionPane.YES_OPTION) {
                playerHit();
            } else if ((choice == JOptionPane.NO_OPTION)) {
                if (player.getBet() > player.getBank()) {
                    showMessageDialog("You don't have enough money to double! Try again later...");
                } else {
                    playerDoubled();
                    turnState = false;
                }
            } else if ((choice == JOptionPane.CANCEL_OPTION)) {
                turnState = false;
            }
        }
        showEveryP(player.getInPlay());
        whoWon();
    }

    //main choice the player makes in the "gameplay loop"
    public int playerChoice() {
        return JOptionPane.showOptionDialog(
                this,
                "Hit, Double, or Stand?",
                "Choose Wisely...",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{"Hit", "Double", "Stand"},
                "Hit");
    }

    //MODIFIES:this
    //EFFECTS: if the deck is under 25% of what it started as, shuffles the deck
    public void checkDeck() {
        if (gameDeck.getDeck().size() < gameDeck.getMin()) {
            int i = (gameDeck.getMin() * 4);
            i = i / 52;
            gameDeck.getDeck().clear();
            gameDeck.initialDeck(i);
            blankGUI();
            showMessageDialog("The dealer shuffles the deck!");
        }
    }

    //EFFECTS: doubles the players bet
    public void playerDoubled() {
        player.doubleFN(gameDeck);
        showEveryP(player.getInPlay());
    }

    //EFFECTS: hit function
    public void playerHit() {
        player.hit(gameDeck);
        showEveryP(player.getInPlay());
        updateGUI(false);
    }

    //MODIFIES:this
    //EFFECTS: determines who won and changes bank balance appropriately.
    public void whoWon() {
        boolean playerNotBust = (player.getValue() <= 21);
        dealer.dealerTurn(gameDeck);
        showEveryD(dealer.getInPlay());
        updateGUI(true);

        if ((playerNotBust && (player.getValue() > dealer.getValue())) || (dealer.getValue() > 21 && playerNotBust)) {
            player.setBank(player.getBank() + (player.getBet() * 2));
            updateGUI(true);
            showMessageDialog("You won $" + (player.getBet() * 2) + "!!!");
        } else if ((player.getValue() <= 21) && (player.getValue() == dealer.getValue())) {
            player.setBank(player.getBank() + player.getBet());
            updateGUI(true);
            showMessageDialog("You pushed! You get your bet back...");

        } else {
            showMessageDialog("You lost $" + (player.getBet()) + ".Better luck next time!");
            player.setBank(player.getBank());
            updateGUI(true);
        }

        blankGUI();
        player.clearHand();
        dealer.dealerClear();
        checkDeck();
    }

    //EFFECTS:prints every player card
    public void showEveryP(ArrayList<Cards> h) {
        south.removeAll();

        for (Cards c : h) {
            ImageIcon cardIcon = new ImageIcon("graphics/" + getCardPic(c));
            Image image = cardIcon.getImage();
            Image newimg = image.getScaledInstance(120, 180,  java.awt.Image.SCALE_SMOOTH);
            cardIcon = new ImageIcon(newimg);
            JLabel cardLabel = new JLabel(cardIcon);
            south.add(cardLabel);
        }
        showValue(player.getValue(), player.getInPlay());

    }

    //grabs the card graphic
    public String getCardPic(Cards c) {
        if (c.getSuit() == "Diamonds" || c.getSuit() == "Hearts") {
            return (c.getName() + "_red.png");
        } else {
            return (c.getName() + "_black.png");
        }
    }

    //EFFECTS: dealer draws and prints first card
    public void dealerDraw() {
        dealer.dealerDraw(gameDeck);
        showFirstD();
    }

    //EFFECTS:prints every dealer card
    public void showEveryD(ArrayList<Cards> h) {
        north.removeAll();

        for (Cards c : h) {
            ImageIcon cardIcon = new ImageIcon("graphics/" + getCardPic(c));
            Image image = cardIcon.getImage();
            Image newimg = image.getScaledInstance(120, 180,  java.awt.Image.SCALE_SMOOTH);
            cardIcon = new ImageIcon(newimg);
            JLabel cardLabel = new JLabel(cardIcon);
            north.add(cardLabel);
        }
        showValue(dealer.getValue(), dealer.getInPlay());

    }

    //EFFECTS:prints first dealer card
    public void showFirstD() {
        ImageIcon cardIcon = new ImageIcon("graphics/" + getCardPic(dealer.getInPlay().get(0)));
        Image properImg = cardIcon.getImage();
        Image proprNewImg = properImg.getScaledInstance(120, 180,  java.awt.Image.SCALE_SMOOTH);
        cardIcon = new ImageIcon(proprNewImg);
        JLabel cardLabel = new JLabel(cardIcon);
        north.add(cardLabel);

        ImageIcon blankIcon = new ImageIcon("graphics/card_back.png");
        Image image = blankIcon.getImage();
        Image newimg = image.getScaledInstance(120, 180,  java.awt.Image.SCALE_SMOOTH);
        blankIcon = new ImageIcon(newimg);
        JLabel blankCard = new JLabel(blankIcon);
        north.add(blankCard);

        showValue(player.getValue(), player.getInPlay());


    }

    //EFFECTS: produces true if value is variable due to ace.
    public boolean variableValue(ArrayList<Cards> h) {
        boolean containsHighAce = false;
        for (Cards c : h) {
            if (c.getName() == "Ace" && c.getValue() == 11) {
                containsHighAce = true;
                break;
            }
        }
        return containsHighAce;
    }

    //EFFECTS: prints value of player or dealer hand
    public String showValue(int i, ArrayList<Cards> h) {
        if (variableValue(h)) {
            return (i - 10) + " or " + i;
        } else {
            return Integer.toString(i);
        }
    }

    // Taken from the workroom app
    // EFFECTS: saves the game to file
    private void saveGame() {
        try {
            jsonWriter.open();
            jsonWriterP.open();
            jsonWriter.writeD(gameDeck);
            jsonWriterP.writeP(player);
            jsonWriter.close();
            jsonWriterP.close();
            showMessageDialog("Saved the game!");
        } catch (FileNotFoundException e) {
            showMessageDialog("ERROR: Unable to write to file: " + JSON_STORE + "or" + JSON_STOREPLAYA);
        }
    }

    // Taken from the workroom app
    // MODIFIES: this
    // EFFECTS: loads game from file
    private void loadGame() {
        try {
            gameDeck = jsonReader.readD();
            player = jsonReaderP.read();
            blankGUI();
            showMessageDialog("Loaded the game!" + "\nl There are " + gameDeck.getDeck().size()
                    + " cards left in the shoe.");
        } catch (IOException e) {
            showMessageDialog("ERROR: Unable to write to file: " + JSON_STORE + "or" + JSON_STOREPLAYA);
        }
    }

}
