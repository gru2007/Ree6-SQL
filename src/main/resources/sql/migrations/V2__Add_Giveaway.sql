CREATE TABLE Giveaway
(
    messageId BIGINT NOT NULL,
    creatorId BIGINT NOT NULL,
    guildId   BIGINT NOT NULL,
    channelId BIGINT NOT NULL,
    prize     VARCHAR(255) NULL,
    winners   BIGINT NOT NULL,
    created   datetime NULL,
    ending    datetime NULL,
    CONSTRAINT pk_giveaway PRIMARY KEY (messageId)
);