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
    @Column(name = "gid")
    private String guildId;

    // TODO:: fix this by renaming the column to wid and adding the actual channel id to the column cid

    /**
     * The Webhook Id of the Webhook.
     */
    @Column(name = "cid")
    private String channelId;

    /**
     * The Token of the Webhook.
     */
    @Column(name = "token")
    private String token;

    /**
     * Constructor.
     *
     * @param guildId   The GuildID of the Webhook.
     * @param channelId The ChannelID of the Webhook.
     * @param token     The Token of the Webhook.
     */
    public Webhook(String guildId, String channelId, String token) {
        this.guildId = guildId;
        this.channelId = channelId;
        this.token = token;
    }
}
