package de.presti.ree6.sql.entities.webhook;

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
public class Webhook {

    /**
     * The PrimaryKey of the Entity.
     */
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    /**
     * The GuildID of the Webhook.
     */
    @Column(name = "gid", nullable = false)
    private String guildId;


    @Column(name = "channel", nullable = false)
    private long channelId = 0;

    /**
     * The Webhook Id of the Webhook.
     */
    @Column(name = "cid", nullable = false)
    private String webhookId;

    /**
     * The Token of the Webhook.
     */
    @Column(name = "token", nullable = false)
    private String token;

    /**
     * Constructor.
     *
     * @param guildId   The GuildID of the Webhook.
     * @param channelId The ChannelID of the Webhook.
     * @param webhookId The WebhookId of the Webhook.
     * @param token     The Token of the Webhook.
     */
    public Webhook(String guildId, long channelId, String webhookId, String token) {
        this.guildId = guildId;
        this.channelId = channelId;
        this.webhookId = webhookId;
        this.token = token;
    }
}
