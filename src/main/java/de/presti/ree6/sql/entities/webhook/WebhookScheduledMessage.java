package de.presti.ree6.sql.entities.webhook;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * SQL Entity for the ScheduledMessage-Webhooks.
 */
@Entity
@Table(name = "ScheduledMessageWebhooks")
public class WebhookScheduledMessage extends Webhook {

    /**
     * Constructor.
     */
    public WebhookScheduledMessage() {
    }

    /**
     * @inheritDoc
     */
    public WebhookScheduledMessage(String guildId, String channelId, String token) {
        super(guildId, channelId, token);
    }

    /**
     * The actual ID of the channel.
     */
    @Column(name = "actualChannelId")
    long actualChannelId;
}
