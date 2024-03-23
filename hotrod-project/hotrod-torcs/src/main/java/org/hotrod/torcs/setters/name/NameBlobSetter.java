package org.hotrod.torcs.setters.name;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.SQLException;

public class NameBlobSetter extends NameSetter {

  private int type;
  private Blob blob;
  private InputStream inputStream;
  private long length;

  public NameBlobSetter(String name, Blob x) {
    super(name);
    this.type = 1;
    this.blob = x;
    this.inputStream = null;
  }

  public NameBlobSetter(String name, InputStream inputStream) {
    super(name);
    this.type = 2;
    this.blob = null;
    this.inputStream = inputStream;
  }

  public NameBlobSetter(String name, InputStream inputStream, long length) {
    super(name);
    this.type = 3;
    this.blob = null;
    this.inputStream = inputStream;
    this.length = length;
  }

  @Override
  public boolean isLOBParameter() {
    return false;
  }

  @Override
  public void applyTo(CallableStatement cs) throws SQLException {
    if (this.type == 1) {
      cs.setBlob(this.name, this.blob);
    } else if (this.type == 2) {
      cs.setBlob(this.name, this.inputStream);
    } else {
      cs.setBlob(this.name, this.inputStream, this.length);
    }
  }

  @Override
  public Object value() {
    return this.blob;
  }

  @Override
  public String guessSQLServerDataType() {
    return "binary";
  }

}
