package com.echelon.data;

import java.util.List;
import java.util.Map;

public interface DataHarvester {
    CryptocurrencyData fetchData(String symbol); // Fetches data for a specific cryptocurrency symbol
    List<String> fetchNewsArticles(String symbol); // Fetches news articles related to a cryptocurrency
    Map<String, Double> fetchTechnicalIndicators(String symbol); // Fetches technical indicators for a cryptocurrency (e.g., RSI, MACD)
}
