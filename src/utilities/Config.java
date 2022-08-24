package utilities;

import main.Scene;

import java.io.*;

import static utilities.Constants.AudioManager.MUTE_OFF;
import static utilities.Constants.AudioManager.MUTE_ON;
import static utilities.LoadSave.dataFile;

public class Config {
    private final Scene scene;

    public Config(Scene scene) {
        this.scene = scene;
    }

    public void saveConfig() {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(dataFile));
            // Mute Music
            if(scene.isMusicMute()) {
                bufferedWriter.write(MUTE_ON);
            }
            if(! scene.isMusicMute()) {
                bufferedWriter.write(MUTE_OFF);
            }
            bufferedWriter.newLine();

            // Mute SE
            if(scene.isSeMute()) {
                bufferedWriter.write(MUTE_ON);
            }
            if(! scene.isSeMute()) {
                bufferedWriter.write(MUTE_OFF);
            }
            bufferedWriter.newLine();

            // Music Volume
            bufferedWriter.write(String.valueOf(scene.getAudioManager().getVolumeBGMScale()));
            bufferedWriter.newLine();

            // SE Volume
            bufferedWriter.write(String.valueOf(scene.getAudioManager().getVolumeSEScale()));
            bufferedWriter.newLine();

            bufferedWriter.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void loadConfig() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(dataFile));
            String s = bufferedReader.readLine();

            // Full Screen
//            if(s.equals("On")) {
//                scene.setFullScreen(true);
//            }
//            if(s.equals("Off")) {
//                scene.setFullScreen(false);
//            }

            // Mute Music
            if(s.equals(MUTE_ON)) {
                scene.setMusicMute(true);
                scene.getAudioManager().toggleMusicMute();
            }
            if(s.equals(MUTE_OFF)) {
                scene.setMusicMute(false);
                scene.getAudioManager().toggleMusicMute();
            }

            // Mute SE
            if(s.equals(MUTE_ON)) {
                scene.setSeMute(true);
                scene.getAudioManager().toggleEffectMute();
            }
            if(s.equals(MUTE_OFF)) {
                scene.setSeMute(false);
                scene.getAudioManager().toggleEffectMute();
            }

            // Music Volume
            s = bufferedReader.readLine();
            scene.getAudioManager().volumeBGMScale = Integer.parseInt(s);

            // SE Volume
            s = bufferedReader.readLine();
            scene.getAudioManager().volumeSEScale = Integer.parseInt(s);

            // TODO: need to one more line to display volume bar, and load options one-second delay
            // TODO: toggle cannot be fixed when load game
            s = bufferedReader.readLine();
            scene.getAudioManager().volumeBGMScale = Integer.parseInt(s);

            bufferedReader.close();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
