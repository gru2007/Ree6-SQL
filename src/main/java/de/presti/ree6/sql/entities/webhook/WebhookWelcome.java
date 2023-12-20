package de.presti.ree6.sql.entities.webhook;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * SQL Entity for the Welcome-Webhooks.
 */
@Entity
@Table(name = "WelcomeWebhooks")
public class WebhookWelcome extends Webhook {

    /**
     * Constructor.
     */
    public WebhookWelcome() {
    }

    /**
     * @inheritDoc
     */
    public WebhookWelcome(long guildId, long channelId, long webhookId, String token) {
        super(guildId, channelId, webhookId, token);
    }
}
