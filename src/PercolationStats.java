import java.util.Random;
public class PercolationStats {
    private int N, T;
    private double[] Values;

    // Perform independent trials on an n-by-n grid.
    public PercolationStats(int n, int trials) {
        N = n;
        T = trials;
        Values = new double[T];
        for (int i = 0; i < trials; i++) {
            Percolation example = new Percolation(N);
            while (!example.percolates()) {
                Random random = new Random();
                int row = random.nextInt(n);
                int col = random.nextInt(n);
                example.open(row + 1, col + 1);
            }
            Values[i] = example.numberOfOpenSites() * 1.0 / (N * N);
        }
    }

    // Sample mean of percolation threshold.
    public double mean() {
        double sum = 0;
        for (double num : Values) {
            sum += num;
        }
        return sum / T;
    }

    // Sample standard deviation of percolation threshold.
    public double stddev() {
        double M = this.mean();
        double stddev = 0;
        for (double num : Values) {
            stddev += Math.pow(num - M, 2);
        }
        return Math.sqrt(stddev / T);
    }

    // Low endpoint of 95% confidence interval.
    public double confidenceLo() {
        double M = this.mean();
        double SD = this.stddev();
        return M - 1.96 * SD / Math.sqrt(T);
    }

    // High endpoint of 95% confidence interval.
    public double confidenceHi() {
        double M = this.mean();
        double SD = this.stddev();
        return M + 1.96 * SD / Math.sqrt(T);
    }

    public static void main(String[] args) {
        PercolationStats example = new PercolationStats(200, 500);
        System.out.println("Mean: " + example.mean());
        System.out.println("StdDev: " + example.stddev());
        System.out.println("95% confidence interval = [" + example.confidenceLo() + ", " + example.confidenceHi() + "]");
    }
}
