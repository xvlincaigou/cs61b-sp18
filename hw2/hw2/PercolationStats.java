package hw2;

import java.util.Random;

public class PercolationStats {

    private int N;
    private int T;
    private int[] data;

    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T, PercolationFactory pf) {
        this.N = N;
        this.T = T;
        this.data = new int[T];
        for (int i = 0; i < T; ++ i) {
            Percolation problem = pf.make(N);
            while (!problem.percolates()) {
                int x = new Random().nextInt(N), y = new Random().nextInt(N);
                problem.open(x, y);
            }
            this.data[i] = problem.numberOfOpenSites();
        }
    }

    public double mean() {
        double sum = 0.0;
        for (int i = 0; i < T; i++) {
            sum += data[i];
        }
        return sum / T;
    }

    public double stddev() {
        double mean = mean();
        double sum = 0.0;
        for (int i = 0; i < T; i++) {
            sum += Math.pow(data[i] - mean, 2);
        }
        return Math.sqrt(sum / (T - 1));
    }

    public double confidenceLow() {
        double mean = mean();
        double stddev = stddev();
        return mean - (1.96 * stddev / Math.sqrt(T));
    }

    public double confidenceHigh() {
        double mean = mean();
        double stddev = stddev();
        return mean + (1.96 * stddev / Math.sqrt(T));
    }

    public static void main(String[] args) {
        PercolationStats stats = new PercolationStats(10, 10, new PercolationFactory());
        System.out.println(stats.mean());
    }
}
