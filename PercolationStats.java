import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.lang.Exception;

public class PercolationStats {

    private final double[] thresholds;
    private final int width;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("The parameter is out of range.");
        }
        width = n;
        thresholds = new double[trials];


        for (int i = 0; i < trials; i++) {
            thresholds[i] = thresholdCal(n);
        }

    }

    private double thresholdCal(int n) {
        Percolation matrixN = new Percolation(n);
        int count = 0;
        while (!matrixN.percolates()) {
            int row = StdRandom.uniform(n) + 1;
            int col = StdRandom.uniform(n) + 1;
            if (!matrixN.isOpen(row, col)) {
                matrixN.open(row, col);
                count++;
            }
        }
        return count / Math.pow(n, 2);
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(thresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        if(width == 1) return Double.NaN;
        return StdStats.stddev(thresholds);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - 1.96 * stddev() / Math.sqrt(thresholds.length);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + 1.96 * stddev() / Math.sqrt(thresholds.length);
    }

    // test client (see below)
    public static void main(String[] args) {
        int nValue = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats perColStats = new PercolationStats(nValue, trials);
        System.out.println("Mean is " + perColStats.mean());
        System.out.println("Stddev is " + perColStats.stddev());
        System.out.println("confidenceLo is " + perColStats.confidenceLo());
        System.out.println("confidenceHi is " + perColStats.confidenceHi());

    }

}

