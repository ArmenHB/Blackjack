package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;


public class TestCards {
    public Cards testCardKH;
    public Cards testCardAD;
    public Cards testCard6S;
    public Cards testCard5S;
    public Cards testCard2C;
    public Cards testCardAS;

    @BeforeEach
    void runBefore() {
        testCardKH = new Cards("Hearts", 10, "King");
        testCardAD = new Cards("Diamonds", 0, "Ace");
        testCard6S = new Cards("Spades", 6, "Six");
        testCard5S = new Cards("Spades", 5, "Five");
        testCard2C = new Cards("Clubs", 2, "Two");
        testCardAS = new Cards("Spades", 0, "Ace");
    }

    @Test
    void testConstructor() {
        assertEquals(0, testCardAD.getValue());
        assertEquals("Ace", testCardAD.getName());
        assertEquals("Diamonds", testCardAD.getSuit());

    }

    @Test
    void testSuitCreator() {
        ArrayList<Cards> out = new ArrayList<Cards>();
        out.add(new Cards("Hearts", 2, "Two"));
        out.add(new Cards("Hearts", 3, "Three"));
        out.add(new Cards("Hearts", 4, "Four"));
        out.add(new Cards("Hearts", 5, "Five"));
        out.add(new Cards("Hearts", 6, "Six"));
        out.add(new Cards("Hearts", 7, "Seven"));
        out.add(new Cards("Hearts", 8, "Eight"));
        out.add(new Cards("Hearts", 9, "Nine"));
        out.add(new Cards("Hearts", 10, "Ten"));
        out.add(new Cards("Hearts", 10, "Jack"));
        out.add(new Cards("Hearts", 10, "Queen"));
        out.add(new Cards("Hearts", 10, "King"));
        out.add(new Cards("Hearts", 0, "Ace"));
        assertEquals(out.size(), (Cards.suitCreator("Hearts").size()));
    }

    @Test
    void testGetString() {
        assertEquals("King of Hearts", testCardKH.getString());
    }

    @Test
    void testIsAceHigh() {
        PlayerHand testHand = new PlayerHand();
        testHand.addCard(testCardKH);
        testHand.addCard(testCardAD);
        assertEquals(2, testHand.getInPlay().size());
        assertEquals(0, testCardAD.getValue());
        testCardAD.isAceHigh(testHand.getInPlay());
        assertEquals(11, testCardAD.getValue());

        PlayerHand testHand2 = new PlayerHand();
        testHand2.addCard(testCardKH);
        testHand2.addCard(testCard6S);
        testHand2.addCard(testCardAD);
        testCardAD.isAceHigh(testHand2.getInPlay());
        assertEquals(3, testHand2.getInPlay().size());
        assertEquals(1, testCardAD.getValue());

        PlayerHand testHand3 = new PlayerHand();
        testHand3.addCard(testCard6S);
        testHand3.addCard(testCardAD);
        testHand3.addCard(testCardAS);
        testCardAD.isAceHigh(testHand3.getInPlay());
        assertEquals(3, testHand3.getInPlay().size());
        assertEquals(11, testCardAD.getValue());
        assertEquals(1, testCardAS.getValue());
    }

    @Test
    void testContainsAce() {
        PlayerHand testHand = new PlayerHand();
        testHand.addCard(testCardKH);
        testHand.addCard(testCardAD);
        assertEquals(2, testHand.getInPlay().size());
        assertTrue(testCardAD.containsAce(testHand.getInPlay()));

        testHand.addCard(testCardAS);
        assertEquals(3, testHand.getInPlay().size());
        assertTrue(testCardAD.containsAce(testHand.getInPlay()));

        PlayerHand testHand2 = new PlayerHand();
        testHand2.addCard(testCard6S);
        testHand2.addCard(testCardKH);
        testHand2.addCard(testCard5S);
        assertEquals(3, testHand2.getInPlay().size());
        assertFalse(testCardAD.containsAce(testHand2.getInPlay()));

    }

}
