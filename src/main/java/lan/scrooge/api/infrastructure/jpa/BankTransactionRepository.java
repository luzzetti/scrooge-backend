package lan.scrooge.api.infrastructure.jpa;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface BankTransactionRepository
    extends JpaRepository<BankTransactionJpaEntity, UUID>,
        JpaSpecificationExecutor<BankTransactionJpaEntity> {

  @Query("""
          SELECT bt
          FROM BankTransactionJpaEntity bt
          WHERE 1=1
          AND (
                    bt.sourceAccount.id = :bankAccountId
                    OR bt.targetAccount.id = :bankAccountId
          )
          ORDER BY bt.createdAt DESC
          """)
  List<BankTransactionJpaEntity> findAllByBankAccountId(UUID bankAccountId);
}
