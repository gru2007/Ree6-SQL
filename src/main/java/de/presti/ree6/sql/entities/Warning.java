package de.presti.ree6.sql.entities;

import de.presti.ree6.sql.keys.GuildUserId;
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
     * The primary key for the entity.
     */
    @EmbeddedId
    GuildUserId guildUserId;

    /**
     * The warnings that the user has.
     */
    int warnings;
}
