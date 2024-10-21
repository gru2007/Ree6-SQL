package de.presti.ree6.sql.entities;

import de.presti.ree6.sql.keys.GuildAndCode;
import jakarta.persistence.*;

/**
 * Invite class to store information about an Invite.
 */
@Entity
@Table(name = "Invites", indexes = @Index(columnList = "code, guildId"))
public class Invite {

    /**
     * The PrimaryKey of the Entity.
     */
    @EmbeddedId
    GuildAndCode guildAndCode;

    /**
     * The UserID of the Invite.
     */
    @Column(name = "userId")
    long userId;

    /**
     * The Usages of the Invite.
     */
    @Column(name = "uses")
    long uses;

    /**
     * Constructor.
     */
    public Invite() {
    }

    /**
     * Constructor for the Invite.
     *
     * @param guild  the GuildID of the Invite.
     * @param userId the UserID of the Invite.
     * @param uses   the Usages of the Invite.
     * @param code   the Code of the Invite.
     */
    public Invite(long guild, long userId, long uses, String code) {
        this.guildAndCode = new GuildAndCode(guild, code);
        this.userId = userId;
        this.uses = uses;
    }

    /**
     * Set the GuildID.
     * @param guildId The GuildID.
     */
    public void setGuildId(long guildId) {
        if (guildAndCode == null) guildAndCode = new GuildAndCode();
        guildAndCode.setGuildId(guildId);
    }

    /**
     * Set the Code.
     * @param code The Code.
     */
    public void setCode(String code) {
        if (guildAndCode == null) guildAndCode = new GuildAndCode();
        guildAndCode.setCode(code);
    }

    /**
     * Get the GuildID.
     *
     * @return {@link long} as GuildID.
     */
    public long getGuild() {
        if (guildAndCode == null)
            return 0;

        return guildAndCode.getGuildId();
    }

    /**
     * Get the ID.
     *
     * @return the ID.
     */
    public String getCode() {
        if (guildAndCode == null) guildAndCode = new GuildAndCode();
        return guildAndCode.getCode();
    }

    /**
     * Get the UserID of the Invite.
     *
     * @return {@link String} as UserID.
     */
    public long getUserId() {
        return userId;
    }

    /**
     * Get the Usages of the Invite.
     *
     * @return {@link long} as Usages.
     */
    public long getUses() {
        return uses;
    }
}
