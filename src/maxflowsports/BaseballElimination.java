package maxflowsports;

import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.StdOut;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class BaseballElimination {
    private final int teams;
    private final int[] w, l, r;
    private final int mostWins;
    private final String withMostWins;
    private final HashMap<String, Integer> teamIds;
    private final String[] teamNames;
    private final int[][] g;
    public BaseballElimination(String filename) {
        In in = new In(filename);
        teams = Integer.parseInt(in.readLine());
        teamIds = new HashMap<>(teams);
        teamNames = new String[teams];
        w = new int[teams];
        l = new int[teams];
        r = new int[teams];
        g = new int[teams][teams];
        int tmp = -1, tmpPos = 0;
        for (int i = 0; i < teams; i++) {
            String[] line = in.readLine().strip().split("\\s+");
            teamIds.put(line[0], i);
            teamNames[i] = line[0];
            w[i] = Integer.parseInt(line[1]);
            if (w[i] > tmp) {
                tmp = w[i];
                tmpPos = i;
            }
            tmp = Math.max(w[i], tmp);
            l[i] = Integer.parseInt(line[2]);
            r[i] = Integer.parseInt(line[3]);
            for (int j = 0; j < teams; j++) {
                g[i][j] = Integer.parseInt(line[4 + j]);
            }
        }
        mostWins = tmp;
        withMostWins = teamNames[tmpPos];
    }
    public int numberOfTeams() {
        return teams;
    }
    public Iterable<String> teams() {
        return teamIds.keySet();
    }
    public int wins(String team) {
        validateTeam(team);
        return w[teamIds.get(team)];
    }
    public int losses(String team) {
        validateTeam(team);
        return l[teamIds.get(team)];
    }
    public int remaining(String team) {
        validateTeam(team);
        return r[teamIds.get(team)];
    }
    public int against(String team1, String team2) {
        validateTeam(team1);
        validateTeam(team2);
        return g[teamIds.get(team1)][teamIds.get(team2)];
    }
    public boolean isEliminated(String team) {
        validateTeam(team);
        return !(computeCertificateOfElimination(team) == null);
    }
    public Iterable<String> certificateOfElimination(String team) {
        validateTeam(team);
        return computeCertificateOfElimination(team);
    }
    private List<String> computeCertificateOfElimination(String team) {
        validateTeam(team);
        int index = teamIds.get(team);
        if (w[index] + r[index] < mostWins) {
            List<String> ans = new LinkedList<String>();
            ans.add(withMostWins);
            return ans;
        }
        int totalVertices = 1 + (teams - 1)*(teams - 2)/2 + teams;
        FlowNetwork flow = new FlowNetwork(totalVertices);
        int gamesRemaining = 0;
        int counter = 1;
        for (int i = 0; i < teams - 1; i++) {
            if (i == index) {
                continue;
            }
            for (int j = i + 1; j < teams; j++) {
                if (j == index) {
                    continue;
                }
                int posi = i, posj = j;
                if (i > index) {
                    posi--;
                }
                if (j > index) {
                    posj--;
                }
                flow.addEdge(new FlowEdge(0, counter, g[i][j]));
                flow.addEdge(new FlowEdge(counter, 1 + (teams - 1) * (teams - 2)/2 + posi, Double.POSITIVE_INFINITY));
                flow.addEdge(new FlowEdge(counter, 1 + (teams - 1) * (teams - 2)/2 + posj, Double.POSITIVE_INFINITY));
                gamesRemaining += g[i][j];
                counter++;
            }
        }
        for (int i = 0; i < teams; i++) {
            if (i == index) {
                continue;
            }
            int posi = i;
            if (i > index) {
                posi--;
            }
            flow.addEdge(new FlowEdge(1 + (teams - 1) * (teams - 2)/2 + posi, totalVertices - 1,
                    w[index] + r[index] - w[i]));
        }
        FordFulkerson ff = new FordFulkerson(flow, 0, totalVertices - 1);
        if (gamesRemaining > ff.value()) {
            List<String> certificateOfElimination = new LinkedList<String>();
            for (int i = 0; i < teams; i++) {
                if (i == index) {
                    continue;
                }
                int posi = i;
                if (i > index) {
                    posi--;
                }
                if (ff.inCut(1 + (teams - 1) * (teams - 2)/2 + posi)) {
                    certificateOfElimination.add(teamNames[i]);
                }
            }
            return certificateOfElimination;
        }
        return null;
    }

    private void validateTeam(String team) {
        if (!teamIds.containsKey(team)) {
            throw new IllegalArgumentException();
        }
    }
    public static void main(String[] args) {
        BaseballElimination b = new BaseballElimination("teams5.txt");
        if (b.isEliminated("Detroit")) {
            Iterable<String> certificate = b.certificateOfElimination("Detroit");
            for (String a : certificate) {
                StdOut.println(a);
            }
        }
        StdOut.println(b.isEliminated("Detroit"));
    }
}
