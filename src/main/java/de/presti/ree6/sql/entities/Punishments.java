package de.presti.ree6.sql.entities;

import de.presti.ree6.sql.keys.GuildAndId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity class for the Warning punishments.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Punishments")
public class Punishments {

    /**
     * Primary key for the entity.
     */
    @EmbeddedId
    GuildAndId guildAndId;

    /**
     * The required warnings.
     */
    int warnings;

    /**
     * The action (1 -> timeout, 2 -> roleAdd, 3 -> roleRemove, 4 -> kick, 5 -> ban)
     */
    int action;

    /**
     * The Role id related to the role actions.
     */
    long roleId;

    /**
     * The Timeout time in milliseconds for the timeout action.
     */
    long timeoutTime;

    /**
     * The reason for the kick and ban action.
     */
    String reason;

    /**
     * Set the GuildID.
     * @param guildId The GuildID.
     */
    public void setGuildId(long guildId) {
        if (guildAndId == null) guildAndId = new GuildAndId(guildId);
        guildAndId.setGuildId(guildId);
    }

    /**
     * Set the ID.
     * @param id The ID.
     */
    public void setId(long id) {
        if (guildAndId == null) guildAndId = new GuildAndId(0, id);
        guildAndId.setId(id);
    }

    /**
     * Get the GuildID.
     *
     * @return {@link long} as GuildID.
     */
    public long getGuild() {
        if (guildAndId == null)
            return 0;

        return guildAndId.getGuildId();
    }

    /**
     * Get the ID.
     *
     * @return the ID.
     */
    public long getId() {
        if (guildAndId == null) return -1;
        return guildAndId.getId();
    }
}
