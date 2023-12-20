package de.presti.ree6.sql.entities.economy;

import de.presti.ree6.sql.keys.GuildUserId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class representing the MoneyHolder.
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
    @EmbeddedId
    GuildUserId guildUserId;

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
