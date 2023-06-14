package ai;

import ai.alexander.AIAlexander;
import ai.julius.AIJulius;
import ai.julius.smart.JAISmart;
import ai.marian.AIMarian;

public class AIFactory {
    /**
     * Creates the correct AI Instance for the specific
     * user that starts this client
     *
     * @param aiName the name of the ai
     * @return the correct AI for that user
     */
    public static IArtificialIntelligence getAI(String aiName) {
        return switch (aiName) {
            case "AI Julius" -> new AIJulius(new JAISmart());
            case "AI Marian" -> new AIMarian();
            default -> new AIAlexander();
        };
    }
}
