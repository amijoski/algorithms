// package seamcarver;

import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Topological;

import java.util.Arrays;

public class SeamCarver {
    private Picture pic;
    private double[][] energy;
    private boolean transposed;

    private static final double BORDER_ENERGY = 1000.0;
    public SeamCarver(Picture newPic) {
        if (newPic == null) {
            throw new IllegalArgumentException();
        }
        pic = new Picture(newPic);
        recalculateEnergyMatrix();
    }
    public Picture picture() {
        if (transposed) {
            transpose();
        }
        return new Picture(pic);
    }
    public int width() {
        if (transposed) {
            transpose();
        }
        return pic.width();
    }
    public int height() {
        if (transposed) {
            transpose();
        }
        return pic.height();
    }
    public double energy(int i, int j) {
        if (transposed) {
            transpose();
        }
        if (checkBorder(i, j)) {
            return BORDER_ENERGY;
        }
        return energy[i][j];
    }
    public int[] findHorizontalSeam() {
        if (transposed) {
            transpose();
        }
        return findPath();
    }
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null || seam.length != pic.width()) {
            throw new IllegalArgumentException();
        }
        Picture newPic = new Picture(pic.width(), pic.height() - 1);
        for (int i = 0; i < pic.width(); i++) {
            int y = seam[i];
            if (i < pic.width() - 1 && Math.abs(seam[i] - seam[i+1]) > 1) {
                throw new IllegalArgumentException("");
            }
            for (int j = 0; j < pic.height(); j++) {
                if (j < y) {
                    newPic.setRGB(i, j, pic.getRGB(i, j));
                } else if (j > y) {
                    newPic.setRGB(i, j - 1, pic.getRGB(i, j));
                }
            }
        }
        pic = newPic;
        recalculateEnergyMatrix();
    }

    public int[] findVerticalSeam() {
        if (!transposed) {
            transpose();
        }
        int[] path = findPath();
        transpose();
        return path;
    }

    public void removeVerticalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException();
        }
        if (!transposed) {
            transpose();
        }
        removeHorizontalSeam(seam);
        transpose();
    }

    private int[] findPath() {
        if (pic.width() == 1) { return new int[]{0}; }
        if (pic.width() == 2) { return new int[]{0, 0}; }
        EdgeWeightedDigraph g = new EdgeWeightedDigraph(2 + pic.width() * pic.height());
        for (int y = 0; y < pic.height(); y++) {
            g.addEdge(new DirectedEdge(0, 1 + y, BORDER_ENERGY));
        }
        for (int x = 0; x < pic.width() - 1; x++) {
            for (int y = 0; y < pic.height(); y++) {
                if (y > 0) {
                    g.addEdge(new DirectedEdge(
                            getIndexFromXY(x, y),
                            getIndexFromXY(x + 1, y - 1),
                            energy[x + 1][y - 1]
                    ));
                }
                g.addEdge(new DirectedEdge(
                        getIndexFromXY(x, y),
                        getIndexFromXY(x + 1, y),
                        energy[x + 1][y]
                ));
                if (y < pic.height() - 1) {
                    g.addEdge(new DirectedEdge(
                            getIndexFromXY(x, y),
                            getIndexFromXY(x + 1, y + 1),
                            energy[x + 1][y + 1]
                    ));
                }
            }
        }
        for (int y = 0; y < pic.height(); y++) {
            g.addEdge(new DirectedEdge(
                    getIndexFromXY(pic.width() - 1, y),
                    1 + pic.width()*pic.height(),
                    0
            ));
        }

        Topological topological = new Topological(g);
        Iterable<Integer> orderIterable = topological.order();
        double[] distTo = new double[g.V()];
        Arrays.fill(distTo, Double.POSITIVE_INFINITY);
        int[] edgeTo = new int[g.V()];
        distTo[0] = 0.0;
        edgeTo[0] = -1;
        for (Integer curr: orderIterable) {
            for (DirectedEdge e: g.adj(curr)) {
                if (distTo[curr] + e.weight() < distTo[e.to()]) {
                    distTo[e.to()] = distTo[curr] + e.weight();
                    edgeTo[e.to()] = curr;
                }
            }
        }
        int[] pathIterable = new int[pic.width() + 2];
        int curr = 1 + pic.width() * pic.height();
        int counter = 0;
        while (curr != -1) {
            pathIterable[counter] = curr;
            counter++;
            curr = edgeTo[curr];
        }
        int[] path = new int[pic.width()];
        for (int i = pic.width(); i >= 1; i--) {
            path[pic.width() - i] = getXYfromIndex(pathIterable[i])[1];
        }
        return path;
    }
    private void transpose() {
        Picture newPic = new Picture(pic.height(), pic.width());
        double [][] newEnergy = new double[pic.height()][pic.width()];
        for (int i = 0; i < pic.width(); i++) {
            for (int j = 0; j < pic.height(); j++) {
                newPic.setRGB(j, i, pic.getRGB(i, j));
                newEnergy[j][i] = energy[i][j];
            }
        }
        pic = newPic;
        energy = newEnergy;
        transposed = !transposed;
    }
    /*
    private void showEnergyMatrix() {
        Picture tmpPic = new Picture(pic.width(), pic.height());
        double max = 0;
        for (int i = 0; i < pic.width(); i++) {
            for (int j = 0; j < pic.height(); j++) {
                max = Math.max(max, energy[i][j]);
            }
        }
        for (int i = 0; i < pic.width(); i++) {
            for (int j = 0; j < pic.height(); j++) {
                tmpPic.set(i, j, new Color((float) (energy[i][j]/max),
                        (float) (energy[i][j]/max),
                        (float) (energy[i][j]/max)));
            }
        }
        tmpPic.show();
    }

    private void colorRedLineToBeDeleted(int[] seam) {
        for (int index: seam) {
            pic.set(getXYfromIndex(index)[0], getXYfromIndex(index)[1], Color.RED);
        }
        if (transposed) {
            transpose();
        }
    }
    */
    private void recalculateEnergyMatrix() {
        double [][] newEnergy = new double[pic.width()][pic.height()];
        for (int i = 0; i < pic.width(); i++) {
            for (int j = 0; j < pic.height(); j++) {
                newEnergy[i][j] = calculateEnergy(i, j);
            }
        }
        energy = newEnergy;
    }

    private int getIndexFromXY(int x, int y) {
        return 1 + pic.height()*x + y;
    }

    private int[] getXYfromIndex(int index) {
        return new int[]{(index - 1)/pic.height(), (index - 1) % pic.height()};
    }

    private double calculateEnergy(int x, int y) {
        if (checkBorder(x, y)) {
            return BORDER_ENERGY;
        }
        int rgbLeft = pic.getRGB(x - 1, y);
        int rgbRight = pic.getRGB(x + 1, y);
        int rgbUp = pic.getRGB(x , y - 1);
        int rgbDown = pic.getRGB(x, y + 1);
        return Math.sqrt(deltaSquared(rgbLeft, rgbRight) + deltaSquared(rgbUp, rgbDown));
    }

    private double deltaSquared(int rgb1, int rgb2) {
        return Math.pow(getRed(rgb1)-getRed(rgb2), 2) +
                Math.pow(getGreen(rgb1) - getGreen(rgb2), 2) +
                Math.pow(getBlue(rgb1) - getBlue(rgb2), 2);
    }

    private int getRed(int rgb) {
        return (rgb >> 16) & 0xFF;
    }
    private int getGreen(int rgb) {
        return (rgb >> 8) & 0xFF;
    }
    private int getBlue(int rgb) {
        return rgb & 0xFF;
    }
    private boolean checkBorder(int x, int y) {
        if (x < 0 || x >= pic.width() || y < 0 || y >= pic.height()) {
            throw new IllegalArgumentException("Values out of scope");
        }
        return x == 0 || x == pic.width() - 1 || y == 0 || y == pic.height() - 1;
    }

    public static void main(String[] args) {
        // SeamCarver tmp = new SeamCarver(new Picture("HJoceanSmall.png"));
        // tmp.picture().show();
        // tmp.showEnergyMatrix();
        // tmp.picture().show();
        // tmp.transpose();
        // tmp.showEnergyMatrix();
        // tmp.picture().show();
        /*for(int i = 0; i < 10; i++) {
            tmp.colorRedLineToBeDeleted(tmp.findVerticalSeam());
            tmp.picture().show();
            tmp.removeVerticalSeam(tmp.findVerticalSeam());
            tmp.picture().show();
        }*/
    }
}