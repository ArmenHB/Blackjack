package model;

import java.util.ArrayList;
import java.util.Random;

//represents the hand of the dealer
public class DealerHand {
    private int value; //Value of cards in the dealer's hand (you want as close to 21 as possible without going over)
    private ArrayList<Cards> inPlay; //list of all the cards the dealer has

    //MODIFIES:this
    //EFFECTS: Constructor
    public DealerHand() {
        this.value = 0;
        this.inPlay = new ArrayList<>();
    }

    //REQUIRES: the game's deck
    //MODIFES:this
    //EFFECTS: makes the dealer draw until value>16(>17 if soft 17), returns it's final value
    public int dealerTurn(Deck d) {
        int outp = -1;
        boolean finished = false;
        while (finished == false) {
            if (value > 21) {
                finished = true;
            } else if (value > 16 && !softSeventeen(getInPlay())) {
                outp = value;
                finished = true;
            } else {
                Cards c = dealerHit(d);
                EventLog.getInstance().logEvent(new Event("Dealer drew " + c.getString()
                        + ", which was removed from the deck."));
            }
        }
        return outp;
    }

    //REQUIRES:Game's deck
    //MODIFIES:this
    //EFFECTS:Gives the dealer his starting two cards
    public void dealerDraw(Deck d) {
        Cards firstDealer = dealerHit(d);
        Cards secondDealer = dealerHit(d);
        EventLog.getInstance().logEvent(new Event("Dealer drew " + firstDealer.getString() + ", and "
                + secondDealer.getString() + ", which were removed from the deck."));
    }

    //REQUIRES: Game's deck
    //MODIFIES: this
    //EFFECTS: gives the dealer a random card from the deck and discards it.
    //Learnt how to implement random from:
    //https://www.educative.io/answers/how-to-generate-random-numbers-in-java
    public Cards dealerHit(Deck d) {
        Random deckIndex = new Random();
        int upperbound = d.getDeck().size();
        Cards drawn;
        if (d.getDeck().size() == 1) {
            drawn = d.getDeck().get(0);
        } else {
            drawn = d.getDeck().get((deckIndex.nextInt(upperbound)));
        }
        this.inPlay.add(drawn);
        d.getDeck().remove(drawn);
        dealerUpdate(getInPlay());
        return drawn;
    }

    public ArrayList<Cards> getInPlay() {
        return this.inPlay;
    }

    //MODIFIES: this
    //EFFECTS: clears out the dealer's fields so we can start a new hand
    public void dealerClear() {
        this.value = 0;
        getInPlay().clear();
        EventLog.getInstance().logEvent(new Event("Dealer and Player cleared their hands!"));
    }

    //REQUIRES: inPlay
    //MODIFIES: this
    //EFFECTS: updates the value for dealer
    public void dealerUpdate(ArrayList<Cards> h) {
        if (Cards.containsAce(h)) {
            Cards.isAceHigh(h);
        }
        int total = 0;
        for (Cards c : h) {
            total += c.getValue();
        }
        this.value = total;

    }

    public int getValue() {
        return value;
    }

    //REQUIRES:h must be this.inPlay
    //EFFECTS: produces true if the dealer has a soft seventeen, false otherwise.
    public boolean softSeventeen(ArrayList<Cards> h) {
        boolean condition = false;
        for (Cards c : h) {
            if (c.getName() == "Ace" && c.getValue() == 11) {
                condition = true;
                break;
            }
        }
        dealerUpdate(h);
        return condition && (value == 17);
    }
}
