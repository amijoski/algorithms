package DequeAndRandomizedQueue;

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] q;
    private int size;

    public RandomizedQueue() {
        q = (Item[]) new Object[8];
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (size == q.length) {
            Item[] new_q = (Item[]) new Object[2 * size];
            System.arraycopy(q, 0, new_q, 0, q.length);
            q = new_q;
        }
        q[size] = item;
        size++;
    }

    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int randomIndex = StdRandom.uniformInt(size);
        Item ans = q[randomIndex];
        if (randomIndex != size - 1) {
            q[randomIndex] = q[size - 1];
        }
        q[--size] = null;
        if (size <= q.length / 4) {
            Item[] new_q = (Item[]) new Object[q.length / 2];
            System.arraycopy(q, 0, new_q, 0, size);
        }
        return ans;
    }

    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return q[StdRandom.uniformInt(size)];
    }

    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<>();
        for (int i = 1; i <= 10; i++) {
            rq.enqueue(i);
        }
        for (int i : rq) {
            System.out.print(i + " ");
        }

        rq.dequeue();
        rq.dequeue();

        System.out.println();
        for (int i : rq) {
            System.out.print(i + " ");
        }
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private Item[] local;
        private int localSize;

        public RandomizedQueueIterator() {
            local = q.clone();
            localSize = size;
        }

        @Override
        public boolean hasNext() {
            return localSize > 0;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            int randomIndex = StdRandom.uniformInt(localSize);
            Item ans = local[randomIndex];
            if (randomIndex != localSize - 1) {
                local[randomIndex] = local[localSize - 1];
            }
            local[--localSize] = null;
            if (localSize <= local.length / 4) {
                Item[] new_local = (Item[]) new Object[local.length / 2];
                System.arraycopy(local, 0, new_local, 0, localSize);
            }
            return ans;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }


}
