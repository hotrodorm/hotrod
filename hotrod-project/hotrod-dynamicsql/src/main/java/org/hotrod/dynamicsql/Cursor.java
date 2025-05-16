package org.hotrod.dynamicsql;

import java.io.Closeable;

public interface Cursor<T> extends Closeable, Iterable<T> {

}
