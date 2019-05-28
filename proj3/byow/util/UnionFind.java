package byow.util;

public class UnionFind {


    private int[] parent;

    /* Creates a UnionFind data structure holding n vertices. Initially, all
       vertices are in disjoint sets. */
    public UnionFind(int n) {
        parent = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = -1;
        }
    }

    /* Throws an exception if v1 is not a valid index. */
    private void validate(int vertex) {
        if (vertex >= parent.length || vertex < 0) {
            throw new IllegalArgumentException("not a valid index");
        }
    }

    /* Returns the size of the set v1 belongs to. */

    /**
     * @source lab6
     */
    public int sizeOf(int v1) {
        int r = find(v1);
        return parent[r] * -1;
    }


    /* Returns the parent of v1. If v1 is the root of a tree, returns the
       negative size of the tree for which v1 is the root. */

    /**
     * @source lab6
     */
    public int parent(int v1) {
        int holder = parent[v1];
        return holder;

    }

    /* Returns true if nodes v1 and v2 are connected. */
    public boolean connected(int v1, int v2) {
        return find(v1) == find(v2);
    }

    /* Connects two elements v1 and v2 together. v1 and v2 can be any valid
       elements, and a union-by-size heuristic is used. If the sizes of the sets
       are equal, tie break by connecting v1's root to v2's root. Unioning a
       vertex with itself or vertices that are already connected should not
       change the sets but may alter the internal structure of the data. */

    /**
     * @source lab6
     */
    public void union(int v1, int v2) {
        int rootone = find(v1);
        int roottwo = find(v2);
        if (rootone != roottwo) {
            if (parent[rootone] < parent[roottwo]) {
                parent[rootone] += parent[roottwo];
                parent[roottwo] = rootone;
            } else {
                parent[roottwo] += parent[rootone];
                parent[rootone] = roottwo;
            }
        }
    }

    /* Returns the root of the set V belongs to. Path-compression is employed
       allowing for fast search-time. */

    /**
     * @source lab6
     */
    public int find(int vertex) {
        validate(vertex);
        if (parent[vertex] < 0) {
            return vertex;
        }
        parent[vertex] = find(parent[vertex]);
        return parent[vertex];

    }

}

