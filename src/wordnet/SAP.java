package wordnet;

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class SAP {
    private final Digraph wordNet;
    private final HashMap<HashSet<Integer>, Integer[]> distances;
    public SAP(Digraph G) {
        wordNet = new Digraph(G);
        distances = new HashMap<>();
    }

    public int length(int v, int w) {
        Integer[] ans = preparePairAndCompute(v, w);
        return ans[0];
    }

    public int ancestor(int v, int w) {
        Integer[] ans = preparePairAndCompute(v, w);
        return ans[1];
    }

    private Integer[] preparePairAndCompute(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        ArrayList<Integer> vList = new ArrayList<>(1), wList = new ArrayList<>(1);
        vList.add(v);
        wList.add(w);
        return computeDistance(vList, wList);
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        validateVertices(v);
        validateVertices(w);
        Integer[] ans = computeDistance(v, w);
        return ans[0];
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        validateVertices(v);
        validateVertices(w);
        Integer[] ans = computeDistance(v, w);
        return ans[1];
    }

    private Integer[] computeDistance(Iterable<Integer> v, Iterable<Integer> w) {
        validateVertices(v);
        validateVertices(w);
        int sizeV = getSize(v);
        int sizeW = getSize(w);
        HashSet<Integer> forPair = new HashSet<>();
        boolean pair = sizeV == 1 && sizeW == 1;
        if (pair) {
            forPair.add(v.iterator().next());
            forPair.add(w.iterator().next());
            if (distances.containsKey(forPair)) {
                return distances.get(forPair);
            }
        }
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(wordNet, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(wordNet, w);
        int min = Integer.MAX_VALUE;
        int commonParent = -1;
        for (int vertex = 0; vertex < wordNet.V(); vertex++) {
            if (bfsV.hasPathTo(vertex) && bfsW.hasPathTo(vertex) &&
                    bfsV.distTo(vertex) + bfsW.distTo(vertex) < min) {
                min = bfsV.distTo(vertex) + bfsW.distTo(vertex);
                commonParent = vertex;
            }
        }
        Integer[] ans = new Integer[2];
        ans[0] = min;
        ans[1] = commonParent;
        if (ans[0] == Integer.MAX_VALUE) {
            ans[0] = -1;
        }
        if (pair) {
            distances.put(forPair, ans);
        }
        return ans;
    }
    private int getSize(Iterable<Integer> a) {
        if (a instanceof Collection) {
            return ((Collection<Integer>) a).size();
        } else {
            int count = 0;
            for (Object unused : a) {
                count++;
            }
            return count;
        }
    }

    private void validateVertices(Iterable<Integer> vertices) {
        if (vertices == null) {
            throw new IllegalArgumentException("Iterable of Vertices is null.");
        }
        for (Integer v: vertices) {
            if (v != null) {
                validateVertex(v);
            } else {
                throw new IllegalArgumentException("Iterable contains null");
            }
        }
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= wordNet.V()) {
            throw new IllegalArgumentException("Vertex is not valid.");
        }
    }
    public static void main(String[] args) {
        // add digraph1.txt as an argument
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}