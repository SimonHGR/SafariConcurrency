package visibility;

public class Stopper1 {
  private static boolean stop = false;

  public static void main(String[] args) throws Throwable {
    Runnable stopper = () -> {
      System.out.println("Stopper task starting");
      while (! stop)
        ;
      System.out.println("Stopper task completing");
    };
    Thread.ofPlatform()
        .start(stopper);
    Thread.sleep(1_000);
    System.out.println("main setting stop to true");
    stop = true;
    System.out.println("Main exiting. Stop is " + stop);
  }
}
