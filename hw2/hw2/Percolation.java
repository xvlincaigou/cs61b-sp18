package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private boolean blocks[][];
    private int numberOfOpenSites;
    private WeightedQuickUnionUF blockSet;
    private final int edgeLength;

    private boolean isLegalPos(int row, int col) {
        return row >= 0 && row <= edgeLength - 1 && col >= 0 && col <= edgeLength - 1;
    }

    // create N-by-N grid, with all sites initially blocked
    public Percolation(int N) {
        if (N <= 0)
            throw new IllegalArgumentException();
        edgeLength = N;
        blocks = new boolean[edgeLength][edgeLength];
        numberOfOpenSites = 0;
        blockSet = new WeightedQuickUnionUF(N * N + 2);
        for (int i = 0; i < edgeLength; i++) {
            // 将第一行的节点与顶部虚拟节点连接
            blockSet.union(i, N * N);
            // 将最后一行的节点与底部虚拟节点连接
            blockSet.union(N * (N - 1) + i, N * N + 1);
        }
    }

    // open the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!isLegalPos(row, col))
            throw new java.lang.IndexOutOfBoundsException();
        numberOfOpenSites += blocks[row][col] ? 0 : 1;
        blocks[row][col] = true;
        if (isLegalPos(row - 1, col) && isOpen(row - 1, col)) {
            blockSet.union((row - 1) * edgeLength + col, row * edgeLength + col);
        }
        if (isLegalPos(row + 1, col) && isOpen(row + 1, col)) {
            blockSet.union((row + 1) * edgeLength + col, row * edgeLength + col);
        }
        if (isLegalPos(row, col - 1) && isOpen(row, col - 1)) {
            blockSet.union(row * edgeLength + (col - 1), row * edgeLength + col);
        }
        if (isLegalPos(row, col + 1) && isOpen(row, col + 1)) {
            blockSet.union(row * edgeLength + (col + 1), row * edgeLength + col);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!isLegalPos(row, col)) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        return !blocks[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        return !isOpen(row, col);
    }

    // number of open sites
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return blockSet.connected(edgeLength * edgeLength, edgeLength * edgeLength + 1);
    }

    public static void main(String[] args) {

    }
}
