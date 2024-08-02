package com.echelon.analysis;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.tokenizers.NGramTokenizer;

import java.io.File;
import java.util.ArrayList;

public class MachineLearningSentimentAnalyzer {
    private Classifier model;
    private Instances dataset;

    public MachineLearningSentimentAnalyzer() throws Exception {
        // Load pre-trained model (replace with your actual model path)
        model = (Classifier) SerializationHelper.read("naive_bayes_model.model");

        // Create dataset structure for prediction
        dataset = createDataset();
    }

    public double analyzeSentiment(String text) {
        try {
            // Preprocess text
            String processedText = preprocessText(text);

            // Create instance for prediction
            DenseInstance instance = new DenseInstance(1.0, new double[]{dataset.attribute(0).addStringValue(processedText)});
            instance.setDataset(dataset);

            // Make prediction
            double[] prediction = model.distributionForInstance(instance);
            double positiveProbability = prediction[0];

            // Convert probability to sentiment score between -1 and 1
            return 2 * positiveProbability - 1;
        } catch (Exception e) {
            // Handle exceptions (e.g., model loading/prediction errors)
            e.printStackTrace();
            return 0.0; // Return neutral sentiment on error
        }
    }

    private String preprocessText(String text) {
        // Basic preprocessing (you might want to expand this)
        text = text.toLowerCase();
        text = text.replaceAll("[^a-zA-Z\\s]", "");
        return text;
    }

    private Instances createDataset() {
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("text", (NGramTokenizer) null)); // NGramTokenizer for handling n-grams
        attributes.add(new Attribute("class", List.of("positive", "negative")));

        Instances dataset = new Instances("SentimentDataset", attributes, 0);
        dataset.setClassIndex(1);

        return dataset;
    }
}
