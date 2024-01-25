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
public class GuildAndCode implements Serializable {

    /**
     * The Discord Guild ID.
     */
    @Column(name = "guildId")
    private long guildId;

    /**
     * The code of the Entity.
     */
    @Column(name = "code")
    private String code;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GuildAndCode settingId) {
            return settingId.getGuildId() == guildId && settingId.getCode().equals(code);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(guildId, code);
    }
}
