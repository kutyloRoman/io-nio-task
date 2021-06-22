package com.kutylo.task1.model;

import java.io.Serializable;

public class ElectronicItem extends Item implements Serializable {
  private String country;

  @Override
  public String toString() {
    return "ElectronicItem{" +
        "country='" + country + '\'' +
        "} " + super.toString();
  }
}
