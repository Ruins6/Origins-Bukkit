package me.swagpancakes.originsbukkit.api.util;

import org.bukkit.Material;
import org.bukkit.event.Listener;

/**
 * The interface Origin interface.
 */
public interface OriginInterface extends Listener {

    /**
     * Gets origin identifier.
     *
     * @return the origin identifier
     */
    String getOriginIdentifier();

    /**
     * Gets author.
     *
     * @return the author
     */
    String getAuthor();

    /**
     * Gets origin icon.
     *
     * @return the origin icon
     */
    Material getOriginIcon();

    /**
     * Is origin icon glowing boolean.
     *
     * @return the boolean
     */
    boolean isOriginIconGlowing();

    /**
     * Gets origin title.
     *
     * @return the origin title
     */
    String getOriginTitle();

    /**
     * Get origin description string [ ].
     *
     * @return the string [ ]
     */
    String[] getOriginDescription();
}
