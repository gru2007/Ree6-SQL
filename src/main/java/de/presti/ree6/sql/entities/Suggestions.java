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
@Table(name = "Suggestions")
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
}
