package de.presti.ree6.sql.entities.custom;

import com.google.gson.JsonElement;
import de.presti.ree6.sql.converter.JsonAttributeConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CustomCommands")
public class CustomCommand {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "guild")
    private long guildId;

    @Column(name = "command")
    private String name;

    @Column(name = "responseChannel")
    private long channelId;

    @Column(name = "responseMessage")
    private String messageResponse;

    @Convert(converter = JsonAttributeConverter.class)
    @Column(name = "responseEmbed")
    private JsonElement embedResponse;
}
