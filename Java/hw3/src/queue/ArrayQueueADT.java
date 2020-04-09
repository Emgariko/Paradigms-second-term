package queue;

public class ArrayQueueADT {
    private int head = 0, sz;
    private Object[] elements = new Object[5];

    public static void enqueue(ArrayQueueADT queue, Object element) {
        assert element != null;

        ensureCapacity(queue, queue.sz + 1);
        int pos = queue.head + queue.sz;
        if (pos >= queue.elements.length)
            pos %= queue.elements.length;
        queue.elements[pos] = element;
        queue.sz++;
    }

    private static void ensureCapacity(ArrayQueueADT queue, int capacity) {
        if (capacity > queue.elements.length) {
            Object[] newElements = new Object[2 * capacity];
            System.arraycopy(queue.elements, queue.head, newElements, 0, queue.elements.length - queue.head);
            System.arraycopy(queue.elements, 0, newElements, queue.elements.length - queue.head, queue.head);
            queue.elements = newElements;
            queue.head = 0;
        }
    }

    public static void push(ArrayQueueADT queue, Object element) {
        assert element != null;

        ensureCapacity(queue,queue.sz + 1);
        int pos = queue.head - 1;
        if (pos < 0) {
            pos = queue.elements.length + pos;
        }
        queue.head--;
        if (queue.head < 0) {
            queue.head = queue.elements.length + queue.head;
        }
        queue.elements[pos] = element;
        queue.sz++;
    }

    public static Object peek(ArrayQueueADT queue) {
        int pos = queue.head + queue.sz - 1;
        if (pos >= queue.elements.length) {
            pos -= queue.elements.length;
        }
        return queue.elements[pos];
    }

    public static Object remove(ArrayQueueADT queue) {
        int pos = queue.head + queue.sz - 1;
        if (pos >= queue.elements.length) {
            pos -= queue.elements.length;
        }
        Object res = queue.elements[pos];
        queue.elements[pos] = null;
        queue.sz--;
        return res;
    }

    public static Object element(ArrayQueueADT queue) {
        return queue.elements[queue.head];
    }

    public static Object dequeue(ArrayQueueADT queue) {
        Object element = element(queue);
        queue.elements[queue.head] = null;
        queue.head++;
        queue.sz--;
        if (queue.head == queue.elements.length) {
            queue.head = 0;
        }
        return element;
    }

    public static int size(ArrayQueueADT queue) {
        return queue.sz;
    }

    public static boolean isEmpty(ArrayQueueADT queue) {
        return queue.sz == 0;
    }

    public static void clear(ArrayQueueADT queue) {
        queue.sz = 0;
        queue.head = 0;
        queue.elements = new Object[5];
    }
}
