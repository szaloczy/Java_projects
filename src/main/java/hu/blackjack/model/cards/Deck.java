package hu.blackjack.model.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private static final List<Card> STANDARD_DECK = createDeck();
    private List<Card> deck;
    private final int amountOfStandardDecks;

    public Deck(int amountOfStandardDecks){
        this.amountOfStandardDecks = amountOfStandardDecks;
        init();
    }

    public Card getCard() {
        if(deck.isEmpty()){
            reset();
        }
        return deck.remove(0);
    }

    private void init(){
        deck = new ArrayList<>();
        for(int i = 0; i < amountOfStandardDecks; i++){
            deck.addAll(STANDARD_DECK);
        }
        Collections.shuffle(deck);
    }

    private static List<Card> createDeck(){
        List<Card> deck = new ArrayList<>();
        for(Suit suit : Suit.values()){
            for(Rank rank : Rank.values()){
                deck.add(new Card(suit, rank));
            }
        }
        return List.copyOf(deck);
    }

    public void reset() {
        init();
    }
}
