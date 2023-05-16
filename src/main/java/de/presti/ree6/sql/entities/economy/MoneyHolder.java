package de.presti.ree6.sql.entities.economy;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Masterclass of any Money holding Object.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Money_Holder")
public class MoneyHolder {
    /**
     * The ID of the entity.
     */
    @jakarta.persistence.Id
    @GeneratedValue
    long Id;

    /**
     * The ID of the Guild.
     */
    @Column(name = "guildId")
    private long guildId;

    /**
     * The ID of the User.
     */
    @Column(name = "userId")
    private long userId;

    /**
     * The amount of money the user has.
     */
    @Column(name = "cash")
    private double amount;

    /**
     * The amount of money the user has.
     */
    @Column(name = "bank")
    private double bankAmount;
}
