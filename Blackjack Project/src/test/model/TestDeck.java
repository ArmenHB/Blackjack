package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class TestDeck {
    public Deck testDeck;
    public Cards testCardKH;
    public Cards testCard6H;
    public Cards testCardAS;

    @BeforeEach
    void runBefore() {
        testDeck = new Deck();
        testCardKH = new Cards("Hearts", 10, "King");
        testCard6H = new Cards("Hearts", 6, "Six");
        testCardAS = new Cards("Hearts", 0, "Ace");
    }

    @Test
    void testConstructor() {
        assertEquals(0, testDeck.getDeck().size());
        assertEquals(0, testDeck.getMin());
    }

    @Test
    void testBuildDeck() {
        testDeck.buildDeck();
        assertEquals(52, testDeck.getDeck().size());
    }

    @Test
    void testRemoveCard() {
        testDeck.addCard(testCardKH);
        assertEquals(1, testDeck.getDeck().size());
        testDeck.removeCard(testCardKH);
        assertFalse(testDeck.getDeck().contains(testCardKH));
        assertEquals(0, testDeck.getDeck().size());

        testDeck.addCard(testCardKH);
        testDeck.addCard(testCard6H);
        testDeck.addCard(testCardAS);
        assertEquals(3, testDeck.getDeck().size());
        testDeck.removeCard(testCard6H);
        assertFalse(testDeck.getDeck().contains(testCard6H));
        assertTrue(testDeck.getDeck().contains(testCardAS));
        assertTrue(testDeck.getDeck().contains(testCardKH));
        assertEquals(2, testDeck.getDeck().size());
    }

    @Test
    void testInitializeDeck() {
        testDeck.initialDeck(3);
        assertEquals(156, testDeck.getDeck().size());
        int tester = 0;
        for (Cards c : testDeck.getDeck()) {
            if (c.getName() == "Ace") {
                tester++;
            }
        }
        assertEquals(12, tester);
        assertEquals((156 / 4), testDeck.getMin());

    }

    @Test
    void testAddCard() {
        testDeck.addCard(testCard6H);
        assertTrue(testDeck.getDeck().contains(testCard6H));
        assertEquals(1, testDeck.getDeck().size());
        testDeck.buildDeck();
        assertTrue(testDeck.getDeck().contains(testCard6H));
        assertEquals(53, testDeck.getDeck().size());

    }

}



