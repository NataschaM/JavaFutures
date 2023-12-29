package de.nata.futures;

/**
 * Predictions' random generator
 */
public class PredictionGenerator {

    private final String[] predictions;

    public PredictionGenerator() {
        Predictions predictions = new Predictions();
        this.predictions = predictions.getPredictions();
    }

    /**
     * Generates a random prediction
     * @return a random prediction
     */
    public String generateRandomPrediction() {
        int randomIndex = getRandomIndex();
        return getPredictionByIndex(randomIndex);
    }

    /**
     * Throws an OutOfBoundException to simulate an error
     * @return OutOfBoundException
     */
    public String generatePredictionWithError() {
        return predictions[350];
    }

    public String generatePredictionWithTimeout() throws InterruptedException {
        Thread.sleep(10000);
        return generateRandomPrediction();
    }

    /**
     * Gets a prediction by array index
     * @param index
     * @return prediction text
     */
    public String getPredictionByIndex(int index) {
        return predictions[index];
    }

    /**
     * Generates a random number inside of predictions array length
     * @return  random number
     */
    public int getRandomIndex() {
        int predictionsNumber = predictions.length;
        return  (int) (Math.random() * predictionsNumber);
    }
}
