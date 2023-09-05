package KdTree;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

public class KdTree {

    private Node root;
    private int size = 0;

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return this.size;
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (root == null) {
            root = Node.create(p, 0, null, null);
            size++;
        } else if (!contains(p)) {
            insert(root, p, true);
            size++;
        }
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (isEmpty()) return false;
        return contains(root, p, true);
    }

    public void draw() {
        draw(root, new RectHV(0, 0, 1, 1));
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        ArrayList<Point2D> result = new ArrayList<>();
        range(this.root, rect, result);
        return result;
    }

    public Point2D nearest(Point2D p) {
        if (p == null || isEmpty()) throw new IllegalArgumentException();
        return nearest(p, root.data, root);
    }

    private Point2D nearest(Point2D p, Point2D currentNearestPoint, Node curr) {
        if (curr == null) {
            return currentNearestPoint;
        }
        if (curr.nodeLevel % 2 == 0) {
            if (p.x() > curr.data.x()) { // check right subtree
                Point2D npr = nearest(p, curr.data.distanceSquaredTo(p) < currentNearestPoint.distanceSquaredTo(p) ? curr.data : currentNearestPoint, curr.right);
                if (npr.distanceSquaredTo(p) > Math.abs(curr.data.x() - p.x())) {
                    Point2D npl = nearest(p, npr, curr.left);
                    return npr.distanceSquaredTo(p) > npl.distanceSquaredTo(p) ? npl : npr;
                } else {
                    return npr;
                }
            } else { // check left subtree
                Point2D npl = nearest(p, curr.data.distanceSquaredTo(p) < currentNearestPoint.distanceSquaredTo(p) ? curr.data : currentNearestPoint, curr.left);
                if (npl.distanceSquaredTo(p) > Math.abs(curr.data.x() - p.x())) {
                    Point2D npr = nearest(p, npl, curr.right);
                    return npr.distanceSquaredTo(p) > npl.distanceSquaredTo(p) ? npl : npr;
                } else {
                    return npl;
                }
            }
        } else {
            if (p.y() > curr.data.y()) { // check upper subtree
                Point2D npu = nearest(p,
                        curr.data.distanceSquaredTo(p) < currentNearestPoint.distanceSquaredTo(p) ?
                                curr.data : currentNearestPoint,
                        curr.right);
                if (npu.distanceSquaredTo(p) > Math.abs(curr.data.y() - p.y())) {
                    Point2D npd = nearest(p, npu, curr.left);
                    return npu.distanceSquaredTo(p) > npd.distanceSquaredTo(p) ? npd : npu;
                } else {
                    return npu;
                }
            } else { // check lower subtree
                Point2D npd = nearest(p,
                        curr.data.distanceSquaredTo(p) < currentNearestPoint.distanceSquaredTo(p) ?
                                curr.data : currentNearestPoint,
                        curr.left);
                if (npd.distanceSquaredTo(p) > Math.abs(curr.data.y() - p.y())) {
                    Point2D npu = nearest(p, npd, curr.right);
                    return npu.distanceSquaredTo(p) > npd.distanceSquaredTo(p) ? npd : npu;
                } else {
                    return npd;
                }
            }
        }
    }

    private void range(Node curr, RectHV rect, ArrayList<Point2D> insiders) {
        if (curr == null) {
            return;
        }

        if (rect.contains(curr.data)) {
            insiders.add(curr.data);
        }

        if (curr.nodeLevel % 2 == 0) { // Vertical segment
            // the vertical line intersects with query rectangle
            if (rect.xmin() <= curr.data.x() && curr.data.x() <= rect.xmax()) {
                range(curr.left, rect, insiders);
                range(curr.right, rect, insiders);
            } else if (rect.xmin() > curr.data.x()) { // Search right
                range(curr.right, rect, insiders);
            } else { // Search left
                range(curr.left, rect, insiders);
            }
        } else { // Horizontal segment
            // the horizontal line intersects with query rectangle
            if (rect.ymin() <= curr.data.y() && curr.data.y() <= rect.ymax()) {
                range(curr.left, rect, insiders);
                range(curr.right, rect, insiders);
            } else if (rect.ymin() > curr.data.y()) { // Search up
                range(curr.right, rect, insiders);
            } else { // Search down
                range(curr.left, rect, insiders);
            }
        }
    }

    private void draw(Node curr, RectHV rectHV) {
        if (curr == null) {
            return;
        }
        final double penRadiusForPoint = 0.02;
        final double penRadiusForLine = 0.001;

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(penRadiusForPoint);
        curr.data.draw();

        StdDraw.setPenRadius(penRadiusForLine);
        if (curr.nodeLevel % 2 == 0) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(curr.data.x(), rectHV.ymin(), curr.data.x(), rectHV.ymax());
            draw(curr.left, new RectHV(rectHV.xmin(), rectHV.ymin(), curr.data.x(), rectHV.ymax()));
            draw(curr.right, new RectHV(curr.data.x(), rectHV.ymin(), rectHV.xmax(), rectHV.ymax()));
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(rectHV.xmin(), curr.data.y(), rectHV.xmax(), curr.data.y());
            draw(curr.left, new RectHV(rectHV.xmin(), rectHV.ymin(), rectHV.xmax(), curr.data.y()));
            draw(curr.right, new RectHV(rectHV.xmin(), curr.data.y(), rectHV.xmax(), rectHV.ymax()));
        }
    }

    private boolean contains(Node curr, Point2D p, boolean useX) {
        if (curr.data.equals(p)) return true;
        if ((useX && p.x() < curr.data.x()) || (!useX && p.y() < curr.data.y())) {
            if (curr.left == null) {
                return false;
            } else {
                return contains(curr.left, p, !useX);
            }
        } else {
            if (curr.right == null) {
                return false;
            } else {
                return contains(curr.right, p, !useX);
            }
        }
    }

    private void insert(Node curr, Point2D p, boolean useX) {
        // if the point to be inserted is smaller, go left
        if ((useX && p.x() < curr.data.x()) || (!useX && p.y() < curr.data.y())) {
            if (curr.left == null) {
                curr.left = Node.create(p, curr.nodeLevel + 1, null, null);
            } else {
                insert(curr.left, p, !useX);
            }
        } else if (curr.right == null) {
            curr.right = Node.create(p, curr.nodeLevel + 1,null, null);
        } else {
            insert(curr.right, p, !useX);
        }
    }
    private static class Node {

        private final Point2D data;
        private Node left;
        private Node right;
        private final int nodeLevel;

        private Node(Point2D data, int nodeLevel, Node left, Node right) {
            this.data = data;
            this.left = left;
            this.right = right;
            this.nodeLevel = nodeLevel;
        }

        public static Node create(Point2D data, int nodeLevel, Node left, Node right) {
            return new Node(data, nodeLevel, left, right);
        }
    }

    public static void main(String[] args) {
        KdTree kdTree = new KdTree();

        kdTree.insert(new Point2D(0.7, 0.2)); // A
        kdTree.insert(new Point2D(0.5, 0.4)); // B
        kdTree.insert(new Point2D(0.2, 0.3)); // C
        kdTree.insert(new Point2D(0.4, 0.7)); // D
        kdTree.insert(new Point2D(0.9, 0.6)); // E


        System.out.println(kdTree.nearest(new Point2D(0.078, 0.552)));
        System.out.println(kdTree.nearest(new Point2D(0.684, 0.73)));

    }
}