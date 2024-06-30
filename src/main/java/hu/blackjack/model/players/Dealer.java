package hu.blackjack.model.players;

import hu.blackjack.model.cards.Card;
import hu.blackjack.model.cards.Deck;
import hu.blackjack.model.cards.Rank;

import java.util.List;

import static hu.blackjack.model.players.Hand.BLACK_JACK_VALUE;
import static hu.blackjack.model.players.PlayerStatus.*;

public class Dealer extends AbstractPlayer{

    private static final int TARGET_HAND_VALUE = 17;

    public Dealer() {
        super("Bank");
        hand = new Hand(0);
    }

    @Override
    public void draw(Deck deck){
        if(status != PLAYING){
            throw new IllegalStateException("Cannot throw in "+ status + " status!");
        }
        hand.addCard(deck.getCard());
        int value = hand.getValue();
        if(value > TARGET_HAND_VALUE) {
            if(hand.getValue() == BLACK_JACK_VALUE) {
                if(hand.getNumberOfCards() == 2 && value == BLACK_JACK_VALUE) {
                    status = BLACKJACK;
                } else {
                    status = STANDING;
                }
            }
        }
        if(value > BLACK_JACK_VALUE){
            status = BUSTED;
        }
    }

    public void resetHand() {
        hand = new Hand(0);
        status = PLAYING;
    }

    public boolean isFirstCardAce(){
        return hand.getCard(0).rank() == Rank.ACE;
    }
}
