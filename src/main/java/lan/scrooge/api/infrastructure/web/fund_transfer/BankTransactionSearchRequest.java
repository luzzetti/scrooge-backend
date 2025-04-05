package lan.scrooge.api.infrastructure.web.fund_transfer;

import jakarta.validation.constraints.NotNull;

public record BankTransactionSearchRequest(@NotNull String bankAccountId) {}
