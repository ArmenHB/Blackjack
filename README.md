# Armen's Blackjack Table

## card-gaming for everyone!

**Introduction**

Hello folks, for my CPSC 210 personal project, I will be creating a Blackjack simulator.

Users will be able to save and load their Blackjack games, as well as choose an arbitrary amount of decks to play with.
This project should perfectly replicate the game of blackjack, with players being able to split, double, bet, and stand
as needed. Furthermore, the program should be able to determine whether an ace should be a 1 or an 11 for a given hand,
the dealer must be programmed to act like a real-life dealer, and payouts must be adjusted to be different based on the
type of win (a blackjack victory will pay more than a dealer bust).

The only major difference will be the number of decks in the "shoe"; that's something I will let players choose.

**Who will use this application?**

I think that this application is great for new players who want to learn how to play blackjack quickly, without gambling
real money. Furthermore, it can help players learn to count cards and play optimally if they choose to play with a 
low number of decks.

**Why is this project of interest to me**

I personally quite enjoy blackjack, and being able to play and increase my skill without spending real money appeals to
me. Also, I have some family members and friends who have unhealthy relationships with gambling, so bringing a way for
people to enjoy the game without gambling away their money is something I would love to do.

## User Stories:
- As a user, I want to be able to choose an arbitrary amount of decks with which we will play.
- As a user, I want to be able to add cards from the deck to form a hand.
- As a user, I want to be able to "double" to double my bet, get one more card in my hand, and have my turn auto-end.
- As a user, I want to be able to see the dealer's first card in his hand, so that I can choose what to play.
- As a user, I want to be able to view my hand and the dealer's hand as a list of cards before interacting.
- As a user, I want to be paid out accordingly if I win, and have that money added to my "bank".
- As a user, when quitting, I want the option of saving my game to keep the same deck and balance.
- As a user, when starting the game, I want to be able to load my saved games.

# Instructions for Grader
- when starting up and clicking "new game", you can choose an arbitrary amount of Cards to add to your Deck, 
by typing in the number of decks. This will later be visible on the leftside panel.
- to be able to add cards to your hand, simply deal, place a bet, and then choose "hit" to get a card.
- you can double to add a card and auto-end your turn by pressing "double" during your turn.
- The visual element should be seen on the top (for the dealer's) and bottom (for your hand) of the screen, 
when play begins.
- You can save your game by clicking "quit" after a turn is over, and then choosing "save & quit"
- You can load the game by choosing "load game" when starting up the application.

# Phase 4 : Task 2
Here is an example of a log which would happen when a player chooses to use 2 decks, plays two hands, (betting $100
each time) and wins one with a nice double, while losing the other. 

Deck created with 104 cards!

Player bet $100.

Player bank is now $900.

Player drew Seven of Clubs, which was removed from the deck.

Player drew Two of Spades, which was removed from the deck.

Dealer drew Six of Hearts, and Two of Diamonds, which were removed from the deck.

Player doubled his bet!! it is now $200 ,and his bank is now $800.

Player drew Seven of Diamonds, which was removed from the deck.

Dealer drew Eight of Diamonds, which was removed from the deck.

Dealer drew Queen of Hearts, which was removed from the deck.

Player bank is now $1200

Dealer and Player cleared their hands!

Player bet $100.

Player bank is now $1100.

Player drew King of Hearts, which was removed from the deck.

Player drew Six of Diamonds, which was removed from the deck.

Dealer drew Five of Clubs, and Jack of Clubs, which were removed from the deck.

Player drew Nine of Clubs, which was removed from the deck.

Dealer drew Seven of Spades, which was removed from the deck.

Player bank is now $1100

Dealer and Player cleared their hands!

# Phase 3: Task 4
Here is my UML diagram:


![Diagram](graphics/BJ_UML.png)


As for what I can refactor, I realize that a missed opportunity would be to create a single abstract
"hand" class.
By doing so, I could more easily create separate "DealerHand" and "PlayerHand" classes, without having some
duplicate methods.

Secondly, I would love to separate the graphics from the BlackJackGame class, and make them their own class,
as currently, it is quite messy and difficult to understand my so called "front-end" code. By separating the graphics
into their own class, I'm sure it would be  a lot easier to understand and modify the GUI.
