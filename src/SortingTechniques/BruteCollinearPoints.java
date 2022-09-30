package SortingTechniques;
import java.util.Arrays;

public class BruteCollinearPoints {
    private LineSegment[] segments;
    private int pos, len;
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }
        pos = 0;
        len = 8;
        segments = new LineSegment[len];
        Point[] line = new Point[4];
        for (int i = 0; i < points.length - 3; i++) {
            for (int j = i + 1; j < points.length - 2; j++) {
                for (int k = j + 1; k < points.length - 1; k++) {
                    for (int l = k + 1; l < points.length; l++) {
                        line[0] = points[i];
                        line[1] = points[j];
                        line[2] = points[k];
                        line[3] = points[l];
                        Arrays.sort(line);
                        Point a = line[0], b = line[1], c = line[2], d = line[3];
                        if (a.slopeTo(b) == a.slopeTo(c) && a.slopeTo(c) == a.slopeTo(d)) {
                            System.out.println(a);
                            System.out.println(b);
                            System.out.println(c);
                            System.out.println(d);
                            segments[pos] = new LineSegment(a, d);
                            pos++;
                            if (pos == len) {
                                LineSegment[] tmp = new LineSegment[2 * len];
                                System.arraycopy(segments, 0, tmp, 0, len);
                                segments = tmp;
                                len *= 2;
                            }
                        }
                    }
                }
            }
        }
    }

    public int numberOfSegments() {
        return pos;
    }

    public LineSegment[] segments() {
        return segments;
    }
}
