package de.presti.ree6.sql.util;

import de.presti.ree6.sql.entities.Setting;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to manage the Settings for the SQL-Worker.
 */
public class SettingsManager {

    /**
     * The List of all Settings.
     */
    @Getter(AccessLevel.PUBLIC)
    private static List<Setting> settings;

    /**
     * Constructor called to add all settings to the list.
     */
    public static void loadDefaults() {
        if (settings == null)
            settings = new ArrayList<>();

        settings.clear();
        // TODO:: create an structure. (configuration_,logging_,message_)
        settings.addAll(List.of(
                new Setting(-1, "chatprefix", "Prefix", "ree!"),
                new Setting(-1, "level_message", "Send Levelup Message", false),
                new Setting(-1, "configuration_news", "Receive News", true),
                new Setting(-1, "configuration_language", "Language", "en-GB"),
                new Setting(-1, "message_join", "Welcome Message", "Welcome %user_mention%!\nWe wish you a great stay on %guild_name%"),
                new Setting(-1, "message_join_image", "Welcome Image", ""),
                new Setting(-1, "logging_invite", "Log Invites", true),
                new Setting(-1, "logging_memberjoin", "Log Member joins", true),
                new Setting(-1, "logging_memberleave", "Log Member leave", true),
                new Setting(-1, "logging_memberban", "Log Member ban", true),
                new Setting(-1, "logging_memberunban", "Log Member unban", true),
                new Setting(-1, "logging_nickname", "Log Member Nickname change", true),
                new Setting(-1, "logging_voicejoin", "Log Member voice channel join", true),
                new Setting(-1, "logging_voicemove", "Log Member voice channel move", true),
                new Setting(-1, "logging_voiceleave", "Log Member voice channel leave", true),
                new Setting(-1, "logging_roleadd", "Log Member role receive", true),
                new Setting(-1, "logging_roleremove", "Log Member role removed", true),
                new Setting(-1, "logging_voicechannel", "Log voice channel changes", true),
                new Setting(-1, "logging_textchannel", "Log text channel changes", true),
                new Setting(-1, "logging_rolecreate", "Log role creation", true),
                new Setting(-1, "logging_roledelete", "Log role deletion", true),
                new Setting(-1, "logging_rolename", "Log role name changing", true),
                new Setting(-1, "logging_rolemention", "Log role mention changing", true),
                new Setting(-1, "logging_rolehoisted", "Log role hoisted changing", true),
                new Setting(-1, "logging_rolepermission", "Log role permission changing", true),
                new Setting(-1, "logging_rolecolor", "Log role color changing", true),
                new Setting(-1, "logging_messagedelete", "Log message delete", true),
                new Setting(-1, "logging_timeout", "Log Member timeouts", true),
                new Setting(-1, "configuration_rewards_blackjack_win", "Payment Amount on BlackJack win", 200.0),
                new Setting(-1, "configuration_rewards_musicquiz_win", "Payment Amount on Music Quiz win", 200.0),
                new Setting(-1, "configuration_rewards_musicquiz_feature", "Payment Amount on Music Quiz Feature guess", 100.0),
                new Setting(-1, "configuration_rewards_musicquiz_artist", "Payment Amount on Music Quiz Feature guess", 50.0),
                new Setting(-1, "configuration_rewards_musicquiz_title", "Payment Amount on Music Quiz Title guess", 25.0),
                new Setting(-1, "configuration_work_min", "Minimum received Payment for work", 10.0),
                new Setting(-1, "configuration_work_max", "Maximum received Payment for work", 50.0),
                new Setting(-1, "configuration_work_delay", "Delay between each work", 5L),
                new Setting(-1, "configuration_steal_delay", "Delay between each steal", 5L),
                new Setting(-1, "message_ticket_menu", "Message that should display in the Ticket Menu.", "By clicking on the Button below you can open a Ticket!"),
                new Setting(-1, "message_ticket_open", "Message that should display when a Ticket is opened.", "Welcome to your Ticket!"),
                new Setting(-1, "message_suggestion_menu", "Message that should display in the Suggestion Menu.", "Suggest something"),
                new Setting(-1, "configuration_autopublish", "Automatically publish News messages.", false)
                ));
    }

    /**
     * Add a Setting to the List.
     * @param setting The Setting to add.
     */
    public static void addSetting(Setting setting) {
        setting.setGuildId(-1);
        settings.add(setting);
    }

    /**
     * Retrieve the default Setting by name.
     *
     * @param settingName The name of the Setting.
     * @return The Setting.
     */
    public static Setting getDefault(String settingName) {
        return settings.stream().filter(s -> s.getSettingId().getName().equalsIgnoreCase(settingName)).findFirst().orElse(null);
    }

    /**
     * Retrieve the default value of the Setting by name.
     * @param settingName The name of the Setting.
     * @return The value of the Setting.
     */
    public static Object getDefaultValue(String settingName) {
        return getDefault(settingName).getValue();
    }

    /**
     * Retrieve the default display name of the Setting by name.
     * @param settingName The name of the Setting.
     * @return The display name of the Setting.
     */
    public static String getDefaultDisplayName(String settingName) {
        return getDefault(settingName).getDisplayName();
    }
}
