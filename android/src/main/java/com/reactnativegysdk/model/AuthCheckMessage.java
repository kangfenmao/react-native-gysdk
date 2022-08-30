package com.reactnativegysdk.model;

public class AuthCheckMessage {
  private String msg;
  private String process_id;
  private String operatorType;
  private int clienttype;
  private String number;
  private Long expiredTime;

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
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

  public int getClienttype() {
    return clienttype;
  }

  public void setClienttype(int clienttype) {
    this.clienttype = clienttype;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public Long getExpiredTime() {
    return expiredTime;
  }

  public void setExpiredTime(Long expireTime) {
    this.expiredTime = expireTime;
  }
}
