package org.example;

import java.util.concurrent.*;

public class Main {

    private static final PredictionService service = new PredictionService();

    public static void main(String[] args) throws InterruptedException {

        whenComplete();
    }

    public static void whenComplete() {
        service.findPredictionCompletableFutureWithError()
                .thenAccept(System.out::println)
                .whenComplete((v,e) -> {
                    if (e != null) {
                        System.out.println("Exception occured: " + e);
                    }
                });
    }

    public static void handle() {
        service.findPredictionCompletableFutureWithError()
                .handle((result, error) -> {
                    if (error != null) {
                        System.out.println("Exception occured: " + error);
                        result = "Sorry, no prediction today";
                    }
                    System.out.println(result);
                    return (Void) null;
                }).join();
    }

    public static void exceptionally() {
        service.findPredictionCompletableFutureWithError()
                .exceptionally(throwable -> {
                    System.out.println("Exception occured: " + throwable);
                    return "Sorry, no prediction today";
                })
                .thenAccept(System.out::println);
    }

    public static void combine() {
        service.findPredictionCompletableFuture()
                .thenCombine(service.findPredictionCompletableFuture(), (result1, result2) -> result1 + " + " + result2)
                .thenAccept(System.out::println)
                .join();
    }

    public static void composeWithNumber() {
        CompletableFuture<Integer> timeoutFuture = service.generateIndex();
        service.generateIndex()
                .thenCompose(service::getPredictionToIndex)
                .thenAccept(result -> {
                    System.out.println("In accept:" + Thread.currentThread().getName());
                    System.out.println("Final: " + result);
                }).join();
    }

    public static void applyToEither() {
        CompletableFuture<Integer> timeoutFuture = service.generateIndex();
        service.generateIndex()
                .applyToEither(timeoutFuture, text -> text)
                .thenAccept(result -> {
                    System.out.println("In accept:" + Thread.currentThread().getName());
                    System.out.println("Final:" + result);
                }).join();
    }

    public static void compose() {
        service.findPredictionCompletableFuture()
                .handle((result, error) -> {
                    if(error != null) {
                        System.err.println("Exception: " + error.getMessage());
                    }
                    System.out.println(result);
                    return result;
                })
                .thenCompose(service::compose)
                .exceptionally(throwable -> {
                    System.err.println("Exception: " + throwable);
                    return "new text";
                })
                .thenAccept(System.out::println).join();
    }

    public static void generateCompletableFuture() {
        service.findPredictionCompletableFuture()
                .thenApply(result -> "Prediction: "  + result)
                .thenAccept(System.out::println)
                .thenRun(() -> System.out.println("Computation completed"));
    }

    public static void generateCompletableFutureWithError() {
        service.findPredictionCompletableFutureWithError().thenAccept(System.out::println);
    }

    public static void generateWithFuture() throws InterruptedException {
        for (int i=0; i < 10000; i++) {
            Future<String> futureResult = service.executePredictionWithFuture();

            String result = "no result";

            try {
                result = futureResult.get();
            } catch (InterruptedException | ExecutionException e) {
                //do nothing
            }
            System.out.println(i + ": " + result);
        }
        service.shutdown();
    }

    public static void generateWithFutureError() {
        PredictionGenerator generator = new PredictionGenerator();
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<String> futureResult = service.submit(generator::generatePredictionWithError);

        String result = "no result";

        try {
            result = futureResult.get();
        } catch (InterruptedException | ExecutionException e) {
            //do nothing
        }
        System.out.println(result);

        service.shutdown();
    }

    public static void generateWithFutureTimeout() {
        PredictionGenerator generator = new PredictionGenerator();
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<String> futureResult = service.submit(generator::generatePredictionWithError);

        String result = "no result";

        try {
            result = futureResult.get(2, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            //do nothing
        }
        System.out.println(result);

        service.shutdown();
    }
}