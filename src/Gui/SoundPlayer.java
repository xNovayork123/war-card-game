package Gui;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class SoundPlayer {
    private Clip clip;

    public void loadAndLoop(String resourcePath) {
        try (AudioInputStream audioInput = AudioSystem.getAudioInputStream(
                getClass().getResource(resourcePath))) {
            clip = AudioSystem.getClip();
            clip.open(audioInput);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
