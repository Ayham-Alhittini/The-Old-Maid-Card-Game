package constants;

public enum CardSuit {
    Spades, Hearts, Diamonds, Clubs, Joker;

    public String getUnicode() {
        return switch (this) {
            case Spades -> ConsoleColors.BLACK +  "♠";
            case Clubs -> ConsoleColors.BLACK + "♣";
            case Diamonds -> ConsoleColors.RED + "♦";
            case Hearts -> ConsoleColors.RED + "♥";
            default -> ConsoleColors.GRAY + "JOKER";
        };
    }
}
