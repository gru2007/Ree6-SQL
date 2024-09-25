package de.presti.ree6.sql.entities.webhook;

import de.presti.ree6.sql.entities.webhook.base.WebhookSocial;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * SQL Entity for the YouTube-Webhooks.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "SpotifyNotify")
public class WebhookSpotify extends WebhookSocial {

    /**
     * ID of the Artist/Podcast.
     */
    @Column(name = "entityId")
    private String entityId;

    /**
     * Typ of the Notifier
     */
    @Column(name = "typ")
    private int entityTyp;

    /**
     * Special message content.
     */
    @Column(name = "message")
    private String message;

    /**
     * Constructor.
     *
     * @param guildId   The guild ID.
     * @param entityId  The ID of the Artist/Podcast.
     * @param entityTyp Typ of the Notifier
     * @param channelId The channel ID.
     * @param webhookId The webhook ID.
     * @param message   The message.
     * @param token     The token.
     */
    public WebhookSpotify(long guildId, String entityId, int entityTyp, String message, long channelId, long webhookId, String token) {
        super(guildId, channelId, webhookId, token);
        this.entityId = entityId;
        this.entityTyp = entityTyp;
        this.message = message;
    }
}
