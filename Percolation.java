import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import java.lang.Exception;


public class Percolation {

    private final int size;
    private final int width;
    private final WeightedQuickUnionUF uf;
    private boolean[] gridsites;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n is not proper.");
        }
        width = n;
        size = n * n;
        //gridsites[size] is virtual top site;
        //gridsites[size+1] is virtual bottom site;
        gridsites = new boolean[size];
        uf = new WeightedQuickUnionUF(size + 2);
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation matrix5 = new Percolation(5);
        matrix5.open(1, 1);
        matrix5.open(2, 1);
        matrix5.open(3, 1);
        matrix5.open(4, 1);
//        matrix5.open(5, 1);
        matrix5.open(2, 4);
        System.out.println(matrix5.isFull(2, 4));
        System.out.println(matrix5.percolates());

    }

    // opens the site (row, col) if it is not open already
    // By convention, the row and column indices are integers between 1 and n, where (1, 1) is the upper-left site:
    public void open(int row, int col) {
        checkbounds(row, col);
        int numinarray = pqTon(row, col);
        if (!gridsites[numinarray]) {
            gridsites[numinarray] = true;
        }
        //if the opensite is on the first row, union with the virtual top site
        if (row == 1) {
            uf.union(numinarray, size);
        }
        //else if the opensite is on the last row, union with the virtual bottom site
        if (row == width) {
            uf.union(numinarray, size + 1);
        }


        //Check the 4 neighbors of site(row, col), if the site is open, union them
        if ((row - 1) >= 1 && isOpen(row - 1, col)) {
            uf.union(numinarray, pqTon(row - 1, col));
        }
        if ((col - 1) >= 1 && isOpen(row, col - 1)) {
            uf.union(numinarray, pqTon(row, col - 1));
        }
        if ((row + 1) <= width && isOpen(row + 1, col)) {
            uf.union(numinarray, pqTon(row + 1, col));
        }
        if ((col + 1) <= width && isOpen(row, col + 1)) {
            uf.union(numinarray, pqTon(row, col + 1));
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        checkbounds(row, col);
        return gridsites[pqTon(row, col)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        checkbounds(row, col);
        if (gridsites[pqTon(row, col)]) {
            //if the site row,col is open, check if it is connected with the virtual top site
            return uf.connected(pqTon(row, col), size);
        }
        return false;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        int numOfOpenSites = 0;
        for (int i = 0; i < size; i++) {
            if (gridsites[i]) numOfOpenSites++;
        }
        return numOfOpenSites;
    }

    // By convention, the row and column indices are integers between 1 and n, where (1, 1) is the upper-left site:
    private int pqTon(int p, int q) {
        checkbounds(p, q);
        return (p - 1) * width + q - 1;
    }

    private void checkbounds(int row, int col) {
        if (row < 1 || row > width) {
            throw new IllegalArgumentException("row value is not in the correct range.");
        }
        if (col < 1 || col > width) {
            throw new IllegalArgumentException("col value is not in the correct range.");
        }
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.connected(size, size + 1);
    }
}