ALTER TABLE ChatProtector
    CHANGE word name VARCHAR(255);

ALTER TABLE StreamActions
    CHANGE actionName name VARCHAR(255);

DROP TABLE IF EXISTS JoinMessage;

DROP TABLE IF EXISTS MuteRoles;

DROP TABLE IF EXISTS NewsWebhooks;

DROP TABLE IF EXISTS RainbowWebhooks;

ALTER TABLE ChatProtector
    MODIFY COLUMN id BIGINT NOT NULL;

ALTER TABLE ChatProtector
    DROP PRIMARY KEY;

ALTER TABLE ChatProtector
    DROP COLUMN id;

DELETE FROM Invites
    WHERE code IS NULL;

DELETE n1 FROM Invites n1, Invites n2
WHERE n1.id > n2.id
  AND n1.code = n2.code
  AND n1.guildid = n2.guildid;


ALTER TABLE Invites
    MODIFY COLUMN id BIGINT NOT NULL;

ALTER TABLE Invites
    DROP PRIMARY KEY;

ALTER TABLE Invites
    DROP COLUMN id;

DELETE n1 FROM ReactionRole n1, ReactionRole n2
WHERE n1.id > n2.id
  AND n1.roleId = n2.roleId
  AND n1.guildid = n2.guildid;


ALTER TABLE ReactionRole
    MODIFY COLUMN id BIGINT NOT NULL;

ALTER TABLE ReactionRole
    DROP PRIMARY KEY;

ALTER TABLE ReactionRole
    DROP COLUMN id;

ALTER TABLE StreamActions
    MODIFY COLUMN id BIGINT NOT NULL;

ALTER TABLE StreamActions
    DROP PRIMARY KEY;

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