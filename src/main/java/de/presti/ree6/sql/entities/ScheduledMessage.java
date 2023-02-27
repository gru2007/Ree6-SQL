package de.presti.ree6.sql.entities;

import de.presti.ree6.sql.entities.webhook.WebhookScheduledMessage;
import jakarta.persistence.*;

/**
 * This class is used to represent a Scheduled Message.
 */
@Entity
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

    // TODO:: add options for example every hour, day, month, year and specific date.

    /**
     * The related ScheduledMessage Webhook.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name="webhook_id", nullable=false, updatable=false)
    private WebhookScheduledMessage scheduledMessageWebhook;
}
