package de.presti.ree6.sql.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A Entity representing the warnings a user has received on a specific guild.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Warning")
public class Warning {

    /**
     * The id of the warning.
     */
    @Id
    @GeneratedValue
    long id;

    /**
     * The user id its bound to.
     */
    long userId;

    /**
     * The guild id its bound to.
     */
    long guildId;

    /**
     * The warnings that the user has.
     */
    int warnings;
}
