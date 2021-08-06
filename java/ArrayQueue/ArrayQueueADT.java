package queue;
import java.util.Arrays;
import java.util.Objects;

// Model: a[0].. a[n - 1]
// n -- size of queue

// Inv: n >= 0
// forall i = 0..n - 1 a[i] != null

// Immutable: n == n' && forall i = 0..n - 1 a[i] = a[i]'

public class ArrayQueueADT {
    private static int head = 0;
    private static int size = 0;
    private static Object[] elements = new Object[2];

    // Pred: true
    // Post: R == new ArrayQueueADT && R.n == 0
    public static ArrayQueueADT create() {
        return new ArrayQueueADT();
    }

    // Pred: queue != null && value != null
    // Post: queue.n = queue.n' + 1 && a[queue.n - 1] = value && forall i = 0..queue.n - 2 a[i] = a[i]'
    public static void enqueue(ArrayQueueADT queue, Object value) {
        Objects.requireNonNull(queue);
        Objects.requireNonNull(value);
        size++;
        elements[(head + size - 1) % elements.length] = value;
        ensureCapacity(queue);
    }

    // Pred: queue != null && queue.n > 0
    // Post: R == a[0] && Immutable
    public static Object element(ArrayQueueADT queue) {
        Objects.requireNonNull(queue);
        assert size(queue) > 0;
        return elements[head];
    }

    // Pred: queue != null && queue.n > 0
    // Post: R == a[0] && queue.n = queue.n' - 1 && forall i = 0..queue.n - 1 a[i] = a[i + 1]'
    public static Object dequeue(ArrayQueueADT queue) {
        Objects.requireNonNull(queue);
        assert size > 0;
        size--;
        final Object x = elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        return x;
    }

    // Pred: queue != null
    // Post: R == queue.n
    public static int size(ArrayQueueADT queue) {
        return queue.size;
    }

    // Pred: queue != null
    // Post: R == [queue.n == 0]
    public static boolean isEmpty(ArrayQueueADT queue) {
        return queue.size == 0;
    }

    // Pred: queue != null
    // Post: queue.n == 0
    public static void clear(ArrayQueueADT queue) {
        Objects.requireNonNull(queue);
        while (!isEmpty(queue)) {
            dequeue(queue);
        }
    }

    // Pre: queue != null && value != null
    // Post: queue.n = queue.n' + 1 && a[0] = value && forall i = 1..queue.n - 1 a[i] = a[i - 1]'
    public static void push(ArrayQueueADT queue, Object value) {
        Objects.requireNonNull(queue);
        Objects.requireNonNull(value);
        head = (head - 1 + elements.length) % elements.length;
        elements[head] = value;
        size++;
        ensureCapacity(queue);
    }

    // Pre: queue != null && queue.n > 0
    // Post: R == a[queue.n - 1] && Immutable
    public static Object peek(ArrayQueueADT queue) {
        Objects.requireNonNull(queue);
        assert size > 0;
        return elements[(head + size - 1 + elements.length) % elements.length];
    }

    // Pre: queue != null && queue.n > 0
    // Post: queue.n = queue.n' - 1 && forall i = 0..queue.n - 1 a[i] == a[i]'
    // R == a[queue.n - 1]
    public static Object remove(ArrayQueueADT queue) {
        Objects.requireNonNull(queue);
        assert size > 0;
        final Object x = peek(queue);
        elements[(head + size - 1 + elements.length) % elements.length] = null;
        size--;
        return x;
    }

    // Pred: queue != null
    // Post: R == "a[0], .., a[queue.n - 1]"
    public static String toStr(ArrayQueueADT queue) {
        Objects.requireNonNull(queue);
        return Arrays.toString(toArray(queue));
    }

    // Pred: queue != null
    // Post: R == [a[0], .., a[queue.n - 1]]
    public static Object[] toArray(ArrayQueueADT queue) {
        Objects.requireNonNull(queue);
        copy(queue, false);
        return Arrays.stream(elements).limit(size).toArray();
    }

    // Pred: queue != null
    // Post: forall i = 0..size - 1 elements[i] = elements[head + i]'
    // forall i = size..elements.length - 1 elements[i] == null
    // enableExtension == true -> elements.length == 2 * (size + 1)
    // head == 0 && tail == size
    private static void copy(ArrayQueueADT queue, boolean enableExtension) {
        Objects.requireNonNull(queue);
        final int capacity = enableExtension ? elements.length * 2 : elements.length;
        Object[] current = new Object[capacity];
        System.arraycopy(elements, head, current, 0, elements.length - head);
        System.arraycopy(elements, 0, current, elements.length - head, head);
        head = 0;
        elements = current;
    }

    private static void ensureCapacity(ArrayQueueADT queue) {
        if (size == elements.length) {
            copy(queue, true);
        }
    }
}
