package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;



public class TestDealerHand {
    Deck testDeckHigh;
    Deck testDeckLow;
    Deck testDeckAce;
    Deck testDeck;
    Deck sevenD;
    Deck failDeck;
    Cards testCardKH;
    Cards testCard5H;
    Cards testCardAS;
    Cards testCardAH;
    Cards testCardAC;
    Cards testCardAD;
    Cards testCard8H;
    Cards testCard8C;
    Cards testCard8S;
    Cards testCard8D;
    Cards seven;
    DealerHand testHand;
    DealerHand testHand2;
    DealerHand testHand3;
    DealerHand testHand4;
    Deck testDeckTwo;
    Deck testDeckThree;

    Cards testCard3H;
    Cards testCard2H;

    @BeforeEach
    void runBefore() {
        testHand = new DealerHand();
        testHand2 = new DealerHand();
        testHand3 = new DealerHand();
        testHand4 = new DealerHand();
        testDeck = new Deck();
        failDeck = new Deck();
        testDeck.initialDeck(2);
        testDeckTwo = new Deck();
        testDeckThree = new Deck();

        testCardKH = new Cards("Hearts", 10, "King");
        testCard3H = new Cards("Hearts", 3, "Three");
        testCard2H = new Cards("Hearts", 2, "Two");
        testCard5H = new Cards("Hearts", 5, "Five");
        testCardAS = new Cards("Spades", 0, "Ace");
        testCardAC = new Cards("Clubs", 0, "Ace");
        testCardAD = new Cards("Diamonds", 0, "Ace");
        testCardAH = new Cards("Hearts", 0, "Ace");
        testCard8H = new Cards("Hearts", 8, "Eight");
        testCard8C = new Cards("Clubs", 8, "Eight");
        testCard8S = new Cards("Spades", 8, "Eight");
        testCard8D = new Cards("Diamonds", 8, "Eight");
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

        failDeck.addCard(testCard8C);
        failDeck.addCard(testCard8H);
        failDeck.addCard(testCard8D);
        failDeck.addCard(testCard8S);

        testDeckTwo.addCard(testCard2H);
        testDeckTwo.addCard(testCard2H);
        testDeckTwo.addCard(testCard2H);
        testDeckTwo.addCard(testCard2H);

        testDeckThree.addCard(testCard3H);
        testDeckThree.addCard(testCard3H);
        testDeckThree.addCard(testCard3H);
        testDeckThree.addCard(testCard3H);

        seven = new Cards("Hearts", 7, "Seven");
        sevenD = new Deck();
        sevenD.addCard(seven);

    }

    @Test
    void testConstructor() {
        assertEquals(0, testHand.getValue());
        assertEquals(0, testHand.getInPlay().size());

    }

    @Test
    void testDealerHit() {
        testHand.dealerHit(testDeckAce);
        assertEquals(11, testHand.getValue());

        testHand.dealerHit(testDeckHigh);
        assertEquals(21, testHand.getValue());

    }

    @Test
    void testDealerUpdate() {
        testHand.dealerHit(testDeckAce);
        assertEquals(11, testHand.getValue());

        testHand.dealerHit(testDeckLow);
        assertEquals(16, testHand.getValue());

        testHand.dealerHit(testDeckHigh);
        assertEquals(16, testHand.getValue());
    }

    @Test
    void testDealerDraw() {
        testHand.dealerDraw(testDeck);
        assertTrue(testHand.getValue() > 0);
        assertEquals(102, testDeck.getDeck().size());
    }

    @Test
    void testSoftSeventeen() {
        testHand.dealerHit(sevenD);
        testHand.dealerHit(testDeckHigh);
        assertFalse(testHand.softSeventeen(testHand.getInPlay()));

        testHand2.dealerHit(testDeckLow);
        testHand2.dealerHit(testDeckAce);
        testHand2.dealerHit(testDeckAce);
        assertTrue(testHand2.softSeventeen(testHand2.getInPlay()));

        testDeckHigh.addCard(testCardKH);
        testDeckHigh.addCard(testCardKH);

        testDeckAce.addCard(testCardAD);
        testDeckAce.addCard(testCardAH);

        testHand4.dealerHit(testDeckHigh);
        testHand4.dealerHit(testDeckAce);
        assertFalse(testHand4.softSeventeen(testHand4.getInPlay()));

        testHand3.dealerDraw(testDeckHigh);
        testHand3.dealerTurn(testDeckTwo);
        testHand3.dealerTurn(testDeckTwo);
        testHand3.dealerTurn(testDeckTwo);
        assertFalse(testHand3.softSeventeen(testHand3.getInPlay()));

        testHand.dealerClear();
        Deck bugSquasher = new Deck();
        bugSquasher.addCard(new Cards("Place", 11, "Holder"));
        bugSquasher.addCard(new Cards("Place", 6, "Holder"));
        testHand.dealerHit(bugSquasher);
        testHand.dealerHit(bugSquasher);
        assertFalse(testHand.softSeventeen(testHand.getInPlay()));
    }

    @Test
    void testDealerClear() {
        testHand.dealerHit(testDeckAce);
        assertEquals(11, testHand.getValue());
        assertEquals(1, testHand.getInPlay().size());

        testHand.dealerClear();
        assertEquals(0, testHand.getValue());
        assertEquals(0, testHand.getInPlay().size());

    }

    @Test
    void testDealerTurn() {
        testHand.dealerDraw(testDeckHigh);
        assertEquals(20, testHand.dealerTurn(testDeckHigh));

        testHand2.dealerDraw(failDeck);
        assertEquals(-1, testHand2.dealerTurn(failDeck));

        testHand3.dealerDraw(testDeck);
        testHand3.dealerTurn(testDeck);

        testHand4.dealerHit(testDeckLow);
        testHand4.dealerHit(testDeckAce);
        testHand4.dealerHit(testDeckAce);
        testHand4.dealerTurn(testDeckAce);
        assertEquals(18, testHand4.getValue());

    }


}
