package byow.WorldGen;

import byow.util.Point;
import byow.util.Rect;
import byow.Core.RandomUtils;

import java.util.ArrayList;
import java.util.Random;
import java.util.List;

public class RectGenerator {
    private static final int MINRECTCOUNT = 5;

    public static List<Rect> generate(Rect bounds, Random rand) {
        int rectAttemps = RandomUtils.uniform(rand, 1, 100);
        ArrayList<Rect> rectList = new ArrayList();
        while (rectList.size() < MINRECTCOUNT) {
            for (int i = 0; i < rectAttemps; i++) {
                int width = RandomUtils.uniform(rand, 2, 6) * 2 + 1;
                int height = RandomUtils.uniform(rand, 2, 6) * 2 + 1;
                int maxX = (bounds.w() - width) / 2 - 1;
                int maxY = (bounds.h() - height) / 2 - 1;
                int xPos = RandomUtils.uniform(rand, 0, maxX) * 2 + 1;
                int yPos = RandomUtils.uniform(rand, maxY) * 2 + 1;
                Point newPoint = new Point(xPos, yPos);
                Rect newRect = new Rect(bounds.p().add(newPoint), width, height);

                boolean canAdd = true;

                for (Rect r : rectList) {
                    if (r.overlaps(newRect)) {
                        canAdd = false;
                    }
                }
                if (canAdd) {
                    rectList.add(newRect);
                }
            }
        }
        return rectList;
    }
}
