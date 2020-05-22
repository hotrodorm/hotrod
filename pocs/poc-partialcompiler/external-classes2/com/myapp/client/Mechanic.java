package com.myapp.client;

import com.myapp.data.Car;

public class Mechanic {

  public Car fix(final Car c) {
    return new Car(c.getId(), c.getBrand(), c.getYear() + 1);
  }

}
