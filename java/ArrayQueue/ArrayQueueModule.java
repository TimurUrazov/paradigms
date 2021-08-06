package queue;
import java.util.Arrays;
import java.util.Objects;

// Model: a[0].. a[n - 1]
// n -- size of queue

// Inv: n >= 0
// forall i = 0..n - 1 a[i] != null

// Immutable: n == n' && forall i = 0..n - 1 a[i] = a[i]'

public class ArrayQueueModule {
    private static int head = 0;
    private static int size = 0;
    private static Object[] elements = new Object[2];

    // Pred: value != null
    // Post: n = n' + 1 && a[n - 1] = value && forall i = 0..n - 2 a[i] = a[i]'
    public static void enqueue(Object value) {
        Objects.requireNonNull(value);
        size++;
        elements[(head + size - 1) % elements.length] = value;
        ensureCapacity();
    }

    // Pred: n > 0
    // Post: R == a[0] && Immutable
    public static Object element() {
        assert size > 0;
        return elements[head];
    }

    // Pred: true
    // Post: R == [n == 0] && Immutable
    public static boolean isEmpty() {
        return size == 0;
    }

    // Pred: true
    // Post: R == n && Immutable
    public static int size() {
        return size;
    }

    // Pred: true
    // Post: n == 0
    public static void clear() {
        while (!isEmpty()) {
            dequeue();
        }
    }

    // Pred: n > 0
    // Post: R == a[0] && n = n' - 1 && forall i = 0..n - 1 a[i] = a[i + 1]'
    public static Object dequeue() {
        assert size > 0;
        size--;
        final Object x = elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        return x;
    }

    // Pre: value != null
    // Post: n = n' + 1 && a[0] = value && forall i = 1..n - 1 a[i] = a[i - 1]'
    public static void push(Object value) {
        Objects.requireNonNull(value);
        head = (head - 1 + elements.length) % elements.length;
        elements[head] = value;
        size++;
        ensureCapacity();
    }

    // Pre: n > 0
    // Post: R == a[n - 1] && Immutable
    public static Object peek() {
        assert size > 0;
        return elements[(head + size - 1 + elements.length) % elements.length];
    }

    // Pre: n > 0
    // Post: n = n' - 1 && forall i = 0..n - 1 a[i] == a[i]'
    // R == a[n - 1]
    public static Object remove() {
        assert size > 0;
        final Object x = peek();
        elements[(head + size - 1 + elements.length) % elements.length] = null;
        size--;
        return x;
    }

    // Pred: true
    // Post: R == "a[0], .., a[n - 1]"
    public static String toStr() {
        return Arrays.toString(toArray());
    }

    // Pred: true
    // Post: R == [a[0], .., a[n - 1]]
    public static Object[] toArray() {
        copy(false);
        return Arrays.stream(elements).limit(size).toArray();
    }

    // Pred: true
    // Post: forall i = 0..size - 1 elements[i] = elements[head + i]'
    // forall i = size..elements.length - 1 elements[i] == null
    // enableExtension == true -> elements.length == 2 * elements.length'
    // head == 0 && tail == size
    private static void copy(boolean enableExtension) {
        final int capacity = enableExtension ? elements.length * 2 : elements.length;
        Object[] current = new Object[capacity];
        System.arraycopy(elements, head, current, 0, elements.length - head);
        System.arraycopy(elements, 0, current, elements.length - head, head);
        head = 0;
        elements = current;
    }

    private static void ensureCapacity() {
        if (size == elements.length) {
            copy(true);
        }
    }
}
