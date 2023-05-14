package game_objects;

import gameobjects.TournamentParticipant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TournamentParticipantTest {
    private TournamentParticipant tournamentParticipant;
    @BeforeEach
    void setUp() {
        this.tournamentParticipant = new TournamentParticipant("Aremju",1,false,20);
    }

    @Test
    void tournamentParticipantCreation() {
        TournamentParticipant otherTournamentParticipant = new TournamentParticipant("{\"username\":\"Aremju\",\"ranking\":1,\"disqualified\":false,\"score\":20}");
        assertEquals(tournamentParticipant.getScore(),otherTournamentParticipant.getScore());
        assertEquals(tournamentParticipant.getUsername(),otherTournamentParticipant.getUsername());
        assertEquals(tournamentParticipant.getRanking(),otherTournamentParticipant.getRanking());
        assertEquals(tournamentParticipant.isDisqualified(),otherTournamentParticipant.isDisqualified());
    }

    @Test
    void tournamentParticipantJSON() {
        String expectedJson = "{\"username\":\"Aremju\",\"ranking\":1,\"disqualified\":false,\"score\":20}";
        assertEquals(expectedJson,tournamentParticipant.toJSON());
    }
}
