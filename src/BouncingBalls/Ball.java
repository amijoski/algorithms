package BouncingBalls;

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Ball {
    private double rx, ry;
    private double vx, vy;
    private final double radius;
    public Ball() {
        rx = StdRandom.uniformDouble();
        ry = StdRandom.uniformDouble();
        vx = StdRandom.uniformDouble(-1, 1);
        vy = StdRandom.uniformDouble(-1, 1);
        vx /= 2;
        vy /= 2;
        radius = 0.01;
    }

    public void move(double dt) {
        if ((rx + vx*dt < radius) || (rx + vx*dt > 1.0 - radius)) { vx = -vx; }
        if ((ry + vy*dt < radius) || (ry + vy*dt > 1.0 - radius)) { vy = -vy; }
        rx += vx * dt;
        ry += vy * dt;
    }

    public void draw() {
        StdDraw.filledCircle(rx, ry, radius);
    }
}