package de.presti.ree6.sql.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
     * The ID of the Guild.
     */
    @Id
    @Column(name = "gid")
    long guildId;

    /**
     * The ID of the Voice-channel.
     */
    @Column(name = "vid")
    long voiceChannelId;

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
        this.guildId = guildId;
        this.voiceChannelId = voiceChannelId;
    }
}
