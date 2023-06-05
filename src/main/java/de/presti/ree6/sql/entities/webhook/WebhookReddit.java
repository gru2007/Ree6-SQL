package de.presti.ree6.sql.entities.webhook;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * SQL Entity for the Reddit-Webhooks.
 */
@Entity
@NoArgsConstructor
@Table(name = "RedditNotify")
public class WebhookReddit extends Webhook {

    /**
     * Name of the Channel.
     */
    @Getter
    @Column(name = "subreddit")
    private String subreddit;

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
     * @param subreddit The name of the Subreddit.
     * @param channelId The channel ID.
     * @param webhookId The webhook ID.
     * @param message   The message.
     * @param token     The token.
     */
    public WebhookReddit(String guildId, String subreddit, String message, long channelId, String webhookId, String token) {
        super(guildId, channelId, webhookId, token);
        this.subreddit = subreddit;
        this.message = message;
    }
}
