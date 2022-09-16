package PercolationUsingUnionFind;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final boolean[][] grid;
    private final WeightedQuickUnionUF wqfGrid;
    private final WeightedQuickUnionUF wqfFull;
    private final int gridSize;
    private final int virtualTop;
    private final int virtualBottom;
    private int openSites;
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("N must be > 0");
        gridSize = n;
        grid = new boolean[gridSize][gridSize];
        wqfGrid = new WeightedQuickUnionUF(n * n + 2); // includes virtual top bottom
        wqfFull = new WeightedQuickUnionUF(n * n + 1); // includes virtual top
        virtualBottom = n * n + 1;
        virtualTop = n * n;
        openSites = 0;
    }

    public void open(int row, int col) {
        validateSite(row, col);
        int flatIndex = flattenGrid(row, col);
        if (isOpen(row, col)) {
            return;
        }
        grid[row - 1][col - 1] = true;
        openSites++;
        if (row == 1) {
            wqfGrid.union(virtualTop, flatIndex);
            wqfFull.union(virtualTop, flatIndex);
        }
        if (row == gridSize) {
            wqfGrid.union(virtualBottom, flatIndex);
        }
        unitePositions(row, col, row, col - 1);
        unitePositions(row, col, row, col + 1);
        unitePositions(row, col, row - 1, col);
        unitePositions(row, col, row + 1, col);
    }

    public boolean isOpen(int row, int col) {
        validateSite(row, col);
        return grid[row - 1][col - 1];
    }

    public boolean isFull(int row, int col) {
        validateSite(row, col);
        return wqfFull.find(virtualTop) == wqfFull.find(flattenGrid(row, col));
    }

    public int numberOfOpenSites() {
        return openSites;
    }

    public boolean percolates() {
        return wqfGrid.find(virtualTop) == wqfGrid.find(virtualBottom);
    }

    public static void main(String[] args) {
        int size = Integer.parseInt(args[0]);
        Percolation percolation = new Percolation(size);
        int argCount = args.length;
        for (int i = 1; argCount >= 2; i += 2) {
            int row = Integer.parseInt(args[i]);
            int col = Integer.parseInt(args[i + 1]);
            StdOut.printf("Adding row: %d  col: %d %n", row, col);
            percolation.open(row, col);
            if (percolation.percolates()) {
                StdOut.printf("%nThe System percolates %n");
            }
            argCount -= 2;
        }
        if (!percolation.percolates()) {
            StdOut.print("Does not percolate %n");
        }
    }

    private void unitePositions(int rowCurr, int colCurr, int rowNeighbour, int colNeighbour) {
        if (isOnGrid(rowNeighbour, colNeighbour) && isOpen(rowNeighbour, colNeighbour)) {
            int flatIndexCurr = flattenGrid(rowCurr, colCurr);
            int flatIndexNeighbour = flattenGrid(rowNeighbour, colNeighbour);
            wqfGrid.union(flatIndexCurr, flatIndexNeighbour);
            wqfFull.union(flatIndexCurr, flatIndexNeighbour);
        }
    }

    private int flattenGrid(int row, int col) {
        return gridSize * (row - 1) + col - 1;
    }

    private void validateSite(int row, int col) {
        if (!isOnGrid(row, col)) {
            throw new IllegalArgumentException("Index is out of bounds");
        }
    }

    private boolean isOnGrid(int row, int col) {
        return (row >= 1 && col >= 1 && row <= gridSize && col <= gridSize);
    }
}

