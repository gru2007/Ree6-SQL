package de.presti.ree6.sql.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// TODO:: add a display name.

/**
 * File to store Settings information.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Settings")
public class Setting {

    /**
     * The PrimaryKey of the Entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /**
     * The ID of the Guild.
     */
    @Column(name = "gid")
    private String guildId;

    /**
     * Name / Identifier of the Setting.
     */
    @Column(name = "name")
    private String name;

    /**
     * The Display Name of the Setting.
     */
    @Column(name = "displayname")
    private String displayName;

    /**
     * The value of the Setting.
     */
    @Column(name = "value", columnDefinition = "LONGVARCHAR")
    private String value;


    /**
     * Constructor for the Setting.
     *
     * @param guildId the GuildID of the Setting.
     * @param name    the Name / Identifier of the Setting.
     * @param value   the Value of the Setting.
     */
    public Setting(String guildId, String name, String displayName, Object value) {
        this.guildId = guildId;
        this.name = name;
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