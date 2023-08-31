package de.presti.ree6.sql.entities;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

/**
 * A Class that represents the Giveaway Entity
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Giveaway {

    /**
     * The messageId as primary.
     */
    @Id
    public long messageId;

    /**
     * The UserId of the creator.
     */
    public long creatorId;

    /**
     * The GuildId.
     */
    public long guildId;

    /**
     * The ChannelId.
     */
    public long channelId;

    /**
     * The Price set by the creator.
     */
    public String price;

    /**
     * The amount of possible winners.
     */
    public int winners;

    /**
     * Timestamp of the creation date.
     */
    @CreationTimestamp
    public Timestamp created;

    /**
     * Timestamp of the ending date.
     */
    public Timestamp ending;
}
