package de.presti.ree6.sql.entities;

import com.google.gson.JsonElement;
import de.presti.ree6.sql.converter.JsonAttributeConverter;
import de.presti.ree6.sql.keys.GuildAndId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "StreamActions")
public class StreamAction {

    /**
     * The Key of the Entity.
     */
    @EmbeddedId
    GuildAndId guildAndId;

    /**
     * The name of the action.
     */
    @Column(name = "actionName")
    String actionName;

    /**
     * The related Twitch Auth.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name="auth_id", nullable=false, updatable=false)
    TwitchIntegration integration;

    /**
     * Typ of the Listener
     */
    @Column(name = "listener")
    int listener;

    /**
     * Extra Arguments used on the listeners.
     */
    @Column(name = "argument")
    String argument;

    /**
     * The Actions that are to be executed.
     */
    @Column(name = "actions")
    @Convert(converter = JsonAttributeConverter.class)
    JsonElement actions;

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
