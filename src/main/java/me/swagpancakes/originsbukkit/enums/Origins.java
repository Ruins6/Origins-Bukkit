package me.swagpancakes.originsbukkit.enums;

/**
 * The enum Origins.
 */
public enum Origins {
    /**
     * Human origins.
     */
    HUMAN("Human"),
    /**
     * Enderian origins.
     */
    ENDERIAN("Enderian"),
    /**
     * Merling origins.
     */
    MERLING("Merling"),
    /**
     * Phantom origins.
     */
    PHANTOM("Phantom"),
    /**
     * Elytrian origins.
     */
    ELYTRIAN("Elytrian"),
    /**
     * Blazeborn origins.
     */
    BLAZEBORN("Blazeborn"),
    /**
     * Avian origins.
     */
    AVIAN("Avian"),
    /**
     * Arachnid origins.
     */
    ARACHNID("Arachnid"),
    /**
     * Shulk origins.
     */
    SHULK("Shulk"),
    /**
     * Feline origins.
     */
    FELINE("Feline");

    private final String name;

    Origins(final String name) {
        this.name = name;
    }

    /**
     * To string string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return name;
    }
}
