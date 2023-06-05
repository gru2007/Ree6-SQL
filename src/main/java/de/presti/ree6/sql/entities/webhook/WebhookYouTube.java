package de.presti.ree6.sql.entities.webhook;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * SQL Entity for the YouTube-Webhooks.
 */
@Entity
@NoArgsConstructor
@Table(name = "YouTubeNotify")
public class WebhookYouTube extends Webhook {

    /**
     * Name of the Channel.
     */
    @Getter
    @Column(name = "name")
    private String name;

    /**
     * Special message content.
     */
    @Getter
    @Column(name = "message")
    private String message;

    /**
     * Constructor.
     *
     * @param guildId   The guild ID.
     * @param name      The name of the Channel.
     * @param channelId The channel ID.
     * @param webhookId The webhook ID.
     * @param message  The message.
     * @param token     The token.
     */
    public WebhookYouTube(String guildId, String name, String message, long channelId, String webhookId, String token) {
        super(guildId, channelId, webhookId, token);
        this.name = name;
        this.message = message;
    }
}
