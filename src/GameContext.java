import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class GameContext {
    private static Player currentPlayer, oldMaidPlayer, firstFinishedPlayer;
    private static int playedTurnCount = 0;
    public static final Object turnWaiter = new Object();
    public static CountDownLatch gameFinishWaiter;
    public static CyclicBarrier barrier;

    public static void playerFinish(Player player) {
        if (firstFinishedPlayer == null)
            firstFinishedPlayer = player;
        else
            oldMaidPlayer = player;
    }
    public static void setCurrentPlayer(Player player) {
        currentPlayer = player;
    }
    public static void incrementPlayedTurnCount() {
        playedTurnCount++;
    }
    public static int getPlayedTurnCount() {
        return playedTurnCount;
    }
    public static Player getCurrentPlayer() {
        return currentPlayer;
    }
    public static Player getOldMaidPlayer() {
        return oldMaidPlayer;
    }
    public static Player getFirstFinishedPlayer() {
        return firstFinishedPlayer;
    }
}
