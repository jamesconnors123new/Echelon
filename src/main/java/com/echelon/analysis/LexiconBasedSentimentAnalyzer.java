package com.echelon.analysis;

import com.vdurmont.emoji.EmojiParser;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class LexiconBasedSentimentAnalyzer {

    private static final Map<String, Double> lexicon = new HashMap<>();

    static {
        // Load your sentiment lexicon here (e.g., from a file or database)
        // Example entries:
        lexicon.put("good", 0.5);
        lexicon.put("bad", -0.5);
        lexicon.put("great", 0.8);
        // ... add more entries ...
    }

    private static final Map<String, Double> emojiLexicon = new HashMap<>();

    static {
        // Load your emoji sentiment lexicon here (e.g., from a file or database)
        // Example entries:
        emojiLexicon.put("😊", 0.5);
        emojiLexicon.put("😭", -0.8);
        // ... add more entries ...
    }

    public double analyzeSentiment(String text) {
        // Normalize text (lowercase, remove punctuation, etc.)
        String normalizedText = text.toLowerCase().replaceAll("[^a-zA-Z\\s]", "");

        // Tokenize text into words
        String[] words = normalizedText.split("\\s+");

        double sentimentScore = 0.0;
        for (String word : words) {
            // Look up sentiment score for each word in the lexicon
            Double wordSentiment = lexicon.get(word);
            if (wordSentiment != null) {
                sentimentScore += wordSentiment;
            }
        }

        // Account for emojis (using EmojiParser)
        sentimentScore += analyzeEmojiSentiment(text);

        // Normalize sentiment score to be between -1 and 1
        return normalizeSentimentScore(sentimentScore);
    }

    private double analyzeEmojiSentiment(String text) {
        List<String> emojis = EmojiParser.extractEmojis(text);
        double emojiSentiment = 0.0;
        for (String emoji : emojis) {
            Double emojiValue = emojiLexicon.get(emoji);
            if (emojiValue != null) {
                emojiSentiment += emojiValue;
            }
        }
        return emojiSentiment;
    }

    private double normalizeSentimentScore(double score) {
        double maxScore = lexicon.values().stream().mapToDouble(Math::abs).max().orElse(1.0); // Use the maximum absolute value in the lexicon as the normalization factor
        return score / maxScore;
    }
}
