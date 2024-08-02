package com.echelon.data;

import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class AlphaVantageHarvester implements DataHarvester {

    private static final String API_KEY = "YOUR_API_KEY"; // Replace with your actual API key
    private static final String BASE_URL = "https://www.alphavantage.co/query";

    @Override
    public CryptocurrencyData fetchData(String symbol) {
        Map<String, Double> technicalIndicators = fetchTechnicalIndicators(symbol); // Assuming you are fetching technical indicators from AlphaVantage

        double priceChange = technicalIndicators.getOrDefault("PriceChange", 0.0); // Replace with actual price change calculation
        double volumeChange = technicalIndicators.getOrDefault("VolumeChange", 0.0); // Replace with actual volume change calculation
        List<String> newsArticles = fetchNewsArticles(symbol); // You'll need to implement this method or use another source for news

        return new CryptocurrencyData(priceChange, volumeChange, newsArticles);
    }

    @Override
    public List<String> fetchNewsArticles(String symbol) {
        NewsApiClient newsApiClient = new NewsApiClient("YOUR_NEWSAPI_KEY"); // Replace with your News API key

        EverythingRequest request = new EverythingRequest.Builder()
                .q(symbol) // Search for news articles about the cryptocurrency
                .language("en") // Filter for English articles
                .sortBy("relevancy") // Sort by relevance
                .pageSize(10) // Fetch up to 10 articles (adjust as needed)
                .build();

        newsapi.ApiResponse<Article> response = newsApiClient.getEverything(request);

        if (response.getStatus() == "ok") {
            List<String> newsArticles = new ArrayList<>();
            for (Article article : response.getArticles()) {
                newsArticles.add(article.getTitle() + " " + article.getDescription()); // Combine title and description for analysis
            }
            return newsArticles;
        } else {
            System.err.println("Error fetching news articles: " + response.getStatus());
            return Collections.emptyList(); // Return empty list on error
        }
    }`

    @Override
    public Map<String, Double> fetchTechnicalIndicators(String symbol) {
        Map<String, Double> indicators = new HashMap<>();

        try {
            // Fetch daily time series data
            String function = "TIME_SERIES_DAILY"; // Assuming you want daily data
            String url = BASE_URL + "?function=" + function + "&symbol=" + symbol + "&apikey=" + API_KEY;

            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JSONObject json = new JSONObject(response.body());
                JSONObject timeSeries = json.getJSONObject("Time Series (Daily)");

                // Calculate price change (replace with your actual logic)
                double currentClose = timeSeries.getJSONObject(timeSeries.names().getString(0)).getDouble("4. close");
                double previousClose = timeSeries.getJSONObject(timeSeries.names().getString(1)).getDouble("4. close");
                indicators.put("PriceChange", (currentClose - previousClose) / previousClose);

                // Calculate volume change (replace with your actual logic)
                double currentVolume = timeSeries.getJSONObject(timeSeries.names().getString(0)).getDouble("5. volume");
                double previousVolume = timeSeries.getJSONObject(timeSeries.names().getString(1)).getDouble("5. volume");
                indicators.put("VolumeChange", (currentVolume - previousVolume) / previousVolume);

                // ... (Fetch other technical indicators as needed) ...
            } else {
                // Handle API error
                System.err.println("Error fetching data from AlphaVantage: " + response.statusCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return indicators;
    }
}
