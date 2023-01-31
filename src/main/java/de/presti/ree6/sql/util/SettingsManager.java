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
                new Setting("-1","chatprefix", "ree!"),
                new Setting("-1", "level_message", false),
                new Setting("-1", "configuration_news", true),
                new Setting("-1","configuration_language", "en-GB"),
                new Setting("-1", "message_join", "Welcome %user_mention%!\nWe wish you a great stay on %guild_name%"),
                new Setting("-1", "message_join_image", ""),
                new Setting("-1", "logging_invite", true),
                new Setting("-1", "logging_memberjoin", true),
                new Setting("-1", "logging_memberleave", true),
                new Setting("-1", "logging_memberban", true),
                new Setting("-1", "logging_memberunban", true),
                new Setting("-1", "logging_nickname", true),
                new Setting("-1", "logging_voicejoin", true),
                new Setting("-1", "logging_voicemove", true),
                new Setting("-1", "logging_voiceleave", true),
                new Setting("-1", "logging_roleadd", true),
                new Setting("-1", "logging_roleremove", true),
                new Setting("-1", "logging_voicechannel", true),
                new Setting("-1", "logging_textchannel", true),
                new Setting("-1", "logging_rolecreate", true),
                new Setting("-1", "logging_roledelete", true),
                new Setting("-1", "logging_rolename", true),
                new Setting("-1", "logging_rolemention", true),
                new Setting("-1", "logging_rolehoisted", true),
                new Setting("-1", "logging_rolepermission", true),
                new Setting("-1", "logging_rolecolor", true),
                new Setting("-1", "logging_messagedelete", true),
                new Setting("-1", "logging_timeout", true)));
    }

    /**
     * Retrieve the default Setting by name.
     * @param settingName The name of the Setting.
     * @return The Setting.
     */
    public static Setting getDefault(String settingName) {
        return settings.stream().filter(s -> s.getName().equalsIgnoreCase(settingName)).findFirst().orElse(null);
    }
}
