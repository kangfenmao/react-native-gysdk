package com.reactnativegysdk.model;

public class AuthSuccessData {
  private String token;
  private Long expiredTime;

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Long getExpiredTime() {
    return expiredTime;
  }

  public void setExpiredTime(Long expiredTime) {
    this.expiredTime = expiredTime;
  }
}
