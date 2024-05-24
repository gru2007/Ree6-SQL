package de.presti.ree6.sql.util;

import de.presti.ree6.sql.SQLSession;
import de.presti.ree6.sql.entities.level.ChatUserLevel;
import de.presti.ree6.sql.entities.level.UserLevel;
import de.presti.ree6.sql.entities.level.VoiceUserLevel;
import io.sentry.Sentry;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

/**
 * Utility used to determine data for levels.
 * The current implementation works using these formulas.
 * Level: x * root(XP)
 * XP: (level/x) ^ y
 * <p>
 * Based on the Implementation of <a href="https://gist.github.com/JakeSteam/4d843cc69dff4275acd742b70d4523b6">JakeSteam</a>!
 * Thanks to his great explanation in his <a href="https://blog.jakelee.co.uk/converting-levels-into-xp-vice-versa">blog post</a>!
 */
@Slf4j
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
     * Get the total Experience for the Level.
     *
     * @param level The level.
     * @param userLevel the UserLevel.
     * @return the total Experience.
     */
    public static long getTotalExperienceForLevel(long level, UserLevel userLevel) {
        if (level == 0) return 0;
        float[] values = getLevelingValues(userLevel);

        return (long) Math.pow(level / values[0], values[1]);
    }

    /**
     * Get the needed Experience for the Level.
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
            return new float[] { 0.07f, 2 };
        }
    }

    /**
     * Get the current Progress of the User.
     *
     * @param userLevel the UserLevel.
     * @return the Progress.
     */
    public static double getProgress(UserLevel userLevel) {
        double currentLevelXP = (double)getTotalExperienceForLevel(userLevel.getLevel(), userLevel);
        double nextLevelXP = (double)getTotalExperienceForLevel(userLevel.getLevel() + 1, userLevel);

        double neededXP = nextLevelXP - currentLevelXP;
        double earnedXP = nextLevelXP - userLevel.getExperience();

        if (neededXP <= 0) return 0;

        return 100 - Math.ceil((earnedXP / neededXP) * 100);
    }

    /**
     * Retrieve the current Rank of a User.
     * @param userLevel the UserLevel.
     * @return the current Rank.
     */
    public static int getCurrentRank(UserLevel userLevel) {
        try {
            // TODO:: ASYNC
            if (userLevel instanceof ChatUserLevel) {
                return SQLSession.getSqlConnector().getSqlWorker().getAllChatLevelSorted(userLevel.getGuildId()).block(Duration.of(5, ChronoUnit.SECONDS)).indexOf(userLevel.getUserId()) + 1;
            } else if (userLevel instanceof VoiceUserLevel) {
                return SQLSession.getSqlConnector().getSqlWorker().getAllVoiceLevelSorted(userLevel.getGuildId()).block(Duration.of(5, ChronoUnit.SECONDS)).indexOf(userLevel.getUserId()) + 1;
            }
        } catch (Exception e) {
            log.error("Error while getting current Rank of UserLevel!", e);
            Sentry.captureException(e);
        }

        return 0;
    }
}
