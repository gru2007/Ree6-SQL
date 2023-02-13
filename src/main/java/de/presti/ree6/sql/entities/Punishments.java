package de.presti.ree6.sql.entities;

import jakarta.persistence.Id;

public class Punishments {
    @Id
    long id;
    long guildId;
    int warnings;
    int action;
    long roleId;
    long timeoutTime;
    String reason;
}
