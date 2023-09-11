package bouncingballs;

import edu.princeton.cs.algs4.StdDraw;

public class BouncingBalls {
    public static void main(String[] args) {
        StdDraw.setXscale(0, 1.0);
        StdDraw.setYscale(0, 1.0);
        StdDraw.enableDoubleBuffering();
        int N = Integer.parseInt(args[0]);
        Ball[] balls = new Ball[N];
        for (int i = 0; i < N; i++) {
            balls[i] = new Ball(0.01);
        }
        while(true) {
            StdDraw.clear();
            for (int i = 0; i < N; i++) {
                balls[i].move(0.05);
                balls[i].draw();
            }
            StdDraw.show();
            StdDraw.pause(10);
        }
    }
}
