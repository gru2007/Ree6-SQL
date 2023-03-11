package de.presti.ree6.sql.entities.webhook;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * SQL Entity for the ScheduledMessage-Webhooks.
 */
@Entity
@Getter
@Table(name = "ScheduledMessageWebhooks")
public class WebhookScheduledMessage extends Webhook {

    /**
     * Constructor.
     */
    public WebhookScheduledMessage() {
    }

    /**
     * @inheritDoc
     */
    public WebhookScheduledMessage(String guildId, String channelId, String token) {
        super(guildId, channelId, token);
    }

    /**
     * Constructor.
     *
     * @param guildId   The GuildID of the Webhook.
     * @param channelId The ChannelID of the Webhook.
     * @param token     The Token of the Webhook.
     * @param actualChannelId The actual Channel ID.
     */
    public WebhookScheduledMessage(String guildId, String channelId, String token, long actualChannelId) {
        super(guildId, channelId, token);
        this.actualChannelId = actualChannelId;
    }

    /**
     * The actual ID of the channel.
     */
    @Column(name = "actualChannelId")
    long actualChannelId;
}
