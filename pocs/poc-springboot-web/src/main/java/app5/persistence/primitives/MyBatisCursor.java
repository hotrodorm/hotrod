package app5.persistence.primitives;

import java.io.IOException;
import java.util.Iterator;

import org.hotrod.runtime.cursors.Cursor;

public class MyBatisCursor<T> implements Cursor<T> {

  private org.apache.ibatis.cursor.Cursor<T> cursor;

  public MyBatisCursor(final org.apache.ibatis.cursor.Cursor<T> cursor) {
    this.cursor = cursor;
  }

  @Override
  public Iterator<T> iterator() {
    return this.cursor.iterator();
  }

  @Override
  public void close() throws IOException {
    this.cursor.close();
  }

}
