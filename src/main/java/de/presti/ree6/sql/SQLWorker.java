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
import de.presti.ree6.sql.util.SettingsManager;
import io.sentry.Sentry;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Table;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.query.Query;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import javax.annotation.Nonnull;
import java.sql.SQLNonTransientConnectionException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    public ChatUserLevel getChatLevelData(String guildId, String userId) {
        ChatUserLevel chatUserLevel =
                getEntity(new ChatUserLevel(), "SELECT * FROM Level WHERE GID=:gid AND UID=:uid", Map.of("gid", guildId, "uid", userId));

        if (chatUserLevel == null) {
            chatUserLevel = new ChatUserLevel(guildId, userId, 0);
        }

        return chatUserLevel;
    }

    /**
     * Check if the given combination of UserID and GuildID is saved in our Database.
     *
     * @param guildId the ID of the Guild.
     * @param userId  the ID of the User.
     * @return {@link Boolean} true if there was a match | false if there wasn't a match.
     */
    public boolean existsInChatLevel(String guildId, String userId) {
        return getEntity(new ChatUserLevel(), "SELECT * FROM Level WHERE GID=:gid AND UID=:uid", Map.of("gid", guildId, "uid", userId)) != null;
    }

    /**
     * Give the wanted User more XP.
     *
     * @param guildId   the ID of the Guild.
     * @param userLevel the {@link ChatUserLevel} Entity with all the information.
     */
    public void addChatLevelData(String guildId, @Nonnull ChatUserLevel userLevel) {

        if (isOptOut(guildId, userLevel.getUserId())) {
            return;
        }

        if (userLevel.getId() == 0) {
            ChatUserLevel oldUser =
                    getEntity(new ChatUserLevel(), "SELECT * FROM Level WHERE GID=:gid AND UID=:uid", Map.of("gid", guildId, "uid", userLevel.getUserId()));

            if (oldUser != null) {
                oldUser.setExperience(userLevel.getExperience());
                updateEntity(oldUser);
                return;
            }
        }

        updateEntity(userLevel);
    }

    /**
     * Get the Top list of the Guild Chat XP.
     *
     * @param guildId the ID of the Guild.
     * @param limit   the Limit of how many should be given back.
     * @return {@link List<ChatUserLevel>} as container of the User IDs.
     */
    public List<ChatUserLevel> getTopChat(String guildId, int limit) {
        return getEntityList(new ChatUserLevel(), "SELECT * FROM Level WHERE GID=:gid ORDER BY xp DESC LIMIT :limit", Map.of("gid", guildId, "limit", limit));
    }

    /**
     * Get the Top list of the Guild Chat XP.
     *
     * @param guildId the ID of the Guild.
     * @return {@link List<String>} as container of the User IDs.
     */
    public List<String> getAllChatLevelSorted(String guildId) {
        return getEntityList(new ChatUserLevel(), "SELECT * FROM Level WHERE GID=:gid ORDER BY xp DESC", Map.of("gid", guildId)).stream().map(UserLevel::getUserId).toList();
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
    public VoiceUserLevel getVoiceLevelData(String guildId, String userId) {
        VoiceUserLevel voiceUserLevel =
                getEntity(new VoiceUserLevel(), "SELECT * FROM VCLevel WHERE GID=:gid AND UID=:uid", Map.of("gid", guildId, "uid", userId));

        if (voiceUserLevel == null) {
            voiceUserLevel = new VoiceUserLevel(guildId, userId, 0);
        }

        return voiceUserLevel;
    }

    /**
     * Check if the given combination of UserID and GuildID is saved in our Database.
     *
     * @param guildId the ID of the Guild.
     * @param userId  the ID of the User.
     * @return {@link Boolean} true if there was a match | false if there wasn't a match.
     */
    public boolean existsInVoiceLevel(String guildId, String userId) {
        return getEntity(new VoiceUserLevel(), "SELECT * FROM VCLevel WHERE GID=:gid AND UID=:uid", Map.of("gid", guildId, "uid", userId)) != null;
    }

    /**
     * Give the wanted User more XP.
     *
     * @param guildId        the ID of the Guild.
     * @param voiceUserLevel the {@link VoiceUserLevel} Entity with all the information.
     */
    public void addVoiceLevelData(String guildId, @Nonnull VoiceUserLevel voiceUserLevel) {

        if (isOptOut(guildId, voiceUserLevel.getUserId())) {
            return;
        }

        if (voiceUserLevel.getId() == 0) {
            VoiceUserLevel oldUser =
                    getEntity(new VoiceUserLevel(), "SELECT * FROM VCLevel WHERE GID=:gid AND UID=:uid", Map.of("gid", guildId, "uid", voiceUserLevel.getUserId()));

            if (oldUser != null) {
                oldUser.setExperience(voiceUserLevel.getExperience());
                updateEntity(oldUser);
                return;
            }
        }

        updateEntity(voiceUserLevel);
    }

    /**
     * Get the Top list of the Guild Voice XP.
     *
     * @param guildId the ID of the Guild.
     * @param limit   the Limit of how many should be given back.
     * @return {@link List<VoiceUserLevel>} as container of the User IDs.
     */
    public List<VoiceUserLevel> getTopVoice(String guildId, int limit) {
        // Return the list.
        return getEntityList(new VoiceUserLevel(),
                "SELECT * FROM VCLevel WHERE GID=:gid ORDER BY xp DESC LIMIT :limit", Map.of("gid", guildId, "limit", limit));
    }

    /**
     * Get the Top list of the Guild Voice XP.
     *
     * @param guildId the ID of the Guild.
     * @return {@link List<String>} as container of the UserIds.
     */
    public List<String> getAllVoiceLevelSorted(String guildId) {
        // Creating a SQL Statement to get the Entries from the Level Table by the GuildID.
        return getEntityList(new VoiceUserLevel(), "SELECT * FROM VCLevel WHERE GID=:gid ORDER BY xp DESC", Map.of("gid", guildId)).stream().map(VoiceUserLevel::getUserId).toList();
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
    public WebhookLog getLogWebhook(String guildId) {
        return getEntity(new WebhookLog(), "SELECT * FROM LogWebhooks WHERE GID=:gid", Map.of("gid", guildId));
    }

    /**
     * Set the LogWebhook in our Database.
     *
     * @param guildId   the ID of the Guild.
     * @param channelId the ID of the Channel.
     * @param webhookId the ID of the Webhook.
     * @param authToken the Auth-token to verify the access.
     */
    public void setLogWebhook(String guildId, long channelId, String webhookId, String authToken) {
        WebhookLog webhookLog = getLogWebhook(guildId);
        if (webhookLog == null) {
            webhookLog = new WebhookLog();
            webhookLog.setGuildId(guildId);
        }

        webhookLog.setChannelId(channelId);
        webhookLog.setWebhookId(webhookId);
        webhookLog.setToken(authToken);

        updateEntity(webhookLog);
    }

    /**
     * Check if the Log Webhook has been set in our Database for this Server.
     *
     * @param guildId the ID of the Guild.
     * @return {@link Boolean} if true, it has been set | if false, it hasn't been set.
     */
    public boolean isLogSetup(String guildId) {
        return getEntity(new WebhookLog(), "SELECT * FROM LogWebhooks WHERE GID=:gid", Map.of("gid", guildId)) != null;
    }

    /**
     * Check if the Log Webhook data is in our Database.
     *
     * @param webhookId the ID of the Webhook.
     * @param authToken the Auth-Token of the Webhook.
     * @return {@link Boolean} if true, it has been set | if false, it hasn't been set.
     */
    public boolean existsLogData(long webhookId, String authToken) {
        return getEntity(new WebhookLog(), "SELECT * FROM LogWebhooks WHERE CID=:cid AND TOKEN=:token", Map.of("cid", webhookId, "token", authToken)) != null;
    }

    /**
     * Set the LogWebhook in our Database.
     *
     * @param webhookId the ID of the Webhook.
     * @param authToken the Auth-Token of the Webhook.
     */
    public void deleteLogWebhook(long webhookId, String authToken) {
        WebhookLog webhookLog =
                getEntity(new WebhookLog(), "SELECT * FROM LogWebhooks WHERE CID=:cid AND TOKEN=:token", Map.of("cid", webhookId, "token", authToken));

        if (webhookLog != null) {
            deleteEntity(webhookLog);
        }
    }

    //endregion

    //region Welcome

    /**
     * Get the WelcomeWebhooks data.
     *
     * @param guildId the ID of the Guild.
     * @return {@link WebhookWelcome} with all the needed data.
     */
    public WebhookWelcome getWelcomeWebhook(String guildId) {
        return getEntity(new WebhookWelcome(), "SELECT * FROM WelcomeWebhooks WHERE GID=:gid", Map.of("gid", guildId));
    }

    /**
     * Set the WelcomeWebhooks in our Database.
     *
     * @param guildId   the ID of the Guild.
     * @param channelId the ID of the Channel.
     * @param webhookId the ID of the Webhook.
     * @param authToken the Auth-token to verify the access.
     */
    public void setWelcomeWebhook(String guildId, long channelId, String webhookId, String authToken) {
        WebhookWelcome webhookWelcome = getWelcomeWebhook(guildId);
        if (webhookWelcome == null) {
            webhookWelcome = new WebhookWelcome();
            webhookWelcome.setGuildId(guildId);
        }

        webhookWelcome.setChannelId(channelId);
        webhookWelcome.setWebhookId(webhookId);
        webhookWelcome.setToken(authToken);

        updateEntity(webhookWelcome);
    }

    /**
     * Check if the Welcome Webhook has been set in our Database for this Server.
     *
     * @param guildId the ID of the Guild.
     * @return {@link Boolean} if true, it has been set | if false, it hasn't been set.
     */
    public boolean isWelcomeSetup(String guildId) {
        return getEntity(new WebhookWelcome(), "SELECT * FROM WelcomeWebhooks WHERE GID=:gid", Map.of("gid", guildId)) != null;
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
    public WebhookTwitch getTwitchWebhook(String guildId, String twitchName) {
        return getEntity(new WebhookTwitch(), "SELECT * FROM TwitchNotify WHERE GID=:gid AND NAME=:name", Map.of("gid", guildId, "name", twitchName));
    }

    /**
     * Get the TwitchNotify data.
     *
     * @param twitchName the Username of the Twitch User.
     * @return {@link List<WebhookTwitch>} with all the needed data.
     */
    public List<WebhookTwitch> getTwitchWebhooksByName(String twitchName) {
        return getEntityList(new WebhookTwitch(), "SELECT * FROM TwitchNotify WHERE NAME=:name", Map.of("name", twitchName));
    }

    /**
     * Get the all Twitch-Notifier.
     *
     * @return {@link List<>} the entry.
     */
    public List<String> getAllTwitchNames() {
        return getEntityList(new WebhookTwitch(), "SELECT * FROM TwitchNotify", null).stream().map(WebhookTwitch::getName).toList();
    }

    /**
     * Get every Twitch-Notifier that has been set up for the given Guild.
     *
     * @param guildId the ID of the Guild.
     * @return {@link List<>} the entry.
     */
    public List<String> getAllTwitchNames(String guildId) {
        return getAllTwitchWebhooks(guildId).stream().map(WebhookTwitch::getName).toList();
    }

    /**
     * Get every Twitch-Notifier that has been set up for the given Guild.
     *
     * @param guildId the ID of the Guild.
     * @return {@link List<>} the entry.
     */
    public List<WebhookTwitch> getAllTwitchWebhooks(String guildId) {
        return getEntityList(new WebhookTwitch(), "SELECT * FROM TwitchNotify WHERE GID=:gid", Map.of("gid", guildId));
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
    public void addTwitchWebhook(String guildId, long channelId, String webhookId, String authToken, String twitchName) {
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
    public void addTwitchWebhook(String guildId, long channelId, String webhookId, String authToken, String twitchName, String messageContent) {
        if (messageContent == null)
            messageContent = "%name% is now Live on Twitch! Come and join the stream <%url%>!";

        WebhookTwitch webhookTwitch = getTwitchWebhook(guildId, twitchName);

        if (webhookTwitch == null) {
            webhookTwitch = new WebhookTwitch();
            webhookTwitch.setGuildId(guildId);
        }

        webhookTwitch.setChannelId(channelId);
        webhookTwitch.setWebhookId(webhookId);
        webhookTwitch.setToken(authToken);
        webhookTwitch.setName(twitchName);
        webhookTwitch.setMessage(messageContent);

        // Add a new entry into the Database.
        updateEntity(webhookTwitch);
    }

    /**
     * Remove a Twitch Notifier entry from our Database.
     *
     * @param guildId    the ID of the Guild.
     * @param twitchName the Name of the Twitch User.
     */
    public void removeTwitchWebhook(String guildId, String twitchName) {

        WebhookTwitch webhook = getTwitchWebhook(guildId, twitchName);

        // Check if there is a Webhook set.
        if (webhook != null) {
            // Delete the entry.
            deleteEntity(webhook);
        }
    }

    /**
     * Check if the Twitch Webhook has been set in our Database for this Server.
     *
     * @param guildId the ID of the Guild.
     * @return {@link Boolean} if true, it has been set | if false, it hasn't been set.
     */
    public boolean isTwitchSetup(String guildId) {
        return getEntity(new WebhookTwitch(), "SELECT * FROM TwitchNotify WHERE GID=?", Map.of("gid", guildId)) != null;
    }

    /**
     * Check if the Twitch Webhook has been set for the given User in our Database for this Server.
     *
     * @param guildId    the ID of the Guild.
     * @param twitchName the Username of the Twitch User.
     * @return {@link Boolean} if true, it has been set | if false, it hasn't been set.
     */
    public boolean isTwitchSetup(String guildId, String twitchName) {
        return getEntity(new WebhookTwitch(), "SELECT * FROM TwitchNotify WHERE GID=:gid AND NAME=:name", Map.of("gid", guildId, "name", twitchName)) != null;
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
    public WebhookInstagram getInstagramWebhook(String guildId, String name) {
        return getEntity(new WebhookInstagram(), "SELECT * FROM InstagramNotify WHERE GID=:gid AND NAME=:name", Map.of("gid", guildId, "name", name));
    }

    /**
     * Get the InstagramNotify data.
     *
     * @param name the Name of the Instagram User.
     * @return {@link List<WebhookInstagram>} with all the needed data.
     */
    public List<WebhookInstagram> getInstagramWebhookByName(String name) {
        return getEntityList(new WebhookInstagram(), "SELECT * FROM InstagramNotify WHERE NAME=:name", Map.of("name", name));
    }

    /**
     * Get the all Instagram-Notifier.
     *
     * @return {@link List<>} the entry.
     */
    public List<String> getAllInstagramUsers() {
        return getEntityList(new WebhookInstagram(), "SELECT * FROM InstagramNotify", null).stream().map(WebhookInstagram::getName).toList();
    }

    /**
     * Get every Instagram-Notifier that has been set up for the given Guild.
     *
     * @param guildId the ID of the Guild.
     * @return {@link List<>} the entry.
     */
    public List<String> getAllInstagramUsers(String guildId) {
        return getAllInstagramWebhooks(guildId).stream().map(WebhookInstagram::getName).toList();
    }

    /**
     * Get every Instagram-Notifier that has been set up for the given Guild.
     *
     * @param guildId the ID of the Guild.
     * @return {@link List<>} the entry.
     */
    public List<WebhookInstagram> getAllInstagramWebhooks(String guildId) {
        return getEntityList(new WebhookInstagram(), "SELECT * FROM InstagramNotify WHERE GID=:gid", Map.of("gid", guildId));
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
    public void addInstagramWebhook(String guildId, long channelId, String webhookId, String authToken, String name) {
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
    public void addInstagramWebhook(String guildId, long channelId, String webhookId, String authToken, String name, String messageContent) {
        if (messageContent == null)
            messageContent = "%name% just posted something on their Instagram!";

        // Check if there is already a Webhook set.
        WebhookInstagram webhook = getInstagramWebhook(guildId, name);

        if (webhook == null) {
            webhook = new WebhookInstagram();
            webhook.setGuildId(guildId);
        }

        webhook.setChannelId(channelId);
        webhook.setWebhookId(webhookId);
        webhook.setToken(authToken);
        webhook.setName(name);
        webhook.setMessage(messageContent);

        // Add a new entry into the Database.
        updateEntity(webhook);
    }


    /**
     * Remove an Instagram Notifier entry from our Database.
     *
     * @param guildId the ID of the Guild.
     * @param name    the Name of the Instagram User.
     */
    public void removeInstagramWebhook(String guildId, String name) {

        WebhookInstagram webhook = getInstagramWebhook(guildId, name);

        // Check if there is a Webhook set.
        if (webhook != null) {
            // Delete the entry.
            deleteEntity(webhook);
        }
    }

    /**
     * Check if the Instagram Webhook has been set in our Database for this Server.
     *
     * @param guildId the ID of the Guild.
     * @return {@link Boolean} if true, it has been set | if false, it hasn't been set.
     */
    public boolean isInstagramSetup(String guildId) {
        return getEntity(new WebhookInstagram(), "SELECT * FROM InstagramNotify WHERE GID=:gid", Map.of("gid", guildId)) != null;
    }

    /**
     * Check if the Instagram Webhook has been set for the given User in our Database for this Server.
     *
     * @param guildId the ID of the Guild.
     * @param name    the Name of the Instagram User.
     * @return {@link Boolean} if true, it has been set | if false, it hasn't been set.
     */
    public boolean isInstagramSetup(String guildId, String name) {
        return getEntity(new WebhookInstagram(), "SELECT * FROM InstagramNotify WHERE GID=:gid AND NAME=:name", Map.of("gid", guildId, "name", name)) != null;
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
    public WebhookReddit getRedditWebhook(String guildId, String subreddit) {
        return getEntity(new WebhookReddit(), "SELECT * FROM RedditNotify WHERE GID=:gid AND SUBREDDIT=:name", Map.of("gid", guildId, "name", subreddit));
    }

    /**
     * Get the RedditNotify data.
     *
     * @param subreddit the Name of the Subreddit.
     * @return {@link List<WebhookReddit>} with all the needed data.
     */
    public List<WebhookReddit> getRedditWebhookBySub(String subreddit) {
        return getEntityList(new WebhookReddit(), "SELECT * FROM RedditNotify WHERE SUBREDDIT=:name", Map.of("name", subreddit));
    }

    /**
     * Get the all Reddit-Notifier.
     *
     * @return {@link List<>} the entry.
     */
    public List<String> getAllSubreddits() {
        return getEntityList(new WebhookReddit(), "SELECT * FROM RedditNotify", null).stream().map(WebhookReddit::getSubreddit).toList();
    }

    /**
     * Get every Reddit-Notifier that has been set up for the given Guild.
     *
     * @param guildId the ID of the Guild.
     * @return {@link List<>} the entry.
     */
    public List<String> getAllSubreddits(String guildId) {
        return getAllRedditWebhooks(guildId).stream().map(WebhookReddit::getSubreddit).toList();
    }

    /**
     * Get every Reddit-Notifier that has been set up for the given Guild.
     *
     * @param guildId the ID of the Guild.
     * @return {@link List<>} the entry.
     */
    public List<WebhookReddit> getAllRedditWebhooks(String guildId) {
        return getEntityList(new WebhookReddit(), "SELECT * FROM RedditNotify WHERE GID=:gid", Map.of("gid", guildId));
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
    public void addRedditWebhook(String guildId, long channelId, String webhookId, String authToken, String subreddit) {
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
    public void addRedditWebhook(String guildId, long channelId, String webhookId, String authToken, String subreddit, String messageContent) {
        if (messageContent == null)
            messageContent = "%name% got a new post check it out <%url%>!";

        // Check if there is already a Webhook set.
        WebhookReddit webhook = getRedditWebhook(guildId, subreddit);

        if (webhook == null) {
            webhook = new WebhookReddit();
            webhook.setGuildId(guildId);
        }

        webhook.setChannelId(channelId);
        webhook.setWebhookId(webhookId);
        webhook.setToken(authToken);
        webhook.setSubreddit(subreddit);
        webhook.setMessage(messageContent);

        // Add a new entry into the Database.
        updateEntity(webhook);
    }

    /**
     * Remove a Reddit Notifier entry from our Database.
     *
     * @param guildId   the ID of the Guild.
     * @param subreddit the Name of the Subreddit.
     */
    public void removeRedditWebhook(String guildId, String subreddit) {

        WebhookReddit webhook = getRedditWebhook(guildId, subreddit);

        // Check if there is a Webhook set.
        if (webhook != null) {
            // Delete the entry.
            deleteEntity(webhook);
        }
    }

    /**
     * Check if the Reddit Webhook has been set in our Database for this Server.
     *
     * @param guildId the ID of the Guild.
     * @return {@link Boolean} if true, it has been set | if false, it hasn't been set.
     */
    public boolean isRedditSetup(String guildId) {
        return getEntity(new WebhookReddit(), "SELECT * FROM RedditNotify WHERE GID=:gid", Map.of("gid", guildId)) != null;
    }

    /**
     * Check if the Reddit Webhook has been set for the given User in our Database for this Server.
     *
     * @param guildId   the ID of the Guild.
     * @param subreddit the Name of the Subreddit.
     * @return {@link Boolean} if true, it has been set | if false, it hasn't been set.
     */
    public boolean isRedditSetup(String guildId, String subreddit) {
        return getEntity(new WebhookReddit(), "SELECT * FROM RedditNotify WHERE GID=:gid AND SUBREDDIT=:name", Map.of("gid", guildId, "name", subreddit)) != null;
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
    public WebhookYouTube getYouTubeWebhook(String guildId, String youtubeChannel) {
        return getEntity(new WebhookYouTube(), "SELECT * FROM YouTubeNotify WHERE GID=:gid AND NAME=:name", Map.of("gid", guildId, "name", youtubeChannel));
    }

    /**
     * Get the YouTubeNotify data.
     *
     * @param youtubeChannel the Username of the YouTube channel.
     * @return {@link List<WebhookYouTube>} with all the needed data.
     */
    public List<WebhookYouTube> getYouTubeWebhooksByName(String youtubeChannel) {
        return getEntityList(new WebhookYouTube(), "SELECT * FROM YouTubeNotify WHERE NAME=:name", Map.of("name", youtubeChannel));
    }

    /**
     * Get the all YouTube-Notifier.
     *
     * @return {@link List<>} the entry.
     */
    public List<String> getAllYouTubeChannels() {
        return getEntityList(new WebhookYouTube(), "SELECT * FROM YouTubeNotify", null).stream().map(WebhookYouTube::getName).toList();
    }

    /**
     * Get every YouTube-Notifier that has been set up for the given Guild.
     *
     * @param guildId the ID of the Guild.
     * @return {@link List<>} the entry.
     */
    public List<String> getAllYouTubeChannels(String guildId) {
        return getAllYouTubeWebhooks(guildId).stream().map(WebhookYouTube::getName).toList();
    }

    /**
     * Get every YouTube-Notifier that has been set up for the given Guild.
     *
     * @param guildId the ID of the Guild.
     * @return {@link List<>} the entry.
     */
    public List<WebhookYouTube> getAllYouTubeWebhooks(String guildId) {
        return getEntityList(new WebhookYouTube(), "SELECT * FROM YouTubeNotify WHERE GID=:gid", Map.of("gid", guildId));
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
    public void addYouTubeWebhook(String guildId, long channelId, String webhookId, String authToken, String youtubeChannel) {
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
    public void addYouTubeWebhook(String guildId, long channelId, String webhookId, String authToken, String youtubeChannel, String messageContent) {
        if (messageContent == null)
            messageContent = "%name% just uploaded a new Video! Check it out <%url%>!";

        // Check if there is already a Webhook set.
        WebhookYouTube webhook = getYouTubeWebhook(guildId, youtubeChannel);

        if (webhook == null) {
            webhook = new WebhookYouTube();
            webhook.setGuildId(guildId);
        }

        webhook.setChannelId(channelId);
        webhook.setWebhookId(webhookId);
        webhook.setToken(authToken);
        webhook.setName(youtubeChannel);
        webhook.setMessage(messageContent);

        // Add a new entry into the Database.
        updateEntity(webhook);
    }

    /**
     * Remove a YouTube Notifier entry from our Database.
     *
     * @param guildId        the ID of the Guild.
     * @param youtubeChannel the Name of the YouTube channel.
     */
    public void removeYouTubeWebhook(String guildId, String youtubeChannel) {

        WebhookYouTube webhook = getYouTubeWebhook(guildId, youtubeChannel);

        // Check if there is a Webhook set.
        if (webhook != null) {
            // Delete the entry.
            deleteEntity(webhook);
        }
    }

    /**
     * Check if the YouTube Webhook has been set in our Database for this Server.
     *
     * @param guildId the ID of the Guild.
     * @return {@link Boolean} if true, it has been set | if false, it hasn't been set.
     */
    public boolean isYouTubeSetup(String guildId) {
        return getEntity(new WebhookYouTube(), "SELECT * FROM YouTubeNotify WHERE GID=:gid", Map.of("gid", guildId)) != null;
    }

    /**
     * Check if the YouTube Webhook has been set for the given User in our Database for this Server.
     *
     * @param guildId        the ID of the Guild.
     * @param youtubeChannel the Username of the YouTube channel.
     * @return {@link Boolean} if true, it has been set | if false, it hasn't been set.
     */
    public boolean isYouTubeSetup(String guildId, String youtubeChannel) {
        return getEntity(new WebhookYouTube(), "SELECT * FROM YouTubeNotify WHERE GID=:gid AND NAME=:name", Map.of("gid", guildId, "name", youtubeChannel)) != null;
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
    public WebhookTwitter getTwitterWebhook(String guildId, String twitterName) {
        return getEntity(new WebhookTwitter(), "SELECT * FROM TwitterNotify WHERE GID=:gid AND NAME=:name", Map.of("gid", guildId, "name", twitterName));
    }

    /**
     * Get the TwitterNotify data.
     *
     * @param twitterName the Username of the Twitter User.
     * @return {@link List<WebhookTwitter>} with all the needed data.
     */
    public List<WebhookTwitter> getTwitterWebhooksByName(String twitterName) {
        return getEntityList(new WebhookTwitter(), "SELECT * FROM TwitterNotify WHERE NAME=:name", Map.of("name", twitterName));
    }

    /**
     * Get the all Twitter-Notifier.
     *
     * @return {@link List<>} the entry.
     */
    public List<String> getAllTwitterNames() {
        return getEntityList(new WebhookTwitter(), "SELECT * FROM TwitterNotify", null).stream().map(WebhookTwitter::getName).toList();
    }

    /**
     * Get every Twitter-Notifier that has been set up for the given Guild.
     *
     * @param guildId the ID of the Guild.
     * @return {@link List<>} the entry.
     */
    public List<String> getAllTwitterNames(String guildId) {
        return getAllTwitterWebhooks(guildId).stream().map(WebhookTwitter::getName).toList();
    }

    /**
     * Get every Twitter-Notifier that has been set up for the given Guild.
     *
     * @param guildId the ID of the Guild.
     * @return {@link List<>} the entry.
     */
    public List<WebhookTwitter> getAllTwitterWebhooks(String guildId) {
        return getEntityList(new WebhookTwitter(), "SELECT * FROM TwitterNotify WHERE GID=:gid", Map.of("gid", guildId));
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
    public void addTwitterWebhook(String guildId, long channelId, String webhookId, String authToken, String twitterName) {
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
    public void addTwitterWebhook(String guildId, long channelId, String webhookId, String authToken, String twitterName, String messageContent) {
        if (messageContent == null)
            messageContent = "%name% tweeted something! Check it out <%url%>!";

        // Check if there is already a Webhook set.
        WebhookTwitter webhook = getTwitterWebhook(guildId, twitterName);

        if (webhook == null) {
            webhook = new WebhookTwitter();
            webhook.setGuildId(guildId);
        }

        webhook.setChannelId(channelId);
        webhook.setWebhookId(webhookId);
        webhook.setToken(authToken);
        webhook.setName(twitterName);
        webhook.setMessage(messageContent);

        // Add a new entry into the Database.
        updateEntity(webhook);
    }

    /**
     * Remove a Twitter Notifier entry from our Database.
     *
     * @param guildId     the ID of the Guild.
     * @param twitterName the Name of the Twitter User.
     */
    public void removeTwitterWebhook(String guildId, String twitterName) {
        WebhookTwitter webhook = getTwitterWebhook(guildId, twitterName);

        // Check if there is a Webhook set.
        if (webhook != null) {
            // Delete the entry.
            deleteEntity(webhook);
        }
    }

    /**
     * Check if the Twitter Webhook has been set in our Database for this Server.
     *
     * @param guildId the ID of the Guild.
     * @return {@link Boolean} if true, it has been set | if false, it hasn't been set.
     */
    public boolean isTwitterSetup(String guildId) {
        return getEntity(new WebhookTwitter(), "SELECT * FROM TwitterNotify WHERE GID=:gid", Map.of("gid", guildId)) != null;
    }

    /**
     * Check if the Twitter Webhook has been set for the given User in our Database for this Server.
     *
     * @param guildId     the ID of the Guild.
     * @param twitterName the Username of the Twitter User.
     * @return {@link Boolean} if true, it has been set | if false, it hasn't been set.
     */
    public boolean isTwitterSetup(String guildId, String twitterName) {
        return getEntity(new WebhookTwitter(), "SELECT * FROM TwitterNotify WHERE GID=:gid AND NAME=:name", Map.of("gid", guildId, "name", twitterName)) != null;
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
    public List<AutoRole> getAutoRoles(String guildId) {
        return getEntityList(new AutoRole(), "SELECT * FROM AutoRoles WHERE GID=:gid", Map.of("gid", guildId));
    }

    /**
     * Add a AutoRole in our Database.
     *
     * @param guildId the ID of the Guild.
     * @param roleId  the ID of the Role.
     */
    public void addAutoRole(String guildId, String roleId) {
        // Check if there is a role in the database.
        if (!isAutoRoleSetup(guildId, roleId)) {
            // Add a new entry into the Database.
            updateEntity(new AutoRole(guildId, roleId));
        }
    }

    /**
     * Remove a AutoRole from our Database.
     *
     * @param guildId the ID of the Guild.
     * @param roleId  the ID of the Role.
     */
    public void removeAutoRole(String guildId, String roleId) {
        AutoRole autoRole = getEntity(new AutoRole(), "SELECT * FROM AutoRoles WHERE GID=:gid AND RID=:rid ", Map.of("gid", guildId, "rid", roleId));

        // Check if there is a role in the database.
        if (autoRole != null) {
            // Add a new entry into the Database.
            deleteEntity(autoRole);
        }
    }

    /**
     * Check if a AutoRole has been set in our Database for this Server.
     *
     * @param guildId the ID of the Guild.
     * @return {@link Boolean} as result if true, there is a role in our Database | if false, we couldn't find anything.
     */
    public boolean isAutoRoleSetup(String guildId) {
        return getEntity(new AutoRole(), "SELECT * FROM AutoRoles WHERE GID=:gid ", Map.of("gid", guildId)) != null;
    }

    /**
     * Check if a AutoRole has been set in our Database for this Server.
     *
     * @param guildId the ID of the Guild.
     * @param roleId  the ID of the Role.
     * @return {@link Boolean} as result if true, there is a role in our Database | if false, we couldn't find anything.
     */
    public boolean isAutoRoleSetup(String guildId, String roleId) {
        return getEntity(new AutoRole(), "SELECT * FROM AutoRoles WHERE GID=:gid AND RID=:rid ", Map.of("gid", guildId, "rid", roleId)) != null;
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
    public Map<Long, String> getChatLevelRewards(String guildId) {

        // Create a new HashMap to save the Role Ids and their needed level.
        Map<Long, String> rewards = new HashMap<>();

        getEntityList(new ChatAutoRole(), "SELECT * FROM ChatLevelAutoRoles WHERE GID=:gid", Map.of("gid", guildId)).forEach(chatAutoRole -> rewards.put(chatAutoRole.getLevel(), chatAutoRole.getRoleId()));

        // Return the HashMap.
        return rewards;
    }

    /**
     * Add a Chat Level Reward Role in our Database.
     *
     * @param guildId the ID of the Guild.
     * @param roleId  the ID of the Role.
     * @param level   the Level required to get this Role.
     */
    public void addChatLevelReward(String guildId, String roleId, long level) {
        // Check if there is a role in the database.
        if (!isChatLevelRewardSetup(guildId, roleId, level)) {
            // Add a new entry into the Database.
            updateEntity(new ChatAutoRole(guildId, roleId, level));
        }
    }

    /**
     * Remove a Chat Level Reward Role from our Database.
     *
     * @param guildId the ID of the Guild.
     * @param level   the Level required to get this Role.
     */
    public void removeChatLevelReward(String guildId, long level) {
        ChatAutoRole chatAutoRole = getEntity(new ChatAutoRole(), "SELECT * FROM ChatLevelAutoRoles WHERE GID=:gid AND LVL=:lvl",
                Map.of("gid", guildId, "lvl", level));

        // Check if there is a role in the database.
        if (chatAutoRole != null) {
            // Add a new entry into the Database.
            deleteEntity(chatAutoRole);
        }
    }

    /**
     * Remove a Chat Level Reward Role from our Database.
     *
     * @param guildId the ID of the Guild.
     * @param roleId  the ID of the Role.
     * @param level   the Level required to get this Role.
     */
    public void removeChatLevelReward(String guildId, String roleId, long level) {
        ChatAutoRole chatAutoRole = getEntity(new ChatAutoRole(), "SELECT * FROM ChatLevelAutoRoles WHERE GID=:gid AND RID=:roleId AND LVL=:lvl",
                Map.of("gid", guildId, "rid", roleId, "lvl", level));

        // Check if there is a role in the database.
        if (chatAutoRole != null) {
            // Add a new entry into the Database.
            deleteEntity(chatAutoRole);
        }
    }

    /**
     * Check if a Chat Level Reward has been set in our Database for this Server.
     *
     * @param guildId the ID of the Guild.
     * @return {@link Boolean} as result if true, there is a role in our Database | if false, we couldn't find anything.
     */
    public boolean isChatLevelRewardSetup(String guildId) {
        return getEntity(new ChatAutoRole(), "SELECT * FROM ChatLevelAutoRoles WHERE GID=:gid", Map.of("gid", guildId)) != null;
    }

    /**
     * Check if a Chat Level Reward has been set in our Database for this Server.
     *
     * @param guildId the ID of the Guild.
     * @param roleId  the ID of the Role.
     * @return {@link Boolean} as result if true, there is a role in our Database | if false, we couldn't find anything.
     */
    public boolean isChatLevelRewardSetup(String guildId, String roleId) {
        return getEntity(new ChatAutoRole(), "SELECT * FROM ChatLevelAutoRoles WHERE GID=:gid AND RID=:rid", Map.of("gid", guildId, "rid", roleId)) != null;
    }

    /**
     * Check if a Chat Level Reward has been set in our Database for this Server.
     *
     * @param guildId the ID of the Guild.
     * @param roleId  the ID of the Role.
     * @param level   the Level needed to get the Role.
     * @return {@link Boolean} as result if true, there is a role in our Database | if false, we couldn't find anything.
     */
    public boolean isChatLevelRewardSetup(String guildId, String roleId, long level) {
        return getEntity(new ChatAutoRole(), "SELECT * FROM ChatLevelAutoRoles WHERE GID=:gid AND RID=:rid AND LVL=:lvl", Map.of("gid", guildId, "rid", roleId, "lvl", level)) != null;
    }

    //endregion

    //region Voice Rewards

    /**
     * Get the all Voice Rewards saved in our Database from the given Guild.
     *
     * @param guildId the ID of the Guild.
     * @return {@link Map<>} as List with all Role IDs and the needed Level.
     */
    public Map<Long, String> getVoiceLevelRewards(String guildId) {

        // Create a new HashMap to save the Role Ids and their needed level.
        Map<Long, String> rewards = new HashMap<>();

        getEntityList(new VoiceAutoRole(), "SELECT * FROM VCLevelAutoRoles WHERE GID=:gid", Map.of("gid", guildId)).forEach(voiceAutoRole -> rewards.put(voiceAutoRole.getLevel(), voiceAutoRole.getRoleId()));

        // Return the HashMap.
        return rewards;
    }

    /**
     * Add a Voice Level Reward Role in our Database.
     *
     * @param guildId the ID of the Guild.
     * @param roleId  the ID of the Role.
     * @param level   the Level required to get this Role.
     */
    public void addVoiceLevelReward(String guildId, String roleId, long level) {

        // Check if there is a role in the database.
        if (!isVoiceLevelRewardSetup(guildId, roleId, level)) {
            // Add a new entry into the Database.
            updateEntity(new VoiceAutoRole(guildId, roleId, level));
        }
    }

    /**
     * Remove a Voice Level Reward Role from our Database.
     *
     * @param guildId the ID of the Guild.
     * @param level   the Level required to get this Role.
     */
    public void removeVoiceLevelReward(String guildId, long level) {
        VoiceAutoRole voiceAutoRole = getEntity(new VoiceAutoRole(), "SELECT * FROM VCLevelAutoRoles WHERE GID=:gid AND LVL=:lvl",
                Map.of("gid", guildId, "lvl", level));

        // Check if there is a role in the database.
        if (voiceAutoRole != null) {
            // Add a new entry into the Database.
            deleteEntity(voiceAutoRole);
        }
    }

    /**
     * Remove a Voice Level Reward Role from our Database.
     *
     * @param guildId the ID of the Guild.
     * @param roleId  the ID of the Role.
     * @param level   the Level required to get this Role.
     */
    public void removeVoiceLevelReward(String guildId, String roleId, long level) {
        VoiceAutoRole voiceAutoRole = getEntity(new VoiceAutoRole(), "SELECT * FROM VCLevelAutoRoles WHERE GID=:gid AND RID=:roleId AND LVL=:lvl",
                Map.of("gid", guildId, "rid", roleId, "lvl", level));

        // Check if there is a role in the database.
        if (voiceAutoRole != null) {
            // Add a new entry into the Database.
            deleteEntity(voiceAutoRole);
        }
    }

    /**
     * Check if a Voice Level Reward has been set in our Database for this Server.
     *
     * @param guildId the ID of the Guild.
     * @return {@link Boolean} as result if true, there is a role in our Database | if false, we couldn't find anything.
     */
    public boolean isVoiceLevelRewardSetup(String guildId) {
        return getEntity(new VoiceAutoRole(), "SELECT * FROM VCLevelAutoRoles WHERE GID=:gid", Map.of("gid", guildId)) != null;
    }

    /**
     * Check if a Voice Level Reward has been set in our Database for this Server.
     *
     * @param guildId the ID of the Guild.
     * @param roleId  the ID of the Role.
     * @return {@link Boolean} as result if true, there is a role in our Database | if false, we couldn't find anything.
     */
    public boolean isVoiceLevelRewardSetup(String guildId, String roleId) {
        return getEntity(new VoiceAutoRole(), "SELECT * FROM VCLevelAutoRoles WHERE GID=:gid AND RID=:rid", Map.of("gid", guildId, "rid", roleId)) != null;
    }

    /**
     * Check if a Voice Level Reward has been set in our Database for this Server.
     *
     * @param guildId the ID of the Guild.
     * @param roleId  the ID of the Role.
     * @param level   the Level needed to get the Role.
     * @return {@link Boolean} as result if true, there is a role in our Database | if false, we couldn't find anything.
     */
    public boolean isVoiceLevelRewardSetup(String guildId, String roleId, long level) {
        return getEntity(new VoiceAutoRole(), "SELECT * FROM VCLevelAutoRoles WHERE GID=:gid AND RID=:rid AND LVL=:lvl", Map.of("gid", guildId, "rid", roleId, "lvl", level)) != null;
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
    public List<Invite> getInvites(String guildId) {
        return getEntityList(new Invite(), "SELECT * FROM Invites WHERE GID=:gid", Map.of("gid", guildId));
    }

    /**
     * Check if the given Invite Data is saved in our Database.
     *
     * @param guildId       the ID of the Guild.
     * @param inviteCreator the ID of the Invite Creator.
     * @param inviteCode    the Code of the Invite.
     * @return {@link Boolean} as Result if true, then it's saved in our Database | if false, we couldn't find anything.
     */
    public boolean existsInvite(String guildId, String inviteCreator, String inviteCode) {
        return getEntity(new Invite(), "SELECT * FROM Invites WHERE GID=:gid AND UID=:uid AND CODE=:code", Map.of("gid", guildId, "uid", inviteCreator, "code", inviteCode)) != null;
    }

    /**
     * Remove an entry from our Database.
     *
     * @param guildId    the ID of the Guild.
     * @param inviteCode the Code of the Invite.
     */
    public void removeInvite(String guildId, String inviteCode) {
        deleteEntity(getInvite(guildId, inviteCode));
    }

    /**
     * Change the data of a saved Invite or create a new entry in our Database.
     *
     * @param guildId       the ID of the Guild.
     * @param inviteCreator the ID of the Invite Creator.
     * @param inviteCode    the Code of the Invite Code.
     * @param inviteUsage   the Usage count of the Invite.
     */
    public void setInvite(String guildId, String inviteCreator, String inviteCode, long inviteUsage) {
        updateEntity(new Invite(guildId, inviteCreator, inviteUsage, inviteCode));
    }

    /**
     * Get the Invite from our Database.
     *
     * @param guildId    the ID of the Guild.
     * @param inviteCode the Code of the Invite.
     * @return {@link Invite} as result if true, then it's saved in our Database | may be null.
     */
    public Invite getInvite(String guildId, String inviteCode) {
        return getEntity(new Invite(), "SELECT * FROM Invites WHERE GID=:gid AND CODE=:code", Map.of("gid", guildId, "code", inviteCode));
    }

    /**
     * Get the Invite from our Database.
     *
     * @param guildId       the ID of the Guild.
     * @param inviteCreator the ID of the Invite Creator.
     * @param inviteCode    the Code of the Invite.
     * @return {@link Invite} as result if true, then it's saved in our Database | may be null.
     */
    public Invite getInvite(String guildId, String inviteCreator, String inviteCode) {
        return getEntity(new Invite(), "SELECT * FROM Invites WHERE GID=:gid AND UID=:uid AND CODE=:code", Map.of("gid", guildId, "uid", inviteCreator, "code", inviteCode));
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
    public Invite getInvite(String guildId, String inviteCreator, String inviteCode, String inviteUsage) {
        return getEntity(new Invite(), "SELECT * FROM Invites WHERE GID=:gid AND UID=:uid AND CODE=:code AND USES=:uses",
                Map.of("gid", guildId, "uid", inviteCreator, "code", inviteCode, "uses", inviteUsage));
    }

    /**
     * Remove an entry from our Database.
     *
     * @param guildId       the ID of the Guild.
     * @param inviteCreator the ID of the Invite Creator.
     * @param inviteCode    the Code of the Invite.
     */
    public void removeInvite(String guildId, String inviteCreator, String inviteCode) {
        deleteEntity(getInvite(guildId, inviteCreator, inviteCode));
    }

    /**
     * Remove an entry from our Database.
     *
     * @param guildId       the ID of the Guild.
     * @param inviteCreator the ID of the Invite Creator.
     * @param inviteCode    the Code of the Invite.
     * @param inviteUsage   the usage count of the Invite.
     */
    public void removeInvite(String guildId, String inviteCreator, String inviteCode, int inviteUsage) {
        deleteEntity(getInvite(guildId, inviteCreator, inviteCode, String.valueOf(inviteUsage)));
    }

    /**
     * Remove all entries from our Database.
     *
     * @param guildId the ID of the Guild.
     */
    public void clearInvites(String guildId) {
        getInvites(guildId).forEach(this::deleteEntity);
    }

    //endregion

    //region Configuration

    //region Chat Protector / Word Blacklist

    /**
     * Get every Blacklisted Word saved in our Database from the Guild.
     *
     * @param guildId the ID of the Guild.
     * @return {@link List<String>} as list with every Blacklisted Word.
     */
    public List<String> getChatProtectorWords(String guildId) {
        return getEntityList(new Blacklist(), "SELECT * FROM ChatProtector WHERE GID = :gid", Map.of("gid", guildId)).stream().map(Blacklist::getWord).toList();
    }

    /**
     * Check if there is an entry in our Database for the wanted Guild.
     *
     * @param guildId the ID of the Guild.
     * @return {@link Boolean} as result. If true, there is an entry in our Database | If false, there is no entry in our Database.
     */
    public boolean isChatProtectorSetup(String guildId) {
        return getEntity(new Blacklist(), "SELECT * FROM ChatProtector WHERE GID = :gid", Map.of("gid", guildId)) != null;
    }

    /**
     * Check if there is an entry in our Database for the wanted Guild.
     *
     * @param guildId the ID of the Guild.
     * @param word    the Word that should be checked.
     * @return {@link Boolean} as result. If true, there is an entry in our Database | If false, there is no entry in our Database.
     */
    public boolean isChatProtectorSetup(String guildId, String word) {
        return getEntity(new Blacklist(), "SELECT * FROM ChatProtector WHERE GID = :gid AND WORD = :word", Map.of("gid", guildId, "word", word)) != null;
    }

    /**
     * Add a new Word to the blacklist for the given Guild.
     *
     * @param guildId the ID of the Guild.
     * @param word    the Word to be blocked.
     */
    public void addChatProtectorWord(String guildId, String word) {

        // Check if there is already an entry for it.
        if (isChatProtectorSetup(guildId, word)) return;

        // If not then just add it.
        updateEntity(new Blacklist(guildId, word));
    }

    /**
     * Remove a Word from the blacklist for the given Guild.
     *
     * @param guildId the ID of the Guild.
     * @param word    the Word to be removed.
     */
    public void removeChatProtectorWord(String guildId, String word) {
        Blacklist blacklist =
                getEntity(new Blacklist(), "SELECT * FROM ChatProtector WHERE GID = :gid AND WORD = :word",
                        Map.of("gid", guildId, "word", word));

        // Check if there is no entry for it.
        if (blacklist == null) return;

        // If so then delete it.
        deleteEntity(blacklist);
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
    public Setting getSetting(String guildId, String settingName) {
        Setting setting =
                getEntity(new Setting(), "SELECT * FROM Settings WHERE GID = :gid AND NAME = :name",
                        Map.of("gid", guildId, "name", settingName));

        // Check if there is an entry in the database.
        if (setting != null) {
            if (setting.getDisplayName() == null) {
                setting.setDisplayName(SettingsManager.getDefault(settingName).getDisplayName());
                updateEntity(setting);
            }
            return setting;
        } else {
            // Check if everything is alright with the config.
            checkSetting(guildId, settingName);

            Setting defaultSetting = SettingsManager.getDefault(settingName);
            defaultSetting.setGuildId(guildId);
            return defaultSetting;
        }
    }

    /**
     * Get the every Setting by the Guild.
     *
     * @param guildId the ID of the Guild.
     * @return {@link List<Setting>} which is a List with every Setting that stores every information needed.
     */
    public List<Setting> getAllSettings(String guildId) {
        return getEntityList(new Setting(), "SELECT * FROM Settings WHERE GID = :gid", Map.of("gid", guildId));
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

        Setting databaseSetting =
                getEntity(new Setting(), "SELECT * FROM Settings WHERE GID = :gid AND NAME = :name",
                        Map.of("gid", setting.getGuildId(), "name", setting.getName()));

        if (databaseSetting != null) {
            databaseSetting.setValue(setting.getValue());
            updateEntity(databaseSetting);
        } else {
            updateEntity(setting);
        }
    }

    /**
     * Set the Setting by the Guild and its Identifier.
     *
     * @param guildId      the ID of the Guild.
     * @param settingName  the Identifier of the Setting.
     * @param settingValue the Value of the Setting.
     */
    public void setSetting(String guildId, String settingName, String settingDisplayName, Object settingValue) {
        setSetting(new Setting(guildId, settingName, settingDisplayName, settingValue));
    }

    /**
     * Check if there is a Setting entry for the Guild.
     *
     * @param guildId the ID of the Guild.
     * @param setting the Setting itself.
     * @return {@link Boolean} as result. If true, there is a Setting Entry for the Guild | if false, there is no Entry for it.
     */
    public boolean hasSetting(String guildId, Setting setting) {
        return hasSetting(guildId, setting.getName());
    }

    /**
     * Check if there is a Setting entry for the Guild.
     *
     * @param guildId     the ID of the Guild.
     * @param settingName the Identifier of the Setting.
     * @return {@link Boolean} as result. If true, there is a Setting Entry for the Guild | if false, there is no Entry for it.
     */
    public boolean hasSetting(String guildId, String settingName) {
        return getEntity(new Setting(), "SELECT * FROM Settings WHERE GID =:gid AND NAME =:name", Map.of("gid", guildId, "name", settingName)) != null;
    }

    /**
     * Check if there is an entry for the Setting, if not create one for every Setting that doesn't have an entry.
     *
     * @param guildId the ID of the Guild.
     * @param setting the Setting itself.
     */
    public void checkSetting(String guildId, Setting setting) {
        checkSetting(guildId, setting.getName());
    }

    /**
     * Check if there is an entry for the Setting, if not create one for every Setting that doesn't have an entry.
     *
     * @param guildId     the ID of the Guild.
     * @param settingName the Identifier of the Setting.
     */
    public void checkSetting(String guildId, String settingName) {
        // Check if the Setting exists in our Database.
        if (!hasSetting(guildId, settingName)) {
            // If not then creat every Setting that doesn't exist for the Guild.
            createSettings(guildId);
        }
    }

    /**
     * Create Settings entries for the Guild
     *
     * @param guildId the ID of the Guild.
     */
    public void createSettings(String guildId) {
        SettingsManager.getSettings().forEach(setting -> {
            if (!hasSetting(guildId, setting)) {
                setSetting(guildId, setting.getName(), setting.getDisplayName(), setting.getValue());
            }
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
    public Statistics getStatisticsOfToday() {
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
    public Statistics getStatistics(int day, int month, int year) {
        return getEntity(new Statistics(), "SELECT * FROM Statistics WHERE DAY = :day AND MONTH = :month AND YEAR = :year", Map.of("day", day, "month", month, "year", year));
    }

    /**
     * Retrieve the Statistics of a month.
     *
     * @param month the month you want to receive the Statistics from.
     * @return all {@link Statistics} of the given month.
     */
    public List<Statistics> getStatisticsOfMonth(int month) {
        return getEntityList(new Statistics(), "SELECT * FROM Statistics WHERE MONTH = :month", Map.of("month", month));
    }

    /**
     * Update or add new/existing Statistics.
     *
     * @param statisticObject the {@link JsonObject} for the statistic.
     */
    public void updateStatistic(JsonObject statisticObject) {
        LocalDate today = LocalDate.now();
        Statistics statistics = getEntity(new Statistics(), "SELECT * FROM Statistics WHERE DAY = :day AND MONTH = :month AND YEAR = :year", Map.of("day", today.getDayOfMonth(), "month", today.getMonthValue(), "year", today.getYear()));
        if (statistics != null) {
            statistics.setStatsObject(statisticObject);
            updateEntity(statistics);
        } else {
            statistics = new Statistics(today.getDayOfMonth(), today.getMonthValue(), today.getYear(), statisticObject);
            updateEntity(statistics);
        }
    }

    /**
     * Get the Stats of the Command.
     *
     * @param command the Command.
     * @return the Stats of the Command.
     */
    public CommandStats getStatsCommandGlobal(String command) {
        return getEntity(new CommandStats(), "SELECT * FROM CommandStats WHERE COMMAND = :command", Map.of("command", command));
    }

    /**
     * Get the Stats of the Command in the specific Guild.
     *
     * @param guildId the ID of the Guild.
     * @param command the Command.
     * @return the Stats of the Command.
     */
    public GuildCommandStats getStatsCommand(String guildId, String command) {
        return getEntity(new GuildCommandStats(), "SELECT * FROM GuildStats WHERE GID = :gid AND COMMAND = :command", Map.of("gid", guildId, "command", command));
    }

    /**
     * Get all the Command-Stats related to the given Guild.
     *
     * @param guildId the ID of the Guild.
     * @return all the Command-Stats related to the given Guild.
     */
    public List<GuildCommandStats> getStats(String guildId) {
        return getEntityList(new GuildCommandStats(), "SELECT * FROM GuildStats WHERE GID=:gid ORDER BY CAST(uses as INT) DESC LIMIT 5", Map.of("gid", guildId));
    }

    /**
     * Get all the Command-Stats globally.
     *
     * @return all the Command-Stats globally.
     */
    public List<CommandStats> getStatsGlobal() {
        return getEntityList(new CommandStats(), "SELECT * FROM CommandStats ORDER BY CAST(uses as INT) DESC LIMIT 5", null);
    }

    /**
     * Check if there is any saved Stats for the given Guild.
     *
     * @param guildId the ID of the Guild.
     * @return {@link Boolean} as result. If true, there is data saved in the Database | If false, there is no data saved.
     */
    public boolean isStatsSaved(String guildId) {
        return getEntity(new GuildCommandStats(), "SELECT * FROM GuildStats WHERE GID = :gid ", Map.of("gid", guildId)) != null;
    }

    /**
     * Check if there is any saved Stats for the given Guild and Command.
     *
     * @param guildId the ID of the Guild.
     * @param command the Name of the Command.
     * @return {@link Boolean} as result. If true, there is data saved in the Database | If false, there is no data saved.
     */
    public boolean isStatsSaved(String guildId, String command) {
        return getEntity(new GuildCommandStats(), "SELECT * FROM GuildStats WHERE GID = :gid AND COMMAND = :command", Map.of("gid", guildId, "command", command)) != null;
    }

    /**
     * Check if there is any saved Stats for the given Command.
     *
     * @param command the Name of the Command.
     * @return {@link Boolean} as result. If true, there is data saved in the Database | If false, there is no data saved.
     */
    public boolean isStatsSavedGlobal(String command) {
        return getEntity(new CommandStats(), "SELECT * FROM CommandStats WHERE COMMAND = :command", Map.of("command", command)) != null;
    }

    /**
     * Save the Stats of the Command in the Database.
     *
     * @param guildId the ID of the Guild.
     * @param command the Command.
     */
    public void addStats(String guildId, String command) {
        Statistics statistics = getStatisticsOfToday();
        JsonObject jsonObject = statistics != null ? statistics.getStatsObject() : new JsonObject();
        JsonObject commandStats = statistics != null && jsonObject.has("command") ? jsonObject.getAsJsonObject("command") : new JsonObject();

        if (commandStats.has(command) && commandStats.get(command).isJsonPrimitive()) {
            commandStats.addProperty(command, commandStats.getAsJsonPrimitive(command).getAsInt() + 1);
        } else {
            commandStats.addProperty(command, 1);
        }

        jsonObject.add("command", commandStats);

        sqlConnector.getSqlWorker().updateStatistic(jsonObject);

        // Check if there is an entry.
        if (isStatsSaved(guildId, command)) {
            GuildCommandStats newGuildStats = getStatsCommand(guildId, command);
            newGuildStats.setUses(newGuildStats.getUses() + 1);
            updateEntity(newGuildStats);
        } else {
            updateEntity(new GuildCommandStats(0, guildId, command, 1));
        }

        // Check if there is an entry.
        if (isStatsSavedGlobal(command)) {
            CommandStats stats = getStatsCommandGlobal(command);
            stats.setUses(stats.getUses() + 1);
            updateEntity(stats);
        } else {
            updateEntity(new CommandStats(command, 1));
        }
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
    public boolean isOptOut(String guildId, String userId) {
        // Creating a SQL Statement to check if there is an entry in the Opt-out Table by the Guild Id and User Id
        return getEntity(new OptOut(), "SELECT * FROM Opt_out WHERE GID=:gid AND UID=:uid", Map.of("gid", guildId, "uid", userId)) != null;
    }

    /**
     * Opt a User out of the given Guild.
     *
     * @param guildId the ID of the Guild.
     * @param userId  the ID of the User.
     */
    public void optOut(String guildId, String userId) {
        if (!isOptOut(guildId, userId)) {
            updateEntity(new OptOut(guildId, userId));
        }
    }

    /**
     * Opt in a User to the given Guild.
     *
     * @param guildId the ID of the Guild.
     * @param userId  the ID of the User.
     */
    public void optIn(String guildId, String userId) {
        OptOut optOut = getEntity(new OptOut(), "SELECT * FROM Opt_out WHERE GID=:gid AND UID=:uid",
                Map.of("gid", guildId, "uid", userId));

        if (optOut != null) {
            deleteEntity(optOut);
        }
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
    public void addBirthday(String guildId, String channelId, String userId, String birthday) {
        try {
            BirthdayWish newBirthday = new BirthdayWish(guildId, channelId, userId, new SimpleDateFormat("dd.MM.yyyy").parse(birthday));
            updateEntity(newBirthday);
        } catch (Exception exception) {
            Sentry.captureException(exception);
        }
    }

    /**
     * Check if there is any saved birthday for the given User.
     *
     * @param guildId the ID of the Guild.
     * @param userId  the ID of the User.
     */
    public void removeBirthday(String guildId, String userId) {
        BirthdayWish birthdayWish = getEntity(new BirthdayWish(), "SELECT * FROM BirthdayWish WHERE GID=:gid AND UID=:uid", Map.of("gid", guildId, "uid", userId));
        if (birthdayWish != null) {
            deleteEntity(birthdayWish);
        }
    }

    /**
     * Check if a birthday is saved for the given User.
     *
     * @param guildId the ID of the Guild.
     * @param userId  the ID of the User.
     * @return {@link Boolean} as result. If true, there is data saved in the Database | If false, there is no data saved.
     */
    public boolean isBirthdaySaved(String guildId, String userId) {
        return getEntity(new BirthdayWish(), "SELECT * FROM BirthdayWish WHERE GID=:gid AND UID=:uid", Map.of("gid", guildId, "uid", userId)) != null;
    }

    /**
     * Get the birthday of the given User.
     *
     * @param guildId the ID of the Guild.
     * @param userId  the ID of the User.
     * @return {@link BirthdayWish} as result. If true, there is data saved in the Database | If false, there is no data saved.
     */
    public BirthdayWish getBirthday(String guildId, String userId) {
        return getEntity(new BirthdayWish(), "SELECT * FROM BirthdayWish WHERE GID=:gid AND UID=:uid", Map.of("gid", guildId, "uid", userId));
    }

    /**
     * Get all saved birthdays.
     *
     * @param guildId the ID of the Guild.
     * @return {@link List} of {@link BirthdayWish} as result. If true, there is data saved in the Database | If false, there is no data saved.
     */
    public List<BirthdayWish> getBirthdays(String guildId) {
        return getEntityList(new BirthdayWish(), "SELECT * FROM BirthdayWish WHERE GID=:gid", Map.of("gid", guildId));
    }

    /**
     * Get all saved birthdays.
     *
     * @return {@link List} of {@link BirthdayWish} as result. If true, there is data saved in the Database | If false, there is no data saved.
     */
    public List<BirthdayWish> getBirthdays() {
        return getEntityList(new BirthdayWish(), "SELECT * FROM BirthdayWish", Map.of());
    }

    //endregion

    //region Data delete

    /**
     * Delete Data saved in our Database by the given Guild ID.
     *
     * @param guildId the ID of the Guild.
     */
    public void deleteAllData(String guildId) {
        Set<Class<?>> classSet = new Reflections(
                ConfigurationBuilder
                        .build()
                        .forPackage("de.presti.ree6.webinterface.sql.entities", ClasspathHelper.staticClassLoader()))
                .getTypesAnnotatedWith(Table.class);
        for (Class<?> clazz : classSet) {
            if (clazz.isAnnotationPresent(Table.class)) {
                Table table = clazz.getAnnotation(Table.class);
                sqlConnector.querySQL("DELETE FROM " + table.name() + " WHERE GID=?", guildId);
                sqlConnector.querySQL("DELETE FROM " + table.name() + " WHERE GUILDID=?", guildId);
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
    public <R> R updateEntity(R r) {
        if (r == null) return null;

        if (!sqlConnector.isConnected()) {
            if (sqlConnector.connectedOnce()) {
                sqlConnector.connectToSQLServer();
                return updateEntity(r);
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
                        return updateEntity(r);
                    }
                }
            } else {
                sqlConnector.setConnectedSecond(false);
            }

            throw exception;
        } catch (Exception exception) {
            Sentry.captureException(exception);
            throw exception;
        }
    }

    /**
     * Delete an entity from the database
     *
     * @param <R> The Class-Entity.
     * @param r   The Class-Entity to delete.
     */
    public <R> void deleteEntity(R r) {
        if (r == null) return;

        if (!sqlConnector.isConnected()) {
            if (sqlConnector.connectedOnce()) {
                sqlConnector.connectToSQLServer();
                deleteEntity(r);
            }
        }

        try (Session session = SQLSession.getSessionFactory().openSession()) {

            session.beginTransaction();

            session.remove(r);

            session.getTransaction().commit();
        } catch (Exception exception) {
            Sentry.captureException(exception);
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
    public <R> List<R> getEntityList(@NotNull R r, @NotNull String sqlQuery, @Nullable Map<String, Object> parameters) {
        return getEntityList(r, sqlQuery, parameters, true);
    }

    /**
     * Constructs a new mapped Version of the Entity-class.
     *
     * @param <R>            The Class-Entity.
     * @param r              The Class-Entity to get.
     * @param sqlQuery       the SQL-Query.
     * @param parameters     all parameters.
     * @param useNativeQuery if true, use native query, else use hibernate query.
     * @return The mapped entity.
     */
    public <R> List<R> getEntityList(@NotNull R r, @NotNull String sqlQuery, @Nullable Map<String, Object> parameters, boolean useNativeQuery) {
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

            return query.getResultList();
        } catch (Exception exception) {
            Sentry.captureException(exception);
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
    public <R> R getEntity(@NotNull R r, @NotNull String sqlQuery, @Nullable Map<String, Object> parameters) {
        return getEntity(r, sqlQuery, parameters, true);
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
     */
    public <R> R getEntity(@NotNull R r, @NotNull String sqlQuery, @Nullable Map<String, Object> parameters, boolean useNativeQuery) {
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
            throw exception;
        }
    }

    //endregion
}
