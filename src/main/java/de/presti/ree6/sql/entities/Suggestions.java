package de.presti.ree6.sql.entities;

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
     * The ID of the Guild.
     */
    @Id
    @Column(name = "guildId")
    long guildId;

    /**
     * The ID of the Channel.
     */
    @Column(name = "channelId")
    long channelId;

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
        this.guildId = guildId;
        this.channelId = channelId;
    }
}
