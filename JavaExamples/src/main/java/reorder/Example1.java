package reorder;

// THIS EXAMPLE SHOWS HOW *NOT* TO DO THINGS
// IT DOES NOT WORK RELIABLY! Even if it "works"
// for you, it's still *WRONG*

public class Example1 {
  static int x = 0, y = 0;
  public static void t1() {
    y = 2 + 2; // some calculation
    x = 1; // indicate calculation complete
    System.out.println("y is " + y);
  }

  public static void t2() {
    // This thread is unlikely to see the change in the value of x
    // because there is no "visibility relationship" between the
    // update to x by t1 and any subsequent read here by t2.
    // For this to work we must use a proper mechanism to indicate
    // that the change by t1 matters to other threads
    while (x == 0)
      ; // wait for ready signal
    System.out.println("value seen is " + y);
  }
}
