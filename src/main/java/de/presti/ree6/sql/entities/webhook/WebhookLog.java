package de.presti.ree6.sql.entities.webhook;

import de.presti.ree6.sql.entities.webhook.base.Webhook;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * SQL Entity for the Log-Webhooks.
 */
@Entity
@Table(name = "LogWebhooks", indexes = @Index(columnList = "guildId"))
public class WebhookLog extends Webhook {

    /**
     * Constructor.
     */
    public WebhookLog() {
    }

    /**
     * @inheritDoc
     */
    public WebhookLog(long guildId, long channelId, long webhookId, String token) {
        super(guildId, channelId, webhookId, token);
    }
}
