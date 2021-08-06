package queue;

// Model: a[0].. a[n - 1]
// n -- size of queue

// Inv: n >= 0
// forall i = 0..n - 1 a[i] != null

// Immutable: n == n' && forall i = 0..n - 1 a[i] = a[i]'

public interface Queue {
    // Pred: value != null
    // Post: n = n' + 1 && a[n - 1] = value && forall i = 0..n - 2 a[i] = a[i]'
    void enqueue(Object value);

    // Pred: n > 0
    // Post: R == a[0] && Immutable
    Object element();

    // Pred: n > 0
    // Post: R == a[0] && n = n' - 1 && forall i = 0..n - 1 a[i] = a[i + 1]'
    Object dequeue();

    // Pred: true
    // Post: R == n && Immutable
    int size();

    // Pred: true
    // Post: R == [n == 0] && Immutable
    boolean isEmpty();

    // Pred: true
    // Post: n == 0 && Immutable
    void clear();
}
