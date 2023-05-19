package ai;

import ai.alexander.AIAlexander;
import ai.julius.AIJulius;
import ai.marian.AIMarian;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AIFactoryTest {
    @Test
    void alexander() {
        IArtificialIntelligence actualAI = AIFactory.getAI("AlexanderLauruhn");
        assertTrue(actualAI instanceof AIAlexander);
    }

    @Test
    void julius() {
        IArtificialIntelligence actualAI = AIFactory.getAI("Aremju");
        assertTrue(actualAI instanceof AIJulius);
    }

    @Test
    void marian() {
        IArtificialIntelligence actualAI = AIFactory.getAI("MarianK99");
        assertTrue(actualAI instanceof AIMarian);
    }
}