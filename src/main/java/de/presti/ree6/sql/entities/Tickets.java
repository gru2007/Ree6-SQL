package de.presti.ree6.sql.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

/**
 * SQL Entity for the Tickets.
 */
@Getter
@Setter
@Entity
@Table(name = "Tickets", indexes = @Index(columnList = "channelId, guildId"))
public class Tickets {

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
     * The Category ID of the Tickets.
     */
    @Column(name = "ticketCategory")
    long ticketCategory;

    /**
     * The ID of the channel that is used to send the transcripts to.
     */
    @Column(name = "logChannelId")
    long logChannelId = 0L;

    /**
     * The ID for the Webhook.
     */
    @Column(name = "logChannelWebhookId")
    long logChannelWebhookId = 0L;

    /**
     * The Token for the Webhook.
     */
    @Column(name = "logChannelWebhookToken")
    String logChannelWebhookToken;

    /**
     * The Ticket counter.
     */
    @Column(name = "ticketCount")
    @ColumnDefault("0")
    long ticketCount;
}
