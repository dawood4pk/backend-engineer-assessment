package com.midas.app.workflows;

import com.midas.app.activities.AccountActivity;
import com.midas.app.models.Account;
import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import java.time.Duration;

public class CreateAccountWorkflowImpl implements CreateAccountWorkflow {

  // Activity stubs are created here, using the Activity interface
  private final AccountActivity accountActivity =
      Workflow.newActivityStub(
          AccountActivity.class,
          ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofMinutes(1)).build());

  @Override
  public Account createAccount(Account details) {
    // Log the initiation of account creation
    Workflow.getLogger(CreateAccountWorkflowImpl.class)
        .info("Starting account creation for: {}", details.getEmail());

    // Log initiation of account creation
    Workflow.getLogger(CreateAccountWorkflowImpl.class)
        .info("Starting account creation for: {}", details.getEmail());

    // Save the account using the activity
    Account savedAccount = accountActivity.saveAccount(details);

    // Create the payment profile using the activity
    Account paymentAccount = accountActivity.createPaymentAccount(savedAccount);
    if (paymentAccount == null) {
      throw new RuntimeException("Failed to create payment profile.");
    }

    // Update the savedAccount with provider details from paymentAccount
    savedAccount.setProviderType(paymentAccount.getProviderType());
    savedAccount.setProviderId(paymentAccount.getProviderId());

    // Save the updated account with provider details
    Account updatedAccount = accountActivity.saveAccount(savedAccount);

    return updatedAccount;
  }
}
