package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

// represents the playing cards of the game
public class Cards implements Writable {
    private String suit;
    private int value;
    private String name;

    //Constructs a card with a given name, value, and suit.
    public Cards(String suit, int value, String name) {
        this.suit = suit;
        this.value = value;
        this.name = name;

    }

    //REQUIRES: a list of cards (taken from dealer or player hand)
    //MODIFIES: null
    //EFFECTS: produces true if list contains an ace, false otherwise.
    public static boolean containsAce(ArrayList<Cards> l) {
        boolean hasAce = false;
        for (Cards c : l) {
            if (c.getName() == "Ace") {
                hasAce = true;
                break;
            }
        }
        return hasAce;
    }

    //REQUIRES: the inPlay ArrayList of a hand with an ace card
    //MODIFIES: the value of the ace
    //EFFECTS: if changes value of ace accordingly and returns true if high, false otherwise
    public static void isAceHigh(ArrayList<Cards> l) {
        Cards targetAce = new Cards("place", 0, "holder");
        int total = 0;
        boolean foundAce = false;
        for (Cards c : l) {
            if (c.getName().equals("Ace")) {
                if (foundAce == false) {
                    targetAce = c;
                    c.setValue(0);
                    foundAce = true;
                } else {
                    c.setValue(1);
                }
            }
            total += c.getValue();
        }
        if ((total + 11) > 21) {
            targetAce.setValue(1);
        } else {
            targetAce.setValue(11);
        }
    }

    public String getString() {
        String out;
        out = getName() + " of " + getSuit();
        return out;
    }

    public String getSuit() {
        return suit;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    //REQUIRES: String must be "Clubs" "Diamonds" "Hearts", or "Spades"
    //EFFECTS: Creates a full suit of cards from 2 to ace, with appropriate values (except ace which will be dealt with)
    public static ArrayList<Cards> suitCreator(String s) {
        ArrayList<Cards> out = new ArrayList<Cards>();
        out.add(new Cards(s, 2, "Two"));
        out.add(new Cards(s, 3, "Three"));
        out.add(new Cards(s, 4, "Four"));
        out.add(new Cards(s, 5, "Five"));
        out.add(new Cards(s, 6, "Six"));
        out.add(new Cards(s, 7, "Seven"));
        out.add(new Cards(s, 8, "Eight"));
        out.add(new Cards(s, 9, "Nine"));
        out.add(new Cards(s, 10, "Ten"));
        out.add(new Cards(s, 10, "Jack"));
        out.add(new Cards(s, 10, "Queen"));
        out.add(new Cards(s, 10, "King"));
        out.add(new Cards(s, 0, "Ace"));
        return out;
    }

    public void setValue(int i) {
        this.value = i;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("suit", suit);
        json.put("value", value);
        json.put("name", name);
        return json;
    }
}
