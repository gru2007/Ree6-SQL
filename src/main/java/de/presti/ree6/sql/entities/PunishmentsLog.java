package de.presti.ree6.sql.entities;

import de.presti.ree6.sql.keys.GuildAndId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

/**
 * Entity class for the Warning punishments.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PunishmentsLog")
public class PunishmentsLog {

    /**
     * Primary key for the entity.
     */
    @EmbeddedId
    GuildAndId guildAndId;

    /**
     * The action (1 -> timeout, 2 -> kick, 3 -> warn)
     */
    int action;

    /**
     * The reason for the kick and ban action.
     */
    String reason;

    /**
     * Creation time.
     */
    @CreationTimestamp
    private Timestamp creation;

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
        if (guildAndId == null) guildAndId = new GuildAndId(0);
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
