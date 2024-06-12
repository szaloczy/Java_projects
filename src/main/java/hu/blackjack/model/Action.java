package hu.blackjack.model;

public enum Action {
    HIT("(h)it", 'h'),
    STAND("(s)tand", 's'),
    SURRENDER("s(u)rrender", 'u');

    public final String label;
    public final char command;

    Action(String label, char command) {
        this.label = label;
        this.command = command;
    }
}
