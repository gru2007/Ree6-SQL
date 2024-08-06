CREATE TABLE PunishmentsLog
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    guildId     BIGINT                NOT NULL,
    action      INT                   NOT NULL,
    reason      VARCHAR(255)          NULL,
    creation        datetime              NULL,
    CONSTRAINT pk_punishmentslog PRIMARY KEY (id)
);

ALTER TABLE Punishments
    MODIFY id BIGINT;

ALTER TABLE Punishments
    DROP PRIMARY KEY;

ALTER TABLE Punishments
    ADD PRIMARY KEY (id, guildId);

ALTER TABLE Punishments
    MODIFY id BIGINT NOT NULL AUTO_INCREMENT;