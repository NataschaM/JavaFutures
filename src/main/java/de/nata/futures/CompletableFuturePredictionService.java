package de.nata.futures;

import java.util.concurrent.CompletableFuture;

public class CompletableFuturePredictionService {

    private final PredictionGenerator generator;

    public CompletableFuturePredictionService() {
        generator = new PredictionGenerator();
    }

    public CompletableFuture<Integer> generateIndex() {
        return CompletableFuture.supplyAsync(generator::getRandomIndex);
    }

    public CompletableFuture<Integer> generateIndexWithTimeout() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("In generate index:" + Thread.currentThread().getName());
                Thread.sleep(2000);
                return generator.getRandomIndex();
            } catch (InterruptedException e) {
                System.out.println("ERROR!");
                throw new RuntimeException(e);
            }
        });
    }

    public CompletableFuture<String> getPredictionToIndex(int number) {
        return CompletableFuture.supplyAsync(() -> generator.getPredictionByIndex(number));
    }

    public CompletableFuture<String> compose(String param) {

        return CompletableFuture.supplyAsync(() -> {
            String output = param + " + ";
            String prediction = generator.generateRandomPrediction();
            return output + prediction;
        });
    }

    public CompletableFuture<String> createRandomPrediction() {
        return CompletableFuture.supplyAsync(generator::generateRandomPrediction);
    }

    public CompletableFuture<String> createRandomPredictionWithError() {
        return CompletableFuture.supplyAsync(generator::generatePredictionWithError);
    }
}
