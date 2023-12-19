package de.presti.ree6.sql.entities.roles;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * SQL Entity for the Auto-Roles.
 */
@Entity
@Table(name = "AutoRoles")
public class AutoRole extends Role {

    /**
     * Constructor.
     */
    public AutoRole() {
    }

    /**
     * @inheritDoc
     */
    public AutoRole(long guildId, long roleId) {
        super(guildId, roleId);
    }
}