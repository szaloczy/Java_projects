package hu.blackjack;

import hu.blackjack.model.*;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<Card> deck = createDeck();
        Collections.shuffle(deck);

        Dealer dealer = new Dealer();
        List<HumanPlayer> players = List.of(new HumanPlayer("Player1"));

        List<AbstractPlayer> firstRoundOfDraws = new ArrayList<>(players);
        firstRoundOfDraws.add(dealer);
        firstRoundOfDraws.addAll(players);
        drawAllPlayers(deck, firstRoundOfDraws);



    }

    private static void drawAllPlayers(List<Card> deck, List<AbstractPlayer> players) {
        for (AbstractPlayer player : players) {
            player.draw(deck);
        }
    }

    private static List<Card> createDeck(){
        List<Card> deck = new ArrayList<>();
        for(Suit suit : Suit.values()){
            for(Rank rank : Rank.values()){
                deck.add(new Card(suit, rank));
            }
        }
        return deck;
    }
}
