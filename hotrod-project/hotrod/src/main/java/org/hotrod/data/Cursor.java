package org.hotrod.data;

import java.io.Closeable;

public interface Cursor<T> extends Closeable, Iterable<T> {

}
