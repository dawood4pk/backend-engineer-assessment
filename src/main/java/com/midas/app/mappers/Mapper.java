package com.midas.app.mappers;

import com.midas.app.models.Account;
import com.midas.generated.model.AccountDto;
import com.midas.generated.model.UpdateAccountDto;
import lombok.NonNull;

public class Mapper {
  // Prevent instantiation
  private Mapper() {}

  /**
   * toAccountDto maps an account to an account dto.
   *
   * @param account is the account to be mapped
   * @return AccountDto
   */
  public static AccountDto toAccountDto(@NonNull Account account) {
    var accountDto = new AccountDto();

    accountDto
        .id(account.getId())
        .firstName(account.getFirstName())
        .lastName(account.getLastName())
        .email(account.getEmail())
        .providerType(AccountDto.ProviderTypeEnum.valueOf(account.getProviderType().name()))
        .providerId(account.getProviderId())
        .createdAt(account.getCreatedAt())
        .updatedAt(account.getUpdatedAt());

    return accountDto;
  }

  /**
   * updateAccountFromDto updates the mutable fields of an Account from an UpdateAccountDto.
   *
   * @param account The existing account to be updated
   * @param dto The DTO containing new field values
   * @return Account with updated fields
   */
  public static Account updateAccountFromDto(
      @NonNull Account account, @NonNull UpdateAccountDto dto) {
    account.setFirstName(dto.getFirstName());
    account.setLastName(dto.getLastName());
    account.setEmail(dto.getEmail());

    return account;
  }
}
