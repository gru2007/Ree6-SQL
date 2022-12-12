package de.presti.ree6.sql;

import com.zaxxer.hikari.HikariConfig;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.util.Properties;
import java.util.Set;

/**
 * Classed used as a Bridge between Hibernate and our SQL-Base.
 */
@Slf4j
public class SQLSession {

    /**
     * Various String that keep connection information to use for a connection.
     */
    private static String databaseUser,
            databaseName,
            databasePassword,
            databaseServerIP,
            databasePath;

    /**
     * The port of the Server.
     */
    private static int databaseServerPort;

    /**
     * The JDBC URL to connect to the Database.
     */
    static String jdbcURL;

    /**
     * The Database Typ.
     */
    @Getter
    @Setter
    static DatabaseTyp databaseTyp;

    /**
     * The max amount of connections allowed by Hikari.
     */
    static int maxPoolSize;

    /**
     * The SessionFactory used to create Sessions.
     */
    static SessionFactory sessionFactory;

    /**
     * The SQL-Connector used to connect to the Database.
     */
    @Getter
    static SQLConnector sqlConnector;

    public SQLSession(String databaseUser, String databaseName, String databasePassword, String databaseServerIP, int databaseServerPort, String databasePath, DatabaseTyp databaseTyp, int maxPoolSize) {
        this.databaseUser = databaseUser;
        this.databaseName = databaseName;
        this.databasePassword = databasePassword;
        this.databaseServerIP = databaseServerIP;
        this.databaseServerPort = databaseServerPort;
        this.databasePath = databasePath;

        setMaxPoolSize(maxPoolSize);
        setDatabaseTyp(databaseTyp);
        setJdbcURL(buildConnectionURL());

        sqlConnector = new SQLConnector();
    }

    /**
     * Build a new SessionFactory or return the current one.
     *
     * @param debug if we should print debug messages.
     *
     * @return The SessionFactory.
     */
    public static SessionFactory buildSessionFactory(boolean debug) {
        if (sessionFactory != null) return getSessionFactory();

        try {
            Configuration configuration = new Configuration();
            Properties properties = new Properties();
            properties.put("hibernate.connection.datasource", "com.zaxxer.hikari.HikariDataSource");
            properties.put("hibernate.connection.provider_class", "org.hibernate.hikaricp.internal.HikariCPConnectionProvider");
            properties.put("hibernate.connection.url", jdbcURL);
            properties.put("hibernate.connection.username", databaseName);
            properties.put("hibernate.connection.password", databasePassword);
            properties.put("hibernate.hikari.maximumPoolSize", String.valueOf(maxPoolSize));
            properties.put("hibernate.dialect", getDatabaseTyp().getHibernateDialect());

            if (debug) {
                properties.put("hibernate.show_sql", true);
                properties.put("hibernate.format_sql", true);
            }

            properties.put("hibernate.hbm2ddl.auto", "update");
            properties.put("jakarta.persistence.schema-generation.database.action", "update");

            configuration.addProperties(properties);

            Set<Class<?>> classSet = new Reflections(
                    ConfigurationBuilder
                            .build()
                            .forPackage("de.presti.ree6.webinterface.sql.entities", ClasspathHelper.staticClassLoader()))
                    .getTypesAnnotatedWith(Table.class);

            classSet.forEach(configuration::addAnnotatedClass);

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();

            return sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            log.error("Initial SessionFactory creation failed.", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Build the Connection URL with the given data.
     *
     * @return the Connection URL.
     */
    public String buildConnectionURL() {
        String jdbcUrl;

        switch (getDatabaseTyp()) {
            case MariaDB -> jdbcUrl = getDatabaseTyp().getJdbcURL().formatted(databaseServerIP,
                    databaseServerPort,
                    databaseName);

            default -> jdbcUrl = DatabaseTyp.SQLite.getJdbcURL().formatted(databasePath);
        }
        return jdbcUrl;
    }

    public static HikariConfig buildHikariConfig() {
        HikariConfig hConfig = new HikariConfig();

        hConfig.setJdbcUrl(getJdbcURL());
        hConfig.setUsername(databaseUser);
        hConfig.setPassword(databasePassword);
        hConfig.setMaximumPoolSize(getMaxPoolSize());

        return hConfig;
    }

    /**
     * Set the JDBC URL used to connect to the Database.
     * @param jdbcURL The JDBC URL.
     */
    public static void setJdbcURL(String jdbcURL) {
        SQLSession.jdbcURL = jdbcURL;
    }

    /**
     * Set the max amount of connections allowed by Hikari.
     * @param maxPoolSize The max amount of connections.
     */
    public static void setMaxPoolSize(int maxPoolSize) {
        SQLSession.maxPoolSize = maxPoolSize;
    }

    /**
     * Get the JDBC URL used to connect to the Database.
     * @return The JDBC URL.
     */
    public static String getJdbcURL() {
        return jdbcURL;
    }

    /**
     * Get the max amount of connections allowed by Hikari.
     * @return The max amount of connections.
     */
    public static int getMaxPoolSize() {
        return maxPoolSize;
    }

    /**
     * Get the current SessionFactory.
     * @return The SessionFactory.
     */
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null)
            return sessionFactory = buildSessionFactory(false);
        return sessionFactory;
    }
}
