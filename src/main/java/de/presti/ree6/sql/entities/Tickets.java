package de.presti.ree6.sql.entities;

import com.google.gson.JsonElement;
import de.presti.ree6.sql.converter.JsonAttributeConverter;
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
@Table(name = "Tickets")
public class Tickets {

    /**
     * The ID of the Guild.
     */
    @Id
    @Column(name = "guildId")
    private Long guildId;

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
    long logChannelId;


    /**
     * The ID for the Webhook.
     */
    @Column(name = "logChannelWebhookId")
    long logChannelWebhookId;

    /**
     * The Token for the Webhook.
     */
    @Column(name = "logChannelWebhookToken")
    String logChannelWebhookToken;


    /**
     * The Ticket counter.
     *
     */
    @Column(name = "ticketCount")
    @ColumnDefault("0")
    long ticketCount;
}
