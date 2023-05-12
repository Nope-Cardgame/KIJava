package ai;

import ai.alexander.AIAlexander;
import ai.julius.AIJulius;
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
        IArtificialIntelligence correctAI;
        if(username.equals("AlexanderLauruhn")) {
            correctAI = new AIAlexander();
        } else if (username.equals("Aremju")) {
            correctAI = new AIJulius();
        } else {
            correctAI = new AIMarian();
        }
        return correctAI;
    }
}
