package me.lemonypancakes.originsbukkit.enums;

import org.bukkit.ChatColor;

/**
 * The enum Impact.
 */
public enum Impact {
    NONE(0, "none", ChatColor.GRAY),
    LOW(1, "low", ChatColor.GREEN),
    MEDIUM(2, "medium", ChatColor.YELLOW),
    HIGH(3, "high", ChatColor.RED);

    private final int impactValue;
    private final String value;
    private final ChatColor color;

    /**
     * Instantiates a new Impact.
     *
     * @param impactValue the impact value
     * @param value       the value
     * @param color       the color
     */
    Impact(int impactValue, String value, ChatColor color) {
        this.impactValue = impactValue;
        this.value = value;
        this.color = color;
    }

    /**
     * To string string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "Impact{" +
                "impactValue=" + impactValue +
                ", value='" + value + '\'' +
                ", color=" + color +
                '}';
    }

    /**
     * Gets impact value.
     *
     * @return the impact value
     */
    public int getImpactValue() {
        return impactValue;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Gets color.
     *
     * @return the color
     */
    public ChatColor getColor() {
        return color;
    }
}
