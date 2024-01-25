package de.presti.ree6.sql.util;

/**
 * Class used to handle embedded databases.
 */
public interface IDatabaseServer {

    /**
     * Create the server.
     *
     * @param port     The port of the server.
     * @param password The password of the server.
     * @param path     The path of the storage file.
     *
     * @throws Exception If something goes wrong.
     */
    void createServer(int port, String password, String path) throws Exception;

    /**
     * Destroy the server.
     *
     * @throws Exception If something goes wrong.
     */
    void destroyServer() throws Exception;

}
