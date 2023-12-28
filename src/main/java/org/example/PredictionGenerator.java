package org.example;

public class PredictionGenerator {

    private final String[] predictions;

    public PredictionGenerator() {
        Predictions predictions = new Predictions();
        this.predictions = predictions.getPredictions();
    }

    public String generatePrediction() {
        return generate();
    }

    public String generatePredictionWithError() {
        return generateWithError();
    }

    public String generate() {
        int predictionsNumber = predictions.length;
        int random = (int) (Math.random() * predictionsNumber);
        String prediction = predictions[random];
        System.out.println(random);
        System.out.println(prediction);
        return prediction;
    }

    public String getPredictionToNumber(int number) {
        return predictions[number];
    }

    public int getRandomNumber() {
        int predictionsNumber = predictions.length;
        int index = (int) (Math.random() * predictionsNumber);
        //System.out.println(index);

        return  index;
    }

    public String generateWithError() {
        return predictions[350];
    }
}
