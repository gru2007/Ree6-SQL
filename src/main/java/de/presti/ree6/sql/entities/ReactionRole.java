package de.presti.ree6.sql.entities;

import de.presti.ree6.sql.keys.GuildRoleId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * File to store Reaction-role information.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ReactionRole", indexes = @Index(columnList = "roleId, guildId"))
public class ReactionRole {

    /**
     * The Key of the Entity.
     */
    @EmbeddedId
    GuildRoleId guildRoleId;

    /**
     * The ID of the Emote used as reaction.
     */
    @Column(name = "emoteId")
    private long emoteId;

    /**
     * The formatted Emote.
     */
    @Column(name = "formattedEmote")
    private String formattedEmote = "";

    /**
     * The ID of the Channel.
     */
    @Column(name = "channelId")
    private long channelId = 0;

    /**
     * The ID of the Message used as a reaction message.
     */
    @Column(name = "messageId")
    private long messageId;

    /**
     * Constructor for the Reaction role.
     * @param guildId the GuildID.
     * @param emoteId the EmoteId.
     * @param formattedEmote the formatted Emote.
     * @param roleId the Role ID.
     * @param messageId the Message ID.
     */
    public ReactionRole(long guildId, long emoteId, String formattedEmote, long roleId, long messageId) {
        this.guildRoleId = new GuildRoleId(guildId, roleId);
        this.formattedEmote = formattedEmote;
        this.emoteId = emoteId;
        this.messageId = messageId;
    }

    /**
     * Set the GuildID.
     * @param guildId The GuildID.
     */
    public void setGuildId(long guildId) {
        if (guildRoleId == null) guildRoleId = new GuildRoleId();
        guildRoleId.setGuildId(guildId);
    }

    /**
     * Set the Role ID.
     * @param roleId The Role ID.
     */
    public void setRole(long roleId) {
        if (guildRoleId == null) guildRoleId = new GuildRoleId();
        guildRoleId.setRoleId(roleId);
    }

    /**
     * Get the GuildID.
     *
     * @return {@link long} as GuildID.
     */
    public long getGuild() {
        if (guildRoleId == null)
            return 0;

        return guildRoleId.getGuildId();
    }

    /**
     * Get the ID.
     *
     * @return the ID.
     */
    public long getId() {
        if (guildRoleId == null) return -1;
        return guildRoleId.getRoleId();
    }
}