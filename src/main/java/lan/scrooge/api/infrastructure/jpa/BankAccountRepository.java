package lan.scrooge.api.infrastructure.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface BankAccountRepository
    extends JpaRepository<BankAccountJpaEntity, UUID>,
        JpaSpecificationExecutor<BankAccountJpaEntity> {

  @Query("""
          SELECT ba
          FROM BankAccountJpaEntity ba
          WHERE ba.iban = :iban
          AND ba.status <> 'CLOSED'
          """)
  Optional<BankAccountJpaEntity> findByIban(String iban);

  @Query("""
            SELECT ba
            FROM BankAccountJpaEntity ba
            WHERE ba.owner.id = :ownerId
            AND ba.status <> 'CLOSED'
            ORDER BY ba.mnemonicName ASC
          """)
  List<BankAccountJpaEntity> findAllByOwnerId(UUID ownerId);
}
