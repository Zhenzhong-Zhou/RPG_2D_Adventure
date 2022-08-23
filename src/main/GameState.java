package main;

public enum GameState {
    MENU, PLAY, PAUSE, DIALOGUE, CHARACTER, OPTIONS, LOAD, SETTINGS, QUIT;

    // TODO: change to MENU later
    public static GameState gameState = PLAY;
}
