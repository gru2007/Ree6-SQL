package de.presti.ree6.sql.entities;

import de.presti.ree6.sql.entities.webhook.WebhookScheduledMessage;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;

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
    private long Id;

    /**
     * Special message content.
     */
    @Column(name = "message")
    public String message;

    // TODO:: add options for example hour, day, month, year and specific date.

    @Column(name = "typ")
    int scheduleTyp;

    @Column(name = "delay")
    int delayAmount;

    @Column(name = "repeat")
    boolean repeated;

    LocalDateTime scheduleDate;

    /**
     * The related ScheduledMessage Webhook.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name="webhook_id", nullable=false, updatable=false)
    private WebhookScheduledMessage scheduledMessageWebhook;

    /**
     * Last execute time.
     */
    @UpdateTimestamp
    @Setter(AccessLevel.PRIVATE)
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
