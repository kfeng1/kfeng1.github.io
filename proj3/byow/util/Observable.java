package byow.util;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Observable<T> {
    private final Set<Observer> observers = ConcurrentHashMap.newKeySet();

    public void register(Observer o) {
        observers.add(o);
    }

    public void deregister(Observer o) {
        observers.remove(o);
    }

    protected void notify(T v) {
        observers.forEach(o -> o.notified(v));
    }
}
