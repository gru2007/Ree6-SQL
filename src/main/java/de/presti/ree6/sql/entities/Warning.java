package de.presti.ree6.sql.entities;

import de.presti.ree6.sql.keys.GuildUserId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * An Entity representing the warnings a user has received on a specific guild.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Warning", indexes = @Index(columnList = "userId, guildId"))
public class Warning {

    /**
     * The primary key for the entity.
     */
    @EmbeddedId
    GuildUserId guildUserId;

    /**
     * The warnings that the user has.
     */
    int warnings;

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
