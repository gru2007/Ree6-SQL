CREATE TABLE AutoRoles
(
    id  INT AUTO_INCREMENT NOT NULL,
    gid VARCHAR(255)       NULL,
    rid VARCHAR(255)       NULL,
    CONSTRAINT pk_autoroles PRIMARY KEY (id)
);

CREATE TABLE BirthdayWish
(
    id       INT AUTO_INCREMENT NOT NULL,
    gid      VARCHAR(255)       NULL,
    cid      VARCHAR(255)       NULL,
    uid      VARCHAR(255)       NULL,
    birthday date               NULL,
    CONSTRAINT pk_birthdaywish PRIMARY KEY (id)
);

CREATE TABLE ChannelStats
(
    id                                  INT AUTO_INCREMENT NOT NULL,
    gid                                 VARCHAR(255)       NULL,
    memberChannelId                     VARCHAR(255)       NULL,
    onlyRealMemberChannelId             VARCHAR(255)       NULL,
    botMemberChannelId                  VARCHAR(255)       NULL,
    twitterFollowerChannelId            VARCHAR(255)       NULL,
    twitterFollowerChannelUsername      VARCHAR(255)       NULL,
    instagramFollowerChannelId          VARCHAR(255)       NULL,
    instagramFollowerChannelUsername    VARCHAR(255)       NULL,
    twitchFollowerChannelId             VARCHAR(255)       NULL,
    twitchFollowerChannelUsername       VARCHAR(255)       NULL,
    youtubeSubscribersChannelId         VARCHAR(255)       NULL,
    youtubeSubscribersChannelUsername   VARCHAR(255)       NULL,
    subredditMemberChannelId            VARCHAR(255)       NULL,
    subredditMemberChannelSubredditName VARCHAR(255)       NULL,
    CONSTRAINT pk_channelstats PRIMARY KEY (id)
);

CREATE TABLE ChatLevelAutoRoles
(
    id  INT AUTO_INCREMENT NOT NULL,
    gid VARCHAR(255)       NULL,
    rid VARCHAR(255)       NULL,
    lvl BIGINT             NULL,
    CONSTRAINT pk_chatlevelautoroles PRIMARY KEY (id)
);

CREATE TABLE ChatProtector
(
    id   INT AUTO_INCREMENT NOT NULL,
    gid  VARCHAR(255)       NULL,
    word VARCHAR(255)       NULL,
    CONSTRAINT pk_chatprotector PRIMARY KEY (id)
);

CREATE TABLE CommandStats
(
    id      INT AUTO_INCREMENT NOT NULL,
    command VARCHAR(255)       NULL,
    uses    INT                NULL,
    CONSTRAINT pk_commandstats PRIMARY KEY (id)
);

CREATE TABLE CustomCommands
(
    id              BIGINT AUTO_INCREMENT NOT NULL,
    guild           BIGINT                NULL,
    command         VARCHAR(255)          NULL,
    responseChannel BIGINT                NULL,
    responseMessage VARCHAR(255)          NULL,
    responseEmbed   MEDIUMBLOB            NULL,
    CONSTRAINT pk_customcommands PRIMARY KEY (id)
);

CREATE TABLE CustomEvents
(
    id        BIGINT AUTO_INCREMENT NOT NULL,
    guild     BIGINT                NULL,
    eventName VARCHAR(255)          NULL,
    eventTyp  VARCHAR(255)          NULL,
    actions   MEDIUMBLOB            NULL,
    CONSTRAINT pk_customevents PRIMARY KEY (id)
);

CREATE TABLE GuildStats
(
    id      INT AUTO_INCREMENT NOT NULL,
    gid     VARCHAR(255)       NULL,
    command VARCHAR(255)       NULL,
    uses    INT                NULL,
    CONSTRAINT pk_guildstats PRIMARY KEY (id)
);

