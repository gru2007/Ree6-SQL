package de.presti.ree6.sql.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

/**
 * SQL Entity for the Twitch Integration.
 */
@Getter
@Setter
@Entity
@Table(name = "TwitchIntegration")
public class TwitchIntegration {

    /**
     * The ID of the Twitch Channel.
     */
    @Id
    @Column(name = "channel_id")
    String channelId;

    /**
     * The ID of the Discord User It's linked to.
     */
    @Column(name = "user_id")
    long userId;

    /**
     * The Access Token.
     */
    @Column(name = "token")
    String token;

    /**
     * The Refresh Token.
     */
    @Column(name = "refresh")
    String refresh;

    /**
     * The Name of the Twitch Channel.
     */
    @Column(name = "channel_name")
    String name;

    /**
     * The expiration time of the token.
     */
    @Column(name = "expires")
    int expiresIn;

    /**
     * Last updated time.
     */
    @Setter(AccessLevel.PRIVATE)
    @UpdateTimestamp
    Timestamp lastUpdated;
}
