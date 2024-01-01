package de.presti.ree6.sql.entities;

import de.presti.ree6.sql.keys.GuildAndName;
import jakarta.persistence.*;

/**
 * SQL Entity for the Blacklist.
 */
@Entity
@Table(name = "ChatProtector")
public class Blacklist {

    /**
     * The ID of the Entity.
     */
    @EmbeddedId
    private GuildAndName guildAndName;

    /**
     * Constructor.
     */
    public Blacklist() {
    }

    /**
     * Constructor.
     *
     * @param guildId the GuildID of the Blacklist.
     * @param word    the blacklisted word.
     */
    public Blacklist(long guildId, String word) {
        guildAndName = new GuildAndName(guildId, word);
    }

    /**
     * Get the GuildID of the Blacklist.
     *
     * @return {@link String} as GuildID.
     */
    public long getGuildId() {
        if (guildAndName == null)
            return 0;

        return guildAndName.getGuildId();
    }

    /**
     * Get the blacklisted word.
     *
     * @return {@link String} as blacklisted word.
     */
    public String getWord() {
        return guildAndName.getName();
    }

    /**
     * Override to just return the Word.
     * @return {@link String} as Word.
     */
    @Override
    public String toString() {
        return getWord();
    }
}
