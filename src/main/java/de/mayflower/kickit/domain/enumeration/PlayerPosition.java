package de.mayflower.kickit.domain.enumeration;

/**
 * The PlayerPosition enumeration.
 */
public enum PlayerPosition {
    FRONT("Front_Position"),
    BACK("Back_Position");

    private final String value;

    PlayerPosition(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
