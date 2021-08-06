package queue;

import java.util.Objects;

public abstract class AbstractQueue implements Queue {
    protected int size = 0;
    @Override
    public void enqueue(Object value) {
        Objects.requireNonNull(value);
        size++;
        enqueueImpl(value);
    }

    protected abstract void enqueueImpl(Object value);

    @Override
    public Object dequeue() {
        assert size() > 0;
        size--;
        return dequeueImpl();
    }

    protected abstract Object dequeueImpl();

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        dropNth (1);
    }

    @Override
    public Queue getNth(int n) {
        return processNth(n, queueImpl(), true, true);
    }

    @Override
    public Queue removeNth(int n) {
        return processNth(n, queueImpl(), true, false);
    }

    @Override
    public void dropNth(int n) {
        processNth(n, this, false, false);
    }

    private Queue processNth(int n, Queue queue, boolean buildQueue, boolean saveInitialQueue) {
        assert n > 0;
        final int currentSize = size();
        for (int i = 0; i < currentSize; i++) {
            final Object tmp = dequeue();
            final boolean Nth = (i + 1) % n == 0;
            if (!Nth || saveInitialQueue) {
                enqueue(tmp);
            }
            if (Nth && buildQueue) {
                queue.enqueue(tmp);
            }
        }
        return queue;
    }

    protected abstract Queue queueImpl();
}

