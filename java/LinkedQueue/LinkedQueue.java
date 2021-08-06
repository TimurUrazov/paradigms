package queue;

public class LinkedQueue extends AbstractQueue {
    private Node head = null;
    private Node tail = null;

    private static class Node {
        private Node next;
        private final Object value;

        private Node(Node next, Object value) {
            this.next = next;
            this.value = value;
        }
    }

    @Override
    public void enqueueImpl(Object value) {
        Node tmp = tail;
        tail = new Node(null, value);
        if (size - 1 == 0) {
            head = tail;
        } else {
            tmp.next = tail;
        }
    }

    public Object element() {
        assert size() > 0;
        return head.value;
    }

    @Override
    public Object dequeueImpl() {
        final Object x = head.value;
        head = head.next;
        return x;
    }

    @Override
    protected Queue queueImpl() {
        return new LinkedQueue();
    }
}

