package de.presti.ree6.sql.entities.economy;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

/**
 * Represents any transaction that has been made between money holder entities.
 */
@Entity
@Getter
@Setter
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
     * If the payment is made from the System.
     */
    private boolean isSystemPayment;

    /**
     * The ID of the Guild.
     */
    @Column(name = "guildId")
    private long guildId;

    /**
     * The Bank of the user that sends the money.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name="sender", updatable=false)
    private MoneyHolder sender;

    /**
     * The Bank of the user that receives the money.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name="receiver", updatable=false)
    private MoneyHolder receiver;

    /**
     * If the receiver is a bank or not.
     */
    private boolean isReceiverBank;

    /**
     * If the sender is a bank or not.
     */
    private boolean isSenderBank;

    /**
     * The amount of money the user has.
     */
    @Column(name = "amount")
    private double amount;


    /**
     * Creation time.
     */
    @CreationTimestamp
    private Timestamp creation;
}
