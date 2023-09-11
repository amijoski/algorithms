package bouncingballs;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdDraw;

public class CollisionSystem {
    private static final double refresh = 0.2;
    private MinPQ<Event> pq;
    private double t = 0.0;
    private final Particle[] particles;

    public CollisionSystem(Particle[] particles) {
        this.particles = particles.clone();
    }

    private void predict(Particle a, double limit) {
        if (a == null) return;
        for (Particle particle : particles) {
            double dt = a.timeToHit(particle);
            if (t + dt <= limit) {
                pq.insert(new Event(t + dt, a, particle));
            }
        }
        double dtX = a.timeToHitVerticalWall();
        double dtY = a.timeToHitHorizontalWall();
        if (t + dtX <= limit) pq.insert(new Event(t + dtX, a, null));
        if (t + dtY <= limit) pq.insert(new Event(t + dtY, null, a));
    }

    public void simulate(double limit) {
        StdDraw.setCanvasSize(800, 800);
        pq = new MinPQ<>();
        for (Particle particle : particles) predict(particle, limit);
        pq.insert(new Event(0, null, null));

        while (!pq.isEmpty()) {
            Event event = pq.delMin();
            if (!event.isValid()) continue;
            Particle a = event.getA();
            Particle b = event.getB();

            for (Particle particle : particles) particle.move(event.getTime() - t);
            t = event.getTime();

            if (a != null && b != null) a.bounceOff(b);
            else if (a != null) a.bounceOffVerticalWall();
            else if (b != null) b.bounceOffHorizontalWall();
            else redraw(limit);

            predict(a, limit);
            predict(b, limit);
        }
    }

    private void redraw(double limit) {
        StdDraw.clear();
        for (Particle particle : particles) {
            particle.draw();
        }
        StdDraw.show();
        StdDraw.pause(5);
        if (t < limit) {
            pq.insert(new Event(t + 1.0 / refresh, null, null));
        }
    }

    public static void main(String[] args) {
        Particle[] particles = new Particle[50];
        StdDraw.setXscale(0, 1.0);
        StdDraw.setYscale(0, 1.0);
        StdDraw.enableDoubleBuffering();
        for (int i = 0; i < particles.length; i++) {
            particles[i] = new Particle();
        }
        CollisionSystem s = new CollisionSystem(particles);
        s.simulate(100000000);
    }
}