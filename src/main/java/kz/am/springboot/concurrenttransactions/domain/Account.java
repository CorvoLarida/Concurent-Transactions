package kz.am.springboot.concurrenttransactions.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(generator = "uuid-generator")
    @GenericGenerator(name = "uuid-generator", strategy = "uuid2")
    private String id;
    @PositiveOrZero(message = "Account balance cannot be negative")
    private int money = 10_000;

    public Account() {
    }

    public String getId() {
        return id;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "Account{" + "id='" + id + "\'" + ", money=" + money + "}";
    }
}

