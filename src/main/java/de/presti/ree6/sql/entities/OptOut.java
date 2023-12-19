package de.presti.ree6.sql.entities;

import de.presti.ree6.sql.keys.GuildUserId;
import jakarta.persistence.*;

/**
 * Class that represents the OptOut Database Entity.
 */
@Entity
@Table(name = "Opt_out")
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
     * The ID of the Guild.
     * @return the ID.
     */
    public long getGuildId() {
        return guildUserId.getGuildId();
    }

    /**
     * Set the ID of the Guild.
     * @param guildId new ID.
     */
    public void setGuildId(long guildId) {
        this.guildUserId.setGuildId(guildId);
    }

    /**
     * The ID of the User.
     * @return the ID.
     */
    public long getUserId() {
        return guildUserId.getUserId();
    }

    /**
     * Set the ID of the User.
     * @param userId new ID.
     */
    public void setUserId(long userId) {
        this.guildUserId.setUserId(userId);
    }
}
