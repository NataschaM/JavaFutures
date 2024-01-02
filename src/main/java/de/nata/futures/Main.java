package de.nata.futures;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class Main {

    private static final CompletableFuturePredictionService COMPLETABLE_FUTURE_PREDICTION_SERVICE = new CompletableFuturePredictionService();

    public static void main(String[] args) throws InterruptedException {
        createPredictionWithFuture();
    }

    // ******** Methods with Future ***********

    /**
     * Method for demonstration of execution and handling of Future objects.
     * A random prediction has to be delivered as Future and the content has to be printed
     */
    public static void createPredictionWithFuture() throws InterruptedException {

        FuturePredictionService futurePredictionService = new FuturePredictionService();
        Future<String> futureResult = futurePredictionService.createRandomPrediction();
        String result = "no result";

        try {
            result = futureResult.get();
        } catch (InterruptedException | ExecutionException e) {
            //do nothing
        }
        System.out.println(result);
        futurePredictionService.shutdown();
    }

    /**
     * Demo for error handling on Future object
     */
    public static void createPredictionWithFutureError() {
        FuturePredictionService futurePredictionService = new FuturePredictionService();
        Future<String> futureResult = futurePredictionService.createPredictionWithError();

        String result = "Sorry, no prediction";

        try {
            result = futureResult.get();
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Exception occurred: " + e.getMessage());
        }
        System.out.println(result);

        futurePredictionService.shutdown();
    }

    /**
     * Demo for handling of Future in case when timeout occurs.
     *
     * @throws InterruptedException
     */
    public static void createPredictionWithFutureTimeout() throws InterruptedException {
        FuturePredictionService futurePredictionService = new FuturePredictionService();
        Future<String> futureResult = futurePredictionService.createPredictionWithTimeout();

        String result = "Sorry, no prediction";

        try {
            result = futureResult.get(2, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            System.err.println("Exception occurred: " + e);
        }
        System.out.println(result);
        futurePredictionService.shutdown();
    }
    // ******** End Future ***********

    // ******** Methods with CompletableFuture **********

    /**
     * Demo how to use the result of CompletableFuture
     */
    public static void createPredictionWithCompletableFuture() {
        COMPLETABLE_FUTURE_PREDICTION_SERVICE.createRandomPrediction()
                .thenAccept(System.out::println);
    }

    /**
     * This method shows what happens in case of exception (nothing happens)
     */
    public static void createCompletableFutureWithError() {
        COMPLETABLE_FUTURE_PREDICTION_SERVICE.createRandomPredictionWithError()
                .thenAccept(System.out::println);
    }

    /**
     * Example of basic executions on CompletableFuture object
     */
    public static void applyBasicMethodsOnCompletableFuture() {
        COMPLETABLE_FUTURE_PREDICTION_SERVICE.createRandomPrediction()
                .thenApply(result -> "Prediction: " + result)
                .thenAccept(System.out::println)
                .thenRun(() -> System.out.println("Computation completed"));
    }

    /**
     * Demo for using of compose() on CompletableFuture
     */
    public static void compose() {
        COMPLETABLE_FUTURE_PREDICTION_SERVICE.generateIndex()
                .thenCompose(COMPLETABLE_FUTURE_PREDICTION_SERVICE::getPredictionToIndex)
                .thenAccept(System.out::println)
                .join();
    }

    /**
     * Example of applyToEither()
     */
    public static void applyToEither() {
        CompletableFuture<Integer> indexFuture = COMPLETABLE_FUTURE_PREDICTION_SERVICE.generateIndexWithTimeout();
        COMPLETABLE_FUTURE_PREDICTION_SERVICE.generateIndex()
                .applyToEither(indexFuture, text -> text)
                .thenAccept(System.out::println)
                .join();
    }

    /**
     * Example of combine()
     */
    public static void combine() {
        COMPLETABLE_FUTURE_PREDICTION_SERVICE.createRandomPrediction()
                .thenCombine(
                        COMPLETABLE_FUTURE_PREDICTION_SERVICE.createRandomPrediction(),
                        (result1, result2) -> result1 + " + " + result2)
                .thenAccept(System.out::println)
                .join();
    }

    /**
     * Example of exception handling with exceptionally()
     */
    public static void exceptionally() {
        COMPLETABLE_FUTURE_PREDICTION_SERVICE.createRandomPredictionWithError()
                .exceptionally(throwable -> {
                    System.out.println("Exception occured: " + throwable);
                    return "Sorry, no prediction today";
                })
                .thenAccept(System.out::println);
    }

    /**
     * Exception handling via handle()
     */
    public static void handle() {
        COMPLETABLE_FUTURE_PREDICTION_SERVICE.createRandomPredictionWithError()
                .handle((result, error) -> {
                    if (error != null) {
                        System.err.println("Exception occured: " + error);
                        result = "Sorry, no prediction today";
                    }
                    System.out.println(result);
                    return (Void) null;
                }).join();
    }

    /**
     * Exception handling with whenComplete
     */
    public static void whenComplete() {
        COMPLETABLE_FUTURE_PREDICTION_SERVICE.createRandomPredictionWithError()
                .thenAccept(System.out::println)
                .whenComplete((v, e) -> {
                    if (e != null) {
                        System.err.println("Exception occured: " + e);
                    }
                }).join();
    }

    // ************ End of CompletableFuture methods ************
}
