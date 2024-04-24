package com.midas.app.services;

import com.midas.app.models.Account;
import com.midas.generated.model.UpdateAccountDto;
import java.util.List;
import java.util.UUID;

public interface AccountService {
  /**
   * createAccount creates a new account in the system or provider.
   *
   * @param details is the details of the account to be created.
   * @return Account
   */
  Account createAccount(Account details);

  /**
   * getAccounts returns a list of accounts.
   *
   * @return List<Account>
   */
  List<Account> getAccounts();

  /**
   * Updates an existing account.
   *
   * @param accountId The ID of the account to update.
   * @param details The updated account details.
   * @return The updated Account.
   */
  Account updateAccount(UUID accountId, UpdateAccountDto details);
}
