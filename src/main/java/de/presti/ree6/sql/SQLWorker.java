package de.presti.ree6.sql;

import com.google.gson.JsonObject;
import de.presti.ree6.sql.entities.*;
import de.presti.ree6.sql.entities.level.ChatUserLevel;
import de.presti.ree6.sql.entities.level.UserLevel;
import de.presti.ree6.sql.entities.level.VoiceUserLevel;
import de.presti.ree6.sql.entities.roles.AutoRole;
import de.presti.ree6.sql.entities.roles.ChatAutoRole;
import de.presti.ree6.sql.entities.roles.VoiceAutoRole;
import de.presti.ree6.sql.entities.stats.CommandStats;
import de.presti.ree6.sql.entities.stats.GuildCommandStats;
import de.presti.ree6.sql.entities.stats.Statistics;
import de.presti.ree6.sql.entities.webhook.*;
import de.presti.ree6.sql.entities.webhook.base.Webhook;
import de.presti.ree6.sql.entities.webhook.base.WebhookSocial;
import de.presti.ree6.sql.keys.*;
import de.presti.ree6.sql.util.SettingsManager;
import io.sentry.Sentry;
import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.query.Query;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.sql.SQLNonTransientConnectionException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * A Class to actually handle the SQL data.
 * Used to provide Data from the Database and to save Data into the Database.
 * <p>
 * Constructor to create a new Instance of the SQLWorker with a ref to the SQL-Connector.
 *
 * @param sqlConnector an Instance of the SQL-Connector to retrieve the data from.
 */
@Slf4j
@SuppressWarnings({"SqlNoDataSourceInspection", "SqlResolve", "unused", "SingleStatementInBlock"})
public record SQLWorker(SQLConnector sqlConnector) {

    //region Level

    //region Chat

    /**
     * Get the Chat XP Count of the give UserID from the given Guild.
     *
     * @param guildId the ID of the Guild.
     * @param userId  the ID of the User.
     * @return {@link Long} as XP Count.
     */
    public Mono<ChatUserLevel> getChatLevelData(long guildId, long userId) {
        return getEntity(new ChatUserLevel(), "FROM ChatUserLevel WHERE guildUserId.guildId=:gid AND guildUserId.userId=:uid", Map.of("gid", guildId, "uid", userId)).map(x -> {
            if (x == null) {
                return new ChatUserLevel(guildId, userId, 0);
            }
            return x;
        });
    }

    /**
     * Check if the given combination of UserID and GuildID is saved in our Database.
     *
     * @param guildId the ID of the Guild.
     * @param userId  the ID of the User.
     * @return {@link Boolean} true if there was a match | false if there wasn't a match.
     */
    public Mono<Boolean> existsInChatLevel(long guildId, long userId) {
        return getEntity(new ChatUserLevel(), "FROM ChatUserLevel WHERE guildUserId.guildId=:gid AND guildUserId.userId=:uid", Map.of("gid", guildId, "uid", userId)).map(Objects::nonNull);
    }

    /**
     * Give the wanted User more XP.
     *
     * @param guildId   the ID of the Guild.
     * @param userLevel the {@link ChatUserLevel} Entity with all the information.
     */
    public void addChatLevelData(long guildId, @Nonnull ChatUserLevel userLevel) {
        isOptOut(guildId, userLevel.getUserId()).subscribe(optOut -> {
            if (!optOut) {
                updateEntity(userLevel).block();
            }
        });
    }

    /**
     * Get the Top list of the Guild Chat XP.
     *
     * @param guildId the ID of the Guild.
     * @param limit   the Limit of how many should be given back.
     * @return {@link List<ChatUserLevel>} as container of the User IDs.
     */
    public Mono<List<ChatUserLevel>> getTopChat(long guildId, int limit) {
        return getEntityList(new ChatUserLevel(), "FROM ChatUserLevel WHERE guildUserId.guildId=:gid ORDER BY experience DESC",
                Map.of("gid", guildId), limit);
    }

    /**
     * Get the Top list of the Guild Chat XP.
     *
     * @param guildId the ID of the Guild.
     * @return {@link List<Long>} as container of the User IDs.
     */
    public Mono<List<Long>> getAllChatLevelSorted(long guildId) {
        return getEntityList(new ChatUserLevel(), "FROM ChatUserLevel WHERE guildUserId.guildId=:gid ORDER BY experience DESC",
                Map.of("gid", guildId)).map(x -> x.stream().map(UserLevel::getUserId).toList());
    }

    //endregion

    //region Voice

    /**
     * Get the Voice XP Count of the give UserID from the given Guild.
     *
     * @param guildId the ID of the Guild.
     * @param userId  the ID of the User.
     * @return {@link VoiceUserLevel} with information about the User Level.
     */
    public Mono<VoiceUserLevel> getVoiceLevelData(long guildId, long userId) {
        return getEntity(new VoiceUserLevel(), "FROM VoiceUserLevel WHERE guildUserId.guildId=:gid AND guildUserId.userId=:uid", Map.of("gid", guildId, "uid", userId)).map(x -> {
            if (x == null) {
                return new VoiceUserLevel(guildId, userId, 0);
            }

            return x;
        });
    }

    /**
     * Check if the given combination of UserID and GuildID is saved in our Database.
     *
     * @param guildId the ID of the Guild.
     * @param userId  the ID of the User.
     * @return {@link Boolean} true if there was a match | false if there wasn't a match.
     */
    public Mono<Boolean> existsInVoiceLevel(long guildId, long userId) {
        return getEntity(new VoiceUserLevel(), "FROM VoiceUserLevel WHERE guildUserId.guildId=:gid AND guildUserId.userId=:uid", Map.of("gid", guildId, "uid", userId)).map(Objects::nonNull);
    }

    /**
     * Give the wanted User more XP.
     *
     * @param guildId        the ID of the Guild.
     * @param voiceUserLevel the {@link VoiceUserLevel} Entity with all the information.
     */
    public void addVoiceLevelData(long guildId, @Nonnull VoiceUserLevel voiceUserLevel) {
        isOptOut(guildId, voiceUserLevel.getUserId()).subscribe(optOut -> {
            if (!optOut) {
                updateEntity(voiceUserLevel).block();
            }
        });
    }

    /**
     * Get the Top list of the Guild Voice XP.
     *
     * @param guildId the ID of the Guild.
     * @param limit   the Limit of how many should be given back.
     * @return {@link List<VoiceUserLevel>} as container of the User IDs.
     */
    public Mono<List<VoiceUserLevel>> getTopVoice(long guildId, int limit) {
        // Return the list.
        return getEntityList(new VoiceUserLevel(),
                "FROM VoiceUserLevel WHERE guildUserId.guildId=:gid ORDER BY experience DESC", Map.of("gid", guildId), limit);
    }

    /**
     * Get the Top list of the Guild Voice XP.
     *
     * @param guildId the ID of the Guild.
     * @return {@link List<Long>} as container of the UserIds.
     */
    public Mono<List<Long>> getAllVoiceLevelSorted(long guildId) {
        // Creating an SQL Statement to get the Entries from the Level Table by the GuildID.
        return getEntityList(new VoiceUserLevel(), "FROM VoiceUserLevel WHERE guildUserId.guildId=:gid ORDER BY experience DESC", Map.of("gid", guildId))
                .map(x -> x.stream().map(VoiceUserLevel::getUserId).toList());
    }

    //endregion

    //endregion

    //region Webhooks

    //region Logs

    /**
     * Get the LogWebhook data.
     *
     * @param guildId the ID of the Guild.
     * @return {@link Webhook} with all the needed data.
     */
    public Mono<WebhookLog> getLogWebhook(long guildId) {
        return getEntity(new WebhookLog(), "FROM WebhookLog WHERE guildId=:gid", Map.of("gid", guildId));
    }

    /**
     * Set the LogWebhook in our Database.
     *
     * @param guildId   the ID of the Guild.
     * @param channelId the ID of the Channel.
     * @param webhookId the ID of the Webhook.
     * @param authToken the Auth-token to verify the access.
     */
    public void setLogWebhook(long guildId, long channelId, long webhookId, String authToken) {
        getLogWebhook(guildId).subscribe(webhookLog -> {
            if (webhookLog == null) {
                webhookLog = new WebhookLog();
                webhookLog.setGuildId(guildId);
            }

            webhookLog.setChannelId(channelId);
            webhookLog.setWebhookId(webhookId);
            webhookLog.setToken(authToken);

            updateEntity(webhookLog).block();
        });
    }

    /**
     * Check if the Log Webhook has been set in our Database for this Server.
     *
     * @param guildId the ID of the Guild.
     * @return {@link Boolean} if true, it has been set | if false, it hasn't been set.
     */
    public Mono<Boolean> isLogSetup(long guildId) {
        return getEntity(new WebhookLog(), "FROM WebhookLog WHERE guildId=:gid", Map.of("gid", guildId)).map(Objects::nonNull);
    }

    /**
     * Check if the Log Webhook data is in our Database.
     *
     * @param webhookId the ID of the Webhook.
     * @param authToken the Auth-Token of the Webhook.
     * @return {@link Boolean} if true, it has been set | if false, it hasn't been set.
     */
    public Mono<Boolean> existsLogData(long webhookId, String authToken) {
        return getEntity(new WebhookLog(), "FROM WebhookLog WHERE webhookId=:cid AND token=:token", Map.of("cid", String.valueOf(webhookId), "token", authToken)).map(Objects::nonNull);
    }

    /**
     * Set the LogWebhook in our Database.
     *
     * @param webhookId the ID of the Webhook.
     * @param authToken the Auth-Token of the Webhook.
     */
    public void deleteLogWebhook(long webhookId, String authToken) {
        getEntity(new WebhookLog(), "FROM WebhookLog WHERE webhookId=:cid AND token=:token", Map.of("cid", String.valueOf(webhookId), "token", authToken)).subscribe(webhookLog -> {
            if (webhookLog != null) {
                deleteEntityInternally(webhookLog);
            }
        });
    }

    //endregion

    //region Welcome

    /**
     * Get the WelcomeWebhooks data.
     *
     * @param guildId the ID of the Guild.
     * @return {@link WebhookWelcome} with all the needed data.
     */
    public Mono<WebhookWelcome> getWelcomeWebhook(long guildId) {
        return getEntity(new WebhookWelcome(), "FROM WebhookWelcome WHERE guildId=:gid", Map.of("gid", guildId));
    }

    /**
     * Set the WelcomeWebhooks in our Database.
     *
     * @param guildId   the ID of the Guild.
     * @param channelId the ID of the Channel.
     * @param webhookId the ID of the Webhook.
     * @param authToken the Auth-token to verify the access.
     */
    public void setWelcomeWebhook(long guildId, long channelId, long webhookId, String authToken) {
        getWelcomeWebhook(guildId).subscribe(webhookWelcome -> {
            if (webhookWelcome == null) {
                webhookWelcome = new WebhookWelcome();
                webhookWelcome.setGuildId(guildId);
            }

            webhookWelcome.setChannelId(channelId);
            webhookWelcome.setWebhookId(webhookId);
            webhookWelcome.setToken(authToken);

            updateEntity(webhookWelcome).block();
        });
    }

    /**
     * Check if the Welcome Webhook has been set in our Database for this Server.
     *
     * @param guildId the ID of the Guild.
     * @return {@link Boolean} if true, it has been set | if false, it hasn't been set.
     */
    public Mono<Boolean> isWelcomeSetup(long guildId) {
        return getEntity(new WebhookWelcome(), "FROM WebhookWelcome WHERE guildId=:gid", Map.of("gid", guildId)).map(Objects::nonNull);
    }

    //endregion

    //region Twitch Notifier

    /**
     * Get the TwitchNotify data.
     *
     * @param guildId    the ID of the Guild.
     * @param twitchName the Username of the Twitch User.
     * @return {@link WebhookTwitch} with all the needed data.
     */
    public Mono<WebhookTwitch> getTwitchWebhook(long guildId, String twitchName) {
        return getEntity(new WebhookTwitch(), "FROM WebhookTwitch WHERE guildAndId.guildId=:gid AND name=:name", Map.of("gid", guildId, "name", twitchName));
    }

