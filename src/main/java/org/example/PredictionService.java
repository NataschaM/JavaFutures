package org.example;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PredictionService {

    private final PredictionGenerator generator;
    private ExecutorService service;


    public PredictionService() {
        generator = new PredictionGenerator();
    }

    public CompletableFuture<Integer> generateIndex() {
        return CompletableFuture.supplyAsync(generator::getRandomNumber);
    }

    public CompletableFuture<Integer> generateIndexWithTimeout() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("In generate index:" + Thread.currentThread().getName());
                Thread.sleep(2000);
                return generator.getRandomNumber();
            } catch (InterruptedException e) {
                System.out.println("ERROR!");
                throw new RuntimeException(e);
            }
        });
    }

    public CompletableFuture<String> getPredictionToIndex(int number) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("In compose to index:" + Thread.currentThread().getName());
            return generator.getPredictionToNumber(number);
        });
    }

    public CompletableFuture<String> compose(String param) {

        return CompletableFuture.supplyAsync(() -> {
            String output = param + " + ";
            String prediction = generator.generatePrediction();
            return output + prediction;
        });
    }

    public CompletableFuture<String> findPredictionCompletableFuture() {
        return CompletableFuture.supplyAsync(generator::generatePrediction);
    }

    public CompletableFuture<String> findPredictionCompletableFutureWithError() {
        return CompletableFuture.supplyAsync(generator::generatePredictionWithError);
    }

    public Future<String> executePredictionWithFuture() throws InterruptedException {
        service = Executors.newSingleThreadExecutor();
        return service.submit(generator::generatePrediction);
    }

    public Future<String> executePredictionWithFutureTimeout() throws InterruptedException {
        service = Executors.newSingleThreadExecutor();
        Future<String> futureResult = service.submit(generator::generatePrediction);
        Thread.sleep(5000);
        return futureResult;
    }

    public void shutdown() {
        service.shutdown();
    }
}
