package org.furynet.server;

@FunctionalInterface
public interface TriConsumer<T, U, V> {

    /**
     * Performs this operation on the given argument.
     *
     * @param t  the first consumer argument
     * @param u  the second consumer argument
     * @param v  the third consumer argument
     */
    void accept(T t, U u, V v);
}