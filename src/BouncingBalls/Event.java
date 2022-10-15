package BouncingBalls;

public class Event implements Comparable<Event> {
    private final double time;
    private final Particle a;
    private final Particle b;
    private final int countA;
    private final int countB;

    public Event(double t, Particle a, Particle b) {
        time = t;
        this.a = a;
        this.b = b;
        if (a != null) {
            countA = a.getCount();
        }
        else {
            countA = -1;
        }
        if (b != null) {
            countB = b.getCount();
        }
        else {
            countB = -1;
        }
    }

    public Particle getA() {
        return a;
    }

    public Particle getB() {
        return b;
    }

    public double getTime() {
        return time;
    }

    @Override
    public int compareTo(Event that) {
        return (int) (this.time - that.time);
    }

    public boolean isValid() {
        if (a != null && a.getCount() != countA) return false;
        return b == null || b.getCount() == countB;
    }

}
