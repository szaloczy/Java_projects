package hu.blackjack;

import hu.blackjack.model.RoundResults;
import hu.blackjack.model.cards.Deck;
import hu.blackjack.model.players.*;

import java.util.*;

public class Blackjack {

    private Deck deck = new Deck(1);
    private Dealer dealer = new Dealer();
    private List<HumanPlayer> players = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);

    public void setup(){

        System.out.println("Blackjack game");
        while(true){
            try {
                System.out.print("How many players would like to play? ");
                String userInput = scanner.nextLine();
                int numberOfPlayers = Integer.parseInt(userInput);

                for(int i = 0; i < numberOfPlayers; i++){
                    System.out.println("Name of player #" + i + "? ");
                    userInput = scanner.nextLine();
                    if (userInput.isBlank()) {
                        userInput = "Player #" + i;
                    }
                    players.add(new HumanPlayer(userInput));
                }
                break;
            } catch (NumberFormatException e){
                System.out.println("Invalid input, please try again!");
            }
        }
    }

    public void play(){
        while(hasPlayerWithBudget()){
            collectBets();
            if(!hasPlayerWillingToPlay()) {
                break;
            }
            initialDraw();
            actionDraws();
            evaluateRound();
        }
        displayPostGameStatus();

    }

    private boolean hasPlayerWithBudget() {
        for (HumanPlayer player : players) {
            if(player.getBudget() > 0){
                return true;
            }
        }
        return false;
    }

    private boolean hasPlayerWillingToPlay() {
        for (HumanPlayer player : players) {
                if(player.getStatus() == PlayerStatus.PLAYING){
                    return true;
                }
        }
        return false;
    }

    private void displayPostGameStatus() {
        players.sort(new PlayerBudgetComparator().reversed());
        for (HumanPlayer player : players) {
            System.out.println(player.getName() + " finished with " + player.getBudget() + " tokens");
        }
    }

    private void collectBets() {
        System.out.println("Please make your bets!");
        System.out.println("(place 0 bet, if you would like to skip this round)");
        for (HumanPlayer player : players) {
            while(true){
                try {
                    System.out.printf("%s's bet (0 - %d): ",player.getName(), player.getBudget());
                    String userInput = scanner.nextLine();
                    player.createHand(Integer.parseInt(userInput));
                    break;
                } catch (NumberFormatException e){
                    System.out.println("Invalid input, please try again!");
                } catch (IllegalArgumentException e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    private void initialDraw() {
        List<AbstractPlayer> firstRoundOfDraws = new ArrayList<>(players);
        firstRoundOfDraws.add(dealer);
        firstRoundOfDraws.addAll(players);
        drawAllPlayers(deck, firstRoundOfDraws);
    }

    private void actionDraws() {
        for(HumanPlayer player : players){
            System.out.println(player);
            while(player.getStatus() == PlayerStatus.PLAYING) {
                List<Action> actions = player.getAvailableActions();
                System.out.print("Actions: " + getActionLabels(actions) + "? ");
                String userInput = scanner.nextLine();
                Optional<Action> selectedAction = findActionByCommand(actions, userInput);
                if(selectedAction.isPresent()){
                    player.apply(selectedAction.get(), deck);
                    System.out.println(player);
                } else {
                    System.out.println("Unknown Action commands");
                }
            }
        }

        //Deal should draw cards
        if(isAnyPlayerIn(players, Set.of(PlayerStatus.BLACKJACK, PlayerStatus.STANDING))) {
            while (dealer.getStatus() == PlayerStatus.PLAYING) {
                dealer.draw(deck);
            }
            System.out.println(dealer);
        } else {
            System.out.println(dealer.getName() + " skips drawing cards");
        }
    }

    private void evaluateRound() {
        for (HumanPlayer player : players) {
            RoundResults results = switch (player.getStatus()){
                case BUSTED -> new RoundResults(player.getName() + " busted and lost", 0);
                case SURRENDERED -> new RoundResults(player.getName() + " surrendered", 0.5);
                case BLACKJACK ->  handlePlayerBlackJack(player, dealer);
                case STANDING -> handlePlayerStanding(player, dealer);
                case PLAYING -> throw new IllegalStateException(player.getName() + " + should not be in + " + player.getStatus() + " status");
                case SKIPPED -> new RoundResults(player.getName() + " skipped this round", 0);
            };
            player.collectReward(results.multiplier());
            System.out.println(results.message());
            System.out.println(player.getName() + "'s budget :" + player.getBudget());
        }
    }


    public static void main(String[] args) {
        Blackjack game = new Blackjack();
        game.setup();
        game.play();
    }

    private static boolean isAnyPlayerIn(List<HumanPlayer> players, Set<PlayerStatus> desiredStatuses) {
        for (HumanPlayer player : players) {
            if(desiredStatuses.contains(player.getStatus())){
                return true;
            }
        }
        return false;
    }

    private static RoundResults handlePlayerStanding(HumanPlayer player, Dealer dealer) {
         String playerName = player.getName();
         String dealerName = dealer.getName();
        if(dealer.getStatus() == PlayerStatus.BUSTED){
           return new RoundResults(playerName + " won, because " + dealerName + " busted", 2);
        }
        if(dealer.getHandValue() > player.getHandValue()){
           return new RoundResults(playerName + " lost to " + dealerName + " by having less points", 0);
        } else if(dealer.getHandValue() == player.getHandValue()) {
            return new RoundResults(playerName + " is in tie with " + dealerName, 1);
        } else {
           return new RoundResults(playerName + " won", 2);
        }

    }

    private static RoundResults handlePlayerBlackJack(HumanPlayer player, Dealer dealer) {
        final String playerName = player.getName();
        if(dealer.getStatus() == PlayerStatus.BLACKJACK) {
           return new RoundResults(playerName + " lost, bacause " + dealer.getName() +" has BLACKJACK too", 0);
        } else {
            return new RoundResults(playerName + " won with BLACKJACK", 2.5);
        }
    }

    private static Optional<Action> findActionByCommand(List<Action> actions, String userInput) {
        for (Action action : actions) {
            if(Character.toString(action.command).equalsIgnoreCase(userInput)){
                return  Optional.of(action);
            }
        }
        return Optional.empty();
    }

    private static String getActionLabels(List<Action> actions) {
        StringJoiner joiner = new StringJoiner(", ");
        for (Action action : actions) {
            joiner.add(action.label);
        }
        return joiner.toString();
    }

    private static void drawAllPlayers(Deck deck, List<AbstractPlayer> players) {
        for (AbstractPlayer player : players) {
           if(player.getStatus() == PlayerStatus.PLAYING){
               player.draw(deck);
           }
        }
    }
}
