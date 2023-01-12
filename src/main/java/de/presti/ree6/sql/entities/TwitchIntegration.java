package de.presti.ree6.sql.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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
}
