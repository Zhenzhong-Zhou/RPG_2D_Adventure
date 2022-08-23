package audio;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import static utilities.Constants.AudioManager.MENU;
import static utilities.LoadSave.GetClip;

public class AudioManager {
    private float volume;
    private Clip[] musics, effects;
    private int currentMusicId;
    private boolean musicMute, effectMute;
    private int volumeScale = 3;

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

    public void setVolume(float volume) {
        this.volume = volume;
        updateMusicsVolume();
        updateEffectsVolume();
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
        controlBar(gainControl);
    }

    private void updateEffectsVolume() {
        for(Clip clip : effects) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            controlBar(gainControl);
        }
    }

    private void controlBar(FloatControl gainControl) {
        switch(volumeScale) {
            case 0 -> volume = -80f;
            case 1 -> volume = -20f;
            case 2 -> volume = -12f;
            case 3 -> volume = -5f;
            case 4 -> volume = 1f;
            case 5 -> volume = 6f;
        }
        gainControl.setValue(volume);
    }

    public float getVolume() {
        return volume;
    }

    public int getVolumeScale() {
        return volumeScale;
    }

    public void decreaseVolume() {
        this.volumeScale--;
    }

    public void increaseVolume() {
        this.volumeScale++;
    }
}