CREATE TABLE InstagramNotify
(
    id      INT AUTO_INCREMENT NOT NULL,
    gid     VARCHAR(255)       NOT NULL,
    channel BIGINT             NOT NULL,
    cid     VARCHAR(255)       NOT NULL,
    token   VARCHAR(255)       NOT NULL,
    name    VARCHAR(255)       NULL,
    message VARCHAR(255)       NULL,
    CONSTRAINT pk_instagramnotify PRIMARY KEY (id)
);

CREATE TABLE Invites
(
    id   INT AUTO_INCREMENT NOT NULL,
    gid  VARCHAR(255)       NULL,
    uid  VARCHAR(255)       NULL,
    uses BIGINT             NULL,
    code VARCHAR(255)       NULL,
    CONSTRAINT pk_invites PRIMARY KEY (id)
);

CREATE TABLE Level
(
    id  INT AUTO_INCREMENT NOT NULL,
    gid VARCHAR(255)       NULL,
    uid VARCHAR(255)       NULL,
    xp  BIGINT             NULL,
    CONSTRAINT pk_level PRIMARY KEY (id)
);

CREATE TABLE LogWebhooks
(
    id      INT AUTO_INCREMENT NOT NULL,
    gid     VARCHAR(255)       NOT NULL,
    channel BIGINT             NOT NULL,
    cid     VARCHAR(255)       NOT NULL,
    token   VARCHAR(255)       NOT NULL,
    CONSTRAINT pk_logwebhooks PRIMARY KEY (id)
);

CREATE TABLE Money_Holder
(
    Id      BIGINT AUTO_INCREMENT NOT NULL,
    guildId BIGINT                NULL,
    userId  BIGINT                NULL,
    cash    DOUBLE                NULL,
    bank    DOUBLE                NULL,
    CONSTRAINT pk_money_holder PRIMARY KEY (Id)
);

CREATE TABLE Money_Transaction
(
    transactionId   BIGINT AUTO_INCREMENT NOT NULL,
    isSystemPayment BIT(1)                NOT NULL,
    guildId         BIGINT                NULL,
    sender          BIGINT                NOT NULL,
    receiver        BIGINT                NOT NULL,
    isReceiverBank  BIT(1)                NOT NULL,
    isSenderBank    BIT(1)                NOT NULL,
    amount          DOUBLE                NULL,
    creation        datetime              NULL,
    CONSTRAINT pk_money_transaction PRIMARY KEY (transactionId)
);

CREATE TABLE Opt_out
(
    id  INT AUTO_INCREMENT NOT NULL,
    gid VARCHAR(255)       NULL,
    uid VARCHAR(255)       NULL,
    CONSTRAINT pk_opt_out PRIMARY KEY (id)
);

CREATE TABLE Punishments
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    guildId     BIGINT                NOT NULL,
    warnings    INT                   NOT NULL,
    action      INT                   NOT NULL,
    roleId      BIGINT                NOT NULL,
    timeoutTime BIGINT                NOT NULL,
    reason      VARCHAR(255)          NULL,
    CONSTRAINT pk_punishments PRIMARY KEY (id)
);

CREATE TABLE RSSFeed
(
    id      INT AUTO_INCREMENT NOT NULL,
    gid     VARCHAR(255)       NOT NULL,
    channel BIGINT             NOT NULL,
    cid     VARCHAR(255)       NOT NULL,
    token   VARCHAR(255)       NOT NULL,
    url     VARCHAR(255)       NULL,
    CONSTRAINT pk_rssfeed PRIMARY KEY (id)
);

CREATE TABLE ReactionRole
(
    id             INT AUTO_INCREMENT NOT NULL,
    gid            BIGINT             NULL,
    emoteId        BIGINT             NULL,
    formattedEmote VARCHAR(255)       NULL,
    channelId      BIGINT             NULL,
    roleId         BIGINT             NULL,
    messageId      BIGINT             NULL,
    CONSTRAINT pk_reactionrole PRIMARY KEY (id)
);

CREATE TABLE Recording
(
    id           VARCHAR(255) NOT NULL,
    gid          VARCHAR(255) NULL,
    vid          VARCHAR(255) NULL,
    creator      VARCHAR(255) NULL,
    recording    TEXT         NULL,
    participants MEDIUMBLOB   NULL,
    created      BIGINT       NULL,
    CONSTRAINT pk_recording PRIMARY KEY (id)
);

