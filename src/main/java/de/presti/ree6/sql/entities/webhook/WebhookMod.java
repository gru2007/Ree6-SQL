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
    public WebhookMod(String guildId, long channelId, String webhookId, String token) {
        super(guildId, channelId, webhookId, token);
    }
}
