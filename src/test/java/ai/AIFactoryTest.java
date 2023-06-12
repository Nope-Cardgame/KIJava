package ai;

import ai.alexander.AIAlexander;
import ai.julius.AIJulius;
import ai.marian.AIMarian;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AIFactoryTest {
    @Test
    void alexander() {
        IArtificialIntelligence actualAI = AIFactory.getAI("AI Alexander");
        assertTrue(actualAI instanceof AIAlexander);
    }

    @Test
    void julius() {
        IArtificialIntelligence actualAI = AIFactory.getAI("AI Julius");
        assertTrue(actualAI instanceof AIJulius);
    }

    @Test
    void marian() {
        IArtificialIntelligence actualAI = AIFactory.getAI("AI Marian");
        assertTrue(actualAI instanceof AIMarian);
    }
}