package de.presti.ree6.sql.entities.webhook.base;

import de.presti.ree6.sql.keys.SocialWebhookId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * SQL Entity for the Webhooks.
 */
@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public class WebhookSocial {

    /**
     * The ID of the Entity.
     */
    @EmbeddedId
    SocialWebhookId socialWebhookId;

    @Column(name = "channel", nullable = false)
    long channelId = 0;

    /**
     * The Webhook ID of the Webhook.
     */
    @Column(name = "webhookId", nullable = false)
    long webhookId;

    /**
     * The Token of the Webhook.
     */
    @Column(name = "token", nullable = false)
    String token;

    /**
     * Constructor.
     *
     * @param guildId   The GuildID of the Webhook.
     * @param channelId The ChannelID of the Webhook.
     * @param webhookId The WebhookId of the Webhook.
     * @param token     The Token of the Webhook.
     */
    public WebhookSocial(long guildId, long channelId, long webhookId, String token) {
        this.socialWebhookId = new SocialWebhookId(guildId);
        this.channelId = channelId;
        this.webhookId = webhookId;
        this.token = token;
    }

    /**
     * Set the GuildID of the Webhook.
     * @param guildId The GuildID of the Webhook.
     */
    public void setGuildId(long guildId) {
        this.socialWebhookId.setGuildId(guildId);
    }
}
