package lan.scrooge.api.infrastructure.jpa;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface BankTransactionRepository
    extends JpaRepository<BankTransactionJpaEntity, UUID>,
        JpaSpecificationExecutor<BankTransactionJpaEntity> {

  @Query(
      """
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

  @Query(
      value =
          """
                  select u.id,
                         a.id,
                         a.mnemonic_name,
                         case when a.id = t.source_account_id then 'EXPENSE' else 'INCOME' end,
                         t.amount,
                         t.id,
                         t.target_account_id,
                         t.source_account_id,
                         t.created_at
                  from scrooge_users u
                           join bank_accounts a on u.id = a.owner_id
                           join transactions t on a.id = t.source_account_id or a.id = t.target_account_id
                  where u.id = :userId
                  order by t.created_at DESC
                  offset :pageNumber * :pageSize
                  limit :pageSize
                  """,
      nativeQuery = true)
  List<UserTransactionProjection> findAllByUserId(UUID userId, int pageNumber, int pageSize);
}
