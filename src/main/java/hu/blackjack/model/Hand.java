package hu.blackjack.model;

import java.util.ArrayList;
import java.util.List;

public class Hand {

    public static final int BLACK_JACK_VALUE = 21;
    private final List<Card> cards = new ArrayList<>();

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
}
