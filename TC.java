import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

class Edge <T extends Comparable> {

    public T vertex;
    public int weight;

    public Edge(T vertext, int weight) {
        this.vertex = vertext;
        this.weight = weight;
    }

}

class Graph <T extends Comparable> {

    public Map<T, List<Edge<T>>> adjVerts = new HashMap<>();

    public void addVert(T label) {
        if (!this.adjVerts.containsKey(label)) {
            this.adjVerts.put(label, new ArrayList<>());
        }
    }

    public void addEdge(T source, T target, int weight) {
        this.addVert(source);
        this.addVert(target);
        this.adjVerts.get(source).add(new Edge(target, weight));
    }

    public void addUEdge(T vert1, T vert2, int weight) {
        this.addEdge(vert1, vert2, weight);
        this.addEdge(vert2, vert1, weight);
    }

    public void addEdge(T source, T target) {
        this.addEdge(source, target, 1);
    }

    public void addUEdge(T vert1, T vert2) {
        this.addUEdge(vert1, vert2, 1);
    }

    public List<Edge<T>> getEdges(T vert) {
        return this.adjVerts.get(vert);
    }

    public Set<T> getVertices() {
        return this.adjVerts.keySet();
    }

}

class Path <T extends Comparable> implements Comparable<Path<T>> {

    public T vertex;
    public int cost;
    public Path<T> parent;

    public Path(Path<T> parent, T vertex, int weight) {
        this.parent = parent;
        this.vertex = vertex;
        this.cost = weight + ((parent == null) ? 0 : parent.cost);
    }

    @Override
    public int compareTo(Path<T> rhs) {
        Integer lhs = this.cost;
        return lhs.compareTo(rhs.cost);
    }

    public List<T> vertices() {
        List<T> result = new ArrayList<>();
        result.add(this.vertex);
        while (parent != null) {
            result.add(parent.vertex);
            parent = parent.parent;
        }
        Collections.reverse(result);
        return result;
    }

    @Override
    public String toString() {
        return "Cost: " + this.cost + " Path: " + this.vertices();
    }

}

interface ExecFunc<T extends Comparable> {
    void run(T val);
}

class Node {
    
    private Node left, right;
    
    private int data;

    public int fullcount;

    public int mycount;
    
    public Node(int data) {
        this.data = data;
        this.fullcount = 1;
        this.mycount = 1;
    }
    
    public Node insert(int value) {
        if (value == this.data) {
            this.mycount++;
        } else if (value < this.data) {
            if (this.left == null) {
                this.left = new Node(value);
            } else {
                this.left = this.left.insert(value);
            }
        } else {
            if (this.right == null) {
                this.right = new Node(value);
            } else {
                this.right = this.right.insert(value);
            }
        }
        this.fullcount++;
        this.update();
        return this.balance();
    }

    private void update() {

    }

    private Node balance() {
        return this;
    }

    public int find(int value) {
        if (this.data == value) {
            int result = 0;
            if (this.left != null) {
                result += this.left.fullcount;
            }
            return result;
        }
        if (value < this.data) {
            if (this.left == null) {
                return -1;
            } else {
                return this.left.find(value);
            }
        } else {
            int tmp = 0;
            if (this.left != null) {
                tmp += this.left.fullcount;
            }
            if (this.right == null) {
                return -1;
            } else {
                return this.right.find(value) + tmp + this.mycount;
            }
        }
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{" + this.data + "(" + this.mycount + ")");
        if (this.left != null) {
            builder.append(", L->" + this.left);
        }
        if (this.right != null) {
            builder.append(", R->" + this.right);
        }
        builder.append("}");
        return builder.toString();
    }

}

public class TC {

    public static <T extends Comparable> Path<T> dijkstra(Graph<T> graph, T source, T target) {
        PriorityQueue<Path<T>> queue = new PriorityQueue<>();
        Set<T> visited = new HashSet<T>();
        queue.add(new Path(null, source, 0));
        while (!queue.isEmpty()) {
            Path<T> path = queue.remove();
            if (path.vertex.equals(target)) {
                return path;
            }
            if (!visited.contains(path.vertex)) {
                for (Edge<T> edge : graph.getEdges(path.vertex)) {
                    queue.add(new Path(path, edge.vertex, edge.weight));
                }
                visited.add(path.vertex);
            }
        }
        return null;
    }

    public static <T extends Comparable> void dfs(Graph<T> graph, ExecFunc<T> func) {
        Set<T> visited = new HashSet<T>();
        for (T vertx : graph.getVertices()) {
            dfsx(graph, func, visited, vertx);
        }
    }

    private static <T extends Comparable> void dfsx(Graph<T> graph, ExecFunc func, Set<T> visited, T vertx) {
        if (visited.contains(vertx)) return;
        for (Edge<T> edge : graph.getEdges(vertx)) {
            dfsx(graph, func, visited, edge.vertex);
        }
        func.run(vertx);
        visited.add(vertx);
    }

    public static <T extends Comparable> List<T> topsort(Graph<T> graph) {
        List<T> result = new ArrayList<>();
        dfs(graph, (x) -> { result.add(x); });
        Collections.reverse(result);
        return result;
    }

    public static void main(String[] args) {
        Node root = new Node(10);
        root = root.insert(1);
        root = root.insert(12);
        root = root.insert(12);
        root = root.insert(5);
        root = root.insert(5);
        root = root.insert(120);
        System.out.println("A: " + root.find(12));
        System.out.println("B: " + root.find(120));
        System.out.println("Count: " + root.fullcount);
        System.out.println(root);
    }

}