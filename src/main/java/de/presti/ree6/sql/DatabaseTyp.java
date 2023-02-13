package de.presti.ree6.sql;

import lombok.Getter;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Supported Database Typs.
 */
@Getter
@AllArgsConstructor
public enum DatabaseTyp {

    /**
     * The MariaDB Database Information.
     */
    MariaDB("jdbc:mariadb://%s:%s/%s", "org.hibernate.dialect.MariaDBDialect", true),

    /**
     * The SQLite Database Information.
     */
    SQLite("jdbc:sqlite:%s", "org.hibernate.community.dialect.SQLiteDialect", false);

    /**
     * The JDBC Connection URL used by HikariCP and Hibernate.
     */
    private final String jdbcURL;

    /**
     * The Hibernate Dialect used for the Queries.
     */
    private final String hibernateDialect;

    /**
     * If the SQL-Typ requires an authentication.
     */
    private final boolean authRequired;
}
