package de.presti.ree6.sql.entities.webhook;

import de.presti.ree6.sql.entities.webhook.base.WebhookSocial;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;

/**
 * SQL Entity for the ScheduledMessage-Webhooks.
 */
@Entity
@Getter
@Table(name = "ScheduledMessageWebhooks", indexes = @Index(columnList = "id, guildId"))
public class WebhookScheduledMessage extends WebhookSocial {

    /**
     * Constructor.
     */
    public WebhookScheduledMessage() {
    }

    /**
     * @inheritDoc
     */
    public WebhookScheduledMessage(long guildId, long channelId, long webhookId, String token) {
        super(guildId, channelId, webhookId, token);
    }
}
