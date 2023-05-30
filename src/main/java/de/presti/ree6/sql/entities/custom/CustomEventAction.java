package de.presti.ree6.sql.entities.custom;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @GeneratedValue
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
    @Column(name = "eventTyp")
    @Enumerated(EnumType.STRING)
    private CustomEventTyp event;

    // TODO:: maybe use a json based action system like the streamtools one?

}
