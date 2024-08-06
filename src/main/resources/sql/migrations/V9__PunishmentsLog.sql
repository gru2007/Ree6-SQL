CREATE TABLE PunishmentsLog
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    guildId     BIGINT                NOT NULL,
    userId     BIGINT                 NOT NULL,
    moderId     BIGINT                 NOT NULL,
    action      INT                   NOT NULL,
    reason      VARCHAR(255)          NULL,
    creation        datetime              NULL,
    CONSTRAINT pk_punishmentslog PRIMARY KEY (id)
);

ALTER TABLE PunishmentsLog
    MODIFY id BIGINT;

ALTER TABLE PunishmentsLog
    DROP PRIMARY KEY;

ALTER TABLE PunishmentsLog
    ADD PRIMARY KEY (id, guildId);

ALTER TABLE PunishmentsLog
    MODIFY id BIGINT NOT NULL AUTO_INCREMENT;