CREATE TABLE RedditNotify
(
    id        INT AUTO_INCREMENT NOT NULL,
    gid       VARCHAR(255)       NOT NULL,
    channel   BIGINT             NOT NULL,
    cid       VARCHAR(255)       NOT NULL,
    token     VARCHAR(255)       NOT NULL,
    subreddit VARCHAR(255)       NULL,
    message   VARCHAR(255)       NULL,
    CONSTRAINT pk_redditnotify PRIMARY KEY (id)
);

CREATE TABLE ScheduledMessage
(
    Id           BIGINT AUTO_INCREMENT NOT NULL,
    guild        BIGINT                NULL,
    message      VARCHAR(255)          NULL,
    delay        BIGINT                NULL,
    shouldRepeat BIT(1)                NULL,
    webhook_id   INT                   NOT NULL,
    lastExecute  datetime              NULL,
    lastUpdated  datetime              NULL,
    created      datetime              NULL,
    CONSTRAINT pk_scheduledmessage PRIMARY KEY (Id)
);

CREATE TABLE ScheduledMessageWebhooks
(
    id      INT AUTO_INCREMENT NOT NULL,
    gid     VARCHAR(255)       NOT NULL,
    channel BIGINT             NOT NULL,
    cid     VARCHAR(255)       NOT NULL,
    token   VARCHAR(255)       NOT NULL,
    CONSTRAINT pk_scheduledmessagewebhooks PRIMARY KEY (id)
);

CREATE TABLE Settings
(
    id          INT AUTO_INCREMENT NOT NULL,
    gid         VARCHAR(255)       NULL,
    name        VARCHAR(255)       NULL,
    displayname VARCHAR(255)       NULL,
    value       TEXT               NULL,
    CONSTRAINT pk_settings PRIMARY KEY (id)
);

CREATE TABLE Statistics
(
    id    INT AUTO_INCREMENT NOT NULL,
    day   INT                NULL,
    month INT                NULL,
    year  INT                NULL,
    stats MEDIUMBLOB         NULL,
    CONSTRAINT pk_statistics PRIMARY KEY (id)
);

CREATE TABLE StreamActions
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    guildId    BIGINT                NULL,
    actionName VARCHAR(255)          NULL,
    auth_id    VARCHAR(255)          NOT NULL,
    listener   INT                   NULL,
    argument   VARCHAR(255)          NULL,
    actions    MEDIUMBLOB            NULL,
    CONSTRAINT pk_streamactions PRIMARY KEY (id)
);

CREATE TABLE Suggestions
(
    guildId   BIGINT NOT NULL,
    channelId BIGINT NULL,
    CONSTRAINT pk_suggestions PRIMARY KEY (guildId)
);

CREATE TABLE TemporalVoicechannel
(
    gid VARCHAR(255) NOT NULL,
    vid VARCHAR(255) NULL,
    CONSTRAINT pk_temporalvoicechannel PRIMARY KEY (gid)
);

CREATE TABLE Tickets
(
    guildId                BIGINT       NOT NULL,
    channelId              BIGINT       NULL,
    ticketCategory         BIGINT       NULL,
    logChannelId           BIGINT       NULL,
    logChannelWebhookId    BIGINT       NULL,
    logChannelWebhookToken VARCHAR(255) NULL,
    ticketCount            BIGINT       NULL,
    CONSTRAINT pk_tickets PRIMARY KEY (guildId)
);

CREATE TABLE TikTokNotify
(
    id      INT AUTO_INCREMENT NOT NULL,
    gid     VARCHAR(255)       NOT NULL,
    channel BIGINT             NOT NULL,
    cid     VARCHAR(255)       NOT NULL,
    token   VARCHAR(255)       NOT NULL,
    name    VARCHAR(255)       NULL,
    message VARCHAR(255)       NULL,
    CONSTRAINT pk_tiktoknotify PRIMARY KEY (id)
);

