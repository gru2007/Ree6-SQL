ALTER TABLE Money_Transaction
    DROP FOREIGN KEY FK_MONEY_TRANSACTION_ON_RECEIVER;

ALTER TABLE Money_Transaction
    DROP FOREIGN KEY FK_MONEY_TRANSACTION_ON_SENDER;

ALTER TABLE ScheduledMessage
    DROP FOREIGN KEY FK_SCHEDULEDMESSAGE_ON_WEBHOOK;

ALTER TABLE TemporalVoicechannel
    CHANGE vid channelId BIGINT;

ALTER TABLE TemporalVoicechannel
    CHANGE gid guildId BIGINT;

ALTER TABLE AutoRoles
    CHANGE gid guildId BIGINT;

ALTER TABLE AutoRoles
    CHANGE rid roleId BIGINT;

ALTER TABLE ChatLevelAutoRoles
    CHANGE gid guildId BIGINT;

ALTER TABLE ChatLevelAutoRoles
    CHANGE rid roleId BIGINT;

ALTER TABLE InstagramNotify
    CHANGE gid guildId BIGINT;

ALTER TABLE RSSFeed
    CHANGE gid guildId BIGINT;

ALTER TABLE RedditNotify
    CHANGE gid guildId BIGINT;

ALTER TABLE ScheduledMessageWebhooks
    CHANGE gid guildId BIGINT;

ALTER TABLE TikTokNotify
    CHANGE gid guildId BIGINT;

ALTER TABLE TwitchNotify
    CHANGE gid guildId BIGINT;

ALTER TABLE TwitterNotify
    CHANGE gid guildId BIGINT;

ALTER TABLE VCLevelAutoRoles
    CHANGE gid guildId BIGINT;

ALTER TABLE VCLevelAutoRoles
    CHANGE rid roleId BIGINT;

ALTER TABLE YouTubeNotify
    CHANGE gid guildId BIGINT;

ALTER TABLE WelcomeWebhooks
    CHANGE gid guildId BIGINT NOT NULL;

ALTER TABLE LogWebhooks
    CHANGE gid guildId BIGINT NOT NULL;

ALTER TABLE Money_Transaction
    ADD receiver_guildId BIGINT NOT NULL;

ALTER TABLE Money_Transaction
    ADD receiver_userId BIGINT NOT NULL;

UPDATE Money_Transaction
    JOIN Money_Holder ON Money_Transaction.receiver = Money_Holder.id
SET Money_Transaction.receiver_userId = Money_Holder.userId,
    Money_Transaction.receiver_guildId = Money_Holder.guildId;

ALTER TABLE Money_Transaction
    ADD sender_guildId BIGINT NOT NULL;

ALTER TABLE Money_Transaction
    ADD sender_userId BIGINT NOT NULL;

UPDATE Money_Transaction
    JOIN Money_Holder ON Money_Transaction.sender = Money_Holder.id
SET Money_Transaction.sender_userId = Money_Holder.userId,
    Money_Transaction.sender_guildId = Money_Holder.guildId;

ALTER TABLE ScheduledMessage
    MODIFY webhook_id BIGINT NULL;

ALTER TABLE ScheduledMessage
    ADD webhook_guildId BIGINT NOT NULL;

ALTER TABLE Money_Holder
    DROP COLUMN Id;

ALTER TABLE AutoRoles
    DROP COLUMN id;

ALTER TABLE ChatLevelAutoRoles
    DROP COLUMN id;

ALTER TABLE LogWebhooks
    DROP COLUMN id;

ALTER TABLE VCLevelAutoRoles
    DROP COLUMN id;

ALTER TABLE WelcomeWebhooks
    DROP COLUMN id;

ALTER TABLE Money_Transaction
    DROP COLUMN receiver;

ALTER TABLE Money_Transaction
    DROP COLUMN sender;

ALTER TABLE Suggestions
    MODIFY channelId BIGINT NOT NULL;

ALTER TABLE InstagramNotify
    MODIFY cid BIGINT NOT NULL;

ALTER TABLE LogWebhooks
    MODIFY cid BIGINT NOT NULL;

ALTER TABLE RSSFeed
    MODIFY cid BIGINT NOT NULL;

ALTER TABLE RedditNotify
    MODIFY cid BIGINT NOT NULL;

ALTER TABLE ScheduledMessageWebhooks
    MODIFY cid BIGINT NOT NULL;

ALTER TABLE TikTokNotify
    MODIFY cid BIGINT NOT NULL;

ALTER TABLE TwitchNotify
    MODIFY cid BIGINT NOT NULL;

ALTER TABLE TwitterNotify
    MODIFY cid BIGINT NOT NULL;

ALTER TABLE WelcomeWebhooks
    MODIFY cid BIGINT NOT NULL;

ALTER TABLE YouTubeNotify
    MODIFY cid BIGINT NOT NULL;

