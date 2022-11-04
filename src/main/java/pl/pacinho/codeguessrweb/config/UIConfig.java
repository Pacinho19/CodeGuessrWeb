package pl.pacinho.codeguessrweb.config;

public class UIConfig {
    public static final String HOME = "/code-guessr";
    public static final String GAMES = HOME + "/games";
    public static final String NEW_GAME = GAMES + "/new";
    public static final String GAME_ROOM = GAMES + "/{gameId}/room";
    public static final String GAME_PAGE = GAMES + "/{gameId}";
    public static final String PLAYERS = GAME_ROOM + "/players";
    public static final String GAME_TREE_NODES = HOME + "/nodes";
}