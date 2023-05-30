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
                new Setting("-1", "chatprefix", "Prefix", "ree!"),
                new Setting("-1", "level_message", "Send Levelup Message", false),
                new Setting("-1", "configuration_news", "Receive News", true),
                new Setting("-1", "configuration_language", "Language", "en-GB"),
                new Setting("-1", "message_join", "Welcome Message", "Welcome %user_mention%!\nWe wish you a great stay on %guild_name%"),
                new Setting("-1", "message_join_image", "Welcome Image", ""),
                new Setting("-1", "logging_invite", "Log Invites", true),
                new Setting("-1", "logging_memberjoin", "Log Member joins", true),
                new Setting("-1", "logging_memberleave", "Log Member leave", true),
                new Setting("-1", "logging_memberban", "Log Member ban", true),
                new Setting("-1", "logging_memberunban", "Log Member unban", true),
                new Setting("-1", "logging_nickname", "Log Member Nickname change", true),
                new Setting("-1", "logging_voicejoin", "Log Member voice channel join", true),
                new Setting("-1", "logging_voicemove", "Log Member voice channel move", true),
                new Setting("-1", "logging_voiceleave", "Log Member voice channel leave", true),
                new Setting("-1", "logging_roleadd", "Log Member role receive", true),
                new Setting("-1", "logging_roleremove", "Log Member role removed", true),
                new Setting("-1", "logging_voicechannel", "Log voice channel changes", true),
                new Setting("-1", "logging_textchannel", "Log text channel changes", true),
                new Setting("-1", "logging_rolecreate", "Log role creation", true),
                new Setting("-1", "logging_roledelete", "Log role deletion", true),
                new Setting("-1", "logging_rolename", "Log role name changing", true),
                new Setting("-1", "logging_rolemention", "Log role mention changing", true),
                new Setting("-1", "logging_rolehoisted", "Log role hoisted changing", true),
                new Setting("-1", "logging_rolepermission", "Log role permission changing", true),
                new Setting("-1", "logging_rolecolor", "Log role color changing", true),
                new Setting("-1", "logging_messagedelete", "Log message delete", true),
                new Setting("-1", "logging_timeout", "Log Member timeouts", true)));
    }

    /**
     * Retrieve the default Setting by name.
     *
     * @param settingName The name of the Setting.
     * @return The Setting.
     */
    public static Setting getDefault(String settingName) {
        return settings.stream().filter(s -> s.getName().equalsIgnoreCase(settingName)).findFirst().orElse(null);
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
