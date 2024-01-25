package de.presti.ree6.sql.entities.level;

import de.presti.ree6.sql.SQLSession;
import de.presti.ree6.sql.util.LevelUtil;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Utility class to store information about a Users
 * Experience and their Level.
 */
@Entity
@Table(name = "Level")
public class ChatUserLevel extends UserLevel {

    /**
     * Constructor.
     */
    public ChatUserLevel() {
    }


    /**
     * Constructor to create a UserLevel with the needed Data.
     *
     * @param guildId    the ID of the Guild.
     * @param userId     the ID of the User.
     * @param experience his XP count.
     */
    public ChatUserLevel(long guildId, long userId, long experience) {
        super(guildId, userId, experience);
    }

    /**
     * Constructor to create a UserLevel with the needed Data.
     *
     * @param guildId    the ID of the Guild.
     * @param userId     the ID of the User.
     * @param experience his XP count.
     * @param level      his Level.
     */
    public ChatUserLevel(long guildId, long userId, long experience, long level) {
        super(guildId, userId, experience, level);
    }
}
