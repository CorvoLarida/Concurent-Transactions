package kz.am.springboot.concurrenttransactions;

import java.util.UUID;

public class Account {
    private final String id;
    private int money = 10_000;

    public Account() {
        this.id = UUID.randomUUID().toString();
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

