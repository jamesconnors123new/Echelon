package com.echelon.strategy;

import com.echelon.analysis.EchelonCrunch;
import com.echelon.data.AlphaVantageHarvester; // Import for fetching data
import com.echelon.data.CryptocurrencyData;
import com.echelon.data.DataHarvester;
import com.echelon.data.InvestmentPersonality;
import com.echelon.data.Portfolio;
import com.echelon.notifications.NotificationService;

import java.util.ArrayList;
import java.util.List;

public class EchelonAgent {

    private EchelonCrunch echelonCrunch;
    private InvestmentPersonality userProfile;
    private Portfolio portfolio;
    private NotificationService notificationService;
    private DataHarvester dataHarvester; // To fetch cryptocurrency data

    public EchelonAgent(InvestmentPersonality userProfile, Portfolio portfolio, NotificationService notificationService) {
        this.echelonCrunch = new EchelonCrunch();
        this.userProfile = userProfile;
        this.portfolio = portfolio;
        this.notificationService = notificationService;
        this.dataHarvester = new AlphaVantageHarvester(); // Initialize with AlphaVantageHarvester (you can change this later)
    }

    public void generateInvestmentRecommendations() {
        for (String symbol : portfolio.getCryptocurrencies()) {
            CryptocurrencyData data = fetchData(symbol);
            echelonCrunch.analyzeCryptocurrency(data);

            String recommendation = generateRecommendation(data, userProfile);

            // Process the recommendation
            switch (recommendation) {
                case "Strong Buy":
                    double buyAmount = calculateBuyAmount(data, userProfile);
                    portfolio.buy(symbol, buyAmount);
                    break;
                case "Buy":
                    buyAmount = calculateBuyAmount(data, userProfile) / 2; // Adjust for weaker signal
                    portfolio.buy(symbol, buyAmount);
                    break;
                case "Hold":
                    notificationService.sendNotification(userProfile, "Hold recommendation for " + symbol);
                    break;
                case "Sell":
                    double sellAmount = calculateSellAmount(data, userProfile) / 2; // Adjust for weaker signal
                    portfolio.sell(symbol, sellAmount);
                    break;
                case "Strong Sell":
                    sellAmount = calculateSellAmount(data, userProfile);
                    portfolio.sell(symbol, sellAmount);
                    notificationService.sendUrgentNotification(userProfile, "Urgent sell recommendation for " + symbol + " due to rapid devaluation!");
                    break;
            }

            // Check for stop-loss or take-profit triggers
            if (portfolio.shouldSell(symbol, data)) {
                portfolio.sell(symbol, portfolio.getHoldingAmount(symbol));
                notificationService.sendNotification(userProfile, "Stop-loss or take-profit triggered for " + symbol);
            }

            // Rebalance portfolio if necessary
            portfolio.rebalance(userProfile);
        }

        // Check for new investment opportunities
        List<String> newOpportunities = getNewInvestmentOpportunities();
        if (!newOpportunities.isEmpty()) {
            notificationService.sendNotification(userProfile, "New investment opportunities identified: " + newOpportunities);
        }
    }

    private CryptocurrencyData fetchData(String symbol) {
        return dataHarvester.fetchData(symbol);
    }

    // TODO: Implement logic to generate recommendations based on analysis and user profile
    // Target Completion: August 2, 2024 at 7:00 AM BST
    private String generateRecommendation(CryptocurrencyData data, InvestmentPersonality userProfile) {
        // Placeholder implementation for now (replace with your actual logic)
        return "Hold";
    }

    // TODO: Implement logic to calculate buy amount based on analysis and user profile
    // Target Completion: August 2, 2024 at 7:30 AM BST
    private double calculateBuyAmount(CryptocurrencyData data, InvestmentPersonality userProfile) {
        // Placeholder implementation for now (replace with your actual logic)
        return 1.0;
    }

    // TODO: Implement logic to calculate sell amount based on analysis and user profile
    // Target Completion: August 2, 2024 at 7:30 AM BST
    private double calculateSellAmount(CryptocurrencyData data, InvestmentPersonality userProfile) {

        return 1.0;
    }

    // TODO: Implement logic to identify new investment opportunities
    // Target Completion: August 2, 2024 at 8:00 AM BST
    private List<String> getNewInvestmentOpportunities() {

        return new ArrayList<>();
    }
}

