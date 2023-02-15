package de.presti.ree6.sql.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
public class Warning {

    /**
     * The id of the warning.
     */
    @Id
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
