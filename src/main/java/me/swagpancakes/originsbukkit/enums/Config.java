package me.swagpancakes.originsbukkit.enums;

import me.swagpancakes.originsbukkit.util.ChatUtils;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * The enum Config.
 */
public enum Config {
    /**
     * Title config.
     */
    TITLE("title-name", "&4[&fAWSMPLUGIN&4]:"),
    /**
     * The Player is cool.
     */
    PLAYER_IS_COOL("player-is-cool", "&f%p is cool."),
    /**
     * The Invalid args.
     */
    INVALID_ARGS("invalid-args", "&cInvalid args!"),
    /**
     * The Player only.
     */
    PLAYER_ONLY("player-only", "Sorry but that can only be run by a player!"),
    /**
     * The Must be number.
     */
    MUST_BE_NUMBER("must-be-number", "&cYou need to specify a number, not a word."),
    /**
     * Test config.
     */
    TEST("test", "test"),
    /**
     * The No perms.
     */
    NO_PERMS("no-permissions", "&cYou don''t have permission for that!"),
    /**
     * Test 2 config.
     */
    TEST2("test2", true),
    /**
     * Test 3 config.
     */
    TEST3("test3", "a\na");

    private final String Path;
    private final Object defaultValue;

    private static YamlConfiguration CONFIG;

    Config(String Path, Object defaultValue) {
        this.Path = Path;
        this.defaultValue = defaultValue;
    }

    Config(String Path, String... defaultValue) {
        this.Path = Path;
        this.defaultValue = defaultValue;
    }

    /**
     * Sets file.
     *
     * @param config the config
     */
    public static void setFile(YamlConfiguration config) {
        CONFIG = config;
    }

    /**
     * To string string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        if (this == TITLE) {
            return ChatUtils.format(CONFIG.getString(this.Path, (String) defaultValue)) + " ";
        }
        return ChatUtils.format(CONFIG.getString(this.Path, (String) defaultValue));
    }

    /**
     * To string list string [ ].
     *
     * @return the string [ ]
     */
    public String[] toStringList() {
        return ChatUtils.formatList(CONFIG.getStringList(this.Path).toArray(new String[0]));
    }

    /**
     * Gets default value.
     *
     * @return the default value
     */
    public Object getDefaultValue() {
        return this.defaultValue;
    }

    /**
     * Get default string list value string [ ].
     *
     * @return the string [ ]
     */
    public String[] getDefaultStringListValue() {
        return (String[]) this.defaultValue;
    }

    /**
     * Gets path.
     *
     * @return the path
     */
    public String getPath() {
        return this.Path;
    }
}