    /**
     * Get the TwitchNotify data.
     *
     * @param twitchName the Username of the Twitch User.
     * @return {@link List<WebhookTwitch>} with all the needed data.
     */
    public Mono<List<WebhookTwitch>> getTwitchWebhooksByName(String twitchName) {
        return getEntityList(new WebhookTwitch(), "FROM WebhookTwitch WHERE name=:name", Map.of("name", twitchName));
    }

    /**
     * Get the all Twitch-Notifier.
     *
     * @return {@link List<>} the entry.
     */
    public Mono<List<String>> getAllTwitchNames() {
        return getEntityList(new WebhookTwitch(), "FROM WebhookTwitch", null).map(x -> x.stream().map(WebhookTwitch::getName).toList());
    }

    /**
     * Get every Twitch-Notifier that has been set up for the given Guild.
     *
     * @param guildId the ID of the Guild.
     * @return {@link List<>} the entry.
     */
    public Mono<List<String>> getAllTwitchNames(long guildId) {
        return getAllTwitchWebhooks(guildId).map(x -> x.stream().map(WebhookTwitch::getName).toList());
    }

    /**
     * Get every Twitch-Notifier that has been set up for the given Guild.
     *
     * @param guildId the ID of the Guild.
     * @return {@link List<>} the entry.
     */
    public Mono<List<WebhookTwitch>> getAllTwitchWebhooks(long guildId) {
        return getEntityList(new WebhookTwitch(), "FROM WebhookTwitch WHERE guildAndId.guildId=:gid", Map.of("gid", guildId));
    }

    /**
     * Set the TwitchNotify in our Database.
     *
     * @param guildId    the ID of the Guild.
     * @param channelId  the ID of the Channel.
     * @param webhookId  the ID of the Webhook.
     * @param authToken  the Auth-token to verify the access.
     * @param twitchName the Username of the Twitch User.
     */
    public void addTwitchWebhook(long guildId, long channelId, long webhookId, String authToken, String twitchName) {
        addTwitchWebhook(guildId, channelId, webhookId, authToken, twitchName, null);
    }

    /**
     * Set the TwitchNotify in our Database.
     *
     * @param guildId        the ID of the Guild.
     * @param channelId      the ID of the Channel.
     * @param webhookId      the ID of the Webhook.
     * @param authToken      the Auth-token to verify the access.
     * @param twitchName     the Username of the Twitch User.
     * @param messageContent custom message content.
     */
    public void addTwitchWebhook(long guildId, long channelId, long webhookId, String authToken, String twitchName, String messageContent) {
        if (messageContent == null)
            messageContent = "%name% is now Live on Twitch! Come and join the stream <%url%>!";

        String finalMessageContent = messageContent;
        getTwitchWebhook(guildId, twitchName).subscribe(webhookTwitch -> {
            if (webhookTwitch == null) {
                webhookTwitch = new WebhookTwitch();
                webhookTwitch.setGuildId(guildId);
            }

            webhookTwitch.setChannelId(channelId);
            webhookTwitch.setWebhookId(webhookId);
            webhookTwitch.setToken(authToken);
            webhookTwitch.setName(twitchName);
            webhookTwitch.setMessage(finalMessageContent);

            // Add a new entry into the Database.
            updateEntity(webhookTwitch).block();
        });
    }

    /**
     * Remove a Twitch Notifier entry from our Database.
     *
     * @param guildId    the ID of the Guild.
     * @param twitchName the Name of the Twitch User.
     */
    public void removeTwitchWebhook(long guildId, String twitchName) {
        getTwitchWebhook(guildId, twitchName).subscribe(webhook -> {
            // Check if there is a Webhook set.
            if (webhook != null) {
                // Delete the entry.
                deleteEntityInternally(webhook);
            }
        });
    }

    /**
     * Check if the Twitch Webhook has been set in our Database for this Server.
     *
     * @param guildId the ID of the Guild.
     * @return {@link Boolean} if true, it has been set | if false, it hasn't been set.
     */
    public Mono<Boolean> isTwitchSetup(long guildId) {
        return getEntity(new WebhookTwitch(), "FROM WebhookTwitch WHERE guildAndId.guildId=?", Map.of("gid", guildId)).map(Objects::nonNull);
    }

    /**
     * Check if the Twitch Webhook has been set for the given User in our Database for this Server.
     *
     * @param guildId    the ID of the Guild.
     * @param twitchName the Username of the Twitch User.
     * @return {@link Boolean} if true, it has been set | if false, it hasn't been set.
     */
    public Mono<Boolean> isTwitchSetup(long guildId, String twitchName) {
        return getEntity(new WebhookTwitch(), "FROM WebhookTwitch WHERE guildAndId.guildId=:gid AND name=:name", Map.of("gid", guildId, "name", twitchName)).map(Objects::nonNull);
    }

    //endregion

    //region Instagram Notifier

    /**
     * Get the InstagramNotify data.
     *
     * @param guildId the ID of the Guild.
     * @param name    the Name of the Instagram User.
     * @return {@link WebhookInstagram} with all the needed data.
     */
    public Mono<WebhookInstagram> getInstagramWebhook(long guildId, String name) {
        return getEntity(new WebhookInstagram(), "FROM WebhookInstagram WHERE guildAndId.guildId=:gid AND name=:name", Map.of("gid", guildId, "name", name));
    }

    /**
     * Get the InstagramNotify data.
     *
     * @param name the Name of the Instagram User.
     * @return {@link List<WebhookInstagram>} with all the needed data.
     */
    public Mono<List<WebhookInstagram>> getInstagramWebhookByName(String name) {
        return getEntityList(new WebhookInstagram(), "FROM WebhookInstagram WHERE name=:name", Map.of("name", name));
    }

    /**
     * Get the all Instagram-Notifier.
     *
     * @return {@link List<>} the entry.
     */
    public Mono<List<String>> getAllInstagramUsers() {
        return getEntityList(new WebhookInstagram(), "FROM WebhookInstagram", null).map(x -> x.stream().map(WebhookInstagram::getName).toList());
    }

    /**
     * Get every Instagram-Notifier that has been set up for the given Guild.
     *
     * @param guildId the ID of the Guild.
     * @return {@link List<>} the entry.
     */
    public Mono<List<String>> getAllInstagramUsers(long guildId) {
        return getAllInstagramWebhooks(guildId).map(x -> x.stream().map(WebhookInstagram::getName).toList());
    }

    /**
     * Get every Instagram-Notifier that has been set up for the given Guild.
     *
     * @param guildId the ID of the Guild.
     * @return {@link List<>} the entry.
     */
    public Mono<List<WebhookInstagram>> getAllInstagramWebhooks(long guildId) {
        return getEntityList(new WebhookInstagram(), "FROM WebhookInstagram WHERE guildAndId.guildId=:gid", Map.of("gid", guildId));
    }

    /**
     * Set the InstagramNotify in our Database.
     *
     * @param guildId   the ID of the Guild.
     * @param channelId the ID of the Channel.
     * @param webhookId the ID of the Webhook.
     * @param authToken the Auth-token to verify the access.
     * @param name      the Name of the Instagram User.
     */
    public void addInstagramWebhook(long guildId, long channelId, long webhookId, String authToken, String name) {
        addInstagramWebhook(guildId, channelId, webhookId, authToken, name, null);
    }

    /**
     * Set the InstagramNotify in our Database.
     *
     * @param guildId        the ID of the Guild.
     * @param channelId      the ID of the Channel.
     * @param webhookId      the ID of the Webhook.
     * @param authToken      the Auth-token to verify the access.
     * @param name           the Name of the Instagram User.
     * @param messageContent custom message content.
     */
    public void addInstagramWebhook(long guildId, long channelId, long webhookId, String authToken, String name, String messageContent) {
        if (messageContent == null)
            messageContent = "%name% just posted something on their Instagram!";

        // Check if there is already a Webhook set.
        String finalMessageContent = messageContent;
        getInstagramWebhook(guildId, name).subscribe(webhook -> {
            if (webhook == null) {
                webhook = new WebhookInstagram();
                webhook.setGuildId(guildId);
            }

            webhook.setChannelId(channelId);
            webhook.setWebhookId(webhookId);
            webhook.setToken(authToken);
            webhook.setName(name);
            webhook.setMessage(finalMessageContent);

            // Add a new entry into the Database.
            updateEntity(webhook).block();
        });
    }


    /**
     * Remove an Instagram Notifier entry from our Database.
     *
     * @param guildId the ID of the Guild.
     * @param name    the Name of the Instagram User.
     */
    public void removeInstagramWebhook(long guildId, String name) {

        getInstagramWebhook(guildId, name).subscribe(webhook -> {
            // Check if there is a Webhook set.
            if (webhook != null) {
                // Delete the entry.
                deleteEntityInternally(webhook);
            }
        });
    }

    /**
     * Check if the Instagram Webhook has been set in our Database for this Server.
     *
     * @param guildId the ID of the Guild.
     * @return {@link Boolean} if true, it has been set | if false, it hasn't been set.
     */
    public Mono<Boolean> isInstagramSetup(long guildId) {
        return getEntity(new WebhookInstagram(), "FROM WebhookInstagram WHERE guildAndId.guildId=:gid", Map.of("gid", guildId)).map(Objects::nonNull);
    }

    /**
     * Check if the Instagram Webhook has been set for the given User in our Database for this Server.
     *
     * @param guildId the ID of the Guild.
     * @param name    the Name of the Instagram User.
     * @return {@link Boolean} if true, it has been set | if false, it hasn't been set.
     */
    public Mono<Boolean> isInstagramSetup(long guildId, String name) {
        return getEntity(new WebhookInstagram(), "FROM WebhookInstagram WHERE guildAndId.guildId=:gid AND name=:name", Map.of("gid", guildId, "name", name)).map(Objects::nonNull);
    }

    //endregion

    //region Reddit Notifier

    /**
     * Get the RedditNotify data.
     *
     * @param guildId   the ID of the Guild.
     * @param subreddit the Name of the Subreddit.
     * @return {@link WebhookReddit} with all the needed data.
     */
    public Mono<WebhookReddit> getRedditWebhook(long guildId, String subreddit) {
        return getEntity(new WebhookReddit(), "FROM WebhookReddit WHERE guildAndId.guildId=:gid AND subreddit=:name", Map.of("gid", guildId, "name", subreddit));
    }

    /**
     * Get the RedditNotify data.
     *
     * @param subreddit the Name of the Subreddit.
     * @return {@link List<WebhookReddit>} with all the needed data.
     */
    public Mono<List<WebhookReddit>> getRedditWebhookBySub(String subreddit) {
        return getEntityList(new WebhookReddit(), "FROM WebhookReddit WHERE subreddit=:name", Map.of("name", subreddit));
    }

    /**
     * Get the all Reddit-Notifier.
     *
     * @return {@link List<>} the entry.
     */
    public Mono<List<String>> getAllSubreddits() {
        return getEntityList(new WebhookReddit(), "FROM WebhookReddit", null).map(x -> x.stream().map(WebhookReddit::getSubreddit).toList());
    }

    /**
     * Get every Reddit-Notifier that has been set up for the given Guild.
     *
     * @param guildId the ID of the Guild.
     * @return {@link List<>} the entry.
     */
    public Mono<List<String>> getAllSubreddits(long guildId) {
        return getAllRedditWebhooks(guildId).map(x -> x.stream().map(WebhookReddit::getSubreddit).toList());
    }

    /**
     * Get every Reddit-Notifier that has been set up for the given Guild.
     *
     * @param guildId the ID of the Guild.
     * @return {@link List<>} the entry.
     */
    public Mono<List<WebhookReddit>> getAllRedditWebhooks(long guildId) {
        return getEntityList(new WebhookReddit(), "FROM WebhookReddit WHERE guildAndId.guildId=:gid", Map.of("gid", guildId));
    }

