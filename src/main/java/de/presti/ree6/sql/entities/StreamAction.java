package de.presti.ree6.sql.entities;

import com.google.gson.JsonElement;
import de.presti.ree6.sql.converter.JsonAttributeConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "StreamActions")
public class StreamAction {

    /**
     * The ID of the entity.
     */
    @Id
    long id;

    /**
     * The ID of the Guild.
     */
    @Column(name = "guildId")
    long guildId;

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
}
