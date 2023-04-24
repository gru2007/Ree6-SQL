package de.presti.ree6.sql.entities.economy;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

/**
 * Represents any transaction that has been made between money holder entities.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Money_Transaction")
public class MoneyTransaction {
    /**
     * The ID of the entity.
     */
    @jakarta.persistence.Id
    @GeneratedValue
    long transactionId;

    /**
     * The ID of the Guild.
     */
    @Column(name = "guildId")
    private long guildId;

    /**
     * The Bank of the user that sends the money.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name="bankSender", updatable=false)
    private MoneyBank bankSender;

    /**
     * The Bank of the user that receives the money.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name="bankReceiver", updatable=false)
    private MoneyBank bankReceiver;

    /**
     * The Pocket of the user that sends the money.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name="pocketSender", updatable=false)
    private MoneyBank pocketSender;

    /**
     * The Pocket of the user that receives the money.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name="pocketReceiver", updatable=false)
    private MoneyBank pocketReceiver;

    /**
     * The amount of money the user has.
     */
    @Column(name = "amount")
    private float amount;


    /**
     * Creation time.
     */
    @CreationTimestamp
    private Timestamp creation;
}
