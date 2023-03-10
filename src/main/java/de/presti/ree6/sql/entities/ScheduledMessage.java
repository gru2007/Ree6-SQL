package de.presti.ree6.sql.entities;

import de.presti.ree6.sql.entities.webhook.WebhookScheduledMessage;
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
public class ScheduledMessage {

    /**
     * The ID of the entity.
     */
    @Id
    @GeneratedValue
    long Id;

    /**
     * The ID of the Guild.
     */
    @Column(name = "guild")
    long guildId;

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
    @Column(name = "repeat")
    boolean repeated;

    /**
     * The related ScheduledMessage Webhook.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name="webhook_id", nullable=false, updatable=false)
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
