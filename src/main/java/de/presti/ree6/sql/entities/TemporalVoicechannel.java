package de.presti.ree6.sql.entities;


import de.presti.ree6.sql.keys.GuildChannelId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Class used to store information about the temporal Voice-Channel.
 */
@Entity
@Getter
@Setter
@Table(name = "TemporalVoicechannel")
public class TemporalVoicechannel {

    /**
     * The ID of the Entity.
     */
    @EmbeddedId
    GuildChannelId guildChannelId;

    /**
     * Constructor.
     */
    public TemporalVoicechannel() {
    }

    /**
     * Constructor for the TemporalVoicechannel.
     *
     * @param guildId the ID of the Guild.
     * @param voiceChannelId the ID of the Voice-channel.
     */
    public TemporalVoicechannel(long guildId, long voiceChannelId) {
        this.guildChannelId = new GuildChannelId(guildId, voiceChannelId);
    }
}
