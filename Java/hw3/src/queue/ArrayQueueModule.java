package queue;

import java.util.Arrays;

public class ArrayQueueModule {
    private static int head = 0, sz;
    private static Object[] elements = new Object[5];

    public static void enqueue(Object element) {
        assert element != null;

        ensureCapacity(sz + 1);
        int pos = head + sz;
        if (pos >= elements.length)
            pos %= elements.length;
        elements[pos] = element;
        sz++;
    }

    private static void ensureCapacity(int capacity) {
        if (capacity > elements.length) {
            Object[] newElements = new Object[2 * capacity];
            System.arraycopy(elements, head, newElements, 0, elements.length - head);
            System.arraycopy(elements, 0, newElements, elements.length - head, head);
            elements = newElements;
            head = 0;
        }
    }

    public static void push(Object element) {
        assert element != null;

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

    public static Object peek() {
        int pos = head + sz - 1;
        if (pos >= elements.length) {
            pos -= elements.length;
        }
        return elements[pos];
    }

    public static Object remove() {
        int pos = head + sz - 1;
        if (pos >= elements.length) {
            pos -= elements.length;
        }
        Object res = elements[pos];
        elements[pos] = null;
        sz--;
        return res;
    }

    public static Object element() {
        return elements[head];
    }

    public static Object dequeue() {
        Object element = element();
        elements[head] = null;
        head++;
        sz--;
        if (head == elements.length) {
            head = 0;
        }
        return element;
    }

    public static int size() {
        return sz;
    }

    public static boolean isEmpty() {
        return sz == 0;
    }

    public static void clear() {
        sz = 0;
        head = 0;
        elements = new Object[5];
    }
}
