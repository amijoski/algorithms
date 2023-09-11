package wordnet;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet  wordNet;
    public Outcast(WordNet wordnet) {
        wordNet = wordnet;
    }
    public String outcast(String[] nouns) {
        int maxd = -1;
        int ans = -1;
        for (int i = 0; i < nouns.length; i++) {
            int d = 0;
            for (String noun: nouns) {
                d += wordNet.distance(nouns[i], noun);
            }
            if (d > maxd) {
                maxd = d;
                ans = i;
            }
        }
        return nouns[ans];
    }
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }

}