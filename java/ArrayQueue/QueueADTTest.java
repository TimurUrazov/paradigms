package queue;

import java.util.Random;

public class QueueADTTest {
    public static void fillByPush(ArrayQueueADT queue, int n, int bound) {
        for (int i = 0; i < n; i++) {
            ArrayQueueADT.push(queue, new Random().nextInt(bound));
        }
    }

    public static void fillByEnqueue(ArrayQueueADT queue, int n, int bound) {
        for (int i = 0; i < n; i++) {
            ArrayQueueADT.enqueue(queue, new Random().nextInt(bound));
        }
    }

    public static void dumpFIFO(ArrayQueueADT queue) {
        while (!ArrayQueueADT.isEmpty(queue)) {
            System.out.println("size: " + ArrayQueueADT.size(queue) +
                    ", head: " + ArrayQueueADT.element(queue) + " should be equal to: " + ArrayQueueADT.dequeue(queue));
        }
    }

    public static void dumpLIFO(ArrayQueueADT queue) {
        while (!ArrayQueueADT.isEmpty(queue)) {
            System.out.println("size: " + ArrayQueueADT.size(queue) +
                    " tail: " + ArrayQueueADT.peek(queue) + " should be equal to: " + ArrayQueueADT.remove(queue));
        }
    }

    public static void dumpStr(ArrayQueueADT queue) {
        System.out.println(ArrayQueueADT.toStr(queue));
        ArrayQueueADT.clear(queue);
    }

    public static void main(String[] args) {
        System.out.println("=== First queue:");
        ArrayQueueADT queue = ArrayQueueADT.create();
        System.out.println("=== fillByEnqueueTest:");
        fillByEnqueue(queue, 100, 200000);
        dumpFIFO(queue);
        System.out.println("=== fillByPushTest:");
        fillByPush(queue, 100, 200000);
        dumpLIFO(queue);
        System.out.println("=== toStrTest:");
        fillByEnqueue(queue, 12, 200000);
        dumpStr(queue);
        System.out.println("=== Second queue:");
        ArrayQueueADT queue2 = ArrayQueueADT.create();
        System.out.println("=== fillByEnqueueTest:");
        fillByEnqueue(queue2, 100, 1000000);
        dumpFIFO(queue2);
        System.out.println("=== fillByPushTest:");
        fillByPush(queue2, 100, 1000000);
        dumpLIFO(queue2);
        System.out.println("=== toStrTest:");
        fillByEnqueue(queue2, 12, 1000000);
        dumpStr(queue2);
    }
}
