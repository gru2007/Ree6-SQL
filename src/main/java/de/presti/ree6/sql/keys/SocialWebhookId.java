package de.presti.ree6.sql.keys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.*;

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
public class SocialWebhookId implements Serializable {

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
     * Constructor for the SocialWebhookId.
     * @param guildId The Discord Guild ID.
     */
    public SocialWebhookId(long guildId) {
        this.guildId = guildId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SocialWebhookId settingId) {
            return settingId.getGuildId() == guildId && settingId.getId() == id;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(guildId, id);
    }
}
