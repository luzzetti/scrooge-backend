package lan.scrooge.api.infrastructure.jpa;

import java.util.List;
import java.util.Optional;
import lan.scrooge.api._shared.exceptions.ApplicationError;
import lan.scrooge.api._shared.exceptions.ElementNotFoundException;
import lan.scrooge.api.application.ports.output.BankAccountPersistencePort;
import lan.scrooge.api.domain.entities.BankAccount;
import lan.scrooge.api.domain.entities.ScroogeUser;
import lan.scrooge.api.domain.vos.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
@RequiredArgsConstructor
public class BankAccountJpaAdapter implements BankAccountPersistencePort {

  private final BankAccountRepository bankAccountRepository;
  private final ScroogeUserRepository scroogeUserRepository;

  @Override
  public void persist(BankAccount bankAccount) {

    ScroogeUserJpaEntity proxiedOwner =
        scroogeUserRepository.getReferenceById(bankAccount.getOwner().getId().getValue());

    var jpaEntity = new BankAccountJpaEntity();
    jpaEntity.setId(bankAccount.getId().getValue());
    jpaEntity.setMnemonicName(bankAccount.getMnemonicName().getValue());
    jpaEntity.setIban(bankAccount.getIban().getValue());
    jpaEntity.setBalance(bankAccount.getBalance());
    jpaEntity.setOwner(proxiedOwner);

    bankAccountRepository.save(jpaEntity);
  }

  @Override
  public BankAccount fetch(BankAccountId bankAccountId) {

    return bankAccountRepository
        .findById(bankAccountId.getValue())
        .map(BankAccountJpaAdapter::toBankAccount)
        .orElseThrow(() -> new ElementNotFoundException(Errors.NOT_FOUND_BANK_ACCOUNT));
  }

  @Override
  public List<BankAccount> fetchAll(ScroogeUser scroogeUser) {
    return bankAccountRepository.findAllByOwnerId(scroogeUser.getId().getValue()).stream()
        .map(BankAccountJpaAdapter::toBankAccount)
        .toList();
  }

  @Override
  public Optional<BankAccount> fetchFromIban(IBAN iban) {
    return bankAccountRepository
        .findByIban(iban.getValue())
        .map(BankAccountJpaAdapter::toBankAccount);
  }

  @Override
  public void delete(BankAccount theBankAccountToClose) {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  private static BankAccount toBankAccount(BankAccountJpaEntity e) {
    return BankAccount.builder()
        .id(BankAccountId.of(e.getId()))
        .mnemonicName(MnemonicName.of(e.getMnemonicName()))
        .iban(IBAN.of(e.getIban()))
        .balance(e.getBalance())
        .owner(
            ScroogeUser.builder()
                .id(ScroogeUserId.of(e.getOwner().getId()))
                .email(Email.of(e.getOwner().getEmail()))
                .build())
        .build();
  }

  @Getter
  @RequiredArgsConstructor
  private enum Errors implements ApplicationError {
    NOT_FOUND_BANK_ACCOUNT("not-found.bank-account");
    private final String code;
  }
}
