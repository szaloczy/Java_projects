package hu.blackjack.model.players;

import hu.blackjack.model.cards.Deck;

import static hu.blackjack.model.players.PlayerStatus.*;

public class HumanPlayer extends AbstractPlayer{

    private int budget;

    public HumanPlayer(String name) {
        super(name);
        this.budget = 50;
    }

    public void executeDoubleAction(Deck deck) {
        draw(deck);
        budget -= hand.getBet();
        hand.doubleBet();
        if (status == PLAYING){
            status = STANDING;
        }
    }

    public void createHand(int bet){
        if(bet < 0) {
            throw new IllegalArgumentException("Bet cannot be negative!");
        }
        if(bet > budget){
            throw new IllegalArgumentException("Bet (" + bet +") cannot be greater than player budget (" + budget + ")");
        }
        if(bet != 0) {
            hand = new Hand(bet);
            budget -= bet;
            status = PLAYING;
        } else {
            status = SKIPPED;
        }
    }

    public void collectReward(double multiplier){
       if(status != SKIPPED){
           budget += hand.getBet() * multiplier;
           hand = null;
       }
    }

    public int getBudget(){
        return budget;
    }

    public void setStatus(PlayerStatus playerStatus) {
        if(status != PLAYING){
            throw new IllegalStateException("Status can be set only for player in " + PLAYING + " status");
        }
    }
}
