package queue;

import java.util.Random;

public class QueueModuleTest {
    public static void fillByPush(int n, int bound) {
        for (int i = 0; i < n; i++) {
            ArrayQueueModule.push(new Random().nextInt(bound));
        }
    }

    public static void fillByEnqueue(int n, int bound) {
        for (int i = 0; i < n; i++) {
            ArrayQueueModule.enqueue(new Random().nextInt(bound));
        }
    }

    public static void dumpFIFO() {
        while (!ArrayQueueModule.isEmpty()) {
            System.out.println("size: " + ArrayQueueModule.size() +
                    ", head: " + ArrayQueueModule.element() + " should be equal to: " + ArrayQueueModule.dequeue());
        }
    }

    public static void dumpLIFO() {
        while (!ArrayQueueModule.isEmpty()) {
            System.out.println("size: " + ArrayQueueModule.size() +
                    " tail: " + ArrayQueueModule.peek() + " should be equal to: " + ArrayQueueModule.remove());
        }
    }

    public static void dumpStr() {
        System.out.println(ArrayQueueModule.toStr());
        ArrayQueueModule.clear();
    }

    public static void main(String[] args) {
        System.out.println("=== fillByEnqueueTest:");
        fillByEnqueue(100, 200000);
        dumpFIFO();
        System.out.println("=== fillByPushTest:");
        fillByPush(100, 200000);
        dumpLIFO();
        System.out.println("=== toStrTest:");
        fillByEnqueue(12, 200000);
        dumpStr();
    }
}
