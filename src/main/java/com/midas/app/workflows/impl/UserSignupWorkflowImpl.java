package com.midas.app.workflows.impl;

import com.midas.app.services.StripeService;
import com.midas.app.workflows.UserSignupWorkflow;

public class UserSignupWorkflowImpl implements UserSignupWorkflow {

  private final StripeService stripeService;

  public UserSignupWorkflowImpl(StripeService stripeService) {
    this.stripeService = stripeService;
  }

  @Override
  public String createUser(String email) {
    // Create a Stripe customer
    var customer = stripeService.createStripeCustomer(email);
    if (customer != null) {
      return customer.getId();
    } else {
      return null; // Or handle more appropriately
    }
  }
}
