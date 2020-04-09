package queue;

import java.util.Arrays;

public class ArrayQueue extends AbstractQueue {
    private int head = 0;
    private Object[] elements = new Object[5];

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

    public Object element() {
        return elements[head];
    }

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

    public void clear() {
        sz = 0;
        head = 0;
        elements = new Object[5];
    }

    @Override
    public ArrayQueue getEmptyQueue() {
        return new ArrayQueue();
    }

}
