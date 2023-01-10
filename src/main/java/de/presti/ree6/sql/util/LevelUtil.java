package de.presti.ree6.sql.util;

import de.presti.ree6.sql.SQLSession;
import de.presti.ree6.sql.entities.level.ChatUserLevel;
import de.presti.ree6.sql.entities.level.UserLevel;
import de.presti.ree6.sql.entities.level.VoiceUserLevel;

/**
 * Utility used to determine data for levels.
 * The current implementation works using these formulas.
 * Level: x * root(XP)
 * XP: (level/x) ^ y
 */
public class LevelUtil {

    /**
     * Calculate on which Level you would be by your experience.
     *
     * @param userLevel the userLevel.
     * @return which Level.
     */
    public static long calculateLevel(UserLevel userLevel) {
        return calculateLevel(userLevel, userLevel.getExperience());
    }

    /**
     * Calculate on which Level you would be by your experience.
     *
     * @param userLevel the UserLevel.
     * @param experience the experience.
     * @return which Level.
     */
    public static long calculateLevel(UserLevel userLevel, long experience) {
        if (experience == 0) return 0;
        return (long)(getLevelingValues(userLevel)[0] * Math.sqrt(experience));
    }

    /**
     * Get the needed Experience for the next Level.
     *
     * @param level The level.
     * @param userLevel the UserLevel.
     * @return the needed Experience.
     */
    public static long getTotalExperienceForLevel(long level, UserLevel userLevel) {
        if (level == 0) return 0;
        float[] values = getLevelingValues(userLevel);

        return (long)((level + 1) / values[0]) ^ (long)values[1];
    }

    /**
     * Get the needed Experience for the next Level.
     *
     * @param level The level.
     * @param userLevel the UserLevel.
     * @return the needed Experience.
     */
    public static long getExperienceNeededForLevel(long level, UserLevel userLevel) {
        if (level < userLevel.getLevel()) {
            return userLevel.getExperience() - getTotalExperienceForLevel(level, userLevel);
        }
        return getTotalExperienceForLevel(level, userLevel) - userLevel.getExperience();
    }

    /**
     * Get the Leveling Values for the UserLevel.
     *
     * @param userLevel the UserLevel.
     * @return the Leveling Values.
     */
    public static float[] getLevelingValues(UserLevel userLevel) {
        if (userLevel instanceof ChatUserLevel) {
            return new float[] { 0.1f, 2 };
        } else {
            return new float[] { 0.08f, 2 };
        }
    }

    /**
     * Get the current Progress of the User.
     *
     * @param userLevel the UserLevel.
     * @return the Progress.
     */
    public static double getProgress(UserLevel userLevel) {
        return (int) ((userLevel.getExperience() * 100) / getTotalExperienceForLevel(userLevel.getLevel() + 1, userLevel));
    }

    /**
     * Retrieve the current Rank of a User.
     * @param userLevel the UserLevel.
     * @return the current Rank.
     */
    public static int getCurrentRank(UserLevel userLevel) {
        if (userLevel instanceof ChatUserLevel) {
            return SQLSession.getSqlConnector().getSqlWorker().getAllChatLevelSorted(userLevel.getGuildId()).indexOf(userLevel.getUserId()) + 1;
        } else if (userLevel instanceof VoiceUserLevel) {
            return SQLSession.getSqlConnector().getSqlWorker().getAllVoiceLevelSorted(userLevel.getGuildId()).indexOf(userLevel.getUserId()) + 1;
        }

        return 0;
    }
}
