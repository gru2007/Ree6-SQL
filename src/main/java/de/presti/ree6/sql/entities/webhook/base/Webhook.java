package de.presti.ree6.sql.entities.webhook.base;

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
     * The GuildID of the Webhook.
     */
    @Id
    @Column(name = "guildId", nullable = false)
    private long guildId;

    /**
     * The ChannelID of the Webhook.
     */
    @Column(name = "channel", nullable = false)
    private long channelId = 0;

    /**
     * The Webhook ID of the Webhook.
     */
    @Column(name = "cid", nullable = false)
    private long webhookId;

    /**
     * The Token of the Webhook.
     */
    @Column(name = "token", nullable = false)
    private String token;

}
