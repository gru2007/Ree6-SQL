package de.presti.ree6.sql.entities.webhook.base;

import de.presti.ree6.sql.entities.GuildAndIdBaseEntity;
import de.presti.ree6.sql.keys.GuildAndId;
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
public class WebhookSocial extends GuildAndIdBaseEntity {

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
        this.guildAndId = new GuildAndId(guildId);
        this.channelId = channelId;
        this.webhookId = webhookId;
        this.token = token;
    }
}
