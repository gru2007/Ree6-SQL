package de.presti.ree6.sql;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DatabaseTyp {

    MariaDB("jdbc:mariadb://%s:%s/%s", "org.hibernate.dialect.MariaDBDialect", true),

    SQLite("jdbc:sqlite:%s", "org.hibernate.community.dialect.SQLiteDialect", false);

    private final String jdbcURL;

    private final String hibernateDialect;

    private final boolean authRequired;
}