    /**
     * Set the RedditNotify in our Database.
     *
     * @param guildId   the ID of the Guild.
     * @param channelId the ID of the Channel.
     * @param webhookId the ID of the Webhook.
     * @param authToken the Auth-token to verify the access.
     * @param subreddit the Name of the Subreddit.
     */
    public void addRedditWebhook(long guildId, long channelId, long webhookId, String authToken, String subreddit) {
        addRedditWebhook(guildId, channelId, webhookId, authToken, subreddit, null);
    }

    /**
     * Set the RedditNotify in our Database.
     *
     * @param guildId        the ID of the Guild.
     * @param channelId      the ID of the Channel.
     * @param webhookId      the ID of the Webhook.
     * @param authToken      the Auth-token to verify the access.
     * @param subreddit      the Name of the Subreddit.
     * @param messageContent custom message content.
     */
    public void addRedditWebhook(long guildId, long channelId, long webhookId, String authToken, String subreddit, String messageContent) {
        if (messageContent == null)
            messageContent = "%name% got a new post check it out <%url%>!";

        // Check if there is already a Webhook set.
        String finalMessageContent = messageContent;
        getRedditWebhook(guildId, subreddit).subscribe(webhook -> {
            if (webhook == null) {
                webhook = new WebhookReddit();
                webhook.setGuildId(guildId);
            }

            webhook.setChannelId(channelId);
            webhook.setWebhookId(webhookId);
            webhook.setToken(authToken);
            webhook.setSubreddit(subreddit);
            webhook.setMessage(finalMessageContent);

            // Add a new entry into the Database.
            updateEntity(webhook).block();
        });
    }

    /**
     * Remove a Reddit Notifier entry from our Database.
     *
     * @param guildId   the ID of the Guild.
     * @param subreddit the Name of the Subreddit.
     */
    public void removeRedditWebhook(long guildId, String subreddit) {
        getRedditWebhook(guildId, subreddit).subscribe(webhook -> {
            // Check if there is a Webhook set.
            if (webhook != null) {
                // Delete the entry.
                deleteEntityInternally(webhook);
            }
        });
    }

    /**
     * Check if the Reddit Webhook has been set in our Database for this Server.
     *
     * @param guildId the ID of the Guild.
     * @return {@link Boolean} if true, it has been set | if false, it hasn't been set.
     */
    public Mono<Boolean> isRedditSetup(long guildId) {
        return getEntity(new WebhookReddit(), "FROM WebhookReddit WHERE guildAndId.guildId=:gid", Map.of("gid", guildId)).map(Objects::nonNull);
    }

    /**
     * Check if the Reddit Webhook has been set for the given User in our Database for this Server.
     *
     * @param guildId   the ID of the Guild.
     * @param subreddit the Name of the Subreddit.
     * @return {@link Boolean} if true, it has been set | if false, it hasn't been set.
     */
    public Mono<Boolean> isRedditSetup(long guildId, String subreddit) {
        return getEntity(new WebhookReddit(), "FROM WebhookReddit WHERE guildAndId.guildId=:gid AND subreddit=:name", Map.of("gid", guildId, "name", subreddit)).map(Objects::nonNull);
    }

    //endregion

    //region YouTube Notifier

    /**
     * Get the YouTubeNotify data.
     *
     * @param guildId        the ID of the Guild.
     * @param youtubeChannel the Username of the YouTube channel.
     * @return {@link WebhookYouTube} with all the needed data.
     */
    public Mono<WebhookYouTube> getYouTubeWebhook(long guildId, String youtubeChannel) {
        return getEntity(new WebhookYouTube(), "FROM WebhookYouTube WHERE guildAndId.guildId=:gid AND name=:name", Map.of("gid", guildId, "name", youtubeChannel));
    }

    /**
     * Get the YouTubeNotify data.
     *
     * @param youtubeChannel the Username of the YouTube channel.
     * @return {@link List<WebhookYouTube>} with all the needed data.
     */
    public Mono<List<WebhookYouTube>> getYouTubeWebhooksByName(String youtubeChannel) {
        return getEntityList(new WebhookYouTube(), "FROM WebhookYouTube WHERE name=:name", Map.of("name", youtubeChannel));
    }

    /**
     * Get the all YouTube-Notifier.
     *
     * @return {@link List<>} the entry.
     */
    public Mono<List<String>> getAllYouTubeChannels() {
        return getEntityList(new WebhookYouTube(), "FROM WebhookYouTube", null).map(webhooks -> webhooks.stream().map(WebhookYouTube::getName).toList());
    }

    /**
     * Get every YouTube-Notifier that has been set up for the given Guild.
     *
     * @param guildId the ID of the Guild.
     * @return {@link List<>} the entry.
     */
    public Mono<List<String>> getAllYouTubeChannels(long guildId) {
        return getAllYouTubeWebhooks(guildId).map(webhooks -> webhooks.stream().map(WebhookYouTube::getName).toList());
    }

    /**
     * Get every YouTube-Notifier that has been set up for the given Guild.
     *
     * @param guildId the ID of the Guild.
     * @return {@link List<>} the entry.
     */
    public Mono<List<WebhookYouTube>> getAllYouTubeWebhooks(long guildId) {
        return getEntityList(new WebhookYouTube(), "FROM WebhookYouTube WHERE guildAndId.guildId=:gid", Map.of("gid", guildId));
    }

    /**
     * Set the YouTubeNotify in our Database.
     *
     * @param guildId        the ID of the Guild.
     * @param channelId      the ID of the Channel.
     * @param webhookId      the ID of the Webhook.
     * @param authToken      the Auth-token to verify the access.
     * @param youtubeChannel the Username of the YouTube channel.
     */
    public void addYouTubeWebhook(long guildId, long channelId, long webhookId, String authToken, String youtubeChannel) {
        addYouTubeWebhook(guildId, channelId, webhookId, authToken, youtubeChannel, null);
    }

    /**
     * Set the YouTubeNotify in our Database.
     *
     * @param guildId        the ID of the Guild.
     * @param channelId      the ID of the Channel.
     * @param webhookId      the ID of the Webhook.
     * @param authToken      the Auth-token to verify the access.
     * @param youtubeChannel the Username of the YouTube channel.
     * @param messageContent custom message content.
     */
    public void addYouTubeWebhook(long guildId, long channelId, long webhookId, String authToken, String youtubeChannel, String messageContent) {
        if (messageContent == null)
            messageContent = "%name% just uploaded a new Video! Check it out <%url%>!";

        // Check if there is already a Webhook set.
        String finalMessageContent = messageContent;
        getYouTubeWebhook(guildId, youtubeChannel).subscribe(webhook -> {
            if (webhook == null) {
                webhook = new WebhookYouTube();
                webhook.setGuildId(guildId);
            }

            webhook.setChannelId(channelId);
            webhook.setWebhookId(webhookId);
            webhook.setToken(authToken);
            webhook.setName(youtubeChannel);
            webhook.setMessage(finalMessageContent);

            // Add a new entry into the Database.
            updateEntity(webhook).block();
        });
    }

    /**
     * Remove a YouTube Notifier entry from our Database.
     *
     * @param guildId        the ID of the Guild.
     * @param youtubeChannel the Name of the YouTube channel.
     */
    public void removeYouTubeWebhook(long guildId, String youtubeChannel) {
        getYouTubeWebhook(guildId, youtubeChannel).subscribe(webhook -> {
            if (webhook != null) {
                deleteEntityInternally(webhook);
            }
        });
    }

    /**
     * Check if the YouTube Webhook has been set in our Database for this Server.
     *
     * @param guildId the ID of the Guild.
     * @return {@link Boolean} if true, it has been set | if false, it hasn't been set.
     */
    public Mono<Boolean> isYouTubeSetup(long guildId) {
        return getEntity(new WebhookYouTube(), "FROM WebhookYouTube WHERE guildAndId.guildId=:gid", Map.of("gid", guildId)).map(Objects::nonNull);
    }

    /**
     * Check if the YouTube Webhook has been set for the given User in our Database for this Server.
     *
     * @param guildId        the ID of the Guild.
     * @param youtubeChannel the Username of the YouTube channel.
     * @return {@link Boolean} if true, it has been set | if false, it hasn't been set.
     */
    public Mono<Boolean> isYouTubeSetup(long guildId, String youtubeChannel) {
        return getEntity(new WebhookYouTube(), "FROM WebhookYouTube WHERE guildAndId.guildId=:gid AND name=:name", Map.of("gid", guildId, "name", youtubeChannel)).map(Objects::nonNull);
    }

    //endregion

    //region Twitter Notifier

    /**
     * Get the Twitter-Notify data.
     *
     * @param guildId     the ID of the Guild.
     * @param twitterName the Username of the Twitter User.
     * @return {@link WebhookTwitter} with all the needed data.
     */
    public Mono<WebhookTwitter> getTwitterWebhook(long guildId, String twitterName) {
        return getEntity(new WebhookTwitter(), "FROM WebhookTwitter WHERE guildAndId.guildId=:gid AND name=:name", Map.of("gid", guildId, "name", twitterName));
    }

    /**
     * Get the TwitterNotify data.
     *
     * @param twitterName the Username of the Twitter User.
     * @return {@link List<WebhookTwitter>} with all the needed data.
     */
    public Mono<List<WebhookTwitter>> getTwitterWebhooksByName(String twitterName) {
        return getEntityList(new WebhookTwitter(), "FROM WebhookTwitter WHERE name=:name", Map.of("name", twitterName));
    }

    /**
     * Get the all Twitter-Notifier.
     *
     * @return {@link List<>} the entry.
     */
    public Mono<List<String>> getAllTwitterNames() {
        return getEntityList(new WebhookTwitter(), "FROM WebhookTwitter", null).map(x -> x.stream().map(WebhookTwitter::getName).toList());
    }

    /**
     * Get every Twitter-Notifier that has been set up for the given Guild.
     *
     * @param guildId the ID of the Guild.
     * @return {@link List<>} the entry.
     */
    public Mono<List<String>> getAllTwitterNames(long guildId) {
        return getAllTwitterWebhooks(guildId).map(x -> x.stream().map(WebhookTwitter::getName).toList());
    }

    /**
     * Get every Twitter-Notifier that has been set up for the given Guild.
     *
     * @param guildId the ID of the Guild.
     * @return {@link List<>} the entry.
     */
    public Mono<List<WebhookTwitter>> getAllTwitterWebhooks(long guildId) {
        return getEntityList(new WebhookTwitter(), "FROM WebhookTwitter WHERE guildAndId.guildId=:gid", Map.of("gid", guildId));
    }

    /**
     * Set the TwitterNotify in our Database.
     *
     * @param guildId     the ID of the Guild.
     * @param channelId   the ID of the Channel.
     * @param webhookId   the ID of the Webhook.
     * @param authToken   the Auth-token to verify the access.
     * @param twitterName the Username of the Twitter User.
     */
    public void addTwitterWebhook(long guildId, long channelId, long webhookId, String authToken, String twitterName) {
        addTwitterWebhook(guildId, channelId, webhookId, authToken, twitterName, null);
    }

    /**
     * Set the TwitterNotify in our Database.
     *
     * @param guildId        the ID of the Guild.
     * @param channelId      the ID of the Channel.
     * @param webhookId      the ID of the Webhook.
     * @param authToken      the Auth-token to verify the access.
     * @param twitterName    the Username of the Twitter User.
     * @param messageContent custom message content.
     */
    public void addTwitterWebhook(long guildId, long channelId, long webhookId, String authToken, String twitterName, String messageContent) {
        if (messageContent == null)
            messageContent = "%name% tweeted something! Check it out <%url%>!";

        // Check if there is already a Webhook set.
        String finalMessageContent = messageContent;
        getTwitterWebhook(guildId, twitterName).subscribe(webhook -> {
            if (webhook == null) {
                webhook = new WebhookTwitter();
                webhook.setGuildId(guildId);
            }

            webhook.setChannelId(channelId);
            webhook.setWebhookId(webhookId);
            webhook.setToken(authToken);
            webhook.setName(twitterName);
            webhook.setMessage(finalMessageContent);

            // Add a new entry into the Database.
            updateEntity(webhook).block();
        });
    }

