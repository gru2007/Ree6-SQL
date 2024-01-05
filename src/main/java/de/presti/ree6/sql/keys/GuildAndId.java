package de.presti.ree6.sql.keys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Class used to store multiple Keys, within one Entity.
 */
@Setter
@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class GuildAndId implements Serializable {

    /**
     * Unique ID of the Webhook.
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    /**
     * The Discord Guild ID.
     */
    @Column(name = "guildId")
    private long guildId;

    /**
     * Constructor for the GuildAndId.
     * @param guildId The Discord Guild ID.
     */
    public GuildAndId(long guildId) {
        this.id = -1;
        this.guildId = guildId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GuildAndId settingId) {
            return settingId.getGuildId() == guildId && settingId.getId() == id;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(guildId, id);
    }
}
