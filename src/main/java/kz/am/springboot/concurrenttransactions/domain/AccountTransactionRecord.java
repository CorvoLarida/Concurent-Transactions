package kz.am.springboot.concurrenttransactions.domain;

import javax.persistence.*;

@Entity
@Table(name = "account_transaction_log")
public class AccountTransactionRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private String accountFromId;
    private String accountToId;
    private int amount;
    private boolean isSuccessful;

    public AccountTransactionRecord() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccountFromId() {
        return accountFromId;
    }

    public void setAccountFromId(String accountFrom) {
        this.accountFromId = accountFrom;
    }

    public String getAccountToId() {
        return accountToId;
    }

    public void setAccountToId(String accountTo) {
        this.accountToId = accountTo;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }
}
