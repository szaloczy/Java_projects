package hu.blackjack.model;

import java.util.List;

import static hu.blackjack.model.PlayerStatus.SKIPPED;
import static hu.blackjack.model.PlayerStatus.SURRENDERED;

public class HumanPlayer extends AbstractPlayer{

    private int budget;

    public HumanPlayer(String name) {
        super(name);
        this.budget = 50;
    }

    @Override
    public List<Action> getAvailableActions() {
        if(status != PlayerStatus.PLAYING){
            throw new IllegalStateException("There are no actions in" + status + " status!");
        }
        if(hand.getNumberOfCards() == 2){
            return List.of(Action.HIT, Action.STAND, Action.SURRENDER);
        } else {
            return List.of(Action.HIT, Action.STAND);
        }
    }

    @Override
    public void apply(Action action, List<Card> deck) {
        if(status != PlayerStatus.PLAYING){
            throw new IllegalStateException("No actions should be apply in" + status + " status!");
        }
        switch (action){
            case HIT -> draw(deck);
            case STAND -> status = PlayerStatus.STANDING;
            case SURRENDER-> status = SURRENDERED;
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
}
