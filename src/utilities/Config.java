package utilities;

import main.Scene;

import java.io.*;

import static utilities.Constants.AudioManager.CURSOR;

public class Config {
    private Scene scene;

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
                bufferedWriter.write("MUTE");
            }
            if(!scene.isMusic()) {
                bufferedWriter.write("UNMUTE");
            }
            bufferedWriter.newLine();

            // Mute SE
            if(scene.isSe()) {
                bufferedWriter.write("MUTE");
            }
            if(!scene.isSe()) {
                bufferedWriter.write("UNMUTE");
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
            if(s.equals("MUTE")) {
                scene.setMusic(true);
                scene.getAudioManager().toggleMusicMute();
            }
            if(s.equals("UNMUTE")) {
                scene.setMusic(false);
                scene.getAudioManager().toggleMusicMute();
            }

            // Mute SE
            if(s.equals("MUTE")) {
                scene.setSe(true);
                scene.getAudioManager().toggleEffectMute();
            }
            if(s.equals("UNMUTE")) {
                scene.setSe(false);
                scene.getAudioManager().toggleEffectMute();
            }

            // Music Volume
            s = bufferedReader.readLine();
//            int x = scene.getAudioManager().loadMusicScale(Integer.parseInt(s));
//            scene.getAudioManager().setVolume(x);

//            if(scene.getGui().getCommandNum() == 1 && scene.getAudioManager().getVolumeBGMScale() < 5 && !scene.isMusic()) {
//                scene.getAudioManager().increaseBGMVolume();
//                scene.getAudioManager().setVolume(x);
//                scene.getAudioManager().playEffect(CURSOR);
//            }
//            int x = scene.getAudioManager().getVolumeBGMScale();
//            scene.getAudioManager().increaseBGMVolume();
//            scene.getAudioManager().setVolume(scene.getAudioManager().setVolumeBGMScale(Integer.parseInt(s)));


            // SE Volume
            s = bufferedReader.readLine();
//            scene.getAudioManager().setVolume(Integer.parseInt(s));
//            scene.getAudioManager().setVolumeSEScale(Integer.parseInt(s));

            bufferedReader.close();
        }  catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
