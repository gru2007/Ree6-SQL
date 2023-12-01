import de.presti.ree6.sql.DatabaseTyp;
import de.presti.ree6.sql.SQLSession;
import de.presti.ree6.sql.entities.Setting;

public class Main {

    public static void main(String[] args) {
        // Used to test stuff quickly.
        new SQLSession("sql11666668", "sql11666668", "zeD8ADy7sT", "sql11.freemysqlhosting.net",
                3306, "storage/", DatabaseTyp.MariaDB, 3, false, true);

        SQLSession.getSqlConnector().getSqlWorker().updateEntity(new Setting("test", "test", "test" ,"test"));
    }

}
