package com.midas.app.enums;

public enum ProviderType {
  STRIPE;

  public String asString() {
    return name();
  }
}
