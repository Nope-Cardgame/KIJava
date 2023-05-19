package ai;

import ai.alexander.AIAlexander;
import ai.julius.AIJulius;
import ai.julius.valid.JAIValidOnly;
import ai.marian.AIMarian;

public class AIFactory {
    /**
     * Creates the correct AI Instance for the specific
     * user that starts this client
     *
     * @param username the username of the Client
     * @return the correct AI for that user
     */
    public static IArtificialIntelligence getAI(String username) {
        IArtificialIntelligence correctAI = switch (username) {
            case "AlexanderLauruhn" -> new AIAlexander();
            case "Aremju" -> new AIJulius(new JAIValidOnly());
            case "MarianK99" -> new AIMarian();
            default -> throw new RuntimeException("No correct username");
        };
        return correctAI;
    }
}
