package de.presti.ree6.sql.entities.webhook;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;

/**
 * SQL Entity for the ScheduledMessage-Webhooks.
 */
@Entity
@Getter
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
    public WebhookScheduledMessage(String guildId, long channelId, String webhookId, String token) {
        super(guildId, channelId, webhookId, token);
    }
}
