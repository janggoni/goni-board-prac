package goni.board.articleread.cache;

@FunctionalInterface
public interface OptimizedCacheOriginDataSupplier<T> {
    T get() throws Throwable;
}