ALTER TABLE Money_Holder
    MODIFY guildId BIGINT NOT NULL;

ALTER TABLE Money_Holder
    MODIFY userId BIGINT NOT NULL;

ALTER TABLE ScheduledMessageWebhooks
    MODIFY id BIGINT NOT NULL;

ALTER TABLE ScheduledMessageWebhooks
    DROP PRIMARY KEY;

ALTER TABLE InstagramNotify
    MODIFY id BIGINT NOT NULL;

ALTER TABLE InstagramNotify
    DROP PRIMARY KEY;

ALTER TABLE YouTubeNotify
    MODIFY id BIGINT NOT NULL;

ALTER TABLE YouTubeNotify
    DROP PRIMARY KEY;

ALTER TABLE TwitchNotify
    MODIFY id BIGINT NOT NULL;

ALTER TABLE TwitchNotify
    DROP PRIMARY KEY;

ALTER TABLE TwitterNotify
    MODIFY id BIGINT NOT NULL;

ALTER TABLE TwitterNotify
    DROP PRIMARY KEY;

ALTER TABLE RSSFeed
    MODIFY id BIGINT NOT NULL;

ALTER TABLE RSSFeed
    DROP PRIMARY KEY;

ALTER TABLE RedditNotify
    MODIFY id BIGINT NOT NULL;

ALTER TABLE RedditNotify
    DROP PRIMARY KEY;

ALTER TABLE TikTokNotify
    MODIFY id BIGINT NOT NULL;

ALTER TABLE TikTokNotify
    DROP PRIMARY KEY;

ALTER TABLE AutoRoles
    ADD PRIMARY KEY (guildId, roleId);

ALTER TABLE ChatLevelAutoRoles
    ADD PRIMARY KEY (guildId, roleId);

ALTER TABLE InstagramNotify
    ADD PRIMARY KEY (id, guildId);

ALTER TABLE InstagramNotify
    MODIFY id BIGINT NOT NULL AUTO_INCREMENT;

ALTER TABLE LogWebhooks
    ADD PRIMARY KEY (guildId);

ALTER TABLE Money_Holder
    ADD PRIMARY KEY (guildId, userId);

ALTER TABLE RedditNotify
    ADD PRIMARY KEY (id, guildId);

ALTER TABLE RedditNotify
    MODIFY id BIGINT NOT NULL AUTO_INCREMENT;

ALTER TABLE RSSFeed
    ADD PRIMARY KEY (id, guildId);

ALTER TABLE RSSFeed
    MODIFY id BIGINT NOT NULL AUTO_INCREMENT;

ALTER TABLE ScheduledMessageWebhooks
    ADD PRIMARY KEY (id, guildId);

ALTER TABLE ScheduledMessageWebhooks
    MODIFY id BIGINT NOT NULL AUTO_INCREMENT;

ALTER TABLE Suggestions
    DROP PRIMARY KEY;

ALTER TABLE Suggestions
    ADD PRIMARY KEY (guildId, channelId);

ALTER TABLE TemporalVoicechannel
    DROP PRIMARY KEY;

ALTER TABLE TemporalVoicechannel
    ADD PRIMARY KEY (guildId, channelId);

ALTER TABLE TikTokNotify
    ADD PRIMARY KEY (id, guildId);

ALTER TABLE TikTokNotify
    MODIFY id BIGINT NOT NULL AUTO_INCREMENT;

ALTER TABLE TwitchNotify
    ADD PRIMARY KEY (id, guildId);

ALTER TABLE TwitchNotify
    MODIFY id BIGINT NOT NULL AUTO_INCREMENT;

ALTER TABLE TwitterNotify
    ADD PRIMARY KEY (id, guildId);

ALTER TABLE TwitterNotify
    MODIFY id BIGINT NOT NULL AUTO_INCREMENT;

ALTER TABLE VCLevelAutoRoles
    ADD PRIMARY KEY (guildId, roleId);

ALTER TABLE WelcomeWebhooks
    ADD PRIMARY KEY (guildId);

ALTER TABLE YouTubeNotify
    ADD PRIMARY KEY (id, guildId);

ALTER TABLE YouTubeNotify
    MODIFY id BIGINT NOT NULL AUTO_INCREMENT;

ALTER TABLE Money_Transaction
    ADD CONSTRAINT FK_MONEY_TRANSACTION_RECEIVER FOREIGN KEY (receiver_guildId, receiver_userId) REFERENCES Money_Holder (guildId, userId);

ALTER TABLE Money_Transaction
    ADD CONSTRAINT FK_MONEY_TRANSACTION_SENDER FOREIGN KEY (sender_guildId, sender_userId) REFERENCES Money_Holder (guildId, userId);

ALTER TABLE ScheduledMessage
    ADD CONSTRAINT FK_SCHEDULED_MESSAGE_WEBHOOK FOREIGN KEY (webhook_id, webhook_guildId) REFERENCES ScheduledMessageWebhooks (id, guildId);