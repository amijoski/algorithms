package wordnet;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class WordNet {
    private static class StringPair {
        String synset;
        String explanation;
        public StringPair(String a, String b) {
            synset = a;
            explanation = b;
        }
    }
    private final ArrayList<StringPair> synsetTable;
    private final ArrayList<HashSet<Integer>> hypernymTable;
    private final Digraph G;
    private final HashMap<String, HashSet<Integer>> nounIds;
    private final SAP sap;

    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null || synsets.isEmpty() || hypernyms.isEmpty()) {
            throw new IllegalArgumentException("Null or empty filenames.");
        }
        synsetTable = new ArrayList<>();
        nounIds = new HashMap<>();
        readSynsets(synsets);
        hypernymTable = new ArrayList<>(synsetTable.size());
        G = new Digraph(synsetTable.size());
        readHypernyms(hypernyms);
        DirectedCycle check = new DirectedCycle(G);
        if (check.hasCycle()) {
            throw new IllegalArgumentException("The graph contains a cycle!");
        }
        sap = new SAP(G);
    }

    public Iterable<String> nouns() {
        return nounIds.keySet();
    }

    public boolean isNoun(String noun) {
        if (noun == null) {
            throw new IllegalArgumentException("Empty string argument for the function isNoun(String s)");
        }
        return nounIds.containsKey(noun);
    }

    public int distance(String nounA, String nounB) {
        validateNoun(nounA);
        validateNoun(nounB);
        HashSet<Integer> idA = nounIds.get(nounA);
        HashSet<Integer> idB = nounIds.get(nounB);
        return sap.length(idA, idB);
    }

    public String sap(String nounA, String nounB) {
        validateNoun(nounA);
        validateNoun(nounB);
        HashSet<Integer> idA = nounIds.get(nounA);
        HashSet<Integer> idB = nounIds.get(nounB);
        return synsetTable.get(sap.ancestor(idA, idB)).synset;
    }

    private void validateNoun(String noun) {
        if (!isNoun(noun)) {
            throw new IllegalArgumentException("Unknown noun.");
        }
    }
    private void readSynsets(String filename) {
        In in = new In(filename);
        int current = 0;
        while (in.hasNextLine()) {
            String[] separated = in.readLine().split(",");
            if (separated[0].isEmpty()) {
                continue;
            }
            synsetTable.add(new StringPair(separated[1], separated[2]));
            separated = separated[1].split(" ");
            for (String noun: separated) {
                if (nounIds.containsKey(noun)) {
                    nounIds.get(noun).add(current);
                } else {
                    HashSet<Integer> b = new HashSet<>();
                    b.add(current);
                    nounIds.put(noun, b);
                }
            }
            current++;
        }
    }

    private void readHypernyms(String filename) {
        In in = new In(filename);
        int roots = 0;
        int current = 0;
        while (in.hasNextLine()) {
            String[] separated = in.readLine().split(",");
            if (separated[0].isEmpty()) {
                continue;
            }
            if (separated.length == 1) {
                roots++;
            }
            HashSet<Integer> connections = new HashSet<>();
            for (int i = 1; i < separated.length; i++) {
                int to = Integer.parseInt(separated[i]);
                connections.add(to);
                G.addEdge(current, to);
            }
            hypernymTable.add(connections);
            current++;
        }
        if (roots != 1) {
            throw new IllegalArgumentException("Number of roots is not one.");
        }
    }
    public void printSynsets() {
        int counter = 0;
        for (StringPair a: synsetTable) {
            StdOut.println(counter + " " + a.synset + " " + a.explanation);
            counter++;
        }
    }

    public void printHypernyms() {
        int counter = 0;
        for (HashSet<Integer> a: hypernymTable) {
            StdOut.print(counter + " ");
            for (Integer b: a) {
                StdOut.print(b + " ");
            }
            counter++;
            StdOut.println();
        }
    }

    public void printEdges() {
        for (int i = 0; i < G.V(); i++) {
            StdOut.print(i + " ");
            for (int j : G.adj(i)) {
                StdOut.print(j + " ");
            }
            StdOut.print("\n");
        }
    }

    public void printNounIds() {
        for (String noun: nounIds.keySet()) {
            StdOut.println(noun + " " + nounIds.get(noun).toString());
        }
    }
    public static void main(String[] args) {
        // WordNet tmp = new WordNet("synsets.txt", "hypernyms.txt");
    }
}