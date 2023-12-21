package de.presti.ree6.sql.entities.webhook;

import de.presti.ree6.sql.entities.webhook.base.Webhook;
import de.presti.ree6.sql.entities.webhook.base.WebhookSocial;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * SQL Entity for the Reddit-Webhooks.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "RedditNotify")
public class WebhookReddit extends WebhookSocial {

    /**
     * Name of the Channel.
     */
    @Column(name = "subreddit")
    private String subreddit;

    /**
     * Special message content.
     */
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
    public WebhookReddit(long guildId, String subreddit, String message, long channelId, long webhookId, String token) {
        super(guildId, channelId, webhookId, token);
        this.subreddit = subreddit;
        this.message = message;
    }
}
