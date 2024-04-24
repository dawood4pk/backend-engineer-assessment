package com.midas.app.services;

import com.stripe.Stripe;
import com.stripe.model.Customer;
import com.stripe.param.CustomerCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

  @Value("${stripe.api-key}")
  private String apiKey;

  public Customer createStripeCustomer(String email) {
    try {
      Stripe.apiKey = apiKey;

      CustomerCreateParams params = CustomerCreateParams.builder().setEmail(email).build();

      return Customer.create(params);
    } catch (Exception e) {
      // Handle exceptions appropriately
      e.printStackTrace();
      return null;
    }
  }
}
