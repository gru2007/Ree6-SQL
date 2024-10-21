package de.presti.ree6.sql.entities.level;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * Utility class to store information about a Users
 * Experience and their Level.
 */
@Entity
@Table(name = "VCLevel", indexes = @Index(columnList = "userId, guildId"))
public class VoiceUserLevel extends UserLevel {

    /**
     * Constructor.
     */
    public VoiceUserLevel() {
    }

    /**
     * Constructor to create a UserLevel with the needed Data.
     *
     * @param guildId    the ID of the Guild.
     * @param userId     the ID of the User.
     * @param experience his XP count.
     */
    public VoiceUserLevel(long guildId, long userId, long experience) {
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
    public VoiceUserLevel(long guildId, long userId, long experience, long level) {
        super(guildId, userId, experience, level);
    }
}
