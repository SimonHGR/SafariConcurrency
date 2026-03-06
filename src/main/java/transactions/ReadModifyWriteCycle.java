package transactions;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAccumulator;

public class ReadModifyWriteCycle {
  private static long count = 0L;
//  private volatile static long count = 0L;
//  private static AtomicLong count = new AtomicLong();
//  private static LongAccumulator count = new LongAccumulator((a, b) -> a + b, 0L);

  public static void main(String[] args) throws Throwable {
    Runnable incrementer = () -> {
      for (int x = 0; x < 1_000_000_000; x++) {
        count++;
//        count.incrementAndGet();
//        count.accumulate(1);
      }
      System.out.println("incrementer task on thread " + Thread.currentThread().getName() + " completed");
    };

    long start = System.nanoTime();
    Thread t1 = Thread.ofPlatform().start(incrementer);
    Thread t2 = Thread.ofPlatform().start(incrementer);
    Thread t3 = Thread.ofPlatform().start(incrementer);
    Thread t4 = Thread.ofPlatform().start(incrementer);
    t1.join();
    t2.join();
    t3.join();
    t4.join();
    long elapsed = System.nanoTime() - start;
    System.out.println("total value of count is " + count + " time was " + (elapsed / 1_000_000_000.0));
  }
}
