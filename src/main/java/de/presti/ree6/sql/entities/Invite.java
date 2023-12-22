package de.presti.ree6.sql.entities;

import de.presti.ree6.sql.keys.GuildAndId;
import jakarta.persistence.*;

/**
 * Invite class to store information about an Invite.
 */
@Entity
@Table(name = "Invites")
public class Invite {

    /**
     * The PrimaryKey of the Entity.
     */
    @EmbeddedId
    GuildAndId guildAndId;

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
     * The Code of the Invite.
     */
    @Column(name = "code")
    String code;

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
        this.guildAndId = new GuildAndId(guild);
        this.userId = userId;
        this.uses = uses;
        this.code = code;
    }

    /**
     * Set the GuildID.
     * @param guildId The GuildID.
     */
    public void setGuildId(long guildId) {
        if (guildAndId == null) guildAndId = new GuildAndId(guildId);
        guildAndId.setGuildId(guildId);
    }

    /**
     * Set the ID.
     * @param id The ID.
     */
    public void setId(long id) {
        if (guildAndId == null) guildAndId = new GuildAndId(0, id);
        guildAndId.setId(id);
    }

    /**
     * Get the GuildID.
     *
     * @return {@link long} as GuildID.
     */
    public long getGuild() {
        if (guildAndId == null)
            return 0;

        return guildAndId.getGuildId();
    }

    /**
     * Get the ID.
     *
     * @return the ID.
     */
    public long getId() {
        if (guildAndId == null) return -1;
        return guildAndId.getId();
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

    /**
     * Get the Code of the Invite.
     *
     * @return {@link String} as Code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Set the Code of the Invite.
     *
     * @param code the Code of the Invite.
     */
    public void setCode(String code) {
        this.code = code;
    }
}
