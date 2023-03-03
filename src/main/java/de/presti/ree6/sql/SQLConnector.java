package de.presti.ree6.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.presti.ree6.sql.migrations.MigrationUtil;
import de.presti.ree6.sql.seed.SeedManager;
import io.sentry.Sentry;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reflections.util.ClasspathHelper;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * A "Connector" Class which connect with the used Database Server.
 * Used to manage the connection between Server and Client.
 */
@Slf4j
@SuppressWarnings({"SqlNoDataSourceInspection", "SqlResolve"})
public class SQLConnector {


    /**
     * An Instance of the actual Java SQL Connection.
     */
    private HikariDataSource dataSource;

    /**
     * An Instance of the SQL-Worker which works with the Data in the Database.
     */
    private final SQLWorker sqlWorker;

    /**
     * A boolean to keep track if there was at least one valid connection.
     */
    private boolean connectedOnce = false;


    /**
     * A boolean to keep track if there was at least two valid connections.
     */
    @Setter
    private boolean connectedSecond = false;

    /**
     * Constructor with the needed data to open an SQL connection.
     */
    public SQLConnector() {
        sqlWorker = new SQLWorker(this);

        connectToSQLServer();
        createTables();
        try {
            MigrationUtil.runAllMigrations(this);
        } catch (Exception exception) {
            log.error("Error while running Migrations!", exception);
        }

        SeedManager.runAllSeeds(this);
    }

    /**
     * Try to open a connection to the SQL Server with the given data.
     */
    public void connectToSQLServer() {
        log.info("Connecting to SQl-Service (SQL).");
        // Check if there is already an open Connection.
        if (isConnected()) {
            try {
                // Close if there is and notify.
                getDataSource().close();
                log.info("Service (SQL) has been stopped.");
            } catch (Exception ignore) {
                // Notify if there was an error.
                log.error("Service (SQL) couldn't be stopped.");
            }
        }

        try {
            dataSource = new HikariDataSource(SQLSession.buildHikariConfig());
            log.info("Service (SQL) has been started. Connection was successful.");
            connectedOnce = true;
        } catch (Exception exception) {
            // Notify if there was an error.
            log.error("Service (SQL) couldn't be started. Connection was unsuccessful.", exception);
        }
    }

    /**
     * Create Tables in the Database if they aren't already set.
     */
    public void createTables() {

        // Check if there is an open Connection if not, skip.
        if (!isConnected()) return;

        try (InputStream inputStream = ClasspathHelper.staticClassLoader().getResourceAsStream("sql/schema.sql")) {
            if (inputStream == null) return;
            List<String> queries = Arrays.stream(new String(inputStream.readAllBytes()).split(";")).filter(s -> !s.isEmpty()).toList();
            for (String query : queries) {
                log.debug("\t\t[*] Executing query {}/{}", queries.indexOf(query) + 1, queries.size());
                log.debug("\t\t[*] Executing query: {}", query);
                querySQL(query);
            }
        } catch (Exception exception) {
            log.error("Couldn't create Tables!", exception);
        }
    }

    //region Utility

    /**
     * Query basic SQL Statements, without using the ORM-System.
     *
     * @param sqlQuery   The SQL Query.
     * @param parameters The Parameters for the Query.
     * @return Either a {@link Integer} or the result object of the ResultSet.
     */
    public Object querySQL(String sqlQuery, Object... parameters) {
        if (!isConnected()) {
            if (connectedOnce()) {
                connectToSQLServer();
                return querySQL(sqlQuery, parameters);
            } else {
                return null;
            }
        }

        try (Connection connection = getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            for (int i = 0; i < parameters.length; i++) {
                preparedStatement.setObject(i + 1, parameters[i]);
            }
            if (sqlQuery.startsWith("SELECT")) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next();
                }
            } else {
                return preparedStatement.executeUpdate();
            }
        } catch (Exception exception) {
            Sentry.captureException(exception);
        }

        return null;
    }

    /**
     * Send an SQL-Query to SQL-Server and get the response.
     *
     * @param <R>        The Class entity.
     * @param r          The class entity.
     * @param sqlQuery   the SQL-Query.
     * @param parameters a list with all parameters that should be considered.
     * @return The Result from the SQL-Server.
     */
    public <R> Query<R> querySQL(@NotNull R r, @NotNull String sqlQuery, @Nullable Map<String, Object> parameters) {

        if (!isConnected()) {
            if (connectedOnce()) {
                connectToSQLServer();
                return querySQL(r, sqlQuery, parameters);
            } else {
                return null;
            }
        }

        try (Session session = SQLSession.getSessionFactory().openSession()) {

            session.beginTransaction();

            Query<R> query = (Query<R>) session.createNativeQuery(sqlQuery, r.getClass());

            if (parameters != null) {
                for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                    query.setParameter(entry.getKey(), entry.getValue());
                }
            }

            session.getTransaction().commit();

            return query;
        } catch (Exception exception) {
            Sentry.captureException(exception);
            throw exception;
        }
    }

    //endregion

    /**
     * Check if there is an open connection to the Database Server.
     *
     * @return boolean If the connection is opened.
     */
    public boolean isConnected() {
        try {
            return getDataSource() != null && !getDataSource().isClosed();
        } catch (Exception ignore) {
        }

        return false;
    }

    /**
     * Call to close the current Connection.
     */
    public void close() {
        // Check if there is already an open Connection.
        if (isConnected()) {
            try {
                // Close if there is and notify.
                getDataSource().close();
                log.info("Service (SQL) has been stopped.");
            } catch (Exception ignore) {
                // Notify if there was an error.
                log.error("Service (SQL) couldn't be stopped.");
            }
        }
    }

    /**
     * Retrieve an Instance of the SQL-Connection.
     *
     * @return DataSource Instance of te SQL-Connection.
     */
    public HikariDataSource getDataSource() {
        return dataSource;
    }

    /**
     * Retrieve an Instance of the SQL-Worker to work with the Data.
     *
     * @return {@link SQLWorker} the Instance saved in this SQL-Connector.
     */
    public SQLWorker getSqlWorker() {
        return sqlWorker;
    }

    /**
     * Check if there was at least one successful Connection to the Database Server.
     *
     * @return boolean If there was at least one successful Connection.
     */
    public boolean connectedOnce() {
        return connectedOnce;
    }

    /**
     * Check if there was at least one successful Connection to the Database Server.
     *
     * @return boolean If there was at least one successful Connection.
     */
    public boolean connectedSecond() {
        return connectedSecond;
    }
}