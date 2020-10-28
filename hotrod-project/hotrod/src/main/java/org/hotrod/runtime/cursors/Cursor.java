package org.hotrod.runtime.cursors;

import java.io.Closeable;

public interface Cursor<T> extends Closeable, Iterable<T> {

}
