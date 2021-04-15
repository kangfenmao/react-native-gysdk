package com.reactnativegysdk.model;

public class AuthFailureMessage {
  private String errorCode;
  private String process_id;
  private String operatorType;
  private String clienttype;
  private AuthFailureMetadata metadata;

  public int getErrorCode() {
    return Integer.valueOf(errorCode);
  }

  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }

  public String getProcess_id() {
    return process_id;
  }

  public void setProcess_id(String process_id) {
    this.process_id = process_id;
  }

  public String getOperatorType() {
    return operatorType;
  }

  public void setOperatorType(String operatorType) {
    this.operatorType = operatorType;
  }

  public String getClienttype() {
    return clienttype;
  }

  public void setClienttype(String clienttype) {
    this.clienttype = clienttype;
  }

  public AuthFailureMetadata getMetadata() {
    return metadata;
  }

  public void setMetadata(AuthFailureMetadata metadata) {
    this.metadata = metadata;
  }
}
