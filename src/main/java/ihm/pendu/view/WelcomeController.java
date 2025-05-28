package ihm.pendu.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import ihm.pendu.PenduFXapp;
import ihm.pendu.util.SoundManager;

public class WelcomeController {
    
    private PenduFXapp penduFXapp;
    
    @FXML
    private Button startButton;
    
    @FXML
    private Button configButton;
    
    @FXML
    private Button statsButton;
    
    /**
    * Initialise le contrôleur. Cette méthode est appelée automatiquement
    * après que le fichier FXML a été chargé.
    */
    @FXML
    private void initialize() {
        // Configurer les gestionnaires d'événements pour les boutons
        setupButtons();
    }
    
    /**
    * Configure les gestionnaires d'événements pour les boutons
    */
    private void setupButtons() {
        startButton.setOnAction(event -> {
            SoundManager.getInstance().playSound(SoundManager.KEYBOARD_CLICK);
            handleStartButton();
        });
        
        configButton.setOnAction(event -> {
            SoundManager.getInstance().playSound(SoundManager.KEYBOARD_CLICK);
            handleConfigButton();
        });
        
        statsButton.setOnAction(event -> {
            SoundManager.getInstance().playSound(SoundManager.KEYBOARD_CLICK);
            handleStatsButton();
        });
    }
    
    /**
    * Est appelé par l'application principale pour donner une référence à elle-même.
    * 
    * @param penduFXapp
    */
    public void setPenduFXapp(PenduFXapp penduFXapp) {
        this.penduFXapp = penduFXapp;
    }
    
    /**
    * Gère le clic sur le bouton Démarrer
    */
    @FXML
    private void handleStartButton() {
        // Démarrer directement le jeu avec les paramètres actuels
        penduFXapp.showGameScreen();
    }
    
    /**
    * Gère le clic sur le bouton Configurer
    */
    @FXML
    private void handleConfigButton() {
        penduFXapp.showThemeConfigScreen();
    }
    
    /**
    * Gère le clic sur le bouton Statistiques
    */
    @FXML
    private void handleStatsButton() {
        // Si vous n'avez pas d'écran de statistiques, vous pouvez rediriger ailleurs
        // ou implémenter une fonctionnalité temporaire
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Statistiques");
        alert.setHeaderText("Fonctionnalité à venir");
        alert.setContentText("Les statistiques seront disponibles dans une future mise à jour.");
        alert.showAndWait();
    }
}