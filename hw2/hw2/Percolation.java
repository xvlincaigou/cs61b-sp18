package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final int edgeLength;
    private boolean[][] isOpen;
    private int numberOfOpenSites;
    private WeightedQuickUnionUF isWet;
    private WeightedQuickUnionUF isFull;

    private boolean isLegalPos(int row, int col) {
        return row >= 0 && row <= edgeLength - 1 && col >= 0 && col <= edgeLength - 1;
    }

    // create N-by-N grid, with all sites initially blocked
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException();
        }
        edgeLength = N;
        isOpen = new boolean[edgeLength][edgeLength];
        for (int i = 0; i < edgeLength; ++i) {
            for (int j = 0; j < edgeLength; ++j) {
                isOpen[i][j] = false;
            }
        }
        numberOfOpenSites = 0;
        isWet = new WeightedQuickUnionUF(N * N + 2);
        isFull = new WeightedQuickUnionUF(N * N + 1);
    }

    // open the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!isLegalPos(row, col)) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        // 本身打开
        numberOfOpenSites += isOpen[row][col] ? 0 : 1;
        isOpen[row][col] = true;
        // 是否被湿润，和周围的联通
        // 是否full，这个没有最底下的那个虚拟的块
        if (row == 0) {
            isWet.union(edgeLength * edgeLength, col);
            isFull.union(edgeLength * edgeLength, col);
        }
        if (row == edgeLength - 1) {
            isWet.union(edgeLength * edgeLength + 1, row * edgeLength + col);
        }
        if (isLegalPos(row - 1, col) && isOpen(row - 1, col)) {
            isWet.union((row - 1) * edgeLength + col, row * edgeLength + col);
            isFull.union((row - 1) * edgeLength + col, row * edgeLength + col);
        }
        if (isLegalPos(row + 1, col) && isOpen(row + 1, col)) {
            isWet.union((row + 1) * edgeLength + col, row * edgeLength + col);
            isFull.union((row + 1) * edgeLength + col, row * edgeLength + col);
        }
        if (isLegalPos(row, col - 1) && isOpen(row, col - 1)) {
            isWet.union(row * edgeLength + (col - 1), row * edgeLength + col);
            isFull.union(row * edgeLength + (col - 1), row * edgeLength + col);
        }
        if (isLegalPos(row, col + 1) && isOpen(row, col + 1)) {
            isWet.union(row * edgeLength + (col + 1), row * edgeLength + col);
            isFull.union(row * edgeLength + (col + 1), row * edgeLength + col);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!isLegalPos(row, col)) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        return isOpen[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        return isOpen(row, col)&&isFull.connected(row * edgeLength + col, edgeLength * edgeLength);
    }

    // number of open sites
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return isWet.connected(edgeLength * edgeLength, edgeLength * edgeLength + 1);
    }

    public static void main(String[] args) {

    }
}
