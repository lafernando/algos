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

class SegmentTree2D {

    public SegmentTree[] tree;
    public SegmentTree[] rows;
    public int size;
    public Combiner combiner;

    public SegmentTree2D(Object[][] vals, Combiner combiner) {
        this.size = vals.length;
        this.combiner = combiner;
        this.tree = new SegmentTree[this.size * 4];
        this.rows = this.buildRows(vals, combiner);
        this.buildTree(this.rows, 0, 0, this.size - 1);
    }

    private SegmentTree[] buildRows(Object[][] vals, Combiner combiner) {
        SegmentTree[] rows = new SegmentTree[vals.length];
        for (int i = 0; i < vals.length; i++) {
            rows[i] = new SegmentTree(vals[i], combiner);
        }
        return rows;
    }

    private void buildTree(SegmentTree[] vals, int treeIndex, int low, int high) {
        if (high == low) {
            this.tree[treeIndex] = vals[low];
            return;
        }
        int mid = (high + low) / 2;
        buildTree(vals, treeIndex * 2 + 1, low, mid);
        buildTree(vals, treeIndex * 2 + 2, mid + 1, high);
        this.tree[treeIndex] = this.combine(this.tree[treeIndex * 2 + 1], this.tree[treeIndex * 2 + 2]);
    }

    private SegmentTree combine(SegmentTree v1, SegmentTree v2) {
        SegmentTree result = new SegmentTree(v1.size, v1.combiner);
        for (int i = 0; i < v1.tree.length; i++) {
            result.tree[i] = result.combiner.combine(v1.tree[i], v2.tree[i]);
        }
        return result;
    }

    private void updateTree(int treeIndex, int low, int high, int i, SegmentTree val) {
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
        this.tree[treeIndex] = this.combine(this.tree[treeIndex * 2 + 1], this.tree[treeIndex * 2 + 2]);
    }

    public void update(int row, int col, Object val) {
        SegmentTree rowst = this.rows[row];
        rowst.update(col, val);
        this.updateTree(0, 0, this.size - 1, row, rowst);
    }

    private Object queryTree(int treeIndex, int low, int high, int row1, int col1, int row2, int col2) {
        if (row1 <= low && row2 >= high) {
            return this.tree[treeIndex].query(col1, col2);
        }
        int mid = (low + high) / 2;
        if (row2 <= mid) {
            return this.queryTree(treeIndex * 2 + 1, low, mid, row1, col1, row2, col2);
        } else if (row1 > mid) {
            return this.queryTree(treeIndex * 2 + 2, mid + 1, high, row1, col1, row2, col2);
        } else {
            Object v1 = this.queryTree(treeIndex * 2 + 1, low, mid, row1, col1, mid, col2);
            Object v2 = this.queryTree(treeIndex * 2 + 2, mid + 1, high, mid + 1, col1, row2, col2);
            return this.combiner.combine(v1, v2);
        }
    }

    public Object query(int row1, int col1, int row2, int col2) {
        return this.queryTree(0, 0, this.size - 1, row1, col1, row2, col2);
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
        Integer[][] vals = new Integer[][] { { 3,0,1,4,2 }, { 5,6,3,2,1 }, { 1,2,0,1,5 }, { 4,1,0,1,7 }, { 1,0,3,0,5 } };
        SegmentTree2D st = new SegmentTree2D(vals, (x, y) -> x != null && y != null ? (Integer) x + (Integer) y : null);
        System.out.println(st.query(2, 1, 4, 3));
        st.update(3, 1, 11);
        System.out.println(st.query(2, 1, 4, 3));
    }

}