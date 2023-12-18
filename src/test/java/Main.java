import de.presti.ree6.sql.DatabaseTyp;
import de.presti.ree6.sql.SQLSession;
import de.presti.ree6.sql.util.SQLConfig;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
        // Used to test stuff quickly.
        SQLConfig sqlConfig = SQLConfig.builder()
                .host("sql11.freemysqlhosting.net")
                .password("zeD8ADy7sT")
                .username("sql11666668")
                .database("sql11666668")
                .typ(DatabaseTyp.SQLite)
                .poolSize(3)
                .createEmbeddedServer(false)
                .debug(true)
                .port(3306)
                .path("storage/").build();

        new SQLSession(sqlConfig);

        SQLSession.getSqlConnector().querySQL("PRAGMA foreign_keys = ON;", true, (Object[])null);

        if (SQLSession.getSqlConnector().querySQL("PRAGMA foreign_keys;", false, (Object[])null) instanceof ResultSet resultSet) {
            System.out.println(resultSet.getBoolean(1));
        }
    }

}
