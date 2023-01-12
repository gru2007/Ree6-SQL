package de.presti.ree6.sql.entities;

import com.google.gson.JsonElement;
import de.presti.ree6.sql.converter.JsonAttributeConverter;
import jakarta.persistence.*;

@Entity
@Table(name = "StreamActions")
public class StreamAction {

    /**
     * The ID of the Guild.
     */
    @Id
    @Column(name = "guildId")
    long guildId;

    /**
     * Typ of the Listener
     */
    @Column(name = "listener")
    StreamListener listener;


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

    public enum StreamListener {
        REDEMPTION, FOLLOW,
    }
}
