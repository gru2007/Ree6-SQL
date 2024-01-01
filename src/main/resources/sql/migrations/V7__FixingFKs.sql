ALTER TABLE ChatProtector
    CHANGE word name VARCHAR(255);

ALTER TABLE StreamActions
    CHANGE actionName name VARCHAR(255);

DROP TABLE IF EXISTS JoinMessage;

DROP TABLE IF EXISTS MuteRoles;

DROP TABLE IF EXISTS NewsWebhooks;

DROP TABLE IF EXISTS RainbowWebhooks;

ALTER TABLE ChatProtector
    DROP COLUMN id;

ALTER TABLE Invites
    DROP COLUMN id;

ALTER TABLE ReactionRole
    DROP COLUMN id;

ALTER TABLE StreamActions
    DROP COLUMN id;

ALTER TABLE Invites
    MODIFY code VARCHAR(255) NOT NULL;

ALTER TABLE ReactionRole
    MODIFY roleId BIGINT NOT NULL;

ALTER TABLE ChatProtector
    ADD PRIMARY KEY (guildId, name);

ALTER TABLE Invites
    ADD PRIMARY KEY (guildId, code);

ALTER TABLE ReactionRole
    ADD PRIMARY KEY (guildId, roleId);

ALTER TABLE StreamActions
    ADD PRIMARY KEY (guildId, name);