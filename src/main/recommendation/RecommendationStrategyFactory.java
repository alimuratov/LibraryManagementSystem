package main.recommendation;

import main.recommendation.*;

public class RecommendationStrategyFactory {
    public static RecommendationStrategy createStrategy(String strategyType, Data data) {
        if (strategyType == null) {
            throw new IllegalArgumentException("Strategy type cannot be null");
        }
        switch (strategyType.toLowerCase()) {
            case "content":
                return new ContentFiltering(data);
            case "collaborative":
                return new CollaborativeFiltering(data);
            default:
                throw new UnsupportedOperationException("Unknown strategy type");
        }
    }
}