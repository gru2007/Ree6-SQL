package de.presti.ree6.sql.entities;

import de.presti.ree6.sql.keys.GuildUserId;
import jakarta.persistence.*;

/**
 * Class that represents the OptOut Database Entity.
 */
@Entity
@Table(name = "Opt_out", indexes = @Index(columnList = "userId, guildId"))
public class OptOut {

    /**
     * The PrimaryKey of the Entity.
     */
    @EmbeddedId
    GuildUserId guildUserId;

    /**
     * Constructor.
     */
    public OptOut() {
    }

    /**
     * Constructor.
     *
     * @param guildId the Guild ID.
     * @param userId the User ID.
     */
    public OptOut(long guildId, long userId) {
        guildUserId = new GuildUserId(guildId, userId);
    }

    /**
     * Set the Guild ID.
     * @param guildId The Guild ID.
     */
    public void setGuildId(long guildId) {
        if (guildUserId == null) guildUserId = new GuildUserId(guildId, 0);
        guildUserId.setGuildId(guildId);
    }

    /**
     * Set the User ID.
     * @param userId The User ID.
     */
    public void setUserId(long userId) {
        if (guildUserId == null) guildUserId = new GuildUserId(0, userId);
        guildUserId.setUserId(userId);
    }

    /**
     * Get the Guild ID.
     * @return The Guild ID.
     */
    public long getGuildId() {
        if (guildUserId == null) return -1;
        return guildUserId.getGuildId();
    }

    /**
     * Get the User ID.
     * @return The User ID.
     */
    public long getUserId() {
        if (guildUserId == null) return -1;
        return guildUserId.getUserId();
    }
}
