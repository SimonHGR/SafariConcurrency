package dataparallel;

import java.util.Arrays;

public class Sequential {
  public static long fib(long n) {
    if (n < 3) return 1;
    else return fib(n - 1) + fib(n - 2);
  }

  public static void main(String[] args) {
    long [] data = {42, 43, 42, 42, 42, 42, 42, 42, 41, 42, 42, 43, 42, 43, 40, 42, 43, 42, 42, 40, 41, 40, 40, 41, 41, 40, 39, 43, 40, 43};

    long start = System.nanoTime();
    for (int i = 0; i < data.length; i++) {
      data[i] = fib(data[i]);
//      System.out.println(i);
    }

    long elapsed = System.nanoTime() - start;
    System.out.println(Arrays.toString(data));
    System.out.printf("Sequential calcuation completed in %7.3f seconds\n", (elapsed / 1_000_000_000.0));
  }
}
