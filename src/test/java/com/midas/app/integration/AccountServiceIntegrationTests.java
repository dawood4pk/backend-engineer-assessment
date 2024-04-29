package com.midas.app.integration;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.midas.app.controllers.AccountController;
import com.midas.app.models.Account;
import com.midas.app.providers.external.stripe.StripeConfiguration;
import com.midas.app.providers.external.stripe.StripePaymentProvider;
import com.midas.app.providers.payment.CreateAccount;
import com.midas.app.services.AccountService;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.param.CustomerCreateParams;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AccountController.class)
class AccountServiceIntegrationTests {

  @Autowired private MockMvc mockMvc;

  @MockBean private AccountService accountService;
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
  void testCreateAccountStripeIntegration() throws StripeException {
    CreateAccount createAccountDetails =
        new CreateAccount("userId", "John", "Doe", "john.doe@example.com");

    try (MockedStatic<Customer> mockedCustomer = Mockito.mockStatic(Customer.class)) {
      Customer mockCustomer = Mockito.mock(Customer.class);
      when(mockCustomer.getId()).thenReturn("cus_testcustomerid");
      mockedCustomer
          .when(() -> Customer.create(any(CustomerCreateParams.class)))
          .thenReturn(mockCustomer);

      Account account = stripePaymentProvider.createAccount(createAccountDetails);

      assertNotNull(account);
      assertEquals("john.doe@example.com", account.getEmail());
      assertEquals("John", account.getFirstName());
      assertEquals("Doe", account.getLastName());
      assertEquals("cus_testcustomerid", account.getProviderId());
    }
  }

  @Test
  void testCreateUserAccount() throws Exception {
    Account account = new Account();
    account.setId(UUID.randomUUID());
    account.setEmail("john.doe@example.com");
    account.setFirstName("John");
    account.setLastName("Doe");

    given(accountService.createAccount(any(Account.class))).willReturn(account);

    mockMvc
        .perform(
            post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john.doe@example.com\"}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.email", is("john.doe@example.com")));
  }

  @Test
  void testGetUserAccounts() throws Exception {
    List<Account> accounts =
        List.of(
            new Account(
                UUID.randomUUID(), "John", "Doe", "john@example.com", null, null, null, null));
    when(accountService.getAccounts()).thenReturn(accounts);

    mockMvc
        .perform(get("/accounts").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].email", is("john@example.com")));
  }
}
