package queue;

import java.util.List;

public class LinkedQueue extends AbstractQueue {
    public class Node {
        private Node next = null;
        private Object value = null;
        public Node (Node node) {
            next = node;
        }
        public Node (Node node, Object val) {
            next = node;
            value = val;
        }

        public Node getNext() {
            return next;
        }
        public void setNext(Node node) {
            next = node;
        }

        public Object getValue() {
            return value;
        }
    }

    private Node head = null;
    private Node tale = null;

    @Override
    public void enqueue(Object element) {
        if (sz == 0) {
            tale = new Node(null, element);
            head = tale;
        } else {
            Node node = new Node(null, element);
            head.setNext(node);
            head = node;
        }
        sz++;
    }

    @Override
    public Object element() {
        return tale.getValue();
    }

    @Override
    public Object dequeue() {
        Object res = tale.getValue();
        tale = tale.getNext();
        sz--;
        return res;
    }

    @Override
    public void clear() {
        head = null;
        sz = 0;
    }


    @Override
    public LinkedQueue getEmptyQueue() {
        return new LinkedQueue();
    }

}
