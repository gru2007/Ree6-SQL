package de.presti.ree6.sql.entities.webhook;

import de.presti.ree6.sql.entities.webhook.base.Webhook;
import de.presti.ree6.sql.entities.webhook.base.WebhookSocial;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * SQL Entity for the Twitter-Webhooks.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "TwitterNotify", indexes = @Index(columnList = "id, guildId"))
public class WebhookTwitter extends WebhookSocial {

    /**
     * Name of the User.
     */
    @Column(name = "name")
    private String name;

    /**
     * Special message content.
     */
    @Column(name = "message")
    private String message;

    /**
     * Constructor.
     *
     * @param guildId   The guild ID.
     * @param name      The name of the User.
     * @param channelId The channel ID.
     * @param webhookId The webhook ID.
     * @param message   The message.
     * @param token     The token.
     */
    public WebhookTwitter(long guildId, String name, String message, long channelId, long webhookId, String token) {
        super(guildId, channelId, webhookId, token);
        this.name = name;
        this.message = message;
    }

}
