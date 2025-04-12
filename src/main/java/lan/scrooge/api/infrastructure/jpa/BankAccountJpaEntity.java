package lan.scrooge.api.infrastructure.jpa;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bank_accounts")
@Getter
@Setter
@NoArgsConstructor
public class BankAccountJpaEntity {

  @Id private UUID id;

  private String mnemonicName;
  private String iban;
  private BigDecimal balance;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "owner_id")
  private ScroogeUserJpaEntity owner;
}
