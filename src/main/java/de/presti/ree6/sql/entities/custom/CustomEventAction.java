package de.presti.ree6.sql.entities.custom;

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
@Table(name = "CustomEvents")
public class CustomEventAction {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "guild")
    private long guildId;

    @Column(name = "eventName")
    private String name;

    @Enumerated(EnumType.STRING)
    private CustomEventTyp event;

    // TODO:: maybe use a json based action system like the streamtools one?

}
