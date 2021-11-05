package me.swagpancakes.originsbukkit.enums;

import me.swagpancakes.originsbukkit.util.ChatUtils;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * The enum Config.
 */
public enum Config {
    /**
     * Origins blazeborn max health config.
     */
    ORIGINS_BLAZEBORN_MAX_HEALTH("Config.Origins.Blazeborn.Max-Health", 20),
    /**
     * Origins blazeborn water damage amount config.
     */
    ORIGINS_BLAZEBORN_WATER_DAMAGE_AMOUNT("Config.Origins.Blazeborn.Water-Damage.Amount", 1),
    /**
     * Origins blazeborn water damage delay config.
     */
    ORIGINS_BLAZEBORN_WATER_DAMAGE_DELAY("Config.Origins.Blazeborn.Water-Damage.Delay", 0L),
    /**
     * Origins blazeborn water damage period delay config.
     */
    ORIGINS_BLAZEBORN_WATER_DAMAGE_PERIOD_DELAY("Config.Origins.Blazeborn.Water-Damage.Period-Delay", 10L),
    /**
     * Origins enderian max health config.
     */
    ORIGINS_ENDERIAN_MAX_HEALTH("Config.Origins.Enderian.Max-Health", 20),
    /**
     * Origins enderian water damage amount config.
     */
    ORIGINS_ENDERIAN_WATER_DAMAGE_AMOUNT("Config.Origins.Enderian.Water-Damage.Amount", 1),
    /**
     * Origins enderian water damage delay config.
     */
    ORIGINS_ENDERIAN_WATER_DAMAGE_DELAY("Config.Origins.Enderian.Water-Damage.Delay", 0L),
    /**
     * Origins enderian water damage period delay config.
     */
    ORIGINS_ENDERIAN_WATER_DAMAGE_PERIOD_DELAY("Config.Origins.Enderian.Water-Damage.Period-Delay", 10L),
    /**
     * Origins enderian ability cooldown config.
     */
    ORIGINS_ENDERIAN_ABILITY_COOLDOWN("Config.Origins.Enderian.Ability.Cooldown", 2),
    /**
     * Origins merling max health config.
     */
    ORIGINS_MERLING_MAX_HEALTH("Config.Origins.Merling.Max-Health", 20),
    /**
     * Origins merling air breathing max time config.
     */
    ORIGINS_MERLING_AIR_BREATHING_MAX_TIME("Config.Origins.Merling.Air-Breathing.Max-Time", 120),
    /**
     * Origins merling air breathing damage amount config.
     */
    ORIGINS_MERLING_AIR_BREATHING_DAMAGE_AMOUNT("Config.Origins.Merling.Air-Breathing.Damage.Amount", 1),
    /**
     * Origins merling air breathing damage delay config.
     */
    ORIGINS_MERLING_AIR_BREATHING_DAMAGE_DELAY("Config.Origins.Merling.Air-Breathing.Damage.Delay", 0),
    /**
     * Origins merling air breathing damage period delay config.
     */
    ORIGINS_MERLING_AIR_BREATHING_DAMAGE_PERIOD_DELAY("Config.Origins.Merling.Air-Breathing.Damage.Period-Delay", 10),
    /**
     * Origins merling bossbar air breathing barcolor on decrease config.
     */
    ORIGINS_MERLING_BOSSBAR_AIR_BREATHING_BARCOLOR_ON_DECREASE("Config.Origins.Merling.Bossbar.Air-Breathing-Timer.BarColor.On-Decrease", BarColor.BLUE),
    /**
     * Origins merling bossbar air breathing barcolor on increase config.
     */
    ORIGINS_MERLING_BOSSBAR_AIR_BREATHING_BARCOLOR_ON_INCREASE("Config.Origins.Merling.Bossbar.Air-Breathing-Timer.BarColor.On-Increase", BarColor.GREEN),
    /**
     * Origins merling bossbar air breathing barcolor on drowning config.
     */
    ORIGINS_MERLING_BOSSBAR_AIR_BREATHING_BARCOLOR_ON_DROWNING("Config.Origins.Merling.Bossbar.Air-Breathing-Timer.BarColor.On-Drowning", BarColor.RED),
    /**
     * Origins merling bossbar air breathing barstyle on decrease config.
     */
    ORIGINS_MERLING_BOSSBAR_AIR_BREATHING_BARSTYLE_ON_DECREASE("Config.Origins.Merling.Bossbar.Air-Breathing-Timer.BarStyle.On-Decrease", BarStyle.SOLID),
    /**
     * Origins merling bossbar air breathing barstyle on increase config.
     */
    ORIGINS_MERLING_BOSSBAR_AIR_BREATHING_BARSTYLE_ON_INCREASE("Config.Origins.Merling.Bossbar.Air-Breathing-Timer.BarStyle.On-Increase", BarStyle.SOLID),
    /**
     * Origins merling bossbar air breathing barstyle on drowning config.
     */
    ORIGINS_MERLING_BOSSBAR_AIR_BREATHING_BARSTYLE_ON_DROWNING("Config.Origins.Merling.Bossbar.Air-Breathing-Timer.BarStyle.On-Drowning", BarStyle.SOLID);

    private static YamlConfiguration CONFIG;
    private final String Path;
    private final Object defaultValue;

    Config(final String Path, final Object defaultValue) {
        this.Path = Path;
        this.defaultValue = defaultValue;
    }

    Config(final String Path, final String... defaultValue) {
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
        return ChatUtils.format(CONFIG.getString(this.Path, (String) this.defaultValue));
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
     * To boolean boolean.
     *
     * @return the boolean
     */
    public boolean toBoolean() {
        return CONFIG.getBoolean(this.Path);
    }

    /**
     * To long long.
     *
     * @return the long
     */
    public long toLong() {
        return CONFIG.getLong(this.Path);
    }

    /**
     * To double double.
     *
     * @return the double
     */
    public double toDouble() {
        return CONFIG.getDouble(this.Path);
    }

    /**
     * To int int.
     *
     * @return the int
     */
    public int toInt() {
        return CONFIG.getInt(this.Path);
    }

    /**
     * To bar color bar color.
     *
     * @return the bar color
     */
    public BarColor toBarColor() {
        return BarColor.valueOf(CONFIG.getString(this.Path));
    }

    /**
     * To bar style bar style.
     *
     * @return the bar style
     */
    public BarStyle toBarStyle() {
        return BarStyle.valueOf(CONFIG.getString(this.Path));
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
