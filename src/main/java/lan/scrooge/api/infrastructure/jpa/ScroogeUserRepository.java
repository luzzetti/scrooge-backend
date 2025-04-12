package lan.scrooge.api.infrastructure.jpa;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ScroogeUserRepository
    extends JpaRepository<ScroogeUserJpaEntity, UUID>,
        JpaSpecificationExecutor<ScroogeUserJpaEntity> {}
