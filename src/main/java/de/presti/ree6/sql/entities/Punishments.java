package de.presti.ree6.sql.entities;

import de.presti.ree6.sql.keys.GuildAndId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity class for the Warning punishments.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Punishments")
public class Punishments {

    /**
     * Primary key for the entity.
     */
    @EmbeddedId
    GuildAndId guildAndId;

    /**
     * The required warnings.
     */
    int warnings;

    /**
     * The action (1 -> timeout, 2 -> roleAdd, 3 -> roleRemove, 4 -> kick, 5 -> ban)
     */
    int action;

    /**
     * The Role id related to the role actions.
     */
    long roleId;

    /**
     * The Timeout time in milliseconds for the timeout action.
     */
    long timeoutTime;

    /**
     * The reason for the kick and ban action.
     */
    String reason;
}
