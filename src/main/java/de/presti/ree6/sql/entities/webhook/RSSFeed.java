package de.presti.ree6.sql.entities.webhook;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * SQL Entity for the RSS Feed Notifications.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "RSSFeed")
public class RSSFeed extends Webhook {

    /**
     * Name of the Channel.
     */
    @Column(name = "url")
    private String url;

    /**
     * Constructor.
     *
     * @param guildId   The guild ID.
     * @param url       The url of the RSS Feed.
     * @param channelId The channel ID.
     * @param webhookId The webhook ID.
     * @param token     The token.
     */
    public RSSFeed(String guildId, String url, long channelId, String webhookId, String token) {
        super(guildId, channelId, webhookId, token);
        this.url = url;
    }

}
