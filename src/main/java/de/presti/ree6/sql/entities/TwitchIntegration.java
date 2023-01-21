package de.presti.ree6.sql.entities;

import de.presti.ree6.sql.converter.ListAttributeConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "TwitchIntegration")
public class TwitchIntegration {

    @Id
    @Column(name = "channel_id")
    String channelId;

    @Column(name = "user_id")
    long userId;

    @Column(name = "token")
    String token;

    @Column(name = "refresh")
    String refresh;

    @Column(name = "channel_name")
    String name;

    @Column(name = "expires")
    int expiresIn;

    @Convert(converter = ListAttributeConverter.class)
    List<String> scopes;
}
