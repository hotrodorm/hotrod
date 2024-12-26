package org.hotrod.interfaces;

@Deprecated
public interface Persistable<T, O> extends Insertable<T>, Selectable<T, O>,
    Updatable<T>, Deletable<T> {
}
