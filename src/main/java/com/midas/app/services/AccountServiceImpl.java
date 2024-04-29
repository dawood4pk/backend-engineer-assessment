package com.midas.app.services;

import com.midas.app.exceptions.ResourceAlreadyExistsException;
import com.midas.app.models.Account;
import com.midas.app.providers.external.stripe.StripePaymentProvider;
import com.midas.app.providers.payment.CreateAccount;
import com.midas.app.repositories.AccountRepository;
import com.midas.app.workflows.CreateAccountWorkflow;
import com.midas.generated.model.CreateAccountDto;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.workflow.Workflow;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
  private final Logger logger = Workflow.getLogger(AccountServiceImpl.class);

  private final WorkflowClient workflowClient;

  private final AccountRepository accountRepository;

  private final StripePaymentProvider stripePaymentProvider;

  /**
   * createAccount creates a new account in the system or provider.
   *
   * @param details is the details of the account to be created.
   * @return Account
   */
  @Override
  public Account createAccount(Account details) {
    if (accountRepository.existsByEmail(details.getEmail())) {
      throw new ResourceAlreadyExistsException("Email already in use");
    }
    var options =
        WorkflowOptions.newBuilder()
            .setTaskQueue(CreateAccountWorkflow.QUEUE_NAME)
            .setWorkflowId(details.getEmail())
            .build();

    logger.info("initiating workflow to create account for email: {}", details.getEmail());

    var workflow = workflowClient.newWorkflowStub(CreateAccountWorkflow.class, options);
    return workflow.createAccount(details);
  }

  /**
   * getAccounts returns a list of accounts.
   *
   * @return List<Account>
   */
  @Override
  public List<Account> getAccounts() {
    return accountRepository.findAll();
  }

  @Override
  public Account updateAccount(UUID accountId, CreateAccountDto accountDetails) {
    return accountRepository
        .findById(accountId)
        .map(
            account -> {
              if (!account.getEmail().equals(accountDetails.getEmail())
                  && accountRepository.existsByEmail(accountDetails.getEmail())) {
                throw new ResourceAlreadyExistsException("Email already in use");
              }
              // Update Stripe customer details
              CreateAccount stripeUpdate =
                  new CreateAccount(
                      account.getId().toString(),
                      accountDetails.getFirstName(),
                      accountDetails.getLastName(),
                      accountDetails.getEmail());
              Account updatedStripeAccount =
                  stripePaymentProvider.updateAccount(stripeUpdate, account.getProviderId());

              if (updatedStripeAccount == null) {
                throw new RuntimeException("Failed to update Stripe details.");
              }
              // Update local account details
              account.setFirstName(accountDetails.getFirstName());
              account.setLastName(accountDetails.getLastName());
              account.setEmail(accountDetails.getEmail());
              // ... Set other fields as necessary
              return accountRepository.save(account);
            })
        .orElseThrow(() -> new NoSuchElementException("Account not found with ID: " + accountId));
  }
}
