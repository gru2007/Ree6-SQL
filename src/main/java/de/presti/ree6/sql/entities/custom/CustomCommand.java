package de.presti.ree6.sql.entities.custom;

import com.google.gson.JsonElement;
import de.presti.ree6.sql.converter.JsonAttributeConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity class for the Custom commands.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CustomCommands")
public class CustomCommand {

    /**
     * The ID of the entity.
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * The ID of the Guild.
     */
    @Column(name = "guild")
    private long guildId;

    /**
     * The name of the command.
     */
    @Column(name = "command")
    private String name;

    /**
     * The channel that will receive the response.
     */
    @Column(name = "responseChannel")
    private long channelId;

    /**
     * The response of the command.
     */
    @Column(name = "responseMessage")
    private String messageResponse;

    /**
     * The response of the command.
     */
    @Convert(converter = JsonAttributeConverter.class)
    @Column(name = "responseEmbed")
    private JsonElement embedResponse;
}
