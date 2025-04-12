package lan.scrooge.api.infrastructure.events.models;

import lan.scrooge.api.domain.vos.BankAccountId;
import lan.scrooge.api.domain.vos.IBAN;

public record BankAccountCreatedEvent(BankAccountId bankAccountId, IBAN iban) {}