    /**
     * Remove a Twitter Notifier entry from our Database.
     *
     * @param guildId     the ID of the Guild.
     * @param twitterName the Name of the Twitter User.
     */
    public void removeTwitterWebhook(long guildId, String twitterName) {
        getTwitterWebhook(guildId, twitterName).subscribe(webhook -> {
            if (webhook != null) {
                deleteEntityInternally(webhook);
            }
        });
    }

    /**
     * Check if the Twitter Webhook has been set in our Database for this Server.
     *
     * @param guildId the ID of the Guild.
     * @return {@link Boolean} if true, it has been set | if false, it hasn't been set.
     */
    public Mono<Boolean> isTwitterSetup(long guildId) {
        return getEntity(new WebhookTwitter(), "FROM WebhookTwitter WHERE guildAndId.guildId=:gid", Map.of("gid", guildId)).map(Objects::nonNull);
    }

    /**
     * Check if the Twitter Webhook has been set for the given User in our Database for this Server.
     *
     * @param guildId     the ID of the Guild.
     * @param twitterName the Username of the Twitter User.
     * @return {@link Boolean} if true, it has been set | if false, it hasn't been set.
     */
    public Mono<Boolean> isTwitterSetup(long guildId, String twitterName) {
        return getEntity(new WebhookTwitter(), "FROM WebhookTwitter WHERE guildAndId.guildId=:gid AND name=:name", Map.of("gid", guildId, "name", twitterName)).map(Objects::nonNull);
    }

    //endregion

    //region TikTok Notifier

    /**
     * Get the TikTok-Notify data.
     *
     * @param guildId  the ID of the Guild.
     * @param tiktokId the ID of the TikTok User.
     * @return {@link WebhookTikTok} with all the needed data.
     */
    public Mono<WebhookTikTok> getTikTokWebhook(long guildId, String tiktokId) {
        return getEntity(new WebhookTikTok(), "FROM WebhookTikTok WHERE guildAndId.guildId=:gid AND name=:name", Map.of("gid", guildId, "name", tiktokId));
    }

    /**
     * Get the TikTokNotify data.
     *
     * @param tiktokId the ID of the TikTok User.
     * @return {@link List<WebhookTikTok>} with all the needed data.
     */
    public Mono<List<WebhookTikTok>> getTikTokWebhooksByName(String tiktokId) {
        return getEntityList(new WebhookTikTok(), "FROM WebhookTikTok WHERE name=:name", Map.of("name", tiktokId));
    }

    /**
     * Get the all TikTok-Notifier.
     *
     * @return {@link List<>} the entry.
     */
    public Mono<List<String>> getAllTikTokNames() {
        return getEntityList(new WebhookTikTok(), "FROM WebhookTikTok", null).map(x -> x.stream().map(WebhookTikTok::getName).toList());
    }

    /**
     * Get every TikTok-Notifier that has been set up for the given Guild.
     *
     * @param guildId the ID of the Guild.
     * @return {@link List<>} the entry.
     */
    public Mono<List<String>> getAllTikTokNames(long guildId) {
        return getAllTikTokWebhooks(guildId).map(x -> x.stream().map(WebhookTikTok::getName).toList());
    }

    /**
     * Get every TikTok-Notifier that has been set up for the given Guild.
     *
     * @param guildId the ID of the Guild.
     * @return {@link List<>} the entry.
     */
    public Mono<List<WebhookTikTok>> getAllTikTokWebhooks(long guildId) {
        return getEntityList(new WebhookTikTok(), "FROM WebhookTikTok WHERE guildAndId.guildId=:gid", Map.of("gid", guildId));
    }

    /**
     * Set the TikTokNotify in our Database.
     *
     * @param guildId   the ID of the Guild.
     * @param channelId the ID of the Channel.
     * @param webhookId the ID of the Webhook.
     * @param authToken the Auth-token to verify the access.
     * @param tiktokId  the ID of the TikTok User.
     */
    public void addTikTokWebhook(long guildId, long channelId, long webhookId, String authToken, String tiktokId) {
        addTikTokWebhook(guildId, channelId, webhookId, authToken, tiktokId, null);
    }

    /**
     * Set the TikTokNotify in our Database.
     *
     * @param guildId        the ID of the Guild.
     * @param channelId      the ID of the Channel.
     * @param webhookId      the ID of the Webhook.
     * @param authToken      the Auth-token to verify the access.
     * @param tiktokId       the ID of the TikTok User.
     * @param messageContent custom message content.
     */
    public void addTikTokWebhook(long guildId, long channelId, long webhookId, String authToken, String tiktokId, String messageContent) {
        if (messageContent == null)
            messageContent = "%name% just posted something new on TikTok!";

        // Check if there is already a Webhook set.
        String finalMessageContent = messageContent;
        getTikTokWebhook(guildId, tiktokId).subscribe(webhook -> {
            if (webhook == null) {
                webhook = new WebhookTikTok();
                webhook.setGuildId(guildId);
            }

            webhook.setChannelId(channelId);
            webhook.setWebhookId(webhookId);
            webhook.setToken(authToken);
            webhook.setName(tiktokId);
            webhook.setMessage(finalMessageContent);

            // Add a new entry into the Database.
            updateEntity(webhook).block();
        });
    }

    /**
     * Remove a TikTok Notifier entry from our Database.
     *
     * @param guildId  the ID of the Guild.
     * @param tiktokId the ID of the TikTok User.
     */
    public void removeTikTokWebhook(long guildId, String tiktokId) {
        getTikTokWebhook(guildId, tiktokId).subscribe(webhook -> {
            // Check if there is a Webhook set.
            if (webhook != null) {
                // Delete the entry.
                deleteEntityInternally(webhook);
            }
        });
    }

    /**
     * Check if the TikTok Webhook has been set in our Database for this Server.
     *
     * @param guildId the ID of the Guild.
     * @return {@link Boolean} if true, it has been set | if false, it hasn't been set.
     */
    public Mono<Boolean> isTikTokSetup(long guildId) {
        return getEntity(new WebhookTikTok(), "FROM WebhookTikTok WHERE guildAndId.guildId=:gid", Map.of("gid", guildId)).map(Objects::nonNull);
    }

    /**
     * Check if the TikTok Webhook has been set for the given User in our Database for this Server.
     *
     * @param guildId  the ID of the Guild.
     * @param tiktokId the ID of the TikTok User.
     * @return {@link Boolean} if true, it has been set | if false, it hasn't been set.
     */
    public Mono<Boolean> isTikTokSetup(long guildId, String tiktokId) {
        return getEntity(new WebhookTikTok(), "FROM WebhookTikTok WHERE guildAndId.guildId=:gid AND name=:name", Map.of("gid", guildId, "name", tiktokId)).map(Objects::nonNull);
    }

    //endregion

    //region RSS Notifier

    /**
     * Get the RSS-Feed data.
     *
     * @param guildId the ID of the Guild.
     * @param url     the Url of the RSS-Feed.
     * @return {@link RSSFeed} with all the needed data.
     */
    public Mono<RSSFeed> getRSSWebhook(long guildId, String url) {
        return getEntity(new RSSFeed(), "FROM RSSFeed WHERE guildAndId.guildId=:gid AND url=:url", Map.of("gid", guildId, "url", url));
    }

    /**
     * Get the RSS-Feed data.
     *
     * @param url the Url of the RSS-Feed.
     * @return {@link List<RSSFeed>} with all the needed data.
     */
    public Mono<List<RSSFeed>> getRSSWebhooksByUrl(String url) {
        return getEntityList(new RSSFeed(), "FROM RSSFeed WHERE url=:url", Map.of("url", url));
    }

    /**
     * Get the all RSS-Feeds.
     *
     * @return {@link List<>} the entry.
     */
    public Mono<List<String>> getAllRSSUrls() {
        return getEntityList(new RSSFeed(), "FROM RSSFeed", null).map(x -> x.stream().map(RSSFeed::getUrl).toList());
    }

    /**
     * Get every RSS-Feed that has been set up for the given Guild.
     *
     * @param guildId the ID of the Guild.
     * @return {@link List<>} the entry.
     */
    public Mono<List<String>> getAllRSSUrls(long guildId) {
        return getAllRSSWebhooks(guildId).map(x -> x.stream().map(RSSFeed::getUrl).toList());
    }

    /**
     * Get every RSS-Feed that has been set up for the given Guild.
     *
     * @param guildId the ID of the Guild.
     * @return {@link List<>} the entry.
     */
    public Mono<List<RSSFeed>> getAllRSSWebhooks(long guildId) {
        return getEntityList(new RSSFeed(), "FROM RSSFeed WHERE guildAndId.guildId=:gid", Map.of("gid", guildId));
    }

    /**
     * Set the RSS-Feed in our Database.
     *
     * @param guildId   the ID of the Guild.
     * @param channelId the ID of the Channel.
     * @param webhookId the ID of the Webhook.
     * @param authToken the Auth-token to verify the access.
     * @param url       the Url of the RSS-Feed.
     */
    public void addRSSWebhook(long guildId, long channelId, long webhookId, String authToken, String url) {
        getRSSWebhook(guildId, url).subscribe(webhook -> {
            // Check if there is already a Webhook set.

            if (webhook == null) {
                webhook = new RSSFeed();
                webhook.setGuildId(guildId);
            }

            webhook.setChannelId(channelId);
            webhook.setWebhookId(webhookId);
            webhook.setToken(authToken);
            webhook.setUrl(url);

            // Add a new entry into the Database.
            updateEntity(webhook).block();
        });
    }

    /**
     * Remove an RSS-Feed entry from our Database.
     *
     * @param guildId the ID of the Guild.
     * @param url     the Url of the RSS-Feed.
     */
    public void removeRSSWebhook(long guildId, String url) {
        getRSSWebhook(guildId, url).subscribe(x -> {
            // Check if there is a Webhook set.
            if (x != null) {
                // Delete the entry.
                deleteEntityInternally(x);
            }
        });
    }

    /**
     * Check if the RSS-Feed has been set in our Database for this Server.
     *
     * @param guildId the ID of the Guild.
     * @return {@link Boolean} if true, it has been set | if false, it hasn't been set.
     */
    public Mono<Boolean> isRSSSetup(long guildId) {
        return getEntity(new RSSFeed(), "FROM RSSFeed WHERE guildAndId.guildId=:gid", Map.of("gid", guildId)).map(Objects::nonNull);
    }

    /**
     * Check if the RSS-Feed has been set for the given Url in our Database for this Server.
     *
     * @param guildId the ID of the Guild.
     * @param url     the Url of the RSS-Feed.
     * @return {@link Boolean} if true, it has been set | if false, it hasn't been set.
     */
    public Mono<Boolean> isRSSSetup(long guildId, String url) {
        return getEntity(new RSSFeed(), "FROM RSSFeed WHERE guildAndId.guildId=:gid AND url=:url", Map.of("gid", guildId, "url", url)).map(Objects::nonNull);
    }

    //endregion

    //endregion

    //region Roles

    //region AutoRoles

    /**
     * Get the all AutoRoles saved in our Database from the given Guild.
     *
     * @param guildId the ID of the Guild.
     * @return {@link List<AutoRole>} as List with all Roles.
     */
    public Mono<List<AutoRole>> getAutoRoles(long guildId) {
        return getEntityList(new AutoRole(), "FROM AutoRole WHERE guildRoleId.guildId=:gid", Map.of("gid", guildId));
    }

