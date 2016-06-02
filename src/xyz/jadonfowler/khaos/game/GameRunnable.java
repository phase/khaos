package xyz.jadonfowler.khaos.game;


public interface GameRunnable {

    public void onStart(Arena arena, Map map);
    
    public void onStop(Arena arena, Map map, Team winner);
    
}
