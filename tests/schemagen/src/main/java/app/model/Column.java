package app.model;

import java.io.BufferedWriter;
import java.io.IOException;

public class Column {

  private String name;
  private String type;

  public Column(String name, String type) {
    this.name = name;
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

  // Build

  public void build(BufferedWriter w) throws IOException {
    w.write(this.name + " " + this.type);
  }

}