    /**
     * Add an AutoRole in our Database.
     *
     * @param guildId the ID of the Guild.
     * @param roleId  the ID of the Role.
     */
    public void addAutoRole(long guildId, long roleId) {
        isAutoRoleSetup(guildId, roleId).subscribe(x -> {
            if (x) return;

            // Add a new entry into the Database.
            updateEntity(new AutoRole(guildId, roleId)).block();
        });
    }

    /**
     * Remove a AutoRole from our Database.
     *
     * @param guildId the ID of the Guild.
     * @param roleId  the ID of the Role.
     */
    public void removeAutoRole(long guildId, long roleId) {
        getEntity(new AutoRole(), "FROM AutoRole WHERE guildRoleId.guildId=:gid AND guildRoleId.roleId=:rid ", Map.of("gid", guildId, "rid", roleId)).subscribe(autoRole -> {
            // Check if there is a role in the database.
            if (autoRole == null) return;

            // Delete the entry.
            deleteEntityInternally(autoRole);
        });
    }

    /**
     * Check if an AutoRole has been set in our Database for this Server.
     *
     * @param guildId the ID of the Guild.
     * @return {@link Boolean} as result if true, there is a role in our Database | if false, we couldn't find anything.
     */
    public Mono<Boolean> isAutoRoleSetup(long guildId) {
        return getEntity(new AutoRole(), "FROM AutoRole WHERE guildRoleId.guildId=:gid ", Map.of("gid", guildId)).map(Objects::nonNull);
    }

    /**
     * Check if an AutoRole has been set in our Database for this Server.
     *
     * @param guildId the ID of the Guild.
     * @param roleId  the ID of the Role.
     * @return {@link Boolean} as result if true, there is a role in our Database | if false, we couldn't find anything.
     */
    public Mono<Boolean> isAutoRoleSetup(long guildId, long roleId) {
        return getEntity(new AutoRole(), "FROM AutoRole WHERE guildRoleId.guildId=:gid AND guildRoleId.roleId=:rid ",
                Map.of("gid", guildId, "rid", roleId)).map(Objects::nonNull);
    }

    //endregion

    //region Level Rewards

    //region Chat Rewards

    /**
     * Get the all Chat Rewards saved in our Database from the given Guild.
     *
     * @param guildId the ID of the Guild.
     * @return {@link HashMap<>} as List with all Role IDs and the needed Level.
     */
    public Mono<Map<Long, Long>> getChatLevelRewards(long guildId) {
        return getEntityList(new VoiceAutoRole(), "FROM ChatAutoRole WHERE guildRoleId.guildId=:gid", Map.of("gid", guildId)).map(x -> {
            // Create a new HashMap to save the Role Ids and their needed level.
            Map<Long, Long> rewards = new HashMap<>();

            x.forEach(chatAutoRole -> rewards.put(chatAutoRole.getLevel(), chatAutoRole.getRoleId()));

            return rewards;
        });
    }

    /**
     * Add a Chat Level Reward Role in our Database.
     *
     * @param guildId the ID of the Guild.
     * @param roleId  the ID of the Role.
     * @param level   the Level required getting this Role.
     */
    public void addChatLevelReward(long guildId, long roleId, long level) {
        isChatLevelRewardSetup(guildId, roleId, level).subscribe(x -> {
            if (x) return;

            // Add a new entry into the Database.
            updateEntity(new ChatAutoRole(guildId, roleId, level)).block();
        });
    }

    /**
     * Remove a Chat Level Reward Role from our Database.
     *
     * @param guildId the ID of the Guild.
     * @param level   the Level required getting this Role.
     */
    public void removeChatLevelReward(long guildId, long level) {
        getEntity(new ChatAutoRole(), "FROM ChatAutoRole WHERE guildRoleId.guildId=:gid AND level=:lvl",
                Map.of("gid", guildId, "lvl", level)).subscribe(chatAutoRole -> {
            // Check if there is a role in the database.
            if (chatAutoRole == null) return;
            deleteEntityInternally(chatAutoRole);
        });
    }

    /**
     * Remove a Chat Level Reward Role from our Database.
     *
     * @param guildId the ID of the Guild.
     * @param roleId  the ID of the Role.
     * @param level   the Level required getting this Role.
     */
    public void removeChatLevelReward(long guildId, long roleId, long level) {
        getEntity(new ChatAutoRole(), "FROM ChatAutoRole WHERE guildRoleId.guildId=:gid AND guildRoleId.roleId=:rid AND level=:lvl",
                Map.of("gid", guildId, "rid", roleId, "lvl", level)).subscribe(chatAutoRole -> {
            // Check if there is a role in the database.
            if (chatAutoRole == null) return;

            deleteEntityInternally(chatAutoRole);
        });
    }

    /**
     * Check if a Chat Level Reward has been set in our Database for this Server.
     *
     * @param guildId the ID of the Guild.
     * @return {@link Boolean} as result if true, there is a role in our Database | if false, we couldn't find anything.
     */
    public Mono<Boolean> isChatLevelRewardSetup(long guildId) {
        return getEntity(new ChatAutoRole(), "FROM ChatAutoRole WHERE guildRoleId.guildId=:gid", Map.of("gid", guildId)).map(Objects::nonNull);
    }

    /**
     * Check if a Chat Level Reward has been set in our Database for this Server.
     *
     * @param guildId the ID of the Guild.
     * @param roleId  the ID of the Role.
     * @return {@link Boolean} as result if true, there is a role in our Database | if false, we couldn't find anything.
     */
    public Mono<Boolean> isChatLevelRewardSetup(long guildId, long roleId) {
        return getEntity(new ChatAutoRole(), "FROM ChatAutoRole WHERE guildRoleId.guildId=:gid AND guildRoleId.roleId=:rid", Map.of("gid", guildId, "rid", roleId)).map(Objects::nonNull);
    }

    /**
     * Check if a Chat Level Reward has been set in our Database for this Server.
     *
     * @param guildId the ID of the Guild.
     * @param roleId  the ID of the Role.
     * @param level   the Level needed to get the Role.
     * @return {@link Boolean} as result if true, there is a role in our Database | if false, we couldn't find anything.
     */
    public Mono<Boolean> isChatLevelRewardSetup(long guildId, long roleId, long level) {
        return getEntity(new ChatAutoRole(), "FROM ChatAutoRole WHERE guildRoleId.guildId=:gid AND guildRoleId.roleId=:rid AND level=:lvl", Map.of("gid", guildId, "rid", roleId, "lvl", level)).map(Objects::nonNull);
    }

    //endregion

    //region Voice Rewards

    /**
     * Get the all Voice Rewards saved in our Database from the given Guild.
     *
     * @param guildId the ID of the Guild.
     * @return {@link Map<>} as List with all Role IDs and the needed Level.
     */
    public Mono<Map<Long, Long>> getVoiceLevelRewards(long guildId) {
        return getEntityList(new VoiceAutoRole(), "FROM VoiceAutoRole WHERE guildRoleId.guildId=:gid", Map.of("gid", guildId)).map(x -> {
            // Create a new HashMap to save the Role Ids and their needed level.
            Map<Long, Long> rewards = new HashMap<>();

            x.forEach(voiceAutoRole -> rewards.put(voiceAutoRole.getLevel(), voiceAutoRole.getRoleId()));

            return rewards;
        });
    }

    /**
     * Add a Voice Level Reward Role in our Database.
     *
     * @param guildId the ID of the Guild.
     * @param roleId  the ID of the Role.
     * @param level   the Level required getting this Role.
     */
    public void addVoiceLevelReward(long guildId, long roleId, long level) {
        isVoiceLevelRewardSetup(guildId, roleId, level).subscribe(x -> {
            if (x) return;

            // Add a new entry into the Database.
            updateEntity(new VoiceAutoRole(guildId, roleId, level)).block();
        });
    }

    /**
     * Remove a Voice Level Reward Role from our Database.
     *
     * @param guildId the ID of the Guild.
     * @param level   the Level required getting this Role.
     */
    public void removeVoiceLevelReward(long guildId, long level) {
        getEntity(new VoiceAutoRole(), "FROM VoiceAutoRole WHERE guildRoleId.guildId=:gid AND level=:lvl",
                Map.of("gid", guildId, "lvl", level)).subscribe(voiceAutoRole -> {
            if (voiceAutoRole == null) return;

            deleteEntityInternally(voiceAutoRole);
        });
    }

    /**
     * Remove a Voice Level Reward Role from our Database.
     *
     * @param guildId the ID of the Guild.
     * @param roleId  the ID of the Role.
     * @param level   the Level required getting this Role.
     */
    public void removeVoiceLevelReward(long guildId, long roleId, long level) {
        getEntity(new VoiceAutoRole(), "FROM VoiceAutoRole WHERE guildRoleId.guildId=:gid AND guildRoleId.roleId=:rid AND LVL=:lvl",
                Map.of("gid", guildId, "rid", roleId, "lvl", level)).subscribe(voiceAutoRole -> {
            if (voiceAutoRole == null) return;

            deleteEntityInternally(voiceAutoRole);
        });
    }

    /**
     * Check if a Voice Level Reward has been set in our Database for this Server.
     *
     * @param guildId the ID of the Guild.
     * @return {@link Boolean} as result if true, there is a role in our Database | if false, we couldn't find anything.
     */
    public Mono<Boolean> isVoiceLevelRewardSetup(long guildId) {
        return getEntity(new VoiceAutoRole(), "FROM VoiceAutoRole WHERE guildRoleId.guildId=:gid", Map.of("gid", guildId)).map(Objects::nonNull);
    }

    /**
     * Check if a Voice Level Reward has been set in our Database for this Server.
     *
     * @param guildId the ID of the Guild.
     * @param roleId  the ID of the Role.
     * @return {@link Boolean} as result if true, there is a role in our Database | if false, we couldn't find anything.
     */
    public Mono<Boolean> isVoiceLevelRewardSetup(long guildId, long roleId) {
        return getEntity(new VoiceAutoRole(), "FROM VoiceAutoRole WHERE guildRoleId.guildId=:gid AND guildRoleId.roleId=:rid", Map.of("gid", guildId, "rid", roleId)).map(Objects::nonNull);
    }

    /**
     * Check if a Voice Level Reward has been set in our Database for this Server.
     *
     * @param guildId the ID of the Guild.
     * @param roleId  the ID of the Role.
     * @param level   the Level needed to get the Role.
     * @return {@link Boolean} as result if true, there is a role in our Database | if false, we couldn't find anything.
     */
    public Mono<Boolean> isVoiceLevelRewardSetup(long guildId, long roleId, long level) {
        return getEntity(new VoiceAutoRole(), "FROM VoiceAutoRole WHERE guildRoleId.guildId=:gid AND guildRoleId.roleId=:rid AND level=:lvl", Map.of("gid", guildId, "rid", roleId, "lvl", level)).map(Objects::nonNull);
    }

    //endregion

    //endregion

    //endregion

    //region Invite

    /**
     * Get a List of every saved Invite from our Database.
     *
     * @param guildId the ID of the Guild.
     * @return {@link List<String>} as List with {@link Invite}.
     */
    public Mono<List<Invite>> getInvites(long guildId) {
        return getEntityList(new Invite(), "FROM Invite WHERE guildAndCode.guildId=:gid", Map.of("gid", guildId));
    }

    /**
     * Check if the given Invite Data is saved in our Database.
     *
     * @param guildId       the ID of the Guild.
     * @param inviteCreator the ID of the Invite Creator.
     * @param inviteCode    the Code of the Invite.
     * @return {@link Boolean} as Result if true, then it's saved in our Database | if false, we couldn't find anything.
     */
    public Mono<Boolean> existsInvite(long guildId, String inviteCreator, String inviteCode) {
        return getEntity(new Invite(), "FROM Invite WHERE guildAndCode.guildId=:gid AND userId=:uid AND guildAndCode.code=:code", Map.of("gid", guildId, "uid", inviteCreator, "code", inviteCode)).map(Objects::nonNull);
    }

