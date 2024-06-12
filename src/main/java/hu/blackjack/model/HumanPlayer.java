package hu.blackjack.model;

import java.util.List;

import static hu.blackjack.model.PlayerStatus.SURRENDERED;

public class HumanPlayer extends AbstractPlayer{

    public HumanPlayer(String name) {
        super(name);
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

}
