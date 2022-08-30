package com.reactnativegysdk.model;

public class AuthExceptionMessage {
  private int errorCode;
  private String errorDesc;
  private String metadata;
  private int costTime;

  public int getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(int errorCode) {
    this.errorCode = errorCode;
  }

  public String getErrorDesc() {
    return errorDesc;
  }

  public void setErrorDesc(String errorDesc) {
    this.errorDesc = errorDesc;
  }

  public String getMetadata() {
    return metadata;
  }

  public void setMetadata(String metadata) {
    this.metadata = metadata;
  }

  public int getCostTime() {
    return costTime;
  }

  public void setCostTime(int costTime) {
    this.costTime = costTime;
  }
}