    /**
     * Remove an entry from our Database.
     *
     * @param guildId    the ID of the Guild.
     * @param inviteCode the Code of the Invite.
     */
    public void removeInvite(long guildId, String inviteCode) {
        deleteEntityInternally(getInvite(guildId, inviteCode));
    }

    /**
     * Change the data of a saved Invite or create a new entry in our Database.
     *
     * @param guildId       the ID of the Guild.
     * @param inviteCreator the ID of the Invite Creator.
     * @param inviteCode    the Code of the Invite Code.
     * @param inviteUsage   the Usage count of the Invite.
     */
    public void setInvite(long guildId, long inviteCreator, String inviteCode, long inviteUsage) {
        updateEntity(new Invite(guildId, inviteCreator, inviteUsage, inviteCode)).subscribe();
    }

    /**
     * Get the Invite from our Database.
     *
     * @param guildId    the ID of the Guild.
     * @param inviteCode the Code of the Invite.
     * @return {@link Invite} as result if true, then it's saved in our Database | may be null.
     */
    public Mono<Invite> getInvite(long guildId, String inviteCode) {
        return getEntity(new Invite(), "FROM Invite WHERE guildAndCode.guildId=:gid AND guildAndCode.code=:code", Map.of("gid", guildId, "code", inviteCode));
    }

    /**
     * Get the Invite from our Database.
     *
     * @param guildId       the ID of the Guild.
     * @param inviteCreator the ID of the Invite Creator.
     * @param inviteCode    the Code of the Invite.
     * @return {@link Invite} as result if true, then it's saved in our Database | may be null.
     */
    public Mono<Invite> getInvite(long guildId, long inviteCreator, String inviteCode) {
        return getEntity(new Invite(), "FROM Invite WHERE guildAndCode.guildId=:gid AND userId=:uid AND guildAndCode.code=:code", Map.of("gid", guildId, "uid", inviteCreator, "code", inviteCode));
    }

    /**
     * Get the Invite from our Database.
     *
     * @param guildId       the ID of the Guild.
     * @param inviteCreator the ID of the Invite Creator.
     * @param inviteCode    the Code of the Invite.
     * @param inviteUsage   the Usage count of the Invite.
     * @return {@link Invite} as result if true, then it's saved in our Database | may be null.
     */
    public Mono<Invite> getInvite(long guildId, long inviteCreator, String inviteCode, long inviteUsage) {
        return getEntity(new Invite(), "FROM Invite WHERE guildAndCode.guildId=:gid AND userId=:uid AND guildAndCode.code=:code AND USES=:uses",
                Map.of("gid", guildId, "uid", inviteCreator, "code", inviteCode, "uses", inviteUsage));
    }

    /**
     * Remove an entry from our Database.
     *
     * @param guildId       the ID of the Guild.
     * @param inviteCreator the ID of the Invite Creator.
     * @param inviteCode    the Code of the Invite.
     */
    public void removeInvite(long guildId, long inviteCreator, String inviteCode) {
        deleteEntityInternally(getInvite(guildId, inviteCreator, inviteCode));
    }

    /**
     * Remove an entry from our Database.
     *
     * @param guildId       the ID of the Guild.
     * @param inviteCreator the ID of the Invite Creator.
     * @param inviteCode    the Code of the Invite.
     * @param inviteUsage   the usage count of the Invite.
     */
    public void removeInvite(long guildId, long inviteCreator, String inviteCode, long inviteUsage) {
        deleteEntityInternally(getInvite(guildId, inviteCreator, inviteCode, inviteUsage));
    }

    /**
     * Remove all entries from our Database.
     *
     * @param guildId the ID of the Guild.
     */
    public void clearInvites(long guildId) {
        getInvites(guildId).subscribe(x -> x.forEach(this::deleteEntityInternally));
    }

    //endregion

    //region Configuration

    //region Chat Protector / Word Blacklist

    /**
     * Get every blacklisted Word saved in our Database from the Guild.
     *
     * @param guildId the ID of the Guild.
     * @return {@link List<String>} as list with every blacklisted Word.
     */
    public Mono<List<String>> getChatProtectorWords(long guildId) {
        return getEntityList(new Blacklist(), "FROM Blacklist WHERE guildAndName.guildId = :gid", Map.of("gid", guildId)).map(x -> x.stream().map(Blacklist::getWord).toList());
    }

    /**
     * Check if there is an entry in our Database for the wanted Guild.
     *
     * @param guildId the ID of the Guild.
     * @return {@link Boolean} as result. If true, there is an entry in our Database | If false, there is no entry in our Database.
     */
    public Mono<Boolean> isChatProtectorSetup(long guildId) {
        return getEntity(new Blacklist(), "FROM Blacklist WHERE guildAndName.guildId = :gid", Map.of("gid", guildId)).map(Objects::nonNull);
    }

    /**
     * Check if there is an entry in our Database for the wanted Guild.
     *
     * @param guildId the ID of the Guild.
     * @param word    the Word that should be checked.
     * @return {@link Boolean} as result. If true, there is an entry in our Database | If false, there is no entry in our Database.
     */
    public Mono<Boolean> isChatProtectorSetup(long guildId, String word) {
        return getEntity(new Blacklist(), "FROM Blacklist WHERE guildAndName.guildId = :gid AND guildAndName.name = :word", Map.of("gid", guildId, "word", word)).map(Objects::nonNull);
    }

    /**
     * Add a new Word to the blacklist for the given Guild.
     *
     * @param guildId the ID of the Guild.
     * @param word    the Word to be blocked.
     */
    public void addChatProtectorWord(long guildId, String word) {
        isChatProtectorSetup(guildId, word).subscribe(isSetup -> {
            // Check if there is already an entry for it.
            if (isSetup) return;

            // If not, then just add it.
            updateEntity(new Blacklist(guildId, word)).block();
        });
    }

    /**
     * Remove a Word from the blacklist for the given Guild.
     *
     * @param guildId the ID of the Guild.
     * @param word    the Word to be removed.
     */
    public void removeChatProtectorWord(long guildId, String word) {
        getEntity(new Blacklist(), "FROM Blacklist WHERE guildAndName.guildId = :gid AND guildAndName.name = :word",
                Map.of("gid", guildId, "word", word)).subscribe(blacklist -> {
            // Check if there is no entry for it.
            if (blacklist == null) return;

            // If so, then delete it.
            deleteEntityInternally(blacklist);
        });
    }

    //endregion

    //region Settings

    /**
     * Get the current Setting by the Guild and its Identifier.
     *
     * @param guildId     the ID of the Guild.
     * @param settingName the Identifier of the Setting.
     * @return {@link Setting} which stores every information needed.
     */
    public Mono<Setting> getSetting(long guildId, String settingName) {
        return getEntity(new Setting(), "FROM Setting WHERE settingId.guildId = :gid AND settingId.name = :name",
                Map.of("gid", guildId, "name", settingName)).publishOn(Schedulers.boundedElastic()).mapNotNull(setting -> {
            // Check if there is an entry in the database.
            if (setting != null) {
                if (setting.getDisplayName() == null) {
                    Setting defaultSetting = SettingsManager.getDefault(settingName);

                    if (defaultSetting == null) {
                        log.info("Missing default for {} in SettingsManager.", settingName);
                    } else {
                        setting.setDisplayName(defaultSetting.getDisplayName());
                        updateEntity(setting).block();
                    }
                }

                return setting;
            } else {
                Setting defaultSetting = SettingsManager.getDefault(settingName);

                if (defaultSetting == null) {
                    log.info("Missing default for {} in SettingsManager.", settingName);
                    return null;
                }

                // Check if everything is alright with the config.
                checkSetting(guildId, settingName);

                defaultSetting.getSettingId().setGuildId(guildId);
                return defaultSetting;
            }
        });
    }

    /**
     * Get the every Setting by the Guild.
     *
     * @param guildId the ID of the Guild.
     * @return {@link List<Setting>} which is a List with every Setting that stores every information needed.
     */
    public Mono<List<Setting>> getAllSettings(long guildId) {
        return getEntityList(new Setting(), "FROM Setting WHERE settingId.guildId = :gid", Map.of("gid", guildId));
    }

    /**
     * Set the Setting by its Identifier.
     *
     * @param setting the Setting.
     */
    public void setSetting(Setting setting) {

        // Check if it is null.
        if (setting.getValue() == null) {
            Object defaultValue = SettingsManager.getDefaultValue(setting.getName());

            if (defaultValue == null) return;

            setting.setValue(defaultValue);
        }

        if (setting.getDisplayName() == null) {
            String defaultDisplayName = SettingsManager.getDefaultDisplayName(setting.getName());

            if (defaultDisplayName == null) return;

            setting.setDisplayName(defaultDisplayName);
        }

        getEntity(new Setting(), "FROM Setting WHERE settingId.guildId = :gid AND settingId.name = :name",
                Map.of("gid", setting.getGuildId(), "name", setting.getName())).subscribe(databaseSetting -> {
            if (databaseSetting != null) {
                databaseSetting.setValue(setting.getValue());
                updateEntity(databaseSetting).block();
            } else {
                updateEntity(setting).block();
            }
        });
    }

    /**
     * Set the Setting by the Guild and its Identifier.
     *
     * @param guildId      the ID of the Guild.
     * @param settingName  the Identifier of the Setting.
     * @param settingValue the Value of the Setting.
     */
    public void setSetting(long guildId, String settingName, String settingDisplayName, Object settingValue) {
        setSetting(new Setting(guildId, settingName, settingDisplayName, settingValue));
    }

    /**
     * Check if there is a Setting entry for the Guild.
     *
     * @param guildId the ID of the Guild.
     * @param setting the Setting itself.
     * @return {@link Boolean} as result. If true, there is a Setting Entry for the Guild | if false, there is no Entry for it.
     */
    public Mono<Boolean> hasSetting(long guildId, Setting setting) {
        return hasSetting(guildId, setting.getName());
    }

    /**
     * Check if there is a Setting entry for the Guild.
     *
     * @param guildId     the ID of the Guild.
     * @param settingName the Identifier of the Setting.
     * @return {@link Boolean} as result. If true, there is a Setting Entry for the Guild | if false, there is no Entry for it.
     */
    public Mono<Boolean> hasSetting(long guildId, String settingName) {
        return getEntity(new Setting(), "FROM Setting WHERE settingId.guildId =:gid AND settingId.name =:name", Map.of("gid", guildId, "name", settingName)).map(Objects::nonNull);
    }

    /**
     * Check if there is an entry for the Setting, if not, create one for every Setting that doesn't have an entry.
     *
     * @param guildId the ID of the Guild.
     * @param setting the Setting itself.
     */
    public void checkSetting(long guildId, Setting setting) {
        checkSetting(guildId, setting.getName());
    }

    /**
     * Check if there is an entry for the Setting, if not, create one for every Setting that doesn't have an entry.
     *
     * @param guildId     the ID of the Guild.
     * @param settingName the Identifier of the Setting.
     */
    public void checkSetting(long guildId, String settingName) {
        // Check if the Setting exists in our Database.
        hasSetting(guildId, settingName).subscribe(hasSetting -> {
            // If not then creat every Setting that doesn't exist for the Guild.
            if (!hasSetting) {
                createSettings(guildId);
            }
        });
    }

    /**
     * Create Settings entries for the Guild
     *
     * @param guildId the ID of the Guild.
     */
    public void createSettings(long guildId) {
        SettingsManager.getSettings().forEach(setting -> {
            hasSetting(guildId, setting).subscribe(hasSetting -> {
                // If not, then create every Setting that doesn't exist for the Guild.
                if (!hasSetting) {
                    setSetting(guildId, setting.getName(), setting.getDisplayName(), setting.getValue());
                }
            });
        });
    }

    //endregion

    //endregion

    //region Stats

    /**
     * Retrieve the Statistics of this day.
     *
     * @return the Statistics.
     */
    public Mono<Statistics> getStatisticsOfToday() {
        LocalDate today = LocalDate.now();
        return getStatistics(today.getDayOfMonth(), today.getMonthValue(), today.getYear());
    }

