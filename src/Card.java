import constants.*;

import java.util.Objects;

public class Card {
    private final CardSuit suit;
    private final String rank;
    private CardColor color;

    public Card(CardSuit suit, String rank) {
        this.suit = suit;
        this.rank = rank;
        determineColor();
    }
    private void determineColor() {
        color = CardColor.Gray;
        if (suit == CardSuit.Diamonds || suit == CardSuit.Hearts) {
            color = CardColor.Red;
        } else if (suit == CardSuit.Spades || suit == CardSuit.Clubs) {
            color = CardColor.Black;
        }
    }

    public boolean isMatch(Card card) {
        return card != null && color == card.getColor() && Objects.equals(rank, card.getRank());
    }

    @Override
    public String toString() {
        return suit.getUnicode() + rank + ConsoleColors.RESET;
    }

    @Override
    public boolean equals(Object obj) {
        try {
            return toString().equals(obj.toString());
        } catch (Exception e) {
            return false;
        }
    }

    // Getters
    public String getRank() {
        return rank;
    }
    public CardColor getColor() {
        return color;
    }
}
