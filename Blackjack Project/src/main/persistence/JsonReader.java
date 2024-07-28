package persistence;

import model.Cards;
import model.Deck;
import model.PlayerHand;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.json.*;

//ALL SOURCED FROM WORKROOM PROJECT
// Represents a reader that reads workroom from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads PlayerHand from file and returns it;
    // throws IOException if an error occurs reading data from file
    public PlayerHand read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parsePlayerHand(jsonObject);
    }

    //EFFECTS: Parses PlayerHand from JSON Object and returns it
    public PlayerHand parsePlayerHand(JSONObject jsonObject) {
        PlayerHand p = new PlayerHand();
        p.setBank(jsonObject.getInt("bank"));
        return p;
    }

    // EFFECTS: reads Deck from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Deck readD() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseDeck(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses deck from JSON object and returns it
    private Deck parseDeck(JSONObject jsonObject) {
        Deck d = new Deck();
        addCards(d, jsonObject);
        d.setMin(jsonObject.getInt("min"));
        return d;
    }

    // MODIFIES: d
    // EFFECTS: parses cards from JSON object and adds them to deck
    private void addCards(Deck d, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("deck");
        for (Object json : jsonArray) {
            JSONObject nextCard = (JSONObject) json;
            addCard(d, nextCard);
        }
    }

    // MODIFIES: d
    // EFFECTS: parses card from JSON object and adds it to the deck
    private void addCard(Deck d, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        int value = jsonObject.getInt("value");
        String suit = jsonObject.getString("suit");
        Cards card = new Cards(suit, value, name);
        d.addCard(card);
    }
}
