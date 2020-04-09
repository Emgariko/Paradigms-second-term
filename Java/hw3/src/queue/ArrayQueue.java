package queue;

import java.util.Arrays;

// Inv:
// size >= 0, forall i = 0, ..., size - 1 q[i] != null
// FIFO(first in(in head), first out(from tale)),
// q[0] - queue tale, q[sz - 1] - queue head
public class ArrayQueue {
    private int head = 0, sz;
    private Object[] elements = new Object[5];

    // Pre: element != null
    // Post: q' = [e_0, ..., e_(size - 1), element], size' = size + 1
    public void enqueue(Object element) {
        ensureCapacity(sz + 1);
        int pos = head + sz;
        if (pos >= elements.length)
            pos -= elements.length;
        elements[pos] = element;
        sz++;
    }

    // Pre: element != null
    // Post: q' = [element, e_0, ..., e_(size - 1)], size' = size + 1
    public void push(Object element) {
        ensureCapacity(sz + 1);
        int pos = head - 1;
        if (pos < 0) {
            pos = elements.length + pos;
        }
        head--;
        if (head < 0) {
            head = elements.length + head;
        }
        elements[pos] = element;
        sz++;
    }

    private void ensureCapacity(int capacity) {
        if (capacity > elements.length) {
            Object[] newElements = new Object[2 * elements.length];
            System.arraycopy(elements, head, newElements, 0, elements.length - head);
            System.arraycopy(elements, 0, newElements, elements.length - head, head);
            elements = newElements;
            head = 0;
        }
    }

    // Pre: size > 0
    // Post: R = q[size], immutable
    public Object peek() {
        int pos = head + sz - 1;
        if (pos >= elements.length) {
            pos -= elements.length;
        }
        return elements[pos];
    }

    // Pre: size > 0
    // Post: R = q[size - 1], size' = size - 1, q' = [e_0, ..., e_(size - 2)]
    public Object remove() {
        int pos = head + sz - 1;
        if (pos >= elements.length) {
            pos -= elements.length;
        }
        Object res = elements[pos];
        elements[pos] = null;
        sz--;
        return res;
    }

    // Pre: size > 0
    // Post: R = q[0], immutable
    public Object element() {
        return elements[head];
    }

    // Pre: size > 0
    // Post: R = q[0], size' = size - 1, q' = [e_1, ..., e_(size - 1)]
    public Object dequeue() {
        Object element = element();
        elements[head] = null;
        head++;
        sz--;
        if (head == elements.length) {
            head = 0;
        }
        return element;
    }

    // Pre: true
    // Post: R = sz, immutable
    public int size() {
        return sz;
    }

    // Pre: true
    // Post: R = bool(sz == 0), immutable
    public boolean isEmpty() {
        return sz == 0;
    }

    // Pre: true
    // Post: q' = [], sz' = 0
    public void clear() {
        sz = 0;
        head = 0;
        elements = new Object[5];
    }
}
