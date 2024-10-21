package de.presti.ree6.sql.entities;


import de.presti.ree6.sql.keys.GuildAndName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;

/**
 * File to store Settings information.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Settings", indexes = @Index(columnList = "name, guildId"))
public class Setting {

    /**
     * The Keys related to the Settings.
     */
    @EmbeddedId
    private GuildAndName settingId;

    /**
     * The Display Name of the Setting.
     */
    @Column(name = "displayname")
    private String displayName;

    /**
     * The value of the Setting.
     */
    @Column(name = "value")
    @JdbcTypeCode(value = Types.LONGNVARCHAR)
    private String value;


    /**
     * Constructor for the Setting.
     *
     * @param name        the Name / Identifier of the Setting.
     * @param displayName the Display Name of the Setting.
     * @param value       the Value of the Setting.
     */
    public Setting(long guildId, String name, String displayName, Object value) {
        if (settingId == null) this.settingId = new GuildAndName(guildId, name);
        this.displayName = displayName;
        this.value = String.valueOf(value);
    }

    /**
     * Get the Value as Boolean.
     *
     * @return the Value as {@link Boolean}
     */
    public boolean getBooleanValue() {
        Object currentValue = getValue();
        if (currentValue instanceof Boolean booleanValue) {
            return booleanValue;
        } else if (currentValue instanceof String stringValue) {
            if (stringValue.equals("1")) return true;
            if (stringValue.equals("0")) return false;
            return Boolean.parseBoolean(stringValue);
        }
        return true;
    }

    /**
     * Get the Value as String.
     *
     * @return Value as {@link String}
     */
    public String getStringValue() {
        Object currentValue = getValue();
        if (currentValue instanceof String stringValue) {
            return stringValue;
        } else if (getName().equalsIgnoreCase("chatprefix")) {
            return "ree!";
        } else if (currentValue instanceof Boolean booleanValue) {
            return booleanValue + "";
        }
        return "";
    }

    /**
     * Set the ID of the Guild.
     * @param guildId ID of the Guild as {@link Long}
     */
    public void setGuildId(long guildId) {
        if (getSettingId() == null) this.settingId = new GuildAndName();
        else this.settingId.setGuildId(guildId);
    }

    /**
     * Set the Name of the Setting.
     * @param name Name of the Setting as {@link String}
     */
    public void setName(String name) {
        if (getSettingId() == null) this.settingId = new GuildAndName();
        else this.settingId.setName(name);
    }

    /**
     * Get the ID of the Guild.
     * @return ID of the Guild as {@link Long}
     */
    public long getGuildId() {
        if (getSettingId() == null) return -1;

        return getSettingId().getGuildId();
    }

    /**
     * Get the Name of the String.
     * @return Name as {@link String}
     */
    public String getName() {
        if (getSettingId() == null) return "";

        return getSettingId().getName();
    }

    /**
     * Get the Value as Object.
     *
     * @return Value as {@link Object}
     */
    public Object getValue() {
        return value;
    }

    /**
     * Change the Value Object of the Setting.
     *
     * @param value new Value as {@link Object}
     */
    public void setValue(Object value) {
        this.value = String.valueOf(value);
    }
}