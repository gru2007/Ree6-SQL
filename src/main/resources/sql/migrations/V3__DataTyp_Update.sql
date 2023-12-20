ALTER TABLE Level
    CHANGE gid guildId BIGINT;

ALTER TABLE Level
    CHANGE uid userId BIGINT;

ALTER TABLE Opt_out
    CHANGE gid guildId BIGINT;

ALTER TABLE Opt_out
    CHANGE uid userId BIGINT;

ALTER TABLE Settings
    CHANGE gid guildId BIGINT;

ALTER TABLE VCLevel
    CHANGE gid guildId BIGINT;

ALTER TABLE VCLevel
    CHANGE uid userId BIGINT;

ALTER TABLE ChannelStats
    DROP COLUMN id;

ALTER TABLE Level
    DROP COLUMN id;

ALTER TABLE Opt_out
    DROP COLUMN id;

ALTER TABLE Settings
    DROP COLUMN id;

ALTER TABLE VCLevel
    DROP COLUMN id;

ALTER TABLE BirthdayWish
    MODIFY cid BIGINT;

ALTER TABLE InstagramNotify
    MODIFY cid BIGINT;

ALTER TABLE LogWebhooks
    MODIFY cid BIGINT;

ALTER TABLE RSSFeed
    MODIFY cid BIGINT;

ALTER TABLE RedditNotify
    MODIFY cid BIGINT;

ALTER TABLE ScheduledMessageWebhooks
    MODIFY cid BIGINT;

ALTER TABLE TikTokNotify
    MODIFY cid BIGINT;

ALTER TABLE TwitchNotify
    MODIFY cid BIGINT;

ALTER TABLE TwitterNotify
    MODIFY cid BIGINT;

ALTER TABLE WelcomeWebhooks
    MODIFY cid BIGINT;

ALTER TABLE YouTubeNotify
    MODIFY cid BIGINT;

ALTER TABLE Recording
    MODIFY creator BIGINT;

ALTER TABLE AutoRoles
    MODIFY gid BIGINT;

ALTER TABLE BirthdayWish
    MODIFY gid BIGINT;

ALTER TABLE ChannelStats
    MODIFY gid BIGINT;

ALTER TABLE ChatLevelAutoRoles
    MODIFY gid BIGINT;

ALTER TABLE ChatProtector
    MODIFY gid BIGINT;

ALTER TABLE GuildStats
    MODIFY gid BIGINT;

ALTER TABLE InstagramNotify
    MODIFY gid BIGINT;

ALTER TABLE Invites
    MODIFY gid BIGINT;

ALTER TABLE LogWebhooks
    MODIFY gid BIGINT;

ALTER TABLE RSSFeed
    MODIFY gid BIGINT;

ALTER TABLE Recording
    MODIFY gid BIGINT;

ALTER TABLE RedditNotify
    MODIFY gid BIGINT;

ALTER TABLE ScheduledMessageWebhooks
    MODIFY gid BIGINT;

ALTER TABLE TemporalVoicechannel
    MODIFY gid BIGINT;

ALTER TABLE TikTokNotify
    MODIFY gid BIGINT;

ALTER TABLE TwitchNotify
    MODIFY gid BIGINT;

ALTER TABLE TwitterNotify
    MODIFY gid BIGINT;

ALTER TABLE VCLevelAutoRoles
    MODIFY gid BIGINT;

ALTER TABLE WelcomeWebhooks
    MODIFY gid BIGINT;

ALTER TABLE YouTubeNotify
    MODIFY gid BIGINT;

ALTER TABLE Settings
    MODIFY name VARCHAR(255) NOT NULL;

ALTER TABLE AutoRoles
    MODIFY rid BIGINT;

ALTER TABLE ChatLevelAutoRoles
    MODIFY rid BIGINT;

ALTER TABLE VCLevelAutoRoles
    MODIFY rid BIGINT;

ALTER TABLE BirthdayWish
    MODIFY uid BIGINT;

ALTER TABLE Invites
    MODIFY uid BIGINT;

ALTER TABLE Recording
    MODIFY vid BIGINT;

ALTER TABLE TemporalVoicechannel
    MODIFY vid BIGINT;

ALTER TABLE ChannelStats
    ADD PRIMARY KEY (gid);

ALTER TABLE Level
    ADD PRIMARY KEY (guildId, userId);

ALTER TABLE Opt_out
    ADD PRIMARY KEY (guildId, userId);

ALTER TABLE Settings
    ADD PRIMARY KEY (guildId, name);

ALTER TABLE VCLevel
    ADD PRIMARY KEY (guildId, userId);