package lan.scrooge.api.infrastructure.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "scrooge_users")
@Getter
@Setter
@NoArgsConstructor
public class ScroogeUserJpaEntity {

  @Id private UUID id;

  private String email;
}
