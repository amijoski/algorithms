package DequeAndRandomizedQueue;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Item[] q;
    private int head, tail;

    // Construct an empty deque.
    public Deque() {
        q = (Item[]) new Object[8];
        head = 3;
        tail = 4;
    }

    // Check if the deque is empty.
    public boolean isEmpty() {
        return tail - head == 1;
    }

    // Return the number of items in the deck.
    public int size() {
        return tail - head - 1;
    }

    // add an item to the front
    public void addFirst(Item item) {
        checkNull(item);
        if (head == 0) {
            doubleAndCopy();
        }
        q[head--] = item;
    }

    // add an item to the back
    public void addLast(Item item) {
        checkNull(item);
        if (tail == q.length - 1) {
            doubleAndCopy();
        }
        q[tail++] = item;
    }

    // Remove and return an item from the front.
    public Item removeFirst() {
        checkEmpty();
        return q[++head];
    }

    // Remove and return an item from the back.
    public Item removeLast() {
        checkEmpty();
        return q[--tail];
    }

    // Return an iterator from front to back.
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        deque.addFirst(5);
        deque.print();
        deque.addFirst(4);
        deque.print();
        deque.addFirst(3);
        deque.print();
        deque.addLast(2);
        deque.print();
        deque.addLast(1);
        deque.print();
        deque.addLast(0);
        deque.print();
        deque.addFirst(-1);
        deque.print();
        deque.addFirst(-2);
        deque.print();
        deque.addFirst(-3);
        deque.print();
        deque.addLast(8);
        deque.print();
        deque.addLast(7);
        deque.print();
        deque.addLast(6);
        deque.print();
        deque.removeFirst();
        deque.print();
        deque.removeLast();
        deque.print();
        System.out.println(deque.size());
        deque.removeLast();
        deque.removeLast();
        deque.removeLast();
        deque.removeLast();
        deque.removeLast();
        deque.removeLast();
        deque.removeLast();
        deque.removeLast();
        deque.print();
        deque.removeLast();
        deque.print();
        deque.removeLast();
        deque.print();
        deque.addFirst(5);
        deque.print();
        deque.removeLast();
        deque.print();
        deque.addFirst(0);
        deque.removeLast();
        deque.print();
        deque.addFirst(0);
        deque.print();
        deque.removeLast();
        deque.print();
        if (deque.isEmpty()) {
            System.out.println("The deque is empty!");
        }

    }

    private void print() {
        System.out.println();
        for (Item i : this) {
            System.out.print(i + " ");
        }
        System.out.println();
        System.out.println(head);
        System.out.println(tail);
    }

    private class DequeIterator implements Iterator<Item> {
        private int current = head + 1;

        @Override
        public boolean hasNext() {
            return current < tail;
        }

        @Override
        public Item next() {
            if (hasNext()) {
                return q[current++];
            } else {
                throw new NoSuchElementException();
            }
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private void doubleAndCopy() {
        Item[] new_q = (Item[]) new Object[q.length * 2];
        if (tail > head) {
            System.arraycopy(q, head + 1, new_q, q.length / 2 + head + 1, tail - head - 1);
        }
        head += q.length / 2;
        tail += q.length / 2;
        q = new_q;
    }

    private void checkNull(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
    }

    private void checkEmpty() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
    }
}