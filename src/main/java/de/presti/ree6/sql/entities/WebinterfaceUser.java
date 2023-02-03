package de.presti.ree6.sql.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "WebinterfaceUser")
public class WebinterfaceUser {
    @Id
    @Column(name = "userId")
    private long userId;
}
