package com.reactnativegysdk.model;

public class AuthSuccessMessage {
  private int verifyType;
  private AuthSuccessData data;

  public int getVerifyType() {
    return verifyType;
  }

  public void setVerifyType(int verifyType) {
    this.verifyType = verifyType;
  }

  public AuthSuccessData getData() {
    return data;
  }

  public void setData(AuthSuccessData data) {
    this.data = data;
  }
}
