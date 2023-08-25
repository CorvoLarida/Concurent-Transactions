package kz.am.springboot.concurrenttransactions.repository;

import kz.am.springboot.concurrenttransactions.domain.AccountTransactionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRecordRepository extends JpaRepository<AccountTransactionRecord, String> {
}
