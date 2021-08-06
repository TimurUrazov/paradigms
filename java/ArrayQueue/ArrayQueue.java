package queue;

import java.util.Arrays;
import java.util.Objects;

public class ArrayQueue extends AbstractQueue {
    private int head = 0;
    private Object[] elements = new Object[2];

    @Override
    protected void enqueueImpl(Object value) {
        elements[(head + size - 1) % elements.length] = value;
        ensureCapacity();
    }

    public Object element() {
        assert size() > 0;
        return elements[head];
    }

    @Override
    protected Object dequeueImpl() {
        final Object x = elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        return x;
    }

    // Pre: value != null
    // Post: n = n' + 1 && a[0] = value && forall i = 1..n - 1 a[i] = a[i - 1]'
    public void push(Object value) {
        Objects.requireNonNull(value);
        head = (head - 1 + elements.length) % elements.length;
        elements[head] = value;
        size++;
        ensureCapacity();
    }

    // Pre: n > 0
    // Post: R == a[n - 1] && Immutable
    public Object peek() {
        assert size() > 0;
        return elements[(head + size - 1 + elements.length) % elements.length];
    }

    // Pre: n > 0
    // Post: n = n' - 1 && forall i = 0..n - 1 a[i] == a[i]'
    // R == a[n - 1]
    public Object remove() {
        assert size() > 0;
        final Object x = peek();
        elements[(head + size - 1 + elements.length) % elements.length] = null;
        size--;
        return x;
    }

    // Pred: true
    // Post: R == "a[0], .., a[n - 1]"
    public String toStr() {
        return Arrays.toString(toArray());
    }

    // Pred: true
    // Post: R == [a[0], .., a[n - 1]]
    public Object[] toArray() {
        copy(false);
        return Arrays.stream(elements).limit(size()).toArray();
    }

    // Pred: true
    // Post: forall i = 0..size - 1 elements[i] = elements[head + i]'
    // forall i = size..elements.length - 1 elements[i] == null
    // enableExtension == true -> elements.length == 2 * elements.length'
    // head == 0 && tail == size
    private void copy(boolean enableExtension) {
        final int capacity = enableExtension ? elements.length * 2 : elements.length;
        Object[] current = new Object[capacity];
        System.arraycopy(elements, head, current, 0, elements.length - head);
        System.arraycopy(elements, 0, current, elements.length - head, head);
        head = 0;
        elements = current;
    }

    private void ensureCapacity() {
        if (size == elements.length) {
            copy(true);
        }
    }
}
