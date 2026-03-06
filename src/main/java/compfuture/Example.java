package compfuture;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class Example {
  public static CompletableFuture<String> getFakeFileContents(String fn) {
    System.out.println("Triggering fake file io");
    CompletableFuture cf = new CompletableFuture<>();
    Thread.ofPlatform().start(() -> {
      try {
        // simulate IO delay
        Thread.sleep(1000 + (int) (Math.random() * 2000));
        if (Math.random() > 0.5) {
          // simulate success
          cf.complete("Contents of the file " + fn + " are:\n" +
              "It was a dark and stormy night, and the IDE was acting scared");
        } else {
          // simulate IO failure
          cf.completeExceptionally(new IOException("Something broke in the file system"));
        }
      } catch (Exception e) {
        cf.completeExceptionally(e);
      }
    });
    System.out.println("Triggered fake file io, what next?...");
    return cf;
  }

  public static String reportError(String s, Throwable t) {
    if (t != null) {
      System.out.println("yikes, something broke: " + t.getMessage());
      return "Recovery text";
    } else return s;
  }

  public static void main(String[] args) {
    var cf = CompletableFuture.supplyAsync(() -> "sometext")
        .thenApply(s -> {
          System.out.println(Thread.currentThread().getName() + " in step 1, processing " + s);
          return s + ".txt";
        })
        .thenComposeAsync(Example::getFakeFileContents)
        .handleAsync(Example::reportError)
        .thenAcceptAsync(System.out::println);
    System.out.println("Pipeline constructed");
    cf.join();
  }
}