    /**
     * Retrieve the Statistics of this day.
     *
     * @param day   the day the statics has been taken from.
     * @param month the month the statics has been taken from.
     * @param year  the year the statics has been taken from.
     * @return the Statistics.
     */
    public Mono<Statistics> getStatistics(int day, int month, int year) {
        return getEntity(new Statistics(), "FROM Statistics WHERE day = :day AND month = :month AND year = :year", Map.of("day", day, "month", month, "year", year));
    }

    /**
     * Retrieve the Statistics of a month.
     *
     * @param month the month you want to receive the Statistics from.
     * @return all {@link Statistics} of the given month.
     */
    public Mono<List<Statistics>> getStatisticsOfMonth(int month) {
        return getEntityList(new Statistics(), "FROM Statistics WHERE month = :month", Map.of("month", month));
    }

    /**
     * Update or add new/existing Statistics.
     *
     * @param statisticObject the {@link JsonObject} for the statistic.
     */
    public void updateStatistic(JsonObject statisticObject) {
        LocalDate today = LocalDate.now();
        getEntity(new Statistics(), "FROM Statistics WHERE day = :day AND month = :month AND year = :year", Map.of("day", today.getDayOfMonth(), "month", today.getMonthValue(), "year", today.getYear())).subscribe(statistics -> {
            if (statistics != null) {
                statistics.setStatsObject(statisticObject);
                updateEntity(statistics).block();
            } else {
                statistics = new Statistics(today.getDayOfMonth(), today.getMonthValue(), today.getYear(), statisticObject);
                updateEntity(statistics).block();
            }
        });
    }

    /**
     * Get the Stats of the Command.
     *
     * @param command the Command.
     * @return the Stats of the Command.
     */
    public Mono<CommandStats> getStatsCommandGlobal(String command) {
        return getEntity(new CommandStats(), "FROM CommandStats WHERE command = :command", Map.of("command", command));
    }

    /**
     * Get the Stats of the Command in the specific Guild.
     *
     * @param guildId the ID of the Guild.
     * @param command the Command.
     * @return the Stats of the Command.
     */
    public Mono<GuildCommandStats> getStatsCommand(long guildId, String command) {
        return getEntity(new GuildCommandStats(), "FROM GuildCommandStats WHERE guildId = :gid AND command = :command", Map.of("gid", guildId, "command", command));
    }

    /**
     * Get all the Command-Stats related to the given Guild.
     *
     * @param guildId the ID of the Guild.
     * @return all the Command-Stats related to the given Guild.
     */
    public Mono<List<GuildCommandStats>> getStats(long guildId) {
        return getEntityList(new GuildCommandStats(), "FROM GuildCommandStats WHERE guildId=:gid ORDER BY uses DESC", Map.of("gid", guildId), 5);
    }

    /**
     * Get all the Command-Stats globally.
     *
     * @return all the Command-Stats globally.
     */
    public Mono<List<CommandStats>> getStatsGlobal() {
        return getEntityList(new CommandStats(), "FROM CommandStats ORDER BY uses DESC", null, 5);
    }

    /**
     * Check if there is any saved Stats for the given Guild.
     *
     * @param guildId the ID of the Guild.
     * @return {@link Boolean} as result. If true, there is data saved in the Database | If false, there is no data saved.
     */
    public Mono<Boolean> isStatsSaved(long guildId) {
        return getEntity(new GuildCommandStats(), "FROM GuildCommandStats WHERE guildId = :gid ", Map.of("gid", guildId)).map(Objects::nonNull);
    }

    /**
     * Check if there is any saved Stats for the given Guild and Command.
     *
     * @param guildId the ID of the Guild.
     * @param command the Name of the Command.
     * @return {@link Boolean} as result. If true, there is data saved in the Database | If false, there is no data saved.
     */
    public Mono<Boolean> isStatsSaved(long guildId, String command) {
        return getEntity(new GuildCommandStats(), "FROM GuildCommandStats WHERE guildId = :gid AND command = :command", Map.of("gid", guildId, "command", command)).map(Objects::nonNull);
    }

    /**
     * Check if there is any saved Stats for the given Command.
     *
     * @param command the Name of the Command.
     * @return {@link Boolean} as result. If true, there is data saved in the Database | If false, there is no data saved.
     */
    public Mono<Boolean> isStatsSavedGlobal(String command) {
        return getEntity(new CommandStats(), "FROM CommandStats WHERE command = :command", Map.of("command", command)).map(Objects::nonNull);
    }

    /**
     * Save the Stats of the Command in the Database.
     *
     * @param guildId the ID of the Guild.
     * @param command the Command.
     */
    public void addStats(long guildId, String command) {
        getStatisticsOfToday().subscribe(
                stats -> {
                    JsonObject jsonObject = stats != null ? stats.getStatsObject() : new JsonObject();
                    JsonObject commandStats = stats != null && jsonObject.has("command") ? jsonObject.getAsJsonObject("command") : new JsonObject();

                    if (commandStats.has(command) && commandStats.get(command).isJsonPrimitive()) {
                        commandStats.addProperty(command, commandStats.getAsJsonPrimitive(command).getAsInt() + 1);
                    } else {
                        commandStats.addProperty(command, 1);
                    }

                    jsonObject.add("command", commandStats);

                    updateStatistic(jsonObject);

                    // Check if there is an entry.
                    isStatsSaved(guildId, command).subscribe(x -> {
                        if (x) {
                            getStatsCommand(guildId, command).subscribe(y -> {
                                y.setUses(y.getUses() + 1);
                                updateEntity(y).block();
                            });
                        } else {
                            updateEntity(new GuildCommandStats(0, guildId, command, 1)).block();
                        }
                    });

                    // Check if there is an entry.
                    isStatsSavedGlobal(command).subscribe(x -> {
                        if (x) {
                            getStatsCommandGlobal(command).subscribe(y -> {
                                y.setUses(y.getUses() + 1);
                                updateEntity(y).block();
                            });
                        } else {
                            updateEntity(new CommandStats(command, 1)).block();
                        }
                    });
                }
        );
    }

    //endregion

    //region Opt-out

    /**
     * Check if the given User is opted out.
     *
     * @param guildId the ID of the Guild.
     * @param userId  the ID of the User.
     * @return {@link Boolean} as result. If true, the User is opted out | If false, the User is not opted out.
     */
    public Mono<Boolean> isOptOut(long guildId, long userId) {
        // Creating an SQL Statement to check if there is an entry in the Opt-out Table by the Guild ID and User ID
        return getEntity(new OptOut(), "FROM OptOut WHERE guildUserId.guildId=:gid AND guildUserId.userId=:uid", Map.of("gid", guildId, "uid", userId)).map(Objects::nonNull);
    }

    /**
     * Opt a User out of the given Guild.
     *
     * @param guildId the ID of the Guild.
     * @param userId  the ID of the User.
     */
    public void optOut(long guildId, long userId) {
        isOptOut(guildId, userId).subscribe(optedOut -> {
            if (!optedOut) {
                updateEntity(new OptOut(guildId, userId)).block();
            }
        });
    }

    /**
     * Opt in a User to the given Guild.
     *
     * @param guildId the ID of the Guild.
     * @param userId  the ID of the User.
     */
    public void optIn(long guildId, long userId) {
        getEntity(new OptOut(), "FROM OptOut WHERE guildUserId.guildId=:gid AND guildUserId.userId=:uid",
                Map.of("gid", guildId, "uid", userId)).subscribe(this::deleteEntityInternally);
    }

    //endregion

    //region Birthday

    /**
     * Store the birthday of the user in the database
     *
     * @param guildId   the ID of the Guild.
     * @param channelId the ID of the Channel.
     * @param userId    the ID of the User.
     * @param birthday  the birthday of the user.
     */
    public void addBirthday(long guildId, long channelId, long userId, String birthday) {
        try {
            BirthdayWish newBirthday = new BirthdayWish(guildId, channelId, userId, new SimpleDateFormat("dd.MM.yyyy").parse(birthday));
            updateEntity(newBirthday).subscribe();
        } catch (Exception exception) {
            log.error("Couldn't save birthday!", exception);
            Sentry.captureException(exception);
        }
    }

    /**
     * Check if there is any saved birthday for the given User.
     *
     * @param guildId the ID of the Guild.
     * @param userId  the ID of the User.
     */
    public void removeBirthday(long guildId, long userId) {
        getEntity(new BirthdayWish(), "FROM BirthdayWish WHERE guildId=:gid AND userId=:uid", Map.of("gid", guildId, "uid", userId)).subscribe(this::deleteEntityInternally);
    }

    /**
     * Check if a birthday is saved for the given User.
     *
     * @param guildId the ID of the Guild.
     * @param userId  the ID of the User.
     * @return {@link Boolean} as result. If true, there is data saved in the Database | If false, there is no data saved.
     */
    public Mono<Boolean> isBirthdaySaved(long guildId, long userId) {
        return getEntity(new BirthdayWish(), "FROM BirthdayWish WHERE guildId=:gid AND userId=:uid", Map.of("gid", guildId, "uid", userId)).map(Objects::nonNull);
    }

    /**
     * Get the birthday of the given User.
     *
     * @param guildId the ID of the Guild.
     * @param userId  the ID of the User.
     * @return {@link BirthdayWish} as result. If true, there is data saved in the Database | If false, there is no data saved.
     */
    public Mono<BirthdayWish> getBirthday(long guildId, long userId) {
        return getEntity(new BirthdayWish(), "FROM BirthdayWish WHERE guildId=:gid AND userId=:uid", Map.of("gid", guildId, "uid", userId));
    }

    /**
     * Get all saved birthdays.
     *
     * @param guildId the ID of the Guild.
     * @return {@link List} of {@link BirthdayWish} as result. If true, there is data saved in the Database | If false, there is no data saved.
     */
    public Mono<List<BirthdayWish>> getBirthdays(long guildId) {
        return getEntityList(new BirthdayWish(), "FROM BirthdayWish WHERE guildId=:gid", Map.of("gid", guildId));
    }

    /**
     * Get all saved birthdays.
     *
     * @return {@link List} of {@link BirthdayWish} as result. If true, there is data saved in the Database | If false, there is no data saved.
     */
    public Mono<List<BirthdayWish>> getBirthdays() {
        return getEntityList(new BirthdayWish(), "FROM BirthdayWish", Map.of());
    }

    //endregion

    //region Data delete

    /**
     * Delete Data saved in our Database by the given Guild ID.
     *
     * @param guildId the ID of the Guild.
     */
    public void deleteAllData(long guildId) {
        Set<Class<?>> classSet = new Reflections(
                ConfigurationBuilder
                        .build()
                        .forPackage("de.presti.ree6.sql.entities", ClasspathHelper.staticClassLoader()))
                .getTypesAnnotatedWith(Entity.class);
        for (Class<?> clazz : classSet) {
            Field[] fields = clazz.getFields();
            Field[] superFields = null;
            
            if (clazz.getSuperclass() != null) {
                superFields = clazz.getSuperclass().getDeclaredFields();
            }

            String hibernateQueryForId = "";

            // TODO:: extract this to a method.

            for (Field field : fields) {
                if (field.isAnnotationPresent(Id.class)) {
                    if (field.isAnnotationPresent(Column.class)) {
                        Column column = field.getAnnotation(Column.class);
                        if (column.name().equalsIgnoreCase("guildId")) {
                            hibernateQueryForId = field.getName();
                            break;
                        }
                    }
                }

                if (!field.isAnnotationPresent(EmbeddedId.class))
                    continue;

                Class<?> declaredClass = field.getDeclaringClass();

                if (declaredClass.isAssignableFrom(GuildAndId.class) ||
                        declaredClass.isAssignableFrom(GuildAndName.class) ||
                        declaredClass.isAssignableFrom(GuildUserId.class) ||
                        declaredClass.isAssignableFrom(GuildRoleId.class) ||
                        declaredClass.isAssignableFrom(GuildChannelId.class) ||
                        declaredClass.isAssignableFrom(GuildAndCode.class)) {

                    hibernateQueryForId = field.getName() + ".guildId";
                    break;
                }
            }
            
            if (hibernateQueryForId.isBlank() && superFields != null) {
                for (Field field : superFields) {
                    if (field.isAnnotationPresent(Id.class)) {
                        if (field.isAnnotationPresent(Column.class)) {
                            Column column = field.getAnnotation(Column.class);
                            if (column.name().equalsIgnoreCase("guildId")) {
                                hibernateQueryForId = field.getName();
                                break;
                            }
                        }
                    }

                    if (!field.isAnnotationPresent(EmbeddedId.class))
                        continue;

                    Class<?> declaredClass = field.getDeclaringClass();

                    if (declaredClass.isAssignableFrom(GuildAndId.class) ||
                            declaredClass.isAssignableFrom(GuildAndName.class) ||
                            declaredClass.isAssignableFrom(GuildUserId.class) ||
                            declaredClass.isAssignableFrom(GuildRoleId.class) ||
                            declaredClass.isAssignableFrom(GuildChannelId.class) ||
                            declaredClass.isAssignableFrom(GuildAndCode.class)) {

                        hibernateQueryForId = field.getName() + ".guildId";
                        break;
                    }
                }
            }

            if (!hibernateQueryForId.isBlank()) {
                sqlConnector.query("delete " + clazz.getSimpleName() + " where " + hibernateQueryForId + " = :gid", Map.of("gid", guildId));
            }
        }
    }

