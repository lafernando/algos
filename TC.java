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

    public static void main(String[] args) {
        Graph<Integer> graph = new Graph();
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 4, 1);
        graph.addEdge(4, 5, 2);
        graph.addEdge(3, 5, 4);
        Path<Integer> path = dijkstra(graph, 1, 5);
        System.out.println(path);
    }

}