package de.presti.ree6.sql.entities;

import de.presti.ree6.sql.keys.GuildChannelId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Class used to store information about the Suggestions.
 */
@Entity
@Getter
@Setter
@Table(name = "Suggestions", indexes = @Index(columnList = "channelId, guildId"))
public class Suggestions {

    /**
     * The ID of the Entity.
     */
    @EmbeddedId
    GuildChannelId guildChannelId;

    /**
     * Constructor.
     */
    public Suggestions() {
    }

    /**
     * Constructor for the Suggestions.
     *
     * @param guildId the ID of the Guild.
     * @param channelId the ID of the Channel.
     */
    public Suggestions(long guildId, long channelId) {
        this.guildChannelId = new GuildChannelId(guildId, channelId);
    }

    /**
     * Set the GuildID.
     * @param guildId The GuildID.
     */
    public void setGuildId(long guildId) {
        if (guildChannelId == null) guildChannelId = new GuildChannelId(guildId, 0L);
        guildChannelId.setGuildId(guildId);
    }

    /**
     * Set the ChannelID.
     * @param channelId The ChannelID.
     */
    public void setChannelId(long channelId) {
        if (guildChannelId == null) guildChannelId = new GuildChannelId(0L, channelId);
        guildChannelId.setChannelId(channelId);
    }

    /**
     * Get the GuildID.
     * @return The GuildID.
     */
    public long getGuildId() {
        if (guildChannelId == null) return -1;
        return guildChannelId.getGuildId();
    }

    /**
     * Get the ChannelID.
     * @return The ChannelID.
     */
    public long getChannelId() {
        if (guildChannelId == null) return -1;
        return guildChannelId.getChannelId();
    }
}
