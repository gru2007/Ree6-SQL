package de.presti.ree6.sql.entities.custom;

import com.google.gson.JsonElement;
import de.presti.ree6.sql.converter.JsonToBlobAttributeConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;

/**
 * Entity class for the Custom Events.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CustomEvents")
public class CustomEventAction {

    /**
     * The ID of the entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The ID of the Guild.
     */
    @Column(name = "guild")
    private long guildId;

    /**
     * The name of the custom event.
     */
    @Column(name = "eventName")
    private String name;

    /**
     * The typ of event which should trigger this action.
     */
    @Column(name = "eventTyp", columnDefinition = "VARCHAR(255)")
    @Enumerated(EnumType.STRING)
    private CustomEventTyp event;

    /**
     * The Actions that are to be executed.
     */
    @JdbcTypeCode(value = Types.LONGVARBINARY)
    @Column(name = "actions")
    @Convert(converter = JsonToBlobAttributeConverter.class)
    JsonElement actions;

}
