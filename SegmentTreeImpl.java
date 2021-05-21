import java.lang.reflect.Array;

interface Combiner {
    Object combine(Object v1, Object v2);
}

class SegmentTree {
    
    public Object[] tree;
    public int size;
    public Combiner combiner;

    public SegmentTree(int size, Combiner combiner) {
        this.combiner = combiner;
        this.size = size;
        this.tree = new Object[this.size * 4];
    }

    public SegmentTree(Object[] vals, Combiner combiner) {
        this(vals.length, combiner);
        this.buildTree(vals, 0, 0, this.size - 1);
    }
    
    private void buildTree(Object[] vals, int treeIndex, int low, int high) {
        if (high == low) {
            this.tree[treeIndex] = vals[low];
            return;
        }
        int mid = (high + low) / 2;
        buildTree(vals, treeIndex * 2 + 1, low, mid);
        buildTree(vals, treeIndex * 2 + 2, mid + 1, high);
        this.tree[treeIndex] = this.combiner.combine(this.tree[treeIndex * 2 + 1], this.tree[treeIndex * 2 + 2]);
    }
    
    private void updateTree(int treeIndex, int low, int high, int i, Object val) {
        if (high == low) {
            this.tree[treeIndex] = val;
            return;
        }
        int mid = (high + low) / 2;
        if (i > mid) {
            this.updateTree(treeIndex * 2 + 2, mid + 1, high, i, val);
        } else {
            this.updateTree(treeIndex * 2 + 1, low, mid, i, val);
        }
        this.tree[treeIndex] = this.combiner.combine(this.tree[treeIndex * 2 + 1], this.tree[treeIndex * 2 + 2]);
    }
    
    private Object queryTree(int treeIndex, int low, int high, int i, int j) {
        if (i <= low && j >= high) {
            return this.tree[treeIndex];
        }
        int mid = (low + high) / 2;
        if (j <= mid) {
            return this.queryTree(treeIndex * 2 + 1, low, mid, i, j);
        } else if (i > mid) {
            return this.queryTree(treeIndex * 2 + 2, mid + 1, high, i, j);
        } else {
            Object v1 = this.queryTree(treeIndex * 2 + 1, low, mid, i, mid);
            Object v2 = this.queryTree(treeIndex * 2 + 2, mid + 1, high, mid + 1, j);
            return this.combiner.combine(v1, v2);
        }
    }
    
    public void update(int i, Object val) {
        this.updateTree(0, 0, this.size - 1, i, val);
    }

    public Object query(int i, int j) {
        return this.queryTree(0, 0, this.size - 1, i, j);
    }
    
}

public class SegmentTreeImpl {

    public static void main(String[] args) {
        test2();
    }

    public static void test1() {
        Integer[] vals = new Integer[] { 1, 2, 3, 4, 5 };
        SegmentTree st1 = new SegmentTree(vals, (v1, v2) -> (Integer) v1 + (Integer) v2);
        System.out.println(st1.query(0, 4));
    }

    public static void test2() {
        Integer[][] vals = new Integer[][] { { 1, 2, 3, 4, 5 }, { 1, 2, 3, 4, 5 }, { 1, 2, 3, 4, 5 } };
        SegmentTree[] rows = new SegmentTree[vals.length];
        for (int i = 0; i < vals.length; i++) {
            rows[i] = new SegmentTree(vals[i], (x, y) -> x != null && y != null ? (Integer) x + (Integer) y : null);
        }
        SegmentTree st1 = new SegmentTree(rows, (x, y) -> {
            SegmentTree v1 = (SegmentTree) x;
            SegmentTree v2 = (SegmentTree) y;
            SegmentTree result = new SegmentTree(v1.size, v1.combiner);
            for (int i = 0; i < v1.tree.length; i++) {
                result.tree[i] = result.combiner.combine(v1.tree[i], v2.tree[i]);
            }
            return result;
        });
        System.out.println(((SegmentTree) st1.query(1, 2)).query(1, 3));
    }

}