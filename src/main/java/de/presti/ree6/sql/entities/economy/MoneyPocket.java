package de.presti.ree6.sql.entities.economy;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

/**
 * Instance of a User Pockets that holds the User money.
 */
@Entity
@NoArgsConstructor
@Table(name = "Money_Pocket")
public class MoneyPocket extends MoneyHolder{

}
