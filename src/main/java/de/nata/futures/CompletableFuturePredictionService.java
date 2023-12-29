package de.nata.futures;

import java.util.concurrent.CompletableFuture;

/**
 * Service for creation of CompletableFuture objects
 */
public class CompletableFuturePredictionService {

    private final PredictionGenerator generator;

    public CompletableFuturePredictionService() {
        generator = new PredictionGenerator();
    }

    /**
     * Generate of random index
     * @return CompletableFuture with integer value
     */
    public CompletableFuture<Integer> generateIndex() {
        return CompletableFuture.supplyAsync(generator::getRandomIndex);
    }

    /**
     * Generate an integer after some timeout
     * @return
     */
    public CompletableFuture<Integer> generateIndexWithTimeout() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
                return generator.getRandomIndex();
            } catch (InterruptedException e) {
                System.out.println("ERROR!");
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Returns a prediction by index in the array
     * @param number
     * @return
     */
    public CompletableFuture<String> getPredictionToIndex(int number) {
        return CompletableFuture.supplyAsync(() -> generator.getPredictionByIndex(number));
    }

    /**
     * Creates random prediction
     * @return prediction text
     */
    public CompletableFuture<String> createRandomPrediction() {
        return CompletableFuture.supplyAsync(generator::generateRandomPrediction);
    }

    /**
     * Simulates an error by calling creation of random prediction
     * @return
     */
    public CompletableFuture<String> createRandomPredictionWithError() {
        return CompletableFuture.supplyAsync(generator::generatePredictionWithError);
    }
}
