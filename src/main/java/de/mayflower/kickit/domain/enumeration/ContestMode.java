package de.mayflower.kickit.domain.enumeration;

/**
 * The ContestMode enumeration.
 */
public enum ContestMode {
    ONE("One_Game"),
    BO3("Best_Of_3"),
    BO5("Best_Of_5");

    private final String value;

    ContestMode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
