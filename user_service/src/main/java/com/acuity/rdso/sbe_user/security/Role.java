package com.acuity.rdso.sbe_user.security;

public enum Role {
  ADMIN("ADMIN"),
  AGENT("AGENT"),
  CUSTOMER("CUSTOMER");

  private final String name;

  Role(final String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
