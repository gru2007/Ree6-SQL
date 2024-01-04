package de.presti.ree6.sql.util.server;

import de.presti.ree6.sql.SQLSession;
import de.presti.ree6.sql.util.IDatabaseServer;
import org.h2.tools.Server;

import java.sql.SQLException;

/**
 * Class used to handle and contain information about the H2-Database.
 */
public class H2DatabaseServer implements IDatabaseServer {

    /**
     * H2-Server.
     */
    Server server;

    /**
     * Create the server.
     *
     * @throws SQLException If something goes wrong.
     */
    @Override
    public void createServer(int port, String password, String path) throws SQLException {
        String[] args = new String[] { "-tcp", "-tcpAllowOthers", "-ifNotExists", "-tcpPort", String.valueOf(port), "-tcpPassword", password, "-baseDir", path };
        server = Server.createTcpServer(args).start();
        if (server.isRunning(true)) {
            System.out.println("H2-Server started successfully.");
        } else {
            System.out.println("H2-Server could not be started.");
        }
    }

    /**
     * Destroy the server.
     */
    @Override
    public void destroyServer() {
        server.stop();
    }
}
