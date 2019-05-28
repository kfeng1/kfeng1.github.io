package byow.util;

import java.util.stream.Stream;
import java.util.stream.IntStream;
import java.util.function.Function;

public class Rect {
    protected final Point pos;
    protected final int w;
    protected final int h;

    public Rect(int x, int y, int width, int height) {
        this.pos = new Point(x, y);
        this.w = width;
        this.h = height;
    }

    public Rect(Point lowerLeft, int width, int height) {
        this(lowerLeft.x(), lowerLeft.y(), width, height);
    }

    public Point p() {
        return pos;
    }

    public int x() {
        return pos.x();
    }

    public int y() {
        return pos.y();
    }

    public int x0() {
        return pos.x();
    }

    public int y0() {
        return pos.y();
    }

    public int x1() {
        return pos.x() + w;
    }

    public int y1() {
        return pos.y() + h;
    }

    public int  w() {
        return w;
    }

    public int h() {
        return h;
    }

    public Point getUpperRight() {
        int xCoord = pos.x() + w - 1;
        int yCoord = pos.y() + h - 1;
        return new Point(xCoord, yCoord);
    }

    public boolean overlaps(Rect r) {
        return x0() < r.x1() && x1() > r.x0() && y0() < r.y1() && y1() > r.y0();
    }

    public Stream<Point> streamPoints(int xstep, int ystep) {
        return IntStream.range(0, h / ystep)
                .mapToObj(py -> IntStream.range(0, w / xstep)
                    .mapToObj(px -> new Point(pos.x() + px * xstep, pos.y() + py * ystep)))
                .flatMap(Function.identity());
    }

    public Stream<Point> streamPoints() {
        return streamPoints(1, 1);
    }
}
