package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;


//represents the "shoe" of the game from where player and dealer will receive cards
public class Deck implements Writable {
    private ArrayList<Cards> deck;
    private int min;

    //EFFECTS: constructor, creates empty
    public Deck() {
        this.deck = new ArrayList<>();
        this.min = 0;
    }

    //MODIFIES: this
    //EFFECTS: creates a standard 52-card deck
    public void buildDeck() {
        ArrayList<Cards> clubs = Cards.suitCreator("Clubs");
        ArrayList<Cards> hearts = Cards.suitCreator("Hearts");
        ArrayList<Cards> diamonds = Cards.suitCreator("Diamonds");
        ArrayList<Cards> spades = Cards.suitCreator("Spades");
        this.deck.addAll(clubs);
        this.deck.addAll(hearts);
        this.deck.addAll(diamonds);
        this.deck.addAll(spades);
    }


    //REQUIRES: i>0
    //MODIFIES: the amount of decks/cards with which we will play
    //EFFECTS: duplicates a given number of standard 52-card decks and adds them to the shoe, sets min to 25%
    public void initialDeck(int i) {
        deck.clear();
        min = (i * 52);
        min = (min / 4);
        buildDeck();
        EventLog.getInstance().logEvent(new Event("Deck created with " + (i * 52) + " cards!"));
        while (i != 1) {
            buildDeck();
            i--;
        }
    }

    //MODIFIES: This
    //EFFECTS: Removes a given card from the deck
    public void removeCard(Cards c) {
        deck.remove(c);
    }

    //MODIFIES: This
    //EFFECTS: adds a given card to the deck
    public void addCard(Cards c) {
        this.deck.add(c);//STUB
    }

    //EFFECTS: getter
    public ArrayList<Cards> getDeck() {
        return deck;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMin() {
        return min;
    }

    @Override
    //EFFECTS: makes the deck into a JSONobject
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("deck", deckToJson());
        json.put("min", min);
        return json;
    }

    // EFFECTS: returns Cards in this Deck as a JSON array
    private JSONArray deckToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Cards c : deck) {
            jsonArray.put(c.toJson());
        }

        return jsonArray;
    }

}
