package com.acuity.rdso.sbe_user.security;

public class TokenHolder {
  private static final ThreadLocal<String> tokenHolder = new ThreadLocal<>();

  public static String getToken() {
    return tokenHolder.get();
  }
}
