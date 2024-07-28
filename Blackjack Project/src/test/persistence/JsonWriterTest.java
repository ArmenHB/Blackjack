package persistence;

import model.Cards;
import model.PlayerHand;
import model.Deck;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

//TAKEN FROM WORKROOM PROJECT
class JsonWriterTest {
    //NOTE TO CPSC 210 STUDENTS: the strategy in designing tests for the JsonWriter is to
    //write data to a file and then use the reader to read it back in and check that we
    //read in a copy of what was written out.

    @Test
    void testWriterInvalidFile() {
        try {
            Deck d = new Deck();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyDeck() {
        try {
            Deck d = new Deck();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyD.json");
            writer.open();
            writer.writeD(d);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyD.json");
            d = reader.readD();
            assertEquals(0, d.getMin());
            assertEquals(0, d.getDeck().size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralDeck() {
        try {
                Deck d = new Deck();
                JsonWriter writer = new JsonWriter("./data/testWriterGeneralD.json");
                d.addCard(new Cards("Clubs", 10, "Jack"));
                d.addCard(new Cards("Spades", 10, "Jack"));
                d.setMin(1);
                writer.open();
                writer.writeD(d);
                writer.close();

                JsonReader reader = new JsonReader("./data/testWriterGeneralD.json");
                d = reader.readD();
                assertEquals(1, d.getMin());
                assertEquals(2, d.getDeck().size());
                Cards testCard = d.getDeck().get(0);
                assertEquals(10, testCard.getValue());
                assertEquals("Clubs", testCard.getSuit());
                assertEquals("Jack", testCard.getName());
            } catch (IOException e) {
                fail("Exception should not have been thrown");
            }
    }

    @Test
    void testWriterEmptyPlayerHand() {
        try {
            PlayerHand ph = new PlayerHand();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyPH.json");
            writer.open();
            writer.writeP(ph);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyPH.json");
            ph = reader.read();
            assertEquals(0, ph.getBet());
            assertEquals(1000, ph.getBank());
            assertEquals(0, ph.getValue());
            assertEquals(0, ph.getInPlay().size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralPlayerHand() {
        try {
            PlayerHand ph = new PlayerHand();
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralPH.json");
            ph.addCard(new Cards("Clubs", 10, "Jack"));
            ph.addCard(new Cards("Spades", 10, "Jack"));
            ph.setBank(1);
            ph.setBet(2222);
            ph.updateValue(ph.getInPlay());
            writer.open();
            writer.writeP(ph);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralPH.json");
            ph = reader.read();
            assertEquals(1, ph.getBank());
            assertEquals(0, ph.getValue());
            assertEquals(0, ph.getBet());
            assertEquals(0, ph.getInPlay().size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
