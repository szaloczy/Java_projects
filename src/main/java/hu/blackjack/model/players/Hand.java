package hu.blackjack.model.players;

import hu.blackjack.model.cards.Card;
import hu.blackjack.model.cards.Rank;

import java.util.ArrayList;
import java.util.List;

public class Hand {

    public static final int BLACK_JACK_VALUE = 21;
    private final List<Card> cards = new ArrayList<>();
    private int bet;

    public Hand(int bet){
        this.bet = bet;
    }

    public void addCard(Card card){
        cards.add(card);
    }

    public int getValue(){
        int value = 0;
        int numberOfAces = 0;
        for(Card card : cards){
            if(card.rank() != Rank.ACE){
                value += card.rank().value;
            } else {
                numberOfAces++;
            }
        }
        for(int i = 0; i < numberOfAces; i++){
            if(value + Rank.ACE.value > BLACK_JACK_VALUE){
                value += 1;
            } else {
                value += Rank.ACE.value;
            }
        }

        return value;
    }

    @Override
    public String toString() {
        int value = getValue();
        String valueAsString;
        if(value == BLACK_JACK_VALUE && cards.size() == 2){
            valueAsString = "BLACKJACK";
        }else {
            valueAsString = Integer.toString(value);
        }
        return cards +" (" + valueAsString + ") ";
    }

    public int getNumberOfCards() {
        return cards.size();
    }

    public int getBet() {
        return bet;
    }

    public void doubleBet() {
        bet *= 2;
    }

    public Card getCard(int index) {
        return cards.get(index);
    }
}
