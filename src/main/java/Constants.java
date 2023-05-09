public enum Constants {
    POST_SIGN_UP("http://nope.ddns.net/api/signup/"),
    POST_SIGN_IN("http://nope.ddns.net/api/signin/"),

    GET_USER_CONNECTIONS("http://nope.ddns.net/api/userConnections/"),

    POST_CREATE_GAME("http://nope.ddns.net/api/game/"),
    POST_CREATE_TOURNAMENT("http://nope.ddns.net/api/tournament/"),

    GET_ALL_GAME_INFORMATION("http://nope.ddns.net/api/game"),
    GET_GAME_INFORMATION("http://nope.ddns.net/api/game/"),  // + {id}

    GET_ALL_TOURNAMENT_INFORMATION("http://nope.ddns.net/api/tournament"),
    GET_TOURNAMENT_INFORMATION("http://nope.ddns.net/api/tournament/"),  // + {id}

    USERNAME("MKoge99"),
    PASSWORD("safestPasswordInExistence"),
    DOMAIN("http://nope.ddns.net/");

    private final String content;

    Constants(String content){
        this.content = content;
    }

    /**
     * @return Retrieves the content of the specific enum constant
     */
    public String get() {
        return this.content;
    }
}
