package visibility;

import java.util.concurrent.atomic.AtomicBoolean;

public class Stopper3 {
  private static AtomicBoolean stop = new AtomicBoolean(false);

  public static void main(String[] args) throws Throwable {
    Runnable stopper = () -> {
      System.out.println("Stopper task starting");
      while (!stop.get())
        ;
      System.out.println("Stopper task completing");
    };
    Thread.ofPlatform()
        .start(stopper);
    Thread.sleep(1_000);
    System.out.println("main setting stop to true");
    stop.set(true);
    System.out.println("Main exiting. Stop is " + stop);
  }
}
