package de.mayflower.kickit.domain.enumeration;

/**
 * The Team enumeration.
 */
public enum Team {
    T1("Team1"),
    T2("Team2");

    private final String value;

    Team(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
