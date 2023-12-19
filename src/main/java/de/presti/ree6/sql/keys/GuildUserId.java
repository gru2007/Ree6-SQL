package de.presti.ree6.sql.keys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

/**
 * Class used to store multiple Keys, within one Entity.
 */
@Setter
@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class GuildUserId implements Serializable {

    /**
     * The Discord Guild ID.
     */
    @Column(name = "guildId")
    private long guildId;

    /**
     * The User ID.
     */
    @Column(name = "userId")
    private long userId;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GuildUserId guildUserId) {
            return guildUserId.getGuildId() == guildId && guildUserId.getUserId() == userId;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(guildId, userId);
    }
}