    //endregion

    //region Entity-System

    /**
     * Update an Entity in the Database.
     *
     * @param <R> The Class-Entity.
     * @param r   The Class-Entity to update.
     * @return the new update entity.
     */
    public <R> Mono<R> updateEntity(R r) {
        return Mono.fromSupplier(() -> updateEntityInternal(r));
    }

    private <R> R updateEntityInternal(R r) {
        if (r == null) return null;

        if (!sqlConnector.isConnected()) {
            if (sqlConnector.connectedOnce()) {
                sqlConnector.connectToSQLServer();
                return updateEntityInternal(r);
            }
        }

        // TODO:: Need a better way to handle this.
        if (r instanceof Punishments punishments) {
            if (punishments.getId() <= 0) {
                ((Punishments) r).getGuildAndId().setId(getNextId(r));
            }
        } else if (r instanceof ScheduledMessage scheduledMessage) {
            if (scheduledMessage.getId() <= 0) {
                ((ScheduledMessage) r).getGuildAndId().setId(getNextId(r));
            }
        } else if (r instanceof WebhookSocial webhookSocial) {
            if (webhookSocial.getId() <= 0) {
                ((WebhookSocial) r).getGuildAndId().setId(getNextId(r));
            }
        }

        try (Session session = SQLSession.getSessionFactory().openSession()) {

            session.beginTransaction();

            R newEntity = session.merge(r);

            session.getTransaction().commit();

            return newEntity;
        } catch (PersistenceException exception) {
            if (!sqlConnector.connectedSecond()) {
                if (exception.getCause() instanceof JDBCConnectionException jdbcConnectionException) {
                    if (jdbcConnectionException.getSQLException() instanceof SQLNonTransientConnectionException) {
                        sqlConnector.connectToSQLServer();
                        sqlConnector.setConnectedSecond(true);
                        return updateEntityInternal(r);
                    }
                }
            } else {
                sqlConnector.setConnectedSecond(false);
            }

            throw exception;
        } catch (Exception exception) {
            Sentry.captureException(exception);
            log.error("Failed to update Entity", exception);
            throw exception;
        }
    }

    private <R> long getNextId(R r) {
        List<R> entityList = getEntityListInternal(r, "select max(guildAndId.id) from " + r.getClass().getName(), null, 1);

        if (entityList.isEmpty()) return 1;

        Object databaseReturnValue = entityList.get(0);

        long maxId = 0;

        if (databaseReturnValue != null) {
            maxId = (Long) databaseReturnValue;
        }

        return maxId + 1;
    }

    /**
     * Delete an entity from the database
     *
     * @param <R> The Class-Entity.
     * @param r   The Class-Entity to delete.
     */
    public <R> Mono<Void> deleteEntity(R r) {
        return Mono.fromRunnable(() -> deleteEntityInternally(r));
    }

    private <R> void deleteEntityInternally(R r) {
        if (r == null) return;

        if (!sqlConnector.isConnected()) {
            if (sqlConnector.connectedOnce()) {
                sqlConnector.connectToSQLServer();
                deleteEntityInternally(r);
            }
        }

        try (Session session = SQLSession.getSessionFactory().openSession()) {

            session.beginTransaction();

            session.remove(r);

            session.getTransaction().commit();
        } catch (Exception exception) {
            Sentry.captureException(exception);
            log.error("Failed to delete Entity", exception);
            throw exception;
        }
    }

    /**
     * Constructs a new mapped Version of the Entity-class.
     *
     * @param <R>        The Class-Entity.
     * @param r          The Class-Entity to get.
     * @param sqlQuery   the SQL-Query.
     * @param parameters all parameters.
     * @return The mapped entity.
     */
    public <R> Mono<List<R>> getEntityList(@NotNull R r, @NotNull String sqlQuery, @Nullable Map<String, Object> parameters) {
        return getEntityList(r, sqlQuery, parameters, 0);
    }

    /**
     * Constructs a new mapped Version of the Entity-class.
     *
     * @param <R>            The Class-Entity.
     * @param r              The Class-Entity to get.
     * @param sqlQuery       the SQL-Query.
     * @param parameters     all parameters.
     * @param limit          the result limit of the query.
     * @return The mapped entity.
     */
    public <R> Mono<List<R>> getEntityList(@NotNull R r, @NotNull String sqlQuery, @Nullable Map<String, Object> parameters, int limit) {
        return Mono.fromSupplier(() -> getEntityListInternal(r, sqlQuery, parameters, limit));
    }

    /**
     * Constructs a new mapped Version of the Entity-class.
     *
     * @param <R>            The Class-Entity.
     * @param r              The Class-Entity to get.
     * @param sqlQuery       the SQL-Query.
     * @param parameters     all parameters.
     * @param useNativeQuery if true, use native query, else use hibernate query.
     * @param limit          the result limit of the query.
     * @return The mapped entity.
     * @deprecated Use {@link #getEntityListInternal(Object, String, Map, int)} instead.
     */
    @Deprecated(forRemoval = true)
    private <R> List<R> getEntityListInternal(@NotNull R r, @NotNull String sqlQuery, @Nullable Map<String, Object> parameters, boolean useNativeQuery, int limit) {
        sqlQuery = sqlQuery.isBlank() ? (useNativeQuery ? "SELECT * FROM " : "FROM ") + r.getClass().getSimpleName() : sqlQuery;

        try (Session session = SQLSession.getSessionFactory().openSession()) {

            session.beginTransaction();

            Query<R> query;

            if (useNativeQuery) {
                query = (Query<R>) session.createNativeQuery(sqlQuery, r.getClass());
            } else {
                query = (Query<R>) session.createQuery(sqlQuery, r.getClass());
            }

            if (parameters != null) {
                for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                    query.setParameter(entry.getKey(), entry.getValue());
                }
            }

            if (limit > 0) query.setMaxResults(limit);

            session.getTransaction().commit();

            return query.getResultList();
        } catch (Exception exception) {
            Sentry.captureException(exception);
            log.error("Failed to get Entity List", exception);
            throw exception;
        }
    }

    /**
     * Constructs a new mapped Version of the Entity-class.
     *
     * @param <R>            The Class-Entity.
     * @param r              The Class-Entity to get.
     * @param sqlQuery       the SQL-Query.
     * @param parameters     all parameters.
     * @param limit          the result limit of the query.
     * @return The mapped entity.
     */
    @SuppressWarnings("unchecked")
    private <R> List<R> getEntityListInternal(@NotNull R r, @NotNull String sqlQuery, @Nullable Map<String, Object> parameters, int limit) {
        sqlQuery = sqlQuery.isBlank() ? "FROM " + r.getClass().getSimpleName() : sqlQuery;

        try (Session session = SQLSession.getSessionFactory().openSession()) {

            session.beginTransaction();

            Query<R> query = (Query<R>) session.createQuery(sqlQuery, r.getClass());

            if (parameters != null) {
                for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                    query.setParameter(entry.getKey(), entry.getValue());
                }
            }

            if (limit > 0) query.setMaxResults(limit);

            session.getTransaction().commit();

            return query.getResultList();
        } catch (Exception exception) {
            Sentry.captureException(exception);
            log.error("Failed to get Entity List!", exception);
            throw exception;
        }
    }

    /**
     * Constructs a query for the given Class-Entity, and returns a mapped Version of the given Class-Entity.
     *
     * @param <R>        The Class-Entity.
     * @param r          The Class-Entity to get.
     * @param sqlQuery   The query to use.
     * @param parameters The arguments to use.
     * @return The mapped Version of the given Class-Entity.
     */
    public <R> Mono<R> getEntity(@NotNull R r, @NotNull String sqlQuery, @Nullable Map<String, Object> parameters) {
        return Mono.fromSupplier(() -> getEntityInternal(r, sqlQuery, parameters));
    }

    /**
     * Constructs a query for the given Class-Entity, and returns a mapped Version of the given Class-Entity.
     *
     * @param <R>            The Class-Entity.
     * @param r              The Class-Entity to get.
     * @param sqlQuery       The query to use.
     * @param parameters     The arguments to use.
     * @param useNativeQuery if true, use native query, else use hibernate query.
     * @return The mapped Version of the given Class-Entity.
     * @deprecated Use {@link #getEntityInternal(Object, String, Map)} instead.
     */
    @Deprecated(forRemoval = true)
    private <R> R getEntityInternal(@NotNull R r, @NotNull String sqlQuery, @Nullable Map<String, Object> parameters, boolean useNativeQuery) {
        sqlQuery = sqlQuery.isEmpty() ? (useNativeQuery ? "SELECT * FROM " : "FROM ") + r.getClass().getSimpleName() : sqlQuery;

        try (Session session = SQLSession.getSessionFactory().openSession()) {

            session.beginTransaction();

            Query<R> query;

            if (useNativeQuery) {
                query = (Query<R>) session.createNativeQuery(sqlQuery, r.getClass());
            } else {
                query = (Query<R>) session.createQuery(sqlQuery, r.getClass());
            }

            if (parameters != null) {
                for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                    query.setParameter(entry.getKey(), entry.getValue());
                }
            }

            session.getTransaction().commit();

            return query.setMaxResults(1).getSingleResultOrNull();
        } catch (Exception exception) {
            Sentry.captureException(exception);
            log.error("Failed to get Entity", exception);
            throw exception;
        }
    }

    /**
     * Constructs a query for the given Class-Entity, and returns a mapped Version of the given Class-Entity.
     *
     * @param <R>            The Class-Entity.
     * @param r              The Class-Entity to get.
     * @param sqlQuery       The query to use.
     * @param parameters     The arguments to use.
     * @return The mapped Version of the given Class-Entity.
     */
    @SuppressWarnings("unchecked")
    private <R> R getEntityInternal(@NotNull R r, @NotNull String sqlQuery, @Nullable Map<String, Object> parameters) {
        sqlQuery = sqlQuery.isEmpty() ? "FROM " + r.getClass().getSimpleName() : sqlQuery;

        try (Session session = SQLSession.getSessionFactory().openSession()) {

            session.beginTransaction();

            Query<R> query = (Query<R>) session.createQuery(sqlQuery, r.getClass());

            if (parameters != null) {
                for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                    query.setParameter(entry.getKey(), entry.getValue());
                }
            }

            session.getTransaction().commit();

            return query.setMaxResults(1).getSingleResultOrNull();
        } catch (Exception exception) {
            Sentry.captureException(exception);
            log.error("Failed to get Entity", exception);
            throw exception;
        }
    }

    //endregion
}
