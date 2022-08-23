package utilities;

import main.Scene;

import java.io.*;

public class Config {
    private final Scene scene;

    public Config(Scene scene) {
        this.scene = scene;
    }

    public void saveConfig() {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("config.txt"));

            // Full Screen
//            if(scene.isFullScreen()) {
//                bufferedWriter.write("On");
//            }
//            if(!scene.isFullScreen()) {
//                bufferedWriter.write("Off");
//            }
//            bufferedWriter.newLine();

            // Mute Music
            if(scene.isMusic()) {
                bufferedWriter.write("-1");
            }
            if(!scene.isMusic()) {
                bufferedWriter.write("0");
            }
            bufferedWriter.newLine();

            // Mute SE
            if(scene.isSe()) {
                bufferedWriter.write("-1");
            }
            if(!scene.isSe()) {
                bufferedWriter.write("0");
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
            BufferedReader bufferedReader = new BufferedReader(new FileReader("config.txt"));
            String s = bufferedReader.readLine();

            // Full Screen
//            if(s.equals("On")) {
//                scene.setFullScreen(true);
//            }
//            if(s.equals("Off")) {
//                scene.setFullScreen(false);
//            }

            // Mute Music
            if(s.equals("-1")) {
                scene.setMusic(true);
                scene.getAudioManager().toggleMusicMute();
            }
            if(s.equals("0")) {
                scene.setMusic(false);
                scene.getAudioManager().toggleMusicMute();
            }

            // Mute SE
            if(s.equals("-1")) {
                scene.setSe(true);
                scene.getAudioManager().toggleEffectMute();
            }
            if(s.equals("0")) {
                scene.setSe(false);
                scene.getAudioManager().toggleEffectMute();
            }

            // Music Volume
            s = bufferedReader.readLine();
            scene.getAudioManager().volumeBGMScale = Integer.parseInt(s);

            // SE Volume
            s = bufferedReader.readLine();
            scene.getAudioManager().volumeSEScale = Integer.parseInt(s);

            bufferedReader.close();
        }  catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
