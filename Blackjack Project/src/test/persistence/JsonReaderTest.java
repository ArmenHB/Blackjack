package persistence;

import model.Deck;
import model.PlayerHand;
import org.junit.jupiter.api.Test;

import java.io.IOException;


import static org.junit.jupiter.api.Assertions.*;

//From WorkRoom project
class JsonReaderTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            PlayerHand ph = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyDeck() {
        JsonReader reader = new JsonReader("./data/testreaderemptyD.json");
        try {
            Deck d = reader.readD();
            assertEquals(0, d.getMin());
            assertEquals(0, d.getDeck().size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralDeck() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralD.json");
        try {
            Deck d = reader.readD();
            assertEquals(2, d.getMin());
            assertEquals(2, d.getDeck().size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
