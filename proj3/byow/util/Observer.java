package byow.util;

public interface Observer<T> {
    void notified(T val);
}
