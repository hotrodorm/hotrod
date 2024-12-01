package org.hotrod.cursors;

import java.io.Closeable;

public interface Cursor<T> extends Closeable, Iterable<T> {

}
