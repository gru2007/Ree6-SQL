package de.presti.ree6.sql.entities.economy;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

/**
 * Instance of a Bank that holds the User money.
 */
@Entity
@NoArgsConstructor
@Table(name = "Money_Bank")
public class MoneyBank extends MoneyHolder {
}
