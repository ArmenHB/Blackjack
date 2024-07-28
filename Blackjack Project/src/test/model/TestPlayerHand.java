package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestPlayerHand {
    Deck testDeckHigh;
    Deck testDeckLow;
    Deck testDeckAce;
    Deck testDeck;
    Cards testCardKH;
    Cards testCard5H;
    Cards testCardAS;
    Cards testCardAH;
    Cards testCardAC;
    Cards testCardAD;
    PlayerHand testHand;
    PlayerHand testHand2;
    PlayerHand testHand3;
    PlayerHand testHand4;


    @BeforeEach
    void runBefore() {
        testHand = new PlayerHand();
        testHand2 = new PlayerHand();
        testHand3 = new PlayerHand();
        testHand4 = new PlayerHand();
        testDeck = new Deck();
        testDeck.initialDeck(2);

        testCardKH = new Cards("Hearts", 10, "King");
        testCard5H = new Cards("Hearts", 5, "Six");
        testCardAS = new Cards("Spades", 0, "Ace");
        testCardAC = new Cards("Clubs", 0, "Ace");
        testCardAD = new Cards("Diamonds", 0, "Ace");
        testCardAH = new Cards("Hearts", 0, "Ace");
        testDeckHigh = new Deck();
        testDeckLow = new Deck();
        testDeckAce = new Deck();

        testDeckHigh.addCard(testCardKH);
        testDeckHigh.addCard(testCardKH);
        testDeckHigh.addCard(testCardKH);
        testDeckHigh.addCard(testCardKH);

        testDeckLow.addCard(testCard5H);
        testDeckLow.addCard(testCard5H);
        testDeckLow.addCard(testCard5H);
        testDeckLow.addCard(testCard5H);

        testDeckAce.addCard(testCardAS);
        testDeckAce.addCard(testCardAC);
        testDeckAce.addCard(testCardAD);
        testDeckAce.addCard(testCardAH);

    }

    @Test
    public void testConstructor() {
        assertEquals(0, testHand.getValue());
        assertEquals(0, testHand.getInPlay().size());
        assertEquals(0, testHand.getBet());
        assertEquals(1000, testHand.getBank());
    }

    @Test
    void testHit() {
        assertTrue(testHand.hit(testDeck));
        assertTrue(testHand.getValue() > 0);
        assertEquals(103, testDeck.getDeck().size());

        testHand2.hit(testDeckHigh);
        assertEquals(10, testHand2.getValue());
        assertEquals(3, testDeckHigh.getDeck().size());

        testHand2.hit(testDeckAce);
        assertEquals(21, testHand2.getValue());
        assertEquals(3, testDeckAce.getDeck().size());

        assertTrue(testHand3.hit(testDeckAce));
        assertTrue(testHand3.hit(testDeckAce));
        assertEquals(12, testHand3.getValue());
        assertEquals(1, testDeckAce.getDeck().size());

        assertTrue(testHand4.hit(testDeckHigh));
        assertTrue(testHand4.hit(testDeckHigh));
        assertFalse(testHand4.hit(testDeckHigh));

    }

    @Test
    void testDeal() {
        testHand.deal(testDeck, 100);
        assertEquals(100, testHand.getBet());
        assertEquals(900, testHand.getBank());
        assertTrue(testHand.getValue() > 0);
        assertTrue(testHand.updateValue(testHand.getInPlay()));
        assertEquals(102, testDeck.getDeck().size());
    }
    @Test
    void testSetBank() {
        testHand.setBank(1);
        assertEquals(1, testHand.getBank());
    }
    @Test
    void testClearHand() {
        testHand.deal(testDeckHigh, 100);
        assertEquals(100, testHand.getBet());
        assertEquals(900, testHand.getBank());
        assertEquals(20, testHand.getValue());

        testHand.clearHand();
        assertEquals(0, testHand.getBet());
        assertEquals(900, testHand.getBank());
        assertEquals(0, testHand.getValue());
    }

    @Test
    void testUpdateValue() {
        testHand.addCard(testCardAS);
        testHand.addCard(testCardAC);
        testHand.addCard(testCardAD);
        assertTrue(testHand.updateValue(testHand.getInPlay()));
        assertEquals(13, testHand.getValue());

        testHand2.addCard(testCard5H);
        testHand2.addCard(testCardKH);
        testHand2.addCard(testCardKH);
        assertFalse(testHand2.updateValue(testHand2.getInPlay()));
        assertEquals(25, testHand2.getValue());
    }

    @Test
    void testAddCard() {
        testHand.addCard(testCardAS);
        testHand.addCard(testCardAC);
        testHand.addCard(testCardAD);
        assertEquals(3, testHand.getInPlay().size());
        assertTrue(testHand.updateValue(testHand.getInPlay()));
        assertEquals(13, testHand.getValue());
    }

    @Test
    void testDoubleFN() {
        testHand.deal(testDeckHigh, 200);
        assertFalse(testHand.doubleFN(testDeckHigh));
        assertEquals(400, testHand.getBet());
        assertEquals(600, testHand.getBank());

        testHand2.deal(testDeckLow, 500);
        assertTrue(testHand2.doubleFN(testDeckAce));
        assertEquals(1000, testHand2.getBet());
        assertEquals(0, testHand2.getBank());
    }

}
