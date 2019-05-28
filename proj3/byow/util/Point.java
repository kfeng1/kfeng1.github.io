package byow.util;

import java.util.List;
import java.util.stream.Stream;
import static java.util.stream.Collectors.toList;
import java.util.Random;
import byow.Core.RandomUtils;

public class Point {
    protected final int x;
    protected final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int x() {
        return this.x;
    }

    public int y() {
        return this.y;
    }

    public Point add(Point p) {
        return new Point(x + p.x, y + p.y);
    }

    public Point add(int n) {
        return new Point(x + n, y + n);
    }

    public Point scale(int c) {
        return new Point(x * c, y * c);
    }

    public double dist(int x, int y){
        return Math.sqrt((this.x-x)*(this.x-x)+(this.y-y)*(this.y-y));
    }

    public Stream<Point> add(Stream<Point> points) {
        return points.map(p -> add(p));
    }

    public List<Point> add(List<Point> points) {
        return add(points.stream()).collect(toList());
    }

    public Point clamp(Rect r) {
        int x1 = Math.max(r.p().x(), Math.min(r.p().x() + r.w(), x));
        int y1 = Math.max(r.p().y(), Math.min(r.p().y() + r.h(), y));
        return new Point(x1, y1);
    }

    public List<Point> neighborsDirect() {
        return add(Direction.CARDINAL.stream()
                   .map(d -> d.p())
                   .collect(toList()));
    }

    public List<Point> neighborsDiag() {
        return add(Direction.DIAGONAL.stream()
                   .map(d -> d.p())
                   .collect(toList()));
    }

    public List<Point> neighborsAll() {
        return add(Direction.ALL.stream()
                   .map(d -> d.p())
                   .collect(toList()));
    }

    public List<Point> neighbors() {
        return neighborsDirect();
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }


    public Point addDirection(Direction d) {
        return this.add(d.p());
    }

    public static Point random(Rect bounds, Random rand) {
        int x = RandomUtils.uniform(rand, bounds.x0(), bounds.x1());
        int y = RandomUtils.uniform(rand, bounds.y0(), bounds.y1());
        return new Point(x, y);
    }
}
