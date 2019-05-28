package byow.util;

import java.util.Collections;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class Grid<T> extends Rect {
    private ArrayList<T> data;

    public Grid(int width, int height) {
        super(0, 0, width, height);

        data = new ArrayList<>(Collections.nCopies(width * height, null));
    }

    public boolean has(int x, int y) {
        return x >= 0 && x < w && y >= 0 && y < h;
    }

    public boolean has(Point p) {
        return has(p.x(), p.y());
    }

    public Optional<T> check(int x, int y) {
        return has(x, y) ? Optional.of(get(x, y)) : Optional.empty();
    }

    public Optional<T> check(Point p) {
        return has(p) ? Optional.of(get(p)) : Optional.empty();
    }

    public T get(int x, int y) {
        return data.get(x + y * this.w());
    }

    public T get(Point p) {
        return get(p.x(), p.y());
    }

    public void set(int x, int y, T val) {
        data.set(x + y * this.w(), val);
    }

    public void set(Point p, T val) {
        set(p.x(), p.y(), val);
    }

    public void fill(int x, int y, int w, int h, T val) {
        for (int i = x; i < x + w; i++) {
            for (int j = y; j < y + h; j++) {
                set(i, j, val);
            }
        }
    }

    public void fill(Rect r, T val) {
        fill(r.x(), r.y(), r.w(), r.h(), val);
    }

    public void fill(T val) {
        fill(0, 0, w, h, val);
    }

    public void splice(Grid<T> grid, UnaryOperator<Point> transform) {
        grid.streamPairs().forEach(p -> set(transform.apply(p.k()), p.v()));
    }

    public void splice(Grid<T> grid, Point origin) {
        splice(grid, p -> p.add(origin));
    }

    public void splice(Grid<T> grid) {
        splice(grid, UnaryOperator.identity());
    }

    public Stream<T> stream() {
        return data.stream();
    }

    public Stream<Pair<Point, T>> streamPairs() {
        return StreamUtils.zip(streamPoints(), stream(), (p, v) -> Pair.of(p, v));
    }

    public Stream<T> map(UnaryOperator<T> fn) {
        return streamPoints()
                .map(p -> {
                    T v = fn.apply(get(p));
                    set(p, v);
                    return v;
                });
    }

    public Stream<T> mapPoints(Function<Point, T> fn) {
        return streamPoints()
                .map(p -> {
                    T v = fn.apply(p);
                    set(p, v);
                    return v;
                });
    }
}
