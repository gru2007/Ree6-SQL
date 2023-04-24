package de.presti.ree6.sql.entities.economy;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

/**
 * Masterclass of any Money holding Object.
 */
@Getter
@Setter
@MappedSuperclass
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
    @Column(name = "amount")
    private float amount;
}
