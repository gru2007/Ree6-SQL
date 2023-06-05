package de.presti.ree6.sql;

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
    MariaDB("jdbc:mariadb://%s:%s/%s", "org.hibernate.dialect.MariaDBDialect", "com.mariadb.jdbc.Driver",true),

    /**
     * The SQLite Database Information.
     */
    SQLite("jdbc:sqlite:%s", "org.hibernate.community.dialect.SQLiteDialect", "org.sqlite.JDBC", false),

    /**
     * The PostgreSQL Database Information.
     */
    PostgreSQL("jdbc:postgresql://%s:%s/%s", "org.hibernate.dialect.PostgreSQLDialect", "org.postgresql.Driver",true),

    /**
     * H2 Database Information.
     */
    H2("jdbc:h2:%s", "org.hibernate.dialect.H2Dialect", "org.h2.Driver",false),

    /**
     * H2 Server Database Information.
     */
    H2_Server("jdbc:h2:tcp://%s/%s", "org.hibernate.dialect.H2Dialect", "org.h2.Driver",false);

    /**
     * The JDBC Connection URL used by HikariCP and Hibernate.
     */
    private final String jdbcURL;

    /**
     * The Hibernate Dialect used for the Queries.
     */
    private final String hibernateDialect;

    /**
     * The Driver Class used for the Connection.
     */
    private final String driverClass;

    /**
     * If the SQL-Typ requires an authentication.
     */
    private final boolean authRequired;
}
