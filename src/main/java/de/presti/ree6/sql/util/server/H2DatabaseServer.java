package de.presti.ree6.sql.util.server;

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
     * @throws SQLException If something goes wrong.
     */
    @Override
    public void createServer() throws SQLException {
        server = Server.createTcpServer().start();
    }

    /**
     * Destroy the server.
     * @throws Exception If something goes wrong.
     */
    @Override
    public void destroyServer() throws Exception {
        server.stop();
    }
}
