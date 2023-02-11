package de.presti.ree6.sql;

import lombok.Getter;

/**
 * Supported Database Typs.
 */
public enum DatabaseTyp {

    /**
     * The MariaDB Database Information.
     */
    MariaDB("jdbc:mariadb://%s:%s/%s", "org.hibernate.dialect.MariaDBDialect"),

    /**
     * The SQLite Database Information.
     */
    SQLite("jdbc:sqlite:%s", "org.hibernate.community.dialect.SQLiteDialect");

    /**
     * The JDBC Connection URL used by HikariCP and Hibernate.
     */
    @Getter
    private final String jdbcURL;

    /**
     * The Hibernate Dialect used for the Queries.
     */
    @Getter
    private final String hibernateDialect;

    /**
     * Constructor.
     * @param jdbcURL The Connection Url.
     * @param hibernateDialect The Hibernate Dialect.
     */
    DatabaseTyp(String jdbcURL, String hibernateDialect) {
        this.jdbcURL = jdbcURL;
        this.hibernateDialect = hibernateDialect;
    }
}
