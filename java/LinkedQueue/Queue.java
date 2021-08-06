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
    // Post: n == 0 && a = []
    void clear();

    // Pred: true
    // Post: R == queue && Immutable
    // forall i = 0..queue.n - 1 queue.a[i] == a[index * (i + 1) - 1] && queue.n == k && index * k' <= n && k == max k' && k' >= 1
    // [a[0]..a[n - 1]] == [a[0]..a[index - 1]) U [queue.a[0]] U [a[index]..a[2 * index - 1]) U ...
    // ... [a[(k - 1) * index]..a[k * index - 1]) U [queue.a[k - 1]] U [a[index * k].. a[n - 1]]
    // the type of the returned queue matches the type of original
    Queue getNth(int index);

    // Pred: true
    // Post: R == queue
    // forall i = 0..queue.n - 1 queue.a[i] == a[index * (i + 1) - 1] && queue.n == k && index * k' <= n' && k == max k' && k' >= 1
    // [a[0]'..a[n' - 1]'] == [a[0]'..a[index - 1]') U [queue.a[0]] U [a[index]'..a[2 * index - 1]') U ...
    // ... [a[(k - 1) * index]'..a[k * index - 1]') U [queue.a[k - 1]] U [a[index * k]'.. a[n' - 1]']
    // [a[0]..a[n - 1]] == [a[0]'..a[index - 1]') U [a[index]'..a[2 * index - 1]') U ...
    // ... [a[(k - 1) * index]'..a[k * index - 1]') U [a[index * k]'.. a[n' - 1]']
    // n + queue.n == n'
    // the type of the returned queue matches the type of original
    Queue removeNth(int index);

    // Pred: true
    // index * k' <= n' && k == max k' && k' >= 1 && n + k == n'
    // [a[0]..a[n - 1]] == [a[0]'..a[index - 1]') U [a[index]'..a[2 * index - 1]') U ...
    // ... [a[(k - 1) * index]'..a[k * index - 1]') U [a[index * k]'.. a[n' - 1]']
    void dropNth(int index);
}

