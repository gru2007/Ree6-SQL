package de.presti.ree6.sql.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

/**
 * A Class that represents the Giveaway Entity
 */
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Giveaway", indexes = @Index(columnList = "guildId"))
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
     * The Prize set by the creator.
     */
    public String prize;

    /**
     * The number of possible winners.
     */
    public long winners;

    /**
     * Timestamp of the creation date.
     */
    @CreationTimestamp
    public Timestamp created;

    /**
     * Timestamp of the ending date.
     */
    public Timestamp ending;

    /**
     * Constructor with the needed data.
     */
    public Giveaway(long messageId, long creatorId, long guildId, long channelId, String prize, long winners, Timestamp ending) {
        this.messageId = messageId;
        this.creatorId = creatorId;
        this.guildId = guildId;
        this.channelId = channelId;
        this.prize = prize;
        this.winners = winners;
        this.ending = ending;
    }
}
