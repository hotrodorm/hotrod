package org.hotrod.interfaces;

public interface Persistable<T, O> extends Insertable<T>, Selectable<T, O>,
    Updatable<T>, Deletable<T> {
}
