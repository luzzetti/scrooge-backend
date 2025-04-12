package lan.scrooge.api.infrastructure.jpa;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record UserTransactionProjection(
    UUID userId,
    UUID accountId,
    String mnemonicName,
    String transactionType,
    BigDecimal amount,
    UUID transactionId,
    UUID targetAccountId,
    UUID sourceAccountId,
    Instant createdAt) {}
