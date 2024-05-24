/*
* draw last card case
* */
import constants.CardSuit;
import constants.ConsoleColors;

import java.util.List;
import java.util.Objects;
import java.util.Vector;
public class Player extends Thread{

    private final String name;
    private Player previousPlayer, nextPlayer;
    private List<Card> handCards;

    public Player(String name) {
        this.name = name;
        handCards = new Vector<>();
    }

    @Override
    public void run() {
        try {
            throwAllMatchingPairs();
            waitAllPlayersReach();
            turnBasedGame();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(130);
        }
    }

    private void throwAllMatchingPairs() {
        for (int i = 0; i < handCards.size(); i++) {
            Card firstCard = handCards.get(i);
            for (int j = i + 1; firstCard != null && j < handCards.size(); j++) {
                Card secondCard = handCards.get(j);

                if (firstCard.isMatch(secondCard)) {
                    System.out.println(getPlayerName() + " throw " + firstCard + " and " + secondCard);
                    handCards.set(i, null);
                    handCards.set(j, null);
                    break;
                }

            }
        }
        handCards.removeIf(Objects::isNull);
    }

    private void waitAllPlayersReach() throws Exception{
        GameContext.barrier.await();
    }

    private void turnBasedGame() throws Exception {
        synchronized (GameContext.turnWaiter) {
            while (!isPlayerFinish()) {
                while (!isPlayerTurn()) {
                    GameContext.turnWaiter.wait();
                    // isPlayerFinish() replaced with isEmpty() to stop players who run out of cards and,
                    // and not the joker holder.
                    if (handCards.isEmpty()) return;
                }
                performTurnAction();
                if (!isPlayerFinish())
                    goNextTurn();
            }
            removePlayer();
            goNextTurn();
        }
    }

    private boolean isPlayerFinish() {
        if (handCards.isEmpty())
            return true;
        else if (handCards.size() == 1 && equals(getPreviousPlayer()))
            return handCards.get(0).equals(new Card(CardSuit.Joker, ""));
        return false;
    }

    private boolean isPlayerTurn() {
        return GameContext.getCurrentPlayer().getPlayerName().equals(getPlayerName());
    }

    private void performTurnAction() {
        if (!isPlayerFinish()) {
            System.out.println("\n" + getPlayerName() + " turn: " + handCards);
            GameContext.incrementPlayedTurnCount();
            drawFromPreviousPlayer();
            throwAllMatchingPairs();
        }
    }

    private void drawFromPreviousPlayer() {
        if (!equals(getPreviousPlayer())) {
            int cardIndex = (int) (Math.random() * getPreviousPlayer().getHandCardsSize());
            Card card = getPreviousPlayer().sendCardToAskingPlayer(cardIndex);
            handCards.add(card);
            System.out.println(getPlayerName() + " draw " + card + " from " + getPreviousPlayer().getPlayerName());
            if (getPreviousPlayer().isPlayerFinish())
                getPreviousPlayer().removePlayer();
        }
    }

    private Card sendCardToAskingPlayer(int cardIndex) {
        Card card = handCards.get(cardIndex);
        handCards.remove(cardIndex);
        return card;
    }

    private void goNextTurn() {
        GameContext.setCurrentPlayer(getNextPlayer());
        GameContext.turnWaiter.notifyAll();
    }

    private void removePlayer() {
        Player previousPlayer = getPreviousPlayer();
        Player nextPlayer = getNextPlayer();

        previousPlayer.setNextPlayer(nextPlayer);
        nextPlayer.setPreviousPlayer(previousPlayer);

        System.out.println(ConsoleColors.GREEN + getPlayerName() + " is finish." + ConsoleColors.RESET);
        GameContext.gameFinishWaiter.countDown();
        GameContext.playerFinish(this);
    }


    // Getters and setters
    public void setHandCards(List<Card> handCards) {
        this.handCards = handCards;
    }
    public void setPreviousPlayer(Player previousPlayer) {
        this.previousPlayer = previousPlayer;
    }
    public void setNextPlayer(Player nextPlayer) {
        this.nextPlayer = nextPlayer;
    }
    public String getPlayerName() {
        return name;
    }
    public Player getPreviousPlayer() {
        return previousPlayer;
    }
    public Player getNextPlayer() {
        return nextPlayer;
    }
    public int getHandCardsSize() {
        return handCards.size();
    }
    @Override
    public String toString() {
        return name;
    }
    @Override
    public boolean equals(Object obj) {
        try {
            Player otherPlayer = (Player) obj;
            return name.equals(otherPlayer.getPlayerName());
        } catch (Exception e) {
            return false;
        }
    }
}