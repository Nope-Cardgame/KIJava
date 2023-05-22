package game_objects;

import gameobjects.Game;
import org.junit.jupiter.api.Test;

public class GameTest {
    @Test
    void gameCreation() {
        Game game = new Game("{\"discardPile\":[{\"name\":\"blue and yellow two\",\"type\":\"number\",\"value\":2,\"colors\":[\"blue\",\"yellow\"]}],\"lastAction\":{\"cardAmount\":8,\"type\":\"take\",\"explanation\":\"SERVER is drawing the start cards\",\"player\":{\"socketId\":\"q0rN95D4nv12tLkgAABr\",\"cardAmount\":8,\"disqualified\":false,\"username\":\"kotlin\"}},\"noWildCards\":false,\"currentPlayer\":{\"socketId\":\"TNgsHSPBb_uq9lb9AABp\",\"cardAmount\":8,\"cards\":[{\"name\":\"blue one\",\"type\":\"number\",\"value\":1,\"colors\":[\"blue\"]},{\"name\":\"red and blue two\",\"type\":\"number\",\"value\":2,\"colors\":[\"red\",\"blue\"]},{\"name\":\"red and blue one\",\"type\":\"number\",\"value\":1,\"colors\":[\"red\",\"blue\"]},{\"name\":\"red and green one\",\"type\":\"number\",\"value\":1,\"colors\":[\"red\",\"green\"]},{\"name\":\"green and yellow three\",\"type\":\"number\",\"value\":3,\"colors\":[\"green\",\"yellow\"]},{\"name\":\"yellow two\",\"type\":\"number\",\"value\":2,\"colors\":[\"yellow\"]},{\"name\":\"wildcard\",\"type\":\"number\",\"value\":1,\"colors\":[\"red\",\"green\",\"blue\",\"yellow\"]},{\"name\":\"red and green three\",\"type\":\"number\",\"value\":3,\"colors\":[\"red\",\"green\"]}],\"disqualified\":false,\"username\":\"Aremju\"},\"players\":[{\"socketId\":\"TNgsHSPBb_uq9lb9AABp\",\"cardAmount\":8,\"cards\":[{\"name\":\"blue one\",\"type\":\"number\",\"value\":1,\"colors\":[\"blue\"]},{\"name\":\"red and blue two\",\"type\":\"number\",\"value\":2,\"colors\":[\"red\",\"blue\"]},{\"name\":\"red and blue one\",\"type\":\"number\",\"value\":1,\"colors\":[\"red\",\"blue\"]},{\"name\":\"red and green one\",\"type\":\"number\",\"value\":1,\"colors\":[\"red\",\"green\"]},{\"name\":\"green and yellow three\",\"type\":\"number\",\"value\":3,\"colors\":[\"green\",\"yellow\"]},{\"name\":\"yellow two\",\"type\":\"number\",\"value\":2,\"colors\":[\"yellow\"]},{\"name\":\"wildcard\",\"type\":\"number\",\"value\":1,\"colors\":[\"red\",\"green\",\"blue\",\"yellow\"]},{\"name\":\"red and green three\",\"type\":\"number\",\"value\":3,\"colors\":[\"red\",\"green\"]}],\"disqualified\":false,\"accepted\":true,\"username\":\"Aremju\"},{\"socketId\":\"q0rN95D4nv12tLkgAABr\",\"cardAmount\":8,\"disqualified\":false,\"accepted\":true,\"username\":\"kotlin\"}],\"id\":\"aec7892d-b92a-4fa1-8dab-5e7be355176b\",\"state\":\"turn_start\",\"noActionCards\":true,\"oneMoreStartCard\":false}");
        System.out.println(game.toJSON());
    }

    @Test
    void gameJson() {

    }
}
