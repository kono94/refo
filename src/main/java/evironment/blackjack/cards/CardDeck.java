package evironment.blackjack.cards;

import core.RNG;

import java.util.ArrayList;

public class CardDeck {
    private ArrayList<Card> cards;

    public CardDeck() {
        cards = new ArrayList<>(Suit.values().length * Rank.values().length);
        for(Suit s : Suit.values()) {
            for(Rank r : Rank.values()) {
                Card c = new Card(s, r);
                cards.add(c);
            }
        }
    }

    /**
     * We assume that cards are dealt from an infinite deck (i.e., with replacement)
     * so that there is no advantage to keeping track of the cards already dealt.
     * Therefore no card is removed from the deck.
     *
     * @return next card
     */
    public Card nextCard() {
         /*
            nextInt(int bound) returns random int value from (inclusive) 0
            and EXCLUSIVE! bound
          */
        return cards.get(RNG.getRandom().nextInt(cards.size()));
    }
}
