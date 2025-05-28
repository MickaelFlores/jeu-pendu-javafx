package ihm.pendu.util;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    
    private static SoundManager instance;
    private Map<String, Media> soundCache = new HashMap<>();
    private boolean soundEnabled = true;
    
    // Constantes pour les noms des sons
    public static final String KEYBOARD_CLICK = "keyboard_click.mp3";
    public static final String VICTORY = "victory.mp3";
    public static final String FAIL = "fail.mp3";
    
    private SoundManager() {
        // Précharger les sons
        preloadSound(KEYBOARD_CLICK);
        preloadSound(VICTORY);
        preloadSound(FAIL);
    }
    
    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }
    
    private void preloadSound(String soundFileName) {
        try {
            URL soundURL = getClass().getResource("/ihm/pendu/sounds/" + soundFileName);
            if (soundURL != null) {
                Media sound = new Media(soundURL.toString());
                soundCache.put(soundFileName, sound);
            } else {
                System.err.println("Son non trouvé: " + soundFileName);
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement du son: " + soundFileName);
            e.printStackTrace();
        }
    }
    
    public void playSound(String soundFileName) {
        if (!soundEnabled) return;
        
        Media sound = soundCache.get(soundFileName);
        if (sound != null) {
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();
            
            // Libérer les ressources après la lecture
            mediaPlayer.setOnEndOfMedia(() -> {
                mediaPlayer.dispose();
            });
        } else {
            System.err.println("Son non chargé: " + soundFileName);
        }
    }
    
    public void setSoundEnabled(boolean enabled) {
        this.soundEnabled = enabled;
    }
    
    public boolean isSoundEnabled() {
        return soundEnabled;
    }
}