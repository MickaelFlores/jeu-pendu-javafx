package ihm.pendu.util;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.Parent;
import javafx.util.Duration;

public class AnimationUtil {
    
    /**
     * Vérifie si les animations sont activées dans les paramètres
     */
    public static boolean isAnimationEnabled() {
        return ihm.pendu.view.SettingsController.isAnimationActive();
    }
    
    /**
     * Joue une animation si les animations sont activées
     * @param animation L'animation à jouer
     */
    public static void playIfEnabled(Animation animation) {
        if (isAnimationEnabled()) {
            animation.play();
        }
    }
    
    /**
     * Crée et joue une animation de rebond pour un élément
     * @param node L'élément à animer
     * @param duration Durée de l'animation en millisecondes
     */
    public static void bounce(Node node, double duration) {
        if (!isAnimationEnabled()) return;
        
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(duration), node);
        scaleTransition.setFromX(1.0);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToX(1.2);
        scaleTransition.setToY(1.2);
        scaleTransition.setCycleCount(2);
        scaleTransition.setAutoReverse(true);
        scaleTransition.play();
    }
    
    /**
     * Crée et joue une animation de fondu pour un élément
     * @param node L'élément à animer
     * @param fromValue Valeur initiale d'opacité (0.0 à 1.0)
     * @param toValue Valeur finale d'opacité (0.0 à 1.0)
     * @param duration Durée de l'animation en millisecondes
     */
    public static void fade(Node node, double fromValue, double toValue, double duration) {
        if (!isAnimationEnabled()) {
            // Appliquer directement la valeur finale sans animation
            node.setOpacity(toValue);
            return;
        }
        
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(duration), node);
        fadeTransition.setFromValue(fromValue);
        fadeTransition.setToValue(toValue);
        fadeTransition.play();
    }
    
    /**
     * Crée et joue une animation de secousse pour un élément
     * @param node L'élément à animer
     */
    public static void shake(Node node) {
        if (!isAnimationEnabled()) return;
        
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(50), node);
        translateTransition.setFromX(0);
        translateTransition.setByX(10);
        translateTransition.setCycleCount(6);
        translateTransition.setAutoReverse(true);
        translateTransition.play();
    }
    
    /**
     * Anime un bouton lorsqu'il est cliqué
     * @param button Le bouton à animer
     */
    public static void setupButtonAnimation(Button button) {
        if (!isAnimationEnabled()) return;
        
        button.setOnMousePressed(e -> {
            ScaleTransition scaleDown = new ScaleTransition(Duration.millis(100), button);
            scaleDown.setToX(0.95);
            scaleDown.setToY(0.95);
            scaleDown.play();
        });
        
        button.setOnMouseReleased(e -> {
            ScaleTransition scaleUp = new ScaleTransition(Duration.millis(100), button);
            scaleUp.setToX(1.0);
            scaleUp.setToY(1.0);
            scaleUp.play();
        });
    }
    
    /**
     * Anime un bouton pour indiquer une bonne réponse
     * @param button Le bouton à animer
     */
    public static void animateCorrectButton(Button button) {
        if (!isAnimationEnabled()) return;
        
        // Animation de pulsation verte
        button.setStyle("-fx-background-color: #2ecc71;"); // Vert pour les bonnes lettres
        
        ScaleTransition pulse = new ScaleTransition(Duration.millis(200), button);
        pulse.setFromX(1.0);
        pulse.setFromY(1.0);
        pulse.setToX(1.2);
        pulse.setToY(1.2);
        pulse.setCycleCount(2);
        pulse.setAutoReverse(true);
        pulse.play();
    }
    
    /**
     * Anime un bouton pour indiquer une mauvaise réponse
     * @param button Le bouton à animer
     */
    public static void animateIncorrectButton(Button button) {
        if (!isAnimationEnabled()) return;
        
        // Animation de secousse rouge
        button.setStyle("-fx-background-color: #e74c3c;"); // Rouge pour les mauvaises lettres
        
        
        TranslateTransition shake = new TranslateTransition(Duration.millis(50), button);
        shake.setFromX(0);
        shake.setByX(10);
        shake.setCycleCount(6);
        shake.setAutoReverse(true);
        shake.play();
    }
    
    /**
     * Effectue une transition en fondu enchaîné entre deux écrans
     * @param rootLayout Le conteneur racine
     * @param newScreen Le nouvel écran à afficher
     */
    public static void crossFade(BorderPane rootLayout, Parent newScreen) {
        if (!isAnimationEnabled()) {
            // Si les animations sont désactivées, changer directement l'écran
            rootLayout.setCenter(newScreen);
            return;
        }
        
        // Récupérer l'écran actuel
        Node currentScreen = rootLayout.getCenter();
        
        // Si c'est le premier écran (pas d'écran actuel)
        if (currentScreen == null) {
            // Préparer le nouvel écran (invisible au début)
            newScreen.setOpacity(0.0);
            rootLayout.setCenter(newScreen);
            
            // Animation de fondu pour le premier écran
            FadeTransition fadeIn = new FadeTransition(Duration.millis(400), newScreen);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
            return;
        }
        
        // Créer un conteneur temporaire pour les deux écrans
        StackPane transitionPane = new StackPane();
        
        // Ajouter l'écran actuel au conteneur temporaire
        transitionPane.getChildren().add(currentScreen);
        
        // Préparer le nouvel écran (invisible au début)
        newScreen.setOpacity(0.0);
        transitionPane.getChildren().add(newScreen);
        
        // Remplacer l'écran actuel par le conteneur temporaire
        rootLayout.setCenter(transitionPane);
        
        // Animation de fondu pour faire disparaître l'ancien écran
        FadeTransition fadeOut = new FadeTransition(Duration.millis(400), currentScreen);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        
        // Animation de fondu pour faire apparaître le nouvel écran
        FadeTransition fadeIn = new FadeTransition(Duration.millis(400), newScreen);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        
        // Jouer les deux animations en parallèle
        ParallelTransition crossFade = new ParallelTransition(fadeOut, fadeIn);
        
        crossFade.setOnFinished(event -> {
            // Une fois l'animation terminée, remplacer le conteneur temporaire par le nouvel écran
            rootLayout.setCenter(newScreen);
        });
        
        crossFade.play();
    }
    
    /**
     * Anime l'apparition d'un élément avec un effet de rebond
     * @param node L'élément à animer
     */
    public static void popIn(Node node) {
        if (!isAnimationEnabled()) return;
        
        node.setScaleX(0);
        node.setScaleY(0);
        
        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(300), node);
        scaleIn.setToX(1.1);
        scaleIn.setToY(1.1);
        
        ScaleTransition scaleBack = new ScaleTransition(Duration.millis(100), node);
        scaleBack.setToX(1.0);
        scaleBack.setToY(1.0);
        
        SequentialTransition sequence = new SequentialTransition(scaleIn, scaleBack);
        sequence.play();
    }
    
    /**
     * Anime un élément pour célébrer une victoire
     * @param node L'élément à animer
     */
    public static void celebrate(Node node) {
        if (!isAnimationEnabled()) return;
        
        RotateTransition rotate = new RotateTransition(Duration.millis(500), node);
        rotate.setByAngle(360);
        
        ScaleTransition scale = new ScaleTransition(Duration.millis(500), node);
        scale.setToX(1.5);
        scale.setToY(1.5);
        
        ScaleTransition scaleBack = new ScaleTransition(Duration.millis(300), node);
        scaleBack.setToX(1.0);
        scaleBack.setToY(1.0);
        
        ParallelTransition parallel = new ParallelTransition(rotate, scale);
        SequentialTransition sequence = new SequentialTransition(parallel, scaleBack);
        sequence.play();
    }
}