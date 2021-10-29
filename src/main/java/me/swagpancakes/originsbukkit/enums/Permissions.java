package me.swagpancakes.originsbukkit.enums;

/**
 * The enum Permissions.
 */
public enum Permissions {
    /**
     * Admin permissions.
     */
    ADMIN("origins.admin"),
    /**
     * User permissions.
     */
    USER("origins.user");

    private final Object value;

    Permissions(final Object value) {
        this.value = value;
    }

    /**
     * To string string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return (String) this.value;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public Object getValue() {
        return this.value;
    }
}
