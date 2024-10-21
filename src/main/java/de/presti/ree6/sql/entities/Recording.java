package de.presti.ree6.sql.entities;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import de.presti.ree6.sql.converter.JsonToBlobAttributeConverter;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.security.SecureRandom;
import java.sql.Types;
import java.util.Base64;

/**
 * This class is used to represent a Ree6-Voice-Recording, in our Database.
 */
@Entity
@Table(name = "Recording", indexes = @Index(columnList = "id, guildId"))
public class Recording {

    /**
     * The Identifier for the recording.
     */
    @Id
    @Column(name = "id")
    String identifier;

    /**
     * The ID of the Guild.
     */
    @Column(name = "guildId")
    long guildId;

    /**
     * The ID of the Voice-Channel.
     */
    @Column(name = "channelId")
    long voiceId;

    /**
     * The ID of the Creator.
     */
    @Column(name = "creator")
    long creatorId;

    /**
     * The WAV-File bytes.
     */
    @Lob
    @JdbcTypeCode(value = Types.BLOB)
    @Column(name = "recording", length = 157286400)
    byte[] recording;

    /**
     * An JsonArray containing the IDs of the Users who have participated in the Recording.
     */
    @JdbcTypeCode(value = Types.LONGVARBINARY)
    @Convert(converter = JsonToBlobAttributeConverter.class)
    @Column(name = "participants")
    JsonElement jsonArray;

    /**
     * Value used to tell us when this entry was made.
     */
    @Column(name = "created")
    long creation;

    /**
     * Constructor
     */
    public Recording() {
    }

    /**
     * Constructor.
     * @param guildId the ID of the Guild.
     * @param voiceId the ID of the Voice-Channel.
     * @param creatorId the ID of the Creator.
     * @param recording the WAV-File bytes.
     * @param jsonArray an JsonArray containing the IDs of the Users who have participated in the Recording.
     */
    public Recording(long guildId, long voiceId, long creatorId, byte[] recording, JsonElement jsonArray) {

        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[16];
        secureRandom.nextBytes(randomBytes);

        this.identifier = Base64.getUrlEncoder().encodeToString(randomBytes);
        this.guildId = guildId;
        this.voiceId = voiceId;
        this.creatorId = creatorId;
        this.recording = recording;
        this.jsonArray = jsonArray;
        this.creation = System.currentTimeMillis();
    }

    /**
     * Get the Identifier for the recording.
     * @return the Identifier for the recording.
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Get the ID of the Guild.
     * @return the ID of the Guild.
     */
    public long getGuildId() {
        return guildId;
    }

    /**
     * Get the ID of the Voice-Channel.
     * @return the ID of the Voice-Channel.
     */
    public long getVoiceId() {
        return voiceId;
    }

    /**
     * Get the ID of the Creator.
     * @return the ID of the Creator.
     */
    public long getCreatorId() {
        return creatorId;
    }

    /**
     * Get the WAV-File bytes.
     * @return the WAV-File bytes.
     */
    public byte[] getRecording() {
        return recording;
    }

    /**
     * Get the IDs of the Users who have participated in the Recording.
     * @return the IDs of the Users who have participated in the Recording.
     */
    public JsonArray getJsonArray() {
        return jsonArray.getAsJsonArray();
    }

    /**
     * Get the creation time of this entry.
     * @return the creation time of this entry.
     */
    public long getCreation() {
        return creation;
    }
}
