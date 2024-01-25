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

    /**
     * Set the GuildID of the MoneyHolder.
     * @param guildId The GuildID of the MoneyHolder.
     */
    public void setGuildId(long guildId) {
        if (this.guildUserId == null)
            this.guildUserId = new GuildUserId();

        this.guildUserId.setGuildId(guildId);
    }

    /**
     * Get the GuildID of the MoneyHolder.
     * @return The GuildID of the MoneyHolder.
     */
    public long getGuildId() {
        if (this.guildUserId == null)
            return 0;

        return this.guildUserId.getGuildId();
    }

    /**
     * Set the UserID of the MoneyHolder.
     * @param userId The UserID of the MoneyHolder.
     */
    public void setUserId(long userId) {
        if (this.guildUserId == null)
            this.guildUserId = new GuildUserId();

        this.guildUserId.setUserId(userId);
    }

    /**
     * Get the UserID of the MoneyHolder.
     * @return The UserID of the MoneyHolder.
     */
    public long getUserId() {
        if (this.guildUserId == null)
            return 0;

        return this.guildUserId.getUserId();
    }
}
