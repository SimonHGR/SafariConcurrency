package blockqueue;

import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BlockQueue {
  public static void main(String[] args) {
    BlockingQueue<int[]> queue = new ArrayBlockingQueue<>(10);
    Runnable producer = () -> {
      try {
        for (int i = 0; i < 10_000; i++) {
          int [] data = {-1, i}; // "transactionally invalid"
          if (i > 9_500) {
            Thread.sleep(1);
          }
          data[0] = i; // valid now
          if (i == 5_000) {
            data[1] = -1; // test the test!
          }
          queue.put(data); data = null; // I don't own this!!!
        }
      } catch (InterruptedException ie) {
        System.out.println("Odd, shutdown of producer requested!");
      }
    };
    Runnable consumer = () -> {
      try {
        for (int i = 0; i < 10_000; i++) {
          int [] data = queue.take();
          if (data[0] != data[1] || data[0] != i) {
            System.out.println("**** ERROR at index " + i + " received " + Arrays.toString(data));
          }
          if (i < 500) {
            Thread.sleep(1);
          }
        }
      } catch (InterruptedException ie) {
        System.out.println("Odd, shutdown of consumer requested!");
      }
    };
    Thread.ofPlatform().start(producer);
    Thread.ofPlatform().start(consumer);
    System.out.println("producer and consumer started, main exiting");
  }
}


