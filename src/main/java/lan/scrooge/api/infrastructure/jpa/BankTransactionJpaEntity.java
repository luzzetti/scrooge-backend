package lan.scrooge.api.infrastructure.jpa;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
public class BankTransactionJpaEntity {

  @Id private UUID id;

  @ManyToOne
  @JoinColumn(name = "source_account_id")
  private BankAccountJpaEntity sourceAccount;

  @ManyToOne
  @JoinColumn(name = "target_account_id")
  private BankAccountJpaEntity targetAccount;

  private BigDecimal amount;

  @Column(length = 150)
  private String causale;

  private Instant createdAt;
}
