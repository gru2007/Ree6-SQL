package de.presti.ree6.sql.entities;

import de.presti.ree6.sql.entities.webhook.WebhookScheduledMessage;
import de.presti.ree6.sql.keys.GuildAndId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

/**
 * This class is used to represent a Scheduled Message.
 */
@Entity
@Getter
@Setter
@Table(name = "ScheduledMessage")
public class ScheduledMessage extends GuildAndIdBaseEntity {

    /**
     * Special message content.
     */
    @Column(name = "message")
    String message;

    /**
     * The amount of time from the creation/last execution to the next execution.
     */
    @Column(name = "delay")
    long delayAmount;

    /**
     * If it should be repeated or not.
     */
    @Column(name = "shouldRepeat")
    boolean repeated;

    /**
     * The related ScheduledMessage Webhook.
     */
    @ManyToOne(optional = false)
    @JoinColumns(value = {
            @JoinColumn(name = "webhook_id", referencedColumnName = "id"),
            @JoinColumn(name = "webhook_guildId", referencedColumnName = "guildId")
    }, foreignKey = @ForeignKey(name = "fk_scheduled_message_webhook"))
    private WebhookScheduledMessage scheduledMessageWebhook;

    /**
     * Last execute time.
     */
    @Setter(AccessLevel.PUBLIC)
    @Temporal(TemporalType.TIMESTAMP)
    Timestamp lastExecute;

    /**
     * Last updated time.
     */
    @UpdateTimestamp
    @Setter(AccessLevel.PRIVATE)
    @Temporal(TemporalType.TIMESTAMP)
    Timestamp lastUpdated;

    /**
     * Created time.
     */
    @CreationTimestamp
    @Setter(AccessLevel.PRIVATE)
    @Temporal(TemporalType.TIMESTAMP)
    Timestamp created;
}
