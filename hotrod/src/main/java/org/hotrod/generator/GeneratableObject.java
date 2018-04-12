package org.hotrod.generator;

public abstract class GeneratableObject {

  private boolean generated = false;

  public void markGenerated() {
    this.generated = true;
  }

  public boolean isGenerated() {
    return this.generated;
  }

}
