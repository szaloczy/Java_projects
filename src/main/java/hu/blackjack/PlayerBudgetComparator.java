package hu.blackjack;

import hu.blackjack.model.players.HumanPlayer;

import java.util.Comparator;

public class PlayerBudgetComparator implements Comparator<HumanPlayer> {

    @Override
    public int compare(HumanPlayer o1, HumanPlayer o2) {
        return Integer.compare(o1.getBudget(), o2.getBudget());
    }
}
