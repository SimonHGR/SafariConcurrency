package transactions;

class Incrementer implements Runnable {
  private long count = 0;

  @Override
  public void run() {
    for (int i = 0; i < 100_000_000; i++) {
      // despite the seeming simplicity, this is
      // a "transactional" problem when executed
      // concurrently by two or more threads
      // Options for fixing:
      //   a synchronized block
      //   a library lock
      //   a library "atomic variable"
      count++;
    }
    System.out.println("an incrementer has completed");
  }

  public long getCount() {
    return count;
  }
}
public class ReadModifyWrite {
  public static void main(String[] args) throws Throwable {
    Incrementer task = new Incrementer();
    Thread t1 = Thread.ofPlatform().start(task);
    Thread t2 = Thread.ofPlatform().start(task);

    // First, we have timing and visibility problems
    // fix them both like this
//    t1.join();
//    t2.join();
    System.out.println("Count value is: " + task.getCount());
  }
}
