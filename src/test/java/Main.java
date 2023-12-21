import de.presti.ree6.sql.DatabaseTyp;
import de.presti.ree6.sql.SQLSession;
import de.presti.ree6.sql.util.SQLConfig;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
        // Used to test stuff quickly.
        SQLConfig sqlConfig = SQLConfig.builder()
                .host(args[0])
                .password(args[3])
                .username(args[1])
                .database(args[2])
                .typ(DatabaseTyp.MariaDB)
                .poolSize(3)
                .createEmbeddedServer(false)
                .debug(false)
                .port(3306)
                .path("storage/").build();

        new SQLSession(sqlConfig);

        if (sqlConfig.getTyp() == DatabaseTyp.SQLite) {
            SQLSession.getSqlConnector().querySQL("PRAGMA foreign_keys = ON;", true, (Object[]) null);

            if (SQLSession.getSqlConnector().querySQL("PRAGMA foreign_keys;", false, (Object[]) null) instanceof ResultSet resultSet) {
                System.out.println(resultSet.getBoolean(1));
            }
        }

        SQLSession.getSqlConnector().getSqlWorker().getEntityList(new de.presti.ree6.sql.entities.Warning(), "", null);
    }

}
