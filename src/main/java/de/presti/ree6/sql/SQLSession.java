package de.presti.ree6.sql;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zaxxer.hikari.HikariConfig;
import de.presti.ree6.sql.util.IDatabaseServer;
import de.presti.ree6.sql.util.SettingsManager;
import de.presti.ree6.sql.util.server.H2DatabaseServer;
import io.sentry.Sentry;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.io.InputStream;
import java.util.Objects;
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
    static String databaseUser,
            databaseName,
            databasePassword,
            databaseServerIP,
            databasePath;

    /**
     * The port of the Server.
     */
    static int databaseServerPort;

    /**
     * The JDBC URL to connect to the Database.

     */
    @Getter
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
    @Getter
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

    /**
     * Internal class used to manage embedded Databases.
     */
    IDatabaseServer embeddedDatabaseServer;

    /**
     * If the debug mode is enabled.
     */
    @Getter
    private static boolean debug;

    /**
     * Constructor.
     *
     * @param databaseUser         Database Username
     * @param databaseName         Database Name
     * @param databasePassword     Database User password
     * @param databaseServerIP     Database Address
     * @param databaseServerPort   Database Port
     * @param databasePath         Database Path (SQLite)
     * @param databaseTyp          Database Typ ({@link DatabaseTyp})
     * @param maxPoolSize          Max Hikari-CP Pool Size
     * @param createEmbeddedServer If an embedded Database should be created, should be false if it being created by another instance.
     * @param debug                If debug mode should be enabled.
     */
    public SQLSession(String databaseUser, String databaseName, String databasePassword, String databaseServerIP, int databaseServerPort, String databasePath, DatabaseTyp databaseTyp, int maxPoolSize, boolean createEmbeddedServer, boolean debug) {
        SQLSession.databaseUser = databaseUser;
        SQLSession.databaseName = databaseName;
        SQLSession.databasePassword = databasePassword;
        SQLSession.databaseServerIP = databaseServerIP;
        SQLSession.databaseServerPort = databaseServerPort;
        SQLSession.databasePath = databasePath;

        SQLSession.debug = debug;

        Reflections reflections = new Reflections("sql", Scanners.Resources);

        String dsn = "";

        String[] resources = reflections.getResources("application.configuration").toArray(String[]::new);
        if (resources.length > 0) {

            try (InputStream inputStream = ClasspathHelper.staticClassLoader().getResourceAsStream(resources[0])) {
                if (inputStream == null) return;

                String content = new String(inputStream.readAllBytes());
                JsonObject jsonObject = JsonParser.parseString(content).getAsJsonObject();
                dsn = jsonObject.get("sentry").getAsJsonPrimitive().getAsString();

            } catch (Exception exception) {
                log.error("Couldn't load Application configuration!", exception);
            }
        }

        // DO NOT OVERWRITE!
        if (!Sentry.isEnabled()) {
            String finalDsn = dsn;

            Sentry.init(options -> {
                options.setDsn(finalDsn);
                options.setRelease(Objects.requireNonNullElse(SQLSession.class.getPackage().getImplementationVersion(), "1.2.0"));
            });
        }

        try {
            Class.forName(databaseTyp.getDriverClass());
        } catch (ClassNotFoundException e) {
            // Somehow this fixes the Issue?
            log.warn("Couldn't load " + databaseTyp.name() + " Driver!\nThis could lead to errors!", e);
        }

        if (createEmbeddedServer) {
            if (databaseTyp == DatabaseTyp.H2) {
                try {
                    embeddedDatabaseServer = new H2DatabaseServer();
                    embeddedDatabaseServer.createServer();
                } catch (Exception exception) {
                    log.error("Couldn't start embedded H2 Database!", exception);
                }
            }

            // TODO:: check if there is a better way, how about a method that users have to call themselves?
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (embeddedDatabaseServer != null) {
                    try {
                        embeddedDatabaseServer.destroyServer();
                    } catch (Exception e) {
                        log.error("Couldn't stop embedded Database!", e);
                    }
                }
            }));
        }

        setMaxPoolSize(maxPoolSize);
        setDatabaseTyp(databaseTyp);
        setJdbcURL(buildConnectionURL());

        SettingsManager.loadDefaults();

        sqlConnector = new SQLConnector();
    }

    /**
     * Build a new SessionFactory or return the current one.
     *
     * @param debug if we should print debug messages.
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

            if (!databaseTyp.isAuthRequired() && databaseUser.equalsIgnoreCase("root") && databasePassword.equalsIgnoreCase("yourpw")) {
                properties.put("hibernate.connection.username", "");
                properties.put("hibernate.connection.password", "");
            } else {
                properties.put("hibernate.connection.username", databaseUser);
                properties.put("hibernate.connection.password", databasePassword);
            }

            properties.put("hibernate.hikari.maximumPoolSize", String.valueOf(maxPoolSize));
            properties.put("hibernate.dialect", getDatabaseTyp().getHibernateDialect());

            properties.put("hibernate.show_sql", debug);
            properties.put("hibernate.format_sql", debug);

            //properties.put("hibernate.hbm2ddl.auto", "update");
            properties.put("jakarta.persistence.schema-generation.database.action", "update");

            configuration.addProperties(properties);

            Set<Class<?>> classSet = new Reflections(
                    ConfigurationBuilder
                            .build()
                            .forPackage("de.presti.ree6.sql.entities", ClasspathHelper.staticClassLoader()))
                    .getTypesAnnotatedWith(Table.class);

            if (classSet.isEmpty()) {
                log.error("No Entities found!");
            }

            classSet.forEach(configuration::addAnnotatedClass);

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();

            return sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            log.error("Initial SessionFactory creation failed.", ex);
            Sentry.captureException(ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public void runMigrations() {
        Reflections reflections = new Reflections("sql/migrations", Scanners.Resources);

        String[] resources = reflections.getResources(databaseTyp.name().toLowerCase() + ".sql").toArray(String[]::new);
        if (resources.length > 0) {

            try (InputStream inputStream = ClasspathHelper.staticClassLoader().getResourceAsStream(resources[0])) {
                if (inputStream == null) return;

                String content = new String(inputStream.readAllBytes());
                sqlConnector.querySQL(content);

            } catch (Exception exception) {
                log.error("Couldn't load Migrations!", exception);
            }
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
            case MariaDB, PostgreSQL -> jdbcUrl = getDatabaseTyp().getJdbcURL().formatted(databaseServerIP,
                    databaseServerPort,
                    databaseName);

            case H2_Server -> jdbcUrl = getDatabaseTyp().getJdbcURL().formatted(databaseServerIP, databasePath);

            case H2 -> {
                if (!databasePath.startsWith(".") && !databasePath.startsWith("/") && !databasePath.startsWith("~")) {
                    databasePath = "./" + databasePath;
                }

                if (databasePath.endsWith(".db")) {
                    databasePath = databasePath.replace(".db", "");
                }

                jdbcUrl = getDatabaseTyp().getJdbcURL().formatted(databasePath) + ";MODE=MySQL;NON_KEYWORDS=VALUE,DAY,MONTH,YEAR;";
            }

            default -> jdbcUrl = getDatabaseTyp().getJdbcURL().formatted(databasePath);
        }
        return jdbcUrl;
    }

    public static HikariConfig buildHikariConfig() {
        HikariConfig hConfig = new HikariConfig();

        hConfig.setJdbcUrl(getJdbcURL());

        if (!databaseTyp.isAuthRequired() && databaseUser.equalsIgnoreCase("root") && databasePassword.equalsIgnoreCase("yourpw")) {
            hConfig.setUsername("");
            hConfig.setPassword("");
        } else {
            hConfig.setUsername(databaseUser);
            hConfig.setPassword(databasePassword);
        }

        hConfig.setMaximumPoolSize(getMaxPoolSize());

        return hConfig;
    }

    /**
     * Set the JDBC URL used to connect to the Database.
     *
     * @param jdbcURL The JDBC URL.
     */
    public static void setJdbcURL(String jdbcURL) {
        SQLSession.jdbcURL = jdbcURL;
    }

    /**
     * Set the max amount of connections allowed by Hikari.
     *
     * @param maxPoolSize The max amount of connections.
     */
    public static void setMaxPoolSize(int maxPoolSize) {
        SQLSession.maxPoolSize = maxPoolSize;
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null)
            return sessionFactory = buildSessionFactory(isDebug());
        return sessionFactory;
    }
}
