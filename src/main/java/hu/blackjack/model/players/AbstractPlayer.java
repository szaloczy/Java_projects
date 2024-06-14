package hu.blackjack.model.players;

import hu.blackjack.model.cards.Card;
import hu.blackjack.model.cards.Deck;

import java.util.List;

import static hu.blackjack.model.players.Hand.BLACK_JACK_VALUE;
import static hu.blackjack.model.players.PlayerStatus.*;
import static hu.blackjack.model.players.PlayerStatus.STANDING;

public abstract class AbstractPlayer {

    private final String name;
    protected PlayerStatus status = PLAYING;
    protected Hand hand;

    public AbstractPlayer(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public PlayerStatus getStatus(){ return status; }

    public void draw(Deck deck){
        if(status != PLAYING){
            throw new IllegalStateException("Cannot throw in "+ status + " status!");
        }
        hand.addCard(deck.getCard());
        int value = hand.getValue();
        if(value > BLACK_JACK_VALUE){
            status = BUSTED;
        }
        if(hand.getValue() == BLACK_JACK_VALUE) {
            if(hand.getNumberOfCards() == 2) {
                status = BLACKJACK;
            } else {
                status = STANDING;
            }
        }
    }

    @Override
    public String toString() {
        if(status == SKIPPED){
            return name + " skipped";
        }
        return name + ": " + hand;
    }

    public abstract  List<Action> getAvailableActions();

    public abstract void apply(Action action, Deck deck);

    public int getHandValue() {
        return hand.getValue();
    };
}
