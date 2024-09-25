package de.presti.ree6.sql.entities;

import com.google.gson.JsonElement;
import de.presti.ree6.sql.converter.JsonToBlobAttributeConverter;
import de.presti.ree6.sql.keys.GuildAndName;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;

@Getter
@Setter
@Entity
@Table(name = "StreamActions")
public class StreamAction {

    /**
     * The Key of the Entity.
     */
    @EmbeddedId
    GuildAndName guildAndName;

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
    @JdbcTypeCode(value = Types.LONGVARBINARY)
    @Column(name = "actions")
    @Convert(converter = JsonToBlobAttributeConverter.class)
    JsonElement actions;

    /**
     * Set the GuildID.
     * @param guildId The GuildID.
     */
    public void setGuildId(long guildId) {
        if (guildAndName == null) guildAndName = new GuildAndName();
        guildAndName.setGuildId(guildId);
    }

    /**
     * Set the Name.
     * @param name The Name.
     */
    public void setName(String name) {
        if (guildAndName == null) guildAndName = new GuildAndName();
        guildAndName.setName(name);
    }

    /**
     * Get the GuildID.
     *
     * @return {@link long} as GuildID.
     */
    public long getGuild() {
        if (guildAndName == null)
            return 0;

        return guildAndName.getGuildId();
    }

    /**
     * Get the Name.
     *
     * @return the Name.
     */
    public String getName() {
        if (guildAndName == null) return null;
        return guildAndName.getName();
    }
}
