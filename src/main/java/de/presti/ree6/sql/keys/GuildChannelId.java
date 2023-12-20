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
public class GuildChannelId implements Serializable {

    /**
     * The Discord Guild ID.
     */
    @Column(name = "guildId")
    private long guildId;

    /**
     * The Channel ID.
     */
    @Column(name = "channelId")
    private long channelId;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GuildChannelId guildUserId) {
            return guildUserId.getGuildId() == guildId && guildUserId.getChannelId() == channelId;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(guildId, channelId);
    }
}
