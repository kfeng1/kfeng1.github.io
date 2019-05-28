package byow.util;

public class Pair<A, B> {
    private final A a;
    private final B b;

    public static <A, B> Pair<A, B> of(A a, B b) {
        return new Pair<A, B>(a, b);
    }

    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public A a() {
        return a;
    }

    public A k() {
        return a;
    }

    public B b() {
        return b;
    }

    public B v() {
        return b;
    }
}
