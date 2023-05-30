package de.presti.ree6.sql.entities.custom;

/**
 * The Typ of the Custom Event.
 */
public enum CustomEventTyp {

    /**
     * Event when a member joins the guild.
     */
    MEMBER_JOIN,

    /**
     * Event when a member leaves the guild.
     */
    MEMBER_LEAVE,

    /**
     * Event when a member joins a voice channel.
     */
    VOICE_JOIN,

    /**
     * Event when a member leaves a voice channel.
     */
    VOICE_LEAVE,

    /**
     * Event when a member moves to another voice channel.
     */
    VOICE_MOVE,

    /**
     * Event when a member sends a message.
     */
    MESSAGE,

    /**
     * Event when a member deletes a message.
     */
    MESSAGE_DELETE,

    /**
     * Event when a member edits a message.
     */
    MESSAGE_UPDATE,

    /**
     * Event when a member reacts to a message.
     */
    REACTION_ADD,

    /**
     * Event when a member removes a reaction from a message.
     */
    REACTION_REMOVE,

    /**
     * Event when a member got banned.
     */
    MEMBER_BAN,

    /**
     * Event when a member got unbanned.
     */
    MEMBER_UNBAN,

    /**
     * Event when a member got kicked.
     */
    MEMBER_KICK,

    /**
     * Event when a role has been created.
     */
    ROLE_CREATE,

    /**
     * Event when a role has been deleted.
     */
    ROLE_DELETE,

    /**
     * Event when a role has been updated.
     */
    ROLE_UPDATE,

    /**
     * Event when a channel has been created.
     */
    CHANNEL_CREATE,

    /**
     * Event when a channel has been deleted.
     */
    CHANNEL_DELETE,

    /**
     * Event when a channel has been updated.
     */
    CHANNEL_UPDATE,

    /**
     * Event when the pins in a channel has been modified.
     */
    CHANNEL_PINS_UPDATE,

    /**
     * Event when a webhook has been created/updated.
     */
    WEBHOOKS_UPDATE,
}
