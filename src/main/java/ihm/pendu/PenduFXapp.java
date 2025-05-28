package ihm.pendu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import ihm.pendu.view.WelcomeController;
import ihm.pendu.view.GameController;
import ihm.pendu.view.ThemeConfigController;
import ihm.pendu.view.SettingsController;
import ihm.pendu.util.AnimationUtil;

import java.io.IOException;

public class PenduFXapp extends Application {
    
    private Stage primaryStage;
    private BorderPane rootLayout;
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Jeu du Pendu");
        
        // Initialiser le conteneur racine
        initRootLayout();
        
        // Charger l'écran d'accueil par défaut
        showWelcomeScreen();
    }
    
    /**
     * Initialise le conteneur racine qui contiendra tous les écrans
     */
    private void initRootLayout() {
        try {
            rootLayout = new BorderPane();
            Scene scene = new Scene(rootLayout, 800, 600);
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Affiche l'écran d'accueil
     */
    public void showWelcomeScreen() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view/welcome.fxml"));
            Parent welcomeScreen = loader.load();
            
            // Donne au contrôleur accès à l'application principale
            WelcomeController controller = loader.getController();
            controller.setPenduFXapp(this);
            
            // Utiliser une transition en fondu enchaîné
            AnimationUtil.crossFade(rootLayout, welcomeScreen);
            
            primaryStage.setTitle("Jeu du Pendu - Accueil");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Affiche l'écran de jeu
     */
    public void showGameScreen() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view/game.fxml"));
            Parent gameScreen = loader.load();
            
            // Donne au contrôleur accès à l'application principale
            GameController controller = loader.getController();
            controller.setPenduFXapp(this);
            
            // Utiliser une transition en fondu enchaîné
            AnimationUtil.crossFade(rootLayout, gameScreen);
            
            primaryStage.setTitle("Jeu du Pendu - Partie en cours");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Affiche l'écran de configuration des thèmes
     */
    public void showThemeConfigScreen() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view/config_theme.fxml"));
            Parent themeScreen = loader.load();
            
            // Donne au contrôleur accès à l'application principale
            ThemeConfigController controller = loader.getController();
            controller.setPenduFXapp(this);
            
            // Utiliser une transition en fondu enchaîné
            AnimationUtil.crossFade(rootLayout, themeScreen);
            
            primaryStage.setTitle("Jeu du Pendu - Configuration des thèmes");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Affiche l'écran de configuration des paramètres
     */
    public void showSettingsScreen() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view/config_settings.fxml"));
            Parent settingsScreen = loader.load();
            
            // Donne au contrôleur accès à l'application principale
            SettingsController controller = loader.getController();
            controller.setPenduFXapp(this);
            
            // Utiliser une transition en fondu enchaîné
            AnimationUtil.crossFade(rootLayout, settingsScreen);
            
            primaryStage.setTitle("Jeu du Pendu - Paramètres");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Retourne la scène principale
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    
    public static void main2(String[] args) {
        launch(args);
    }
}