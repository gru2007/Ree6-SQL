CREATE TABLE SpotifyNotify
(
    channel   BIGINT       NOT NULL,
    webhookId BIGINT       NOT NULL,
    token     VARCHAR(255) NOT NULL,
    entityId  VARCHAR(255) NULL,
    typ       INT NULL,
    message   VARCHAR(255) NULL,
    id        BIGINT       NOT NULL,
    guildId   BIGINT       NOT NULL,
    CONSTRAINT pk_spotifynotify PRIMARY KEY (id, guildId)
);