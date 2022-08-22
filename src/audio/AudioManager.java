package audio;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import static utilities.Constants.AudioManager.MENU;
import static utilities.LoadSave.GetClip;

public class AudioManager {
    private final float volume = 0.5f;
    private Clip[] musics, effects;
    private int currentMusicId;
    private boolean musicMute, effectMute;

    public AudioManager() {
        loadMusics();
        loadEffects();
        playMusic(MENU);
    }

    public void loadMusics() {
        String[] musicNames = {"menu", "BlueBoyAdventure", "level1", "level2"};
        musics = new Clip[musicNames.length];
        for(int i = 0; i < musics.length; i++) {
            musics[i] = GetClip(musicNames[i]);
        }
    }

    public void loadEffects() {
        String[] effectNames = {"coin", "powerup", "unlock", "fanfare", "hitmonster", "receivedamage", "swingweapon", "levelup", "cursor"};
        effects = new Clip[effectNames.length];
        for(int i = 0; i < effects.length; i++) {
            effects[i] = GetClip(effectNames[i]);
        }
    }

    public void stopSound() {
        if(musics[currentMusicId].isActive()) {
            musics[currentMusicId].stop();
        }
    }

    public void playMusic(int music) {
        stopSound();

        currentMusicId = music;
        updateMusicsVolume();
        musics[currentMusicId].setMicrosecondPosition(0);
        musics[currentMusicId].loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void playEffect(int effect) {
        effects[effect].setMicrosecondPosition(0);
        effects[effect].start();
    }

    private void updateMusicsVolume() {
        FloatControl gainControl = (FloatControl) musics[currentMusicId].getControl(FloatControl.Type.MASTER_GAIN);
        float range = gainControl.getMaximum() - gainControl.getMinimum();
        float gain = (range * volume) + gainControl.getMinimum();
        gainControl.setValue(gain);
    }
}
