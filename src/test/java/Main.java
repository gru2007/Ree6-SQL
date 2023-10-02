import de.presti.ree6.sql.DatabaseTyp;
import de.presti.ree6.sql.SQLSession;
import de.presti.ree6.sql.entities.Setting;

public class Main {

    public static void main(String[] args) {
        // Used to test stuff quickly.
        new SQLSession("ree6", "ree6", "8s9]qgQsQanCbe5T", "146.19.191.6",
                3306, "storage/", DatabaseTyp.MariaDB, 3, false, true);

        SQLSession.getSqlConnector().getSqlWorker().updateEntity(new Setting("test", "test", "test" ,"test"));
    }

}
