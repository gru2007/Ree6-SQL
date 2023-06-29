package de.presti.ree6.sql.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * An Entity representing the warnings a user has received on a specific guild.
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
