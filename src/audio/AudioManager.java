package audio;

import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import static utilities.Constants.AudioManager.CURSOR;
import static utilities.Constants.AudioManager.MENU;
import static utilities.LoadSave.GetClip;

public class AudioManager {
    private float volume;
    private Clip[] musics, effects;
    private int currentMusicId;
    private boolean musicMute, effectMute;
    private int volumeBGMScale = 3;
    private int volumeSEScale = 3;

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
        switch(volumeBGMScale) {
            case 0 -> volume = - 80f;
            case 1 -> volume = - 20f;
            case 2 -> volume = - 12f;
            case 3 -> volume = - 5f;
            case 4 -> volume = 1f;
            case 5 -> volume = 6f;
        }
        gainControl.setValue(volume);
    }

    private void updateEffectsVolume() {
        for(Clip clip : effects) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            switch(volumeSEScale) {
                case 0 -> volume = - 80f;
                case 1 -> volume = - 20f;
                case 2 -> volume = - 12f;
                case 3 -> volume = - 5f;
                case 4 -> volume = 1f;
                case 5 -> volume = 6f;
            }
            gainControl.setValue(volume);
        }
    }

    public void toggleMusicMute() {
        this.musicMute = ! musicMute;
        for(Clip clip : musics) {
            BooleanControl booleanControl = (BooleanControl) clip.getControl(BooleanControl.Type.MUTE);
            booleanControl.setValue(musicMute);
        }
    }

    public void toggleEffectMute() {
        this.effectMute = ! effectMute;
        for(Clip clip : effects) {
            BooleanControl booleanControl = (BooleanControl) clip.getControl(BooleanControl.Type.MUTE);
            booleanControl.setValue(effectMute);
        }
        if(! effectMute) {
            playEffect(CURSOR);
        }
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
        updateMusicsVolume();
        updateEffectsVolume();
    }

    public int getVolumeBGMScale() {
        return volumeBGMScale;
    }

    public int getVolumeSEScale() {
        return volumeSEScale;
    }

    public void decreaseBGMVolume() {
        this.volumeBGMScale--;
    }

    public void decreaseSEVolume() {
        this.volumeSEScale--;
    }

    public void increaseBGMVolume() {
        this.volumeBGMScale++;
    }

    public void increaseSEVolume() {
        this.volumeSEScale++;
    }
}
