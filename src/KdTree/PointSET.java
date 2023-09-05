package KdTree;

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import java.util.ArrayList;
public class PointSET {
    private final SET<Point2D> points;
    public PointSET() {
        points = new SET<Point2D>();
    }
    public boolean isEmpty() {
        return points.isEmpty();
    }
    public int size() {
        return points.size();
    }
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Nothing to insert!");
        }
        points.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return points.contains(p);
    }
    public void draw() {
        for (Point2D point : points) {
            StdDraw.point(point.x(), point.y());
        }
    }
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("The rectangle is null!");
        }
        ArrayList<Point2D> ans = new ArrayList<>();
        for (Point2D point : points) {
            if (rect.contains(point)) {
                ans.add(point);
            }
        }
        return ans;
    }
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("The point is null!");
        }
        double minDistance = Double.POSITIVE_INFINITY;
        Point2D minDistancePoint= null;
        for (Point2D point : points) {
            if (point.distanceSquaredTo(p) < minDistance) {
                minDistance = point.distanceSquaredTo(p);
                minDistancePoint = point;
            }
        }
        return minDistancePoint;
    }
}
