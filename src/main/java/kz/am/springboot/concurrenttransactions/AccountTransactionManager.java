package kz.am.springboot.concurrenttransactions;

import kz.am.springboot.concurrenttransactions.domain.Account;
import kz.am.springboot.concurrenttransactions.domain.AccountTransactionRecord;
import kz.am.springboot.concurrenttransactions.repository.AccountRepository;
import kz.am.springboot.concurrenttransactions.repository.TransactionRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.Transactional;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class AccountTransactionManager implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountTransactionManager.class);
    private final List<Account> allAccounts;
    private AccountRepository accountRepository;
    private TransactionRecordRepository transactionRecordRepository;
    private int numberOfTransaction = 0;
    private final int neededNumOfSuccessfulTransactions;
    private int numOfSuccessfulTransactions;

    public AccountTransactionManager(List<Account> allAccounts, AccountRepository accountRepository,
                                     TransactionRecordRepository transactionRecordRepository, int neededNumOfSuccessfulTransactions) {
        this.allAccounts = allAccounts;
        this.accountRepository = accountRepository;
        this.transactionRecordRepository = transactionRecordRepository;
        this.neededNumOfSuccessfulTransactions = neededNumOfSuccessfulTransactions;
    }

    public int getTotalAmount() {
        int totalAmount = 0;
        for (Account account : allAccounts) {
            totalAmount += account.getMoney();
        }
        return totalAmount;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 2000 + 1));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (numOfSuccessfulTransactions < neededNumOfSuccessfulTransactions) {
                int acFrom = 0;
                int acTo = 0;
                synchronized (this) {
                    while (acFrom == acTo) {
                        acFrom = ThreadLocalRandom.current().nextInt(allAccounts.size());
                        acTo = ThreadLocalRandom.current().nextInt(allAccounts.size());
                        if (acFrom == allAccounts.size() - 1) {
                            acTo = ThreadLocalRandom.current().nextInt(allAccounts.size() - 1);
                        }
                    }
                    numberOfTransaction++;
                }
                boolean success = this.doTransaction(allAccounts.get(acFrom), allAccounts.get(acTo), numberOfTransaction);
                if (success) numOfSuccessfulTransactions++;
            } else {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Transactional
    public boolean doTransaction(Account acFrom, Account acTo, int transactionNum) {
        int sum = ThreadLocalRandom.current().nextInt(1, 10000 + 1);
        AccountTransactionRecord accountTransactionRecord = new AccountTransactionRecord();
        accountTransactionRecord.setAccountFromId(acFrom.getId());
        accountTransactionRecord.setAccountToId(acTo.getId());
        accountTransactionRecord.setAmount(sum);
        LOGGER.info("{} Transaction started: transfer {} from {} to {}", transactionNum, sum, acFrom, acTo);
        boolean transactionIsSuccessful = false;
        if (acFrom.getMoney() >= sum) {
            changeAccBalance(acFrom, (-1) * sum, transactionNum);
            changeAccBalance(acTo, sum, transactionNum);
            LOGGER.info("{} Transaction complete: transferred {} from {} to {}", transactionNum, sum, acFrom, acTo);
            accountTransactionRecord.setSuccessful(true);
            transactionIsSuccessful = true;
        } else {
            LOGGER.error("{} Transaction cancelled: not enough money in account {}", transactionNum, acFrom.getId());
            accountTransactionRecord.setSuccessful(false);
        }
        transactionRecordRepository.save(accountTransactionRecord);
        return transactionIsSuccessful;
    }

    private void changeAccBalance(Account account, int sum, int transactionNum) {
        account.setMoney(account.getMoney() + sum);
        accountRepository.save(account);
        if (sum < 0) LOGGER.info("{} Removed amount {} from {}", transactionNum, (-1) * sum, account);
        else LOGGER.info("{} Added amount {} to {}", transactionNum, sum, account);
    }
}
