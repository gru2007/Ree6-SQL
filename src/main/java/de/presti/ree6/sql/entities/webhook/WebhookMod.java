package de.presti.ree6.sql.entities.webhook;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * SQL Entity for the Log-Webhooks.
 */
@Entity
@Table(name = "ModWebhooks")
public class WebhookMod extends Webhook {

    /**
     * Constructor.
     */
    public WebhookMod() {
    }

    /**
     * @inheritDoc
     */
    public WebhookMod(long guildId, long channelId, Long webhookId, String token) {
        super(guildId, channelId, webhookId, token);
    }
}
