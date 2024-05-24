import constants.CardSuit;

import java.util.*;

public class Deck {
    List<Card> cards;
    private final String[] ranks = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
    public Deck() {
        initializeCards();
        shuffleCards();
    }

    private void initializeCards() {
        cards = new Vector<>();

        cards.addAll(getCardsBySuit(CardSuit.Diamonds));
        cards.addAll(getCardsBySuit(CardSuit.Hearts));
        cards.addAll(getCardsBySuit(CardSuit.Spades));
        cards.addAll(getCardsBySuit(CardSuit.Clubs));

        cards.add(new Card(CardSuit.Joker, ""));
    }

    private void shuffleCards() {
        for (int i = 0; i < 5; i++)
            Collections.shuffle(cards);
    }

    public void deal(Player []players) {
        int numberOfPlayers = players.length;

        int toDistributeCards = cards.size() / numberOfPlayers;
        int remainingCards = cards.size() % numberOfPlayers;

        // remainingCards ensure that all cards distributed.
        for (Player player : players) {
            List<Card> handCards = new Vector<>();
            for (int j = 0; j < toDistributeCards; j++) {
                handCards.add(cards.remove(0));
            }
            if (remainingCards > 0) {
                handCards.add(cards.remove(0));
                remainingCards--;
            }
            player.setHandCards(handCards);
            System.out.println(player.getPlayerName() + " cards: " + handCards);
        }
    }

    private List<Card> getCardsBySuit(CardSuit suit) {
        List<Card> cards = new Vector<>();

        for (String rank: ranks)
            cards.add(new Card(suit, rank));

        return cards;
    }
}
