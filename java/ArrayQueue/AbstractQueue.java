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
        while (!isEmpty()) {
            dequeue();
        }
    }
}

