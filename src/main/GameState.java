package main;

public enum GameState {
    MENU, PLAY, PAUSE, DIALOGUE, CHARACTER, LOAD, OPTIONS, QUIT;

    // TODO: change to MENU later
    public static GameState gameState = PLAY;
}
