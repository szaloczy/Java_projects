package hu.blackjack.model;

import java.util.List;

import static hu.blackjack.model.Hand.BLACK_JACK_VALUE;
import static hu.blackjack.model.PlayerStatus.*;

public class Dealer extends AbstractPlayer{

    private static final int TARGET_HAND_VALUE = 17;

    public Dealer() {
        super("Bank");
    }

    @Override
    public void draw(List<Card> deck){
        if(status != PLAYING){
            throw new IllegalStateException("Cannot throw in "+ status + " status!");
        }
        hand.addCard(deck.remove(0));
        int value = hand.getValue();
        if(value > TARGET_HAND_VALUE) {
            status = FINISHED;
        }
        if(value > BLACK_JACK_VALUE){
            status = BUSTED;
        }
    }
}
