package org.hotrod.torcs.setters;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BlobSetter extends Setter {

  private int type;
  private Blob blob;
  private InputStream inputStream;
  private long length;

//void  setBlob(int parameterIndex, Blob x)
//void  setBlob(int parameterIndex, InputStream inputStream)
//void  setBlob(int parameterIndex, InputStream inputStream, long length)

  public BlobSetter(int index, Blob x) {
    super(index);
    this.type = 1;
    this.blob = x;
    this.inputStream = null;
  }

  public BlobSetter(int index, InputStream inputStream) {
    super(index);
    this.type = 2;
    this.blob = null;
    this.inputStream = inputStream;
  }

  public BlobSetter(int index, InputStream inputStream, long length) {
    super(index);
    this.type = 3;
    this.blob = null;
    this.inputStream = inputStream;
    this.length = length;
  }

  @Override
  public void applyTo(PreparedStatement ps) throws SQLException {
    if (this.type == 1) {
      ps.setBlob(this.index, this.blob);
    } else if (this.type == 2) {
      ps.setBlob(this.index, this.inputStream);
    } else {
      ps.setBlob(this.index, this.inputStream, this.length);
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
