package com.midas.app.providers.external.stripe;

import com.midas.app.enums.ProviderType;
import com.midas.app.models.Account;
import com.midas.app.providers.payment.CreateAccount;
import com.midas.app.providers.payment.PaymentProvider;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.param.CustomerCreateParams;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class StripePaymentProvider implements PaymentProvider {
  private final Logger logger = LoggerFactory.getLogger(StripePaymentProvider.class);

  private final StripeConfiguration configuration;

  public StripePaymentProvider(StripeConfiguration configuration) {
    this.configuration = configuration;
    Stripe.apiKey = configuration.getApiKey();
  }

  /** providerName is the name of the payment provider */
  @Override
  public String providerName() {
    return "stripe";
  }

  /**
   * Creates a new customer in Stripe.
   *
   * @param details the details of the account to be created.
   * @return Account populated with Stripe customer ID as providerId if successful.
   */
  @Override
  public Account createAccount(CreateAccount details) {
    CustomerCreateParams params =
        CustomerCreateParams.builder()
            .setEmail(details.getEmail())
            .setName(details.getFirstName() + " " + details.getLastName())
            .build();

    try {
      Customer customer = Customer.create(params);
      logger.info("Created Stripe customer with ID: {}", customer.getId());

      // Create an Account object to return
      Account account = new Account();
      account.setEmail(details.getEmail());
      account.setFirstName(details.getFirstName());
      account.setLastName(details.getLastName());
      account.setProviderType(ProviderType.STRIPE);
      account.setProviderId(customer.getId()); // Set the Stripe customer ID

      return account;
    } catch (StripeException e) {
      logger.error("Error creating Stripe customer: {}", e.getMessage(), e);
      return null;
    }
  }

  @Override
  public Account updateAccount(CreateAccount updateDetails, String providerId) {
    try {
      Customer customer = Customer.retrieve(providerId);
      Map<String, Object> updates = new HashMap<>();
      updates.put("email", updateDetails.getEmail());
      updates.put("name", updateDetails.getFirstName() + " " + updateDetails.getLastName());
      customer.update(updates);

      Account account = new Account();
      // Populate account details from the updateDetails and the response from Stripe
      // ...

      return account;
    } catch (StripeException e) {
      logger.error("Error updating Stripe customer: {}", e.getMessage(), e);
      return null;
    }
  }
}
