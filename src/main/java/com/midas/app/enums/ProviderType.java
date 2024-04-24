package com.midas.app.enums;

public enum ProviderType {
  STRIPE("stripe");

  private final String value;

  ProviderType(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return this.value;
  }

  public static ProviderType fromValue(String value) {
    for (ProviderType b : ProviderType.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}
