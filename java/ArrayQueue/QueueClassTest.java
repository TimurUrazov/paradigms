package queue;

import java.util.Random;

public class QueueClassTest {
    public static void fillByPush(ArrayQueue queue, int n, int bound) {
        for (int i = 0; i < n; i++) {
            queue.push(new Random().nextInt(bound));
        }
    }

    public static void fillByEnqueue(ArrayQueue queue, int n, int bound) {
        for (int i = 0; i < n; i++) {
            queue.enqueue(new Random().nextInt(bound));
        }
    }

    public static void dumpFIFO(ArrayQueue queue) {
        while (!queue.isEmpty()) {
            System.out.println("size: " + queue.size() +
                    ", head: " + queue.element() + " should be equal to: " + queue.dequeue());
        }
    }

    public static void dumpLIFO(ArrayQueue queue) {
        while (!queue.isEmpty()) {
            System.out.println("size: " + queue.size() +
                    " tail: " + queue.peek() + " should be equal to: " + queue.remove());
        }
    }

    public static void dumpStr(ArrayQueue queue) {
        System.out.println(queue.toStr());
        queue.clear();
    }

    public static void main(String[] args) {
        ArrayQueue queue = new ArrayQueue();
        System.out.println("=== fillByEnqueueTest:");
        fillByEnqueue(queue, 100, 200000);
        dumpFIFO(queue);
        System.out.println("=== fillByPushTest:");
        fillByPush(queue, 100, 200000);
        dumpLIFO(queue);
        System.out.println("=== toStrTest:");
        fillByEnqueue(queue, 12, 200000);
        dumpStr(queue);
    }
}
