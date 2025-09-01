package com.myapp.data;

public class Car {

  private Integer id;
  private String brand;
  private Integer year;

  public Car(final Integer id, final String brand, final Integer year) {
    this.id = id;
    this.brand = brand;
    this.year = year;
  }

  public Integer getId() {
    return id;
  }

  public String getBrand() {
    return brand;
  }

  public Integer getYear() {
    return year;
  }

}
