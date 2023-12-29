package de.nata.futures;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Service for creation Future objects with prediction text
 */
public class FuturePredictionService {

    private final PredictionGenerator generator;
    private ExecutorService executor;

    public FuturePredictionService() {
        generator = new PredictionGenerator();
    }

    public void shutdown() {
        executor.shutdown();
    }

    /**
     * Creates a Future object with random prediction
     * @return
     */
    public Future<String> createRandomPrediction() {
        executor = Executors.newSingleThreadExecutor();
        return executor.submit(generator::generateRandomPrediction);
    }

    /**
     * This method will call a generator method with thrown excecption
     * @return
     */
    public Future<String> createPredictionWithError() {
        executor = Executors.newSingleThreadExecutor();
        return executor.submit(generator::generatePredictionWithError);
    }

    /**
     * Creates a prediction, which will be delivered with timeout
     * @return
     * @throws InterruptedException
     */
    public Future<String> createPredictionWithTimeout() throws InterruptedException {
        executor = Executors.newSingleThreadExecutor();
        return executor.submit(generator::generatePredictionWithTimeout);
    }
}
