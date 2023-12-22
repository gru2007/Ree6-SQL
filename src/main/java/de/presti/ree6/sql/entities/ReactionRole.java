package de.presti.ree6.sql.entities;


import de.presti.ree6.sql.keys.GuildAndId;
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
@Table(name = "ReactionRole")
public class ReactionRole {

    /**
     * The Key of the Entity.
     */
    @EmbeddedId
    GuildAndId guildAndId;

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
     * The ID of the Role used as a reaction role.
     */
    @Column(name = "roleId")
    private long roleId;

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
        this.guildAndId = new GuildAndId(guildId);
        this.formattedEmote = formattedEmote;
        this.emoteId = emoteId;
        this.roleId = roleId;
        this.messageId = messageId;
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
}