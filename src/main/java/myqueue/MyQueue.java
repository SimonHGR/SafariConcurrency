package myqueue;

import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyQueue<E> {
  private static final int CAPACITY = 10;
  private E[] data = (E[])new Object[CAPACITY];
  private int count = 0;
  private Lock lock = new ReentrantLock();
  private Condition notFull = lock.newCondition();
  private Condition notEmpty = lock.newCondition();

  public void put(E e) throws InterruptedException {
    lock.lock();
    try {
      while (count == CAPACITY) {
        notFull.await();
      }
      data[count++] = e;
      notEmpty.signal();
    } finally {
      lock.unlock();
    }
  }

  public E take() throws InterruptedException {
    lock.lock();
    try {
      while (count == 0) {
        notEmpty.await();
      }
      E rv = data[0];
      System.arraycopy(data, 1, data, 0, --count);
      notFull.signal();
      return rv;
    } finally {
      lock.unlock();
    }
  }

  public static void main(String[] args) {
    MyQueue<int[]> queue = new MyQueue<>();
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
          queue.put(data);
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
