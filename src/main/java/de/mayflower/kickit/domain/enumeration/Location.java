package de.mayflower.kickit.domain.enumeration;

/**
 * The Location enumeration.
 */
public enum Location {
    WUERZBURG("Wuerzburg"),
    BERLIN("Berlin"),
    MUNICH("Munich");

    private final String value;

    Location(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
