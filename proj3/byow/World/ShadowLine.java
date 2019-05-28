package byow.World;

import java.util.List;
import java.util.ArrayList;


public class ShadowLine {
    List<Shadow> shadows;

    public ShadowLine(){
        shadows = new ArrayList<>();
    }

    public class Shadow {
        int start;
        int end;

        public Shadow(int start, int end) {
            this.start = start;
            this.end = end;
        }

        public boolean contains(Shadow other) {
            return start <= other.start && end >= other.end;
        }


    }
    public Shadow projectTile(int row, int col) {
        var topLeft = col / (row + 2);
        var bottomRight = (col + 1) / (row + 1);
        return new Shadow(topLeft, bottomRight);
    }




    public boolean isInShadow(Shadow projection) {
        for (var shadow : shadows) {
            if (shadow.contains(projection)) return true;
        }
        return false;
    }

    public void add(Shadow shadow) {
        // Figure out where to slot the new shadow in the list.
        var index = 0;
        for (; index < shadows.size(); index++) {
            // Stop when we hit the insertion point.
            if (shadows.get(index).start >= shadow.start) break;
        }

        // The new shadow is going here. See if it overlaps the
        // previous or next.
        Shadow overlappingPrevious = null;
        if (index > 0 && shadows.get(index).end > shadow.start) {
            overlappingPrevious = shadows.get(index);
        }

        Shadow overlappingNext = null;
        if (index < shadows.size() &&
                shadows.get(index).start < shadow.end) {
            overlappingNext = shadows.get(index);
        }

        // Insert and unify with overlapping shadows.
        if (overlappingNext != null) {
            if (overlappingPrevious != null) {
                // Overlaps both, so unify one and delete the other.
                overlappingPrevious.end = overlappingNext.end;
                shadows.remove(index);
            } else {
                // Overlaps the next one, so unify it with that.
                overlappingNext.start = shadow.start;
            }
        } else {
            if (overlappingPrevious != null) {
                // Overlaps the previous one, so unify it with that.
                overlappingPrevious.end = shadow.end;
            } else {
                // Does not overlap anything, so insert.
                shadows.add(index, shadow);
            }
        }
    }

    public boolean isFullShadow (){
        return shadows.size() == 1 &&
                shadows.get(0).start == 0 &&
                shadows.get(0).end == 1;
    }

}





