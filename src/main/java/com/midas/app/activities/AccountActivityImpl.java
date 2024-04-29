package com.midas.app.activities;

import com.midas.app.models.Account;
import com.midas.app.providers.external.stripe.StripePaymentProvider;
import com.midas.app.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountActivityImpl implements AccountActivity {
  private final AccountRepository accountRepository;
  private final StripePaymentProvider stripePaymentProvider;

  @Autowired
  public AccountActivityImpl(
      AccountRepository accountRepository, StripePaymentProvider stripePaymentProvider) {
    this.accountRepository = accountRepository;
    this.stripePaymentProvider = stripePaymentProvider;
  }

  @Override
  public Account saveAccount(Account account) {
    return accountRepository.save(account);
  }

  @Override
  public Account createPaymentAccount(Account account) {
    // Invoke the actual Stripe payment provider to create the payment account
    return stripePaymentProvider.createAccount(
        new com.midas.app.providers.payment.CreateAccount(
            account.getId().toString(),
            account.getFirstName(),
            account.getLastName(),
            account.getEmail()));
  }
}
