package hu.blackjack.model;

import java.util.List;

import static hu.blackjack.model.Hand.BLACK_JACK_VALUE;
import static hu.blackjack.model.PlayerStatus.*;
import static hu.blackjack.model.PlayerStatus.FINISHED;

public abstract class AbstractPlayer {

    private final String name;
    protected PlayerStatus status;
    protected Hand hand = new Hand();

    public AbstractPlayer(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void draw(List<Card> deck){
        if(status != PLAYING){
            throw new IllegalStateException("Cannot throw in "+ status + " status!");
        }
        hand.addCard(deck.remove(0));
        int value = hand.getValue();
        if(value > BLACK_JACK_VALUE){
            status = BUSTED;
        }
        if(hand.getValue() == BLACK_JACK_VALUE) {
            status = FINISHED;
        }
    }
}
