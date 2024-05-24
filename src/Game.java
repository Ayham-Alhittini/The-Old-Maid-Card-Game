import constants.ConsoleColors;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class Game {
    private Player []players;

    public void play() {
        try {
            initializeGame(promptNumberOfPlayers());
            startPlayers();
            waitGameFinish();
            printGameReport();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(130);
        }
    }

    private void initializeGame(int numberOfPlayers) {
        GameContext.gameFinishWaiter = new CountDownLatch(numberOfPlayers);
        GameContext.barrier = new CyclicBarrier(numberOfPlayers);
        players = new Player[numberOfPlayers];
        Deck deck = new Deck();

        createPlayers();
        createCyclicTurn();
        deck.deal(players);
        GameContext.setCurrentPlayer(players[0]);
        System.out.println();
    }

    private void createPlayers() {
        for (int i = 0; i < players.length; i++) {
            players[i] = new Player("Player" + (i + 1));
        }
    }

    private void createCyclicTurn() {
        players[0].setPreviousPlayer(players[players.length - 1]);
        players[0].setNextPlayer(players[1]);
        for (int i = 1; i < players.length - 1; i++) {
            players[i].setPreviousPlayer(players[i - 1]);
            players[i].setNextPlayer(players[i + 1]);
        }
        players[players.length - 1].setNextPlayer(players[0]);
        players[players.length - 1].setPreviousPlayer(players[players.length - 2]);
    }

    private void startPlayers() {
        for (Player player : players) player.start();
    }

    private void waitGameFinish() throws InterruptedException{
        GameContext.gameFinishWaiter.await();
    }

    private void printGameReport() {
        System.out.println("\n\n\n---------------------------------------------------------");
        System.out.println("Game finish with the following report:");
        System.out.println("Old maid player: " + GameContext.getOldMaidPlayer());
        System.out.println("Number of played turn: " + GameContext.getPlayedTurnCount());
        System.out.println("First finished player: " + GameContext.getFirstFinishedPlayer());
    }

    private int promptNumberOfPlayers() {
        Scanner scanner = new Scanner(System.in);
        int numberOfPlayers = -1;
        while (numberOfPlayers == -1) {
            System.out.print("Enter number of players: ");
            numberOfPlayers = scanner.nextInt();

            int MAX_NUMBER_OF_PLAYERS = 20;

            if (numberOfPlayers < 2) {
                System.out.println(ConsoleColors.RED + "Min number is 2" + ConsoleColors.RESET);
                numberOfPlayers = -1;
            }

            else if (numberOfPlayers > MAX_NUMBER_OF_PLAYERS) {
                System.out.println(ConsoleColors.RED + "Max number is " + MAX_NUMBER_OF_PLAYERS + ConsoleColors.RESET);
                numberOfPlayers = -1;
            }
        }
        return numberOfPlayers;
    }
}
