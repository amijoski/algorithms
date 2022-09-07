public class Percolation {
    private boolean[][] grid;
    private WeightedQuickUnionUF connected_components;
    private int openSites, N;

    // Creates an n-by-n grid with all sites initially blocked.
    public Percolation(int n) {
        N = n;
        grid = new boolean[n][n];
        connected_components = new WeightedQuickUnionUF(n * n + 2);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                grid[i][j] = false;
            }
        }
        openSites = 0;
    }

    // Opens the site (row, col) if it is not open already.
    public void open(int row, int col) {
        row -= 1;
        col -= 1;
        if (!grid[row][col]) {
            grid[row][col] = true;
            openSites += 1;
            if (row == 0) {
                connected_components.union(0, col + 1);
            } else if (row == N - 1) {
                connected_components.union(row * N + col + 1, N * N + 1);
            } else {
                if (grid[row - 1][col]) {
                    connected_components.union((row - 1) * N + col + 1, row * N + col + 1);
                }
                if (row < N - 1 && grid[row + 1][col]) {
                    connected_components.union(row * N + col + 1, (row + 1) * N + col + 1);
                }
                if (col > 0 && grid[row][col - 1]) {
                    connected_components.union(row * N + col, row * N + col + 1);
                }
                if (col < N - 1 && grid[row][col + 1]) {
                    connected_components.union(row * N + col + 1, row * N + col + 2);
                }
            }
        }
    }

    // Returns the state of the (row, col) site.
    public boolean isOpen(int row, int col) {
        return grid[row-1][col-1];
    }

    // Returns if there is an open path from the top to the (row, col) site.
    public boolean isFull(int row, int col) {
        return connected_components.connected(0, (row-1) * N + col);
    }

    // Returns the number of open sites in the grid.
    public int numberOfOpenSites() {
        return openSites;
    }

    // Returns if the grid percolates or not.
    public boolean percolates() {
        return connected_components.connected(0, N * N + 1);
    }
}