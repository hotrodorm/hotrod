package empusambcore.persistence;

public interface Amendable<T, O> extends Persistable<T, O>, Updatable<T>,
    Deletable<T> {
}
