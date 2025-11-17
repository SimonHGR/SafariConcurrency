package prodcons;

import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Example {
  BlockingQueue<int[]> queue = new ArrayBlockingQueue<>(10);
  final int DATA_COUNT = 10_000;

  Runnable producer = () -> {
    System.out.println("Producer starting");
    try {
      for (int i = 0; i < DATA_COUNT; i++) {
        int [] data = { 0, i }; // transactionally unsound
        if (i < 1000) {
          // force a mostly empty queue, show no duplicated data read
          // also leave transactionally unsound data "vulnerable"
          Thread.sleep(1);
        }
        data[0] = i; // now it's transactionally sound and safe to publish
        if (i == DATA_COUNT / 2) {
          data[1] = -1; // test the test!
        }
        queue.put(data);
        // this is not strictly necessary since we immediately
        // overwrite the variable, but it's safer to ensure
        // we don't accidentally use this data which has now
        // been share -- Java variables are (mostly) references
        data = null;
      }
    } catch (InterruptedException ie) {
      System.out.println("Unexpected!!");
    }
    System.out.println("Producer completed");
  };

  Runnable consumer = () -> {
    System.out.println("Consumer starting");
    try {
      for (int i = 0; i < DATA_COUNT; i++) {
        int [] data = queue.take();
        if (data[0] != data[1] || data[0] != i) {
          System.out.printf("**** ERROR at index %d. data is %s\n",
              i, Arrays.toString(data));
        }
        if (i > DATA_COUNT - 1000) {
          Thread.sleep(1);
        }
      }
    } catch (InterruptedException io) {
      System.out.println("Unexpected!");
    }
    System.out.println("Consumer completed");
  };

  public static void main(String[] args) {
    new Example().go();
    System.out.println("main() exiting");
  }

  public void go() {
    Thread p = Thread.ofPlatform().start(producer);
    Thread c = Thread.ofPlatform().start(consumer);
  }
}
