package com.midas.app.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.midas.app.models.Account;
import com.midas.app.providers.external.stripe.StripeConfiguration;
import com.midas.app.providers.external.stripe.StripePaymentProvider;
import com.midas.app.providers.payment.CreateAccount;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.param.CustomerCreateParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class StripePaymentProviderTest {

  private StripeConfiguration stripeConfiguration;
  private StripePaymentProvider stripePaymentProvider;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    stripeConfiguration = new StripeConfiguration();
    stripeConfiguration.setApiKey("sk_test_apikey");
    stripePaymentProvider = new StripePaymentProvider(stripeConfiguration);
  }

  @Test
  void testCreateAccount() throws StripeException {
    CreateAccount createAccountDetails =
        new CreateAccount("userId", "John", "Doe", "john.doe@example.com");

    // Mock the Stripe Customer class
    try (MockedStatic<Customer> mockedCustomer = Mockito.mockStatic(Customer.class)) {
      Customer mockCustomer = Mockito.mock(Customer.class);
      when(mockCustomer.getId()).thenReturn("cus_testcustomerid");

      // When Customer.create is called with any CustomerCreateParams,
      // then return the mockCustomer
      mockedCustomer
          .when(() -> Customer.create(any(CustomerCreateParams.class)))
          .thenReturn(mockCustomer);

      // Call the method under test
      Account account = stripePaymentProvider.createAccount(createAccountDetails);

      // Assertions
      assertNotNull(account);
      assertEquals("john.doe@example.com", account.getEmail());
      assertEquals("John", account.getFirstName());
      assertEquals("Doe", account.getLastName());
      assertEquals("cus_testcustomerid", account.getProviderId());
    }
  }
}
