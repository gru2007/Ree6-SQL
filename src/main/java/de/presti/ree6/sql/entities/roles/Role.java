package de.presti.ree6.sql.entities.roles;

import de.presti.ree6.sql.keys.GuildRoleId;
import jakarta.persistence.*;

/**
 * Role class to store data about roles.
 */
@MappedSuperclass
public class Role {

    /**
     * The ID of the Entity.
     */
    @EmbeddedId
    private GuildRoleId guildRoleId;

    /**
     * Constructor.
     */
    public Role() {
    }

    /**
     * Constructor.
     * @param guildId the GuildID of the Role.
     * @param roleId the ID of the Role.
     */
    public Role(long guildId, long roleId) {
        guildRoleId = new GuildRoleId(guildId, roleId);
    }

    /**
     * Get the GuildID of the Role.
     * @return {@link String} as GuildID.
     */
    public long getGuildId() {
        if (guildRoleId == null) {
            return 0;
        }

        return guildRoleId.getGuildId();
    }

    /**
     * Get the ID of the Role.
     * @return {@link String} as ID.
     */
    public long getRoleId() {
        if (guildRoleId == null) {
            return 0;
        }

        return guildRoleId.getRoleId();
    }
}
