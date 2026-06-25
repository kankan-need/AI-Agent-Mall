package com.learn.mall.api.auth.constant;

public enum SysTypeEnum {

  ORDINARY(0),
  MULTISHOP(1),
  PLATFORM(2);

  private final int value;

  SysTypeEnum(int value) {
    this.value = value;
  }

  public int value() {
    return value;
  }
}
