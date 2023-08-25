package kz.am.springboot.concurrenttransactions;

import kz.am.springboot.concurrenttransactions.domain.Account;
import kz.am.springboot.concurrenttransactions.repository.AccountRepository;
import kz.am.springboot.concurrenttransactions.repository.TransactionRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ConcurrentTransactionsApplication {
    private final Logger LOGGER = LoggerFactory.getLogger(ConcurrentTransactionsApplication.class);
    private final int NUMBER_OF_ACCOUNTS = 4;
    private final int NUMBER_OF_THREADS = 2;
    private final int NUMBER_OF_SUCCESSFUL_TRANSACTIONS = 30;

    public static void main(String[] args) {
        SpringApplication.run(ConcurrentTransactionsApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(AccountRepository accountRepository, TransactionRecordRepository transactionRepository) {
        return (args -> runApp(accountRepository, transactionRepository));
    }

    private void runApp(AccountRepository accountRepository, TransactionRecordRepository transactionRepository) {
        for (int i = 0; i < NUMBER_OF_ACCOUNTS; i++) {
            accountRepository.save(new Account());
        }
        List<Account> allAccounts = accountRepository.findAll();
        AccountTransactionManager atm = new AccountTransactionManager(allAccounts, accountRepository,
                transactionRepository, NUMBER_OF_SUCCESSFUL_TRANSACTIONS);
        LOGGER.info("Total amount before transfers: {}", atm.getTotalAmount());
        List<Thread> threadList = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            threadList.add(new Thread(atm, "Thread-" + (i + 1)));
        }
        for (Thread thread : threadList) {
            thread.start();
        }
        for (Thread thread : threadList) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        LOGGER.info("Total amount after transfers: {}", atm.getTotalAmount());
    }
}
