package com.echelon.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CryptocurrencyData {
    private double priceChange;
    private double volumeChange;
    private List<String> newsArticles;
    private Map<String, Double> sentimentScores = new HashMap<>();

    // Constructor
    public CryptocurrencyData(double priceChange, double volumeChange, List<String> newsArticles) {
        this.priceChange = priceChange;
        this.volumeChange = volumeChange;
        this.newsArticles = newsArticles;
    }

    // Getters and setters
    public double getPriceChange() {
        return priceChange;
    }

    public void setPriceChange(double priceChange) {
        this.priceChange = priceChange;
    }

    public double getVolumeChange() {
        return volumeChange;
    }

    public void setVolumeChange(double volumeChange) {
        this.volumeChange = volumeChange;
    }

    public List<String> getNewsArticles() {
        return newsArticles;
    }

    public void setNewsArticles(List<String> newsArticles) {
        this.newsArticles = newsArticles;
    }

    public Map<String, Double> getSentimentScores() {
        return sentimentScores;
    }

    public void setSentimentScores(Map<String, Double> sentimentScores) {
        this.sentimentScores = sentimentScores;
    }
}
