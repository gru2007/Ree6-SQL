package de.presti.ree6.sql.entities;

import de.presti.ree6.sql.keys.GuildAndId;
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
    private GuildAndId guildAndId;

    /**
     * The blacklisted word.
     */
    @Column(name = "word")
    private String word;

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
        guildAndId = new GuildAndId(guildId);
        this.word = word;
    }

    /**
     * Get the GuildID of the Blacklist.
     *
     * @return {@link String} as GuildID.
     */
    public long getGuildId() {
        if (guildAndId == null)
            return 0;

        return guildAndId.getGuildId();
    }

    /**
     * Get the blacklisted word.
     *
     * @return {@link String} as blacklisted word.
     */
    public String getWord() {
        return word;
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
