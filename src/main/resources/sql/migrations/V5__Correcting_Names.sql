ALTER TABLE BirthdayWish
    CHANGE cid channelId BIGINT;

ALTER TABLE BirthdayWish
    CHANGE gid guildId BIGINT;

ALTER TABLE BirthdayWish
    CHANGE uid userId BIGINT;

ALTER TABLE Recording
    CHANGE vid channelId BIGINT;

ALTER TABLE Recording
    CHANGE gid guildId BIGINT;

ALTER TABLE ChannelStats
    CHANGE gid guildId BIGINT;

ALTER TABLE ChatProtector
    CHANGE gid guildId BIGINT;

ALTER TABLE Invites
    CHANGE gid guildId BIGINT;

ALTER TABLE Invites
    CHANGE uid userId BIGINT;

ALTER TABLE ReactionRole
    CHANGE gid guildId BIGINT;

ALTER TABLE InstagramNotify
    CHANGE cid webhookId BIGINT;

ALTER TABLE RSSFeed
    CHANGE cid webhookId BIGINT;

ALTER TABLE RedditNotify
    CHANGE cid webhookId BIGINT;

ALTER TABLE ScheduledMessageWebhooks
    CHANGE cid webhookId BIGINT;

ALTER TABLE TikTokNotify
    CHANGE cid webhookId BIGINT;

ALTER TABLE TwitchNotify
    CHANGE cid webhookId BIGINT;

ALTER TABLE TwitterNotify
    CHANGE cid webhookId BIGINT;

ALTER TABLE YouTubeNotify
    CHANGE cid webhookId BIGINT;

ALTER TABLE ReactionRole
    MODIFY id BIGINT;

ALTER TABLE CommandStats
    MODIFY uses BIGINT;

ALTER TABLE GuildStats
    MODIFY uses BIGINT;

ALTER TABLE ScheduledMessage
    MODIFY webhook_id BIGINT NOT NULL;