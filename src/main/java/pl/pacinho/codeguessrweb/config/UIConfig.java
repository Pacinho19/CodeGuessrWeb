package pl.pacinho.codeguessrweb.config;

public class UIConfig {
    public static final String HOME = "/code-guessr";
    public static final String GAMES = HOME + "/games";
    public static final String NEW_GAME = GAMES + "/new";
    public static final String GAME_PAGE = GAMES + "/{gameId}";
    public static final String GAME_ROOM = GAME_PAGE + "/room";
    public static final String PLAYERS = GAME_ROOM + "/players";
    public static final String GAME_TREE_NODES = HOME + "/nodes";
    public static final String CODE_CONTENT = HOME + "/code";
    public static final String GAME_ROUND_SUMMARY = GAME_PAGE + "/summary";
}