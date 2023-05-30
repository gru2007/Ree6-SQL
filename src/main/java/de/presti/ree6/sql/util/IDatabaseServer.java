package de.presti.ree6.sql.util;

/**
 * Class used to handle embedded databases.
 */
public interface IDatabaseServer {

    /**
     * Create the server.
     * @throws Exception If something goes wrong.
     */
    void createServer() throws Exception;

    /**
     * Destroy the server.
     * @throws Exception If something goes wrong.
     */
    void destroyServer() throws Exception;

}
