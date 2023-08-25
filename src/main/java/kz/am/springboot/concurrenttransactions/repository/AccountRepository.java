package kz.am.springboot.concurrenttransactions.repository;

import kz.am.springboot.concurrenttransactions.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
}
