package empusambcore.persistence;

public interface Persistable<T, O> extends Insertable<T>, Selectable<T, O> {
}
