package de.presti.ree6.sql.entities;

import de.presti.ree6.sql.converter.ByteToBlobAttributeConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import java.sql.Types;

@Entity
@Getter
@Setter
@Table(name = "UserRankCard")
public class UserRankCard {

    /**
     * The UserID of the Invite.
     */
    @Id
    @Column(name = "userId")
    long userId;

    /**
     * The image as a byte array.
     */
    @Column(name = "rankCard")
    @JdbcTypeCode(value = Types.BLOB)
    @Convert(converter = ByteToBlobAttributeConverter.class)
    byte[] rankCard;
}
