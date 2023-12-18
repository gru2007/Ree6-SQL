package de.presti.ree6.sql.util;

import de.presti.ree6.sql.DatabaseTyp;
import lombok.Builder;
import lombok.Getter;

/**
 * SQL Config to parse everything we need to connect to the SQL Server.
 */
@Getter
@Builder
public class SQLConfig {

    /**
     * The Database Name.
     */
    String database;

    /**
     * The Host of the Database.
     */
    String host;

    /**
     * The Username of the Database.
     */
    String username;

    /**
     * The Password of the Database.
     */
    String password;

    /**
     * The Path of the Database.
     */
    String path;

    /**
     * The Port of the Database.
     */
    int port;

    /**
     * The Pool Size of the Database.
     */
    int poolSize;

    /**
     * If an Embedded Server should be created for the DatabaseTyp.
     */
    boolean createEmbeddedServer;

    /**
     * If the SQL-System should log out debug info.
     */
    boolean debug;

    /**
     * If Sentry should be used.
     */
    boolean sentry;

    /**
     * The DatabaseTyp.
     */
    DatabaseTyp typ;
}