CREATE TABLE TwitchIntegration
(
    channel_id   VARCHAR(255) NOT NULL,
    user_id      BIGINT       NULL,
    token        VARCHAR(255) NULL,
    refresh      VARCHAR(255) NULL,
    channel_name VARCHAR(255) NULL,
    expires      INT          NULL,
    lastUpdated  datetime     NULL,
    CONSTRAINT pk_twitchintegration PRIMARY KEY (channel_id)
);

CREATE TABLE TwitchNotify
(
    id      INT AUTO_INCREMENT NOT NULL,
    gid     VARCHAR(255)       NOT NULL,
    channel BIGINT             NOT NULL,
    cid     VARCHAR(255)       NOT NULL,
    token   VARCHAR(255)       NOT NULL,
    name    VARCHAR(255)       NULL,
    message VARCHAR(255)       NULL,
    CONSTRAINT pk_twitchnotify PRIMARY KEY (id)
);

CREATE TABLE TwitterNotify
(
    id      INT AUTO_INCREMENT NOT NULL,
    gid     VARCHAR(255)       NOT NULL,
    channel BIGINT             NOT NULL,
    cid     VARCHAR(255)       NOT NULL,
    token   VARCHAR(255)       NOT NULL,
    name    VARCHAR(255)       NULL,
    message VARCHAR(255)       NULL,
    CONSTRAINT pk_twitternotify PRIMARY KEY (id)
);

CREATE TABLE VCLevel
(
    id  INT AUTO_INCREMENT NOT NULL,
    gid VARCHAR(255)       NULL,
    uid VARCHAR(255)       NULL,
    xp  BIGINT             NULL,
    CONSTRAINT pk_vclevel PRIMARY KEY (id)
);

CREATE TABLE VCLevelAutoRoles
(
    id  INT AUTO_INCREMENT NOT NULL,
    gid VARCHAR(255)       NULL,
    rid VARCHAR(255)       NULL,
    lvl BIGINT             NULL,
    CONSTRAINT pk_vclevelautoroles PRIMARY KEY (id)
);

CREATE TABLE Warning
(
    id       BIGINT AUTO_INCREMENT NOT NULL,
    userId   BIGINT                NOT NULL,
    guildId  BIGINT                NOT NULL,
    warnings INT                   NOT NULL,
    CONSTRAINT pk_warning PRIMARY KEY (id)
);

CREATE TABLE WelcomeWebhooks
(
    id      INT AUTO_INCREMENT NOT NULL,
    gid     VARCHAR(255)       NOT NULL,
    channel BIGINT             NOT NULL,
    cid     VARCHAR(255)       NOT NULL,
    token   VARCHAR(255)       NOT NULL,
    CONSTRAINT pk_welcomewebhooks PRIMARY KEY (id)
);

CREATE TABLE YouTubeNotify
(
    id      INT AUTO_INCREMENT NOT NULL,
    gid     VARCHAR(255)       NOT NULL,
    channel BIGINT             NOT NULL,
    cid     VARCHAR(255)       NOT NULL,
    token   VARCHAR(255)       NOT NULL,
    name    VARCHAR(255)       NULL,
    message VARCHAR(255)       NULL,
    CONSTRAINT pk_youtubenotify PRIMARY KEY (id)
);

ALTER TABLE Money_Transaction
    ADD CONSTRAINT FK_MONEY_TRANSACTION_ON_RECEIVER FOREIGN KEY (receiver) REFERENCES Money_Holder (Id);

ALTER TABLE Money_Transaction
    ADD CONSTRAINT FK_MONEY_TRANSACTION_ON_SENDER FOREIGN KEY (sender) REFERENCES Money_Holder (Id);

ALTER TABLE ScheduledMessage
    ADD CONSTRAINT FK_SCHEDULEDMESSAGE_ON_WEBHOOK FOREIGN KEY (webhook_id) REFERENCES ScheduledMessageWebhooks (id);

ALTER TABLE StreamActions
    ADD CONSTRAINT FK_STREAMACTIONS_ON_AUTH FOREIGN KEY (auth_id) REFERENCES TwitchIntegration (channel_id);