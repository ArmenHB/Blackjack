package model;

import java.util.ArrayList;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

//represents the hand of the player, also stores their bank account
public class PlayerHand implements Writable {
    private int value; //Total value of cards in your hand (you want as close to 21 as possible without going over)
    private ArrayList<Cards> inPlay; //list of all the cards you have
    private int bet; //How much you are betting for a given hand
    private int bank;// how much is in the player's bank

    //creates the hand with which a player plays, and keeps track of their bank
    public PlayerHand() {
        this.value = 0;
        this.inPlay = new ArrayList<>();
        this.bet = 0;
        this.bank = 1000;

    }

    //REQUIRES: Non-empty deck
    //MODIFIES:this
    //EFFECTS: Gives a new card to your hand and updates value and inPlay as appropriate. returns false if value > 21
    public boolean hit(Deck d) {
        Random deckIndex = new Random();
        int upperbound = d.getDeck().size();
        Cards drawn;
        drawn = d.getDeck().get((deckIndex.nextInt(upperbound)));
        EventLog.getInstance().logEvent(new Event("Player drew " + drawn.getString()
                + ", which was removed from the deck."));
        inPlay.add(drawn);
        d.removeCard(drawn);
        return updateValue(inPlay);
    }

    //REQUIRES: Non-empty deck
    //MODIFIES:this
    //EFFECTS: doubles your bet adds a new card to your hand and updates this as appropriate returns false if value > 21
    public boolean doubleFN(Deck d) {
        this.bank = bank - bet;
        this.bet = this.bet * 2;
        EventLog.getInstance().logEvent(new Event("Player doubled his bet!! it is now $" + this.bet + " ,and "
                + "his bank is now $" + bank + "."));
        hit(d);
        return updateValue(inPlay);
    }

    //REQUIRES:empty inPlay, at least 2 cards in deck
    //MODIFIES:this
    //EFFECTS:gives the player random two cards, removes them from deck, adjusts bet and balance accordingly
    public void deal(Deck d, int bet) {
        EventLog.getInstance().logEvent(new Event("Player bet $" + bet + "."));
        this.bet = bet;
        this.bank = bank - bet;
        EventLog.getInstance().logEvent(new Event("Player bank is now $" + bank + "."));
        hit(d);
        hit(d);
    }


    //REQUIRES: inPlay
    //MODIFIES: this
    //EFFECTS: updates the value of the player's hand
    public boolean updateValue(ArrayList<Cards> h) {
        if (Cards.containsAce(h)) {
            Cards.isAceHigh(h);
        }
        int total = 0;
        for (Cards c : h) {
            total += c.getValue();
        }
        this.value = total;
        return this.value <= 21;
    }

    //MODIFIES:this
    //EFFECTS: Clears player fields (except bank) so we can start a new hand
    public void clearHand() {
        value = 0;
        inPlay.clear();
        bet = 0;
    }

    //MODIFIES:This
    //EFFECTS: Adds a card into your hand
    public void addCard(Cards c) {
        inPlay.add(c);
    }

    public ArrayList<Cards> getInPlay() {
        return inPlay;
    }

    public int getValue() {
        return value;
    }

    public int getBank() {
        return bank;
    }

    public int getBet() {
        return bet;
    }

    public void setBank(int bank) {
        this.bank = bank;
        EventLog.getInstance().logEvent(new Event("Player bank is now $" + bank));
    }

    public void setBet(int bet) {
        this.bet = bet;
    }

    @Override
    //makes the PlayerHand into a JSONobject
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("value", value);
        json.put("inPlay", inPlayToJson());
        json.put("bet", bet);
        json.put("bank", bank);

        return json;
    }

    // EFFECTS: returns Cards in this inPlay as a JSON array <--- this should always be empty, added for posterity.
    private JSONArray inPlayToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Cards c : inPlay) {
            jsonArray.put(c.toJson());
        }

        return jsonArray;
    }

}
