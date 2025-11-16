package visibility;

public class Example1 {
  private /*volatile*/ static boolean stop = false;

  public static void main(String[] args) throws Throwable {
    Runnable stopper = () -> {
      System.out.println("Stopper task starting");
      while (!stop)
        ; // "spin wait" here until flag is set true
      System.out.println("Stopper task exiting");
    };
    Thread.ofPlatform().start(stopper);
    Thread.sleep(1_000);
    System.out.println("Setting stop flag");
    stop = true;
    System.out.println("Stop flag is " + stop + " main exiting");
  }
}
