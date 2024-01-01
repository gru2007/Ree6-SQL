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
public class GuildAndName implements Serializable {

    /**
     * The Discord Guild ID.
     */
    @Column(name = "guildId")
    private long guildId;

    /**
     * The Name / Identifier of the Setting.
     */
    @Column(name = "name")
    private String name;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GuildAndName settingId) {
            return settingId.getGuildId() == guildId && settingId.getName().equals(name);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(guildId, name);
    }
}
