package ihm.pendu.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Classe utilitaire pour styliser les boîtes de dialogue
 */
public class AlertUtil {

    /**
     * Style les boîtes de dialogue selon le type
     * 
     * @param alert La boîte de dialogue à styliser
     * @param type  Le type de message (success, error, info, warning)
     */
    public static void styleAlert(Alert alert, String type) {
        DialogPane dialogPane = alert.getDialogPane();
        
        // Appliquer directement les styles en ligne
        String backgroundColor;
        String headerColor;
        String textColor;
        
        switch (type) {
            case "success":
                backgroundColor = "#e8f5e9";
                headerColor = "#4caf50";
                textColor = "#1b5e20";
                break;
            case "error":
                backgroundColor = "#ffebee";
                headerColor = "#f44336";
                textColor = "#b71c1c";
                break;
            case "warning":
                backgroundColor = "#fff8e1";
                headerColor = "#ff8f00";
                textColor = "#e65100";
                break;
            case "info":
            default:
                backgroundColor = "#e3f2fd";
                headerColor = "#1976d2";
                textColor = "#01579b";
                break;
        }
        
        // Appliquer les styles directement
        dialogPane.setStyle(
            "-fx-background-color: " + backgroundColor + ";" +
            "-fx-padding: 20px;" +
            "-fx-border-radius: 12px;" +
            "-fx-background-radius: 12px;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 15, 0, 0, 10);" +
            "-fx-border-width: 2px;" +
            "-fx-border-color: " + headerColor + ";"
        );
        
        // Styliser les boutons selon leur type
        dialogPane.lookupAll(".button").forEach(node -> {
            if (node instanceof Button) {
                Button button = (Button) node;
                String buttonText = button.getText();
                
                // Déterminer la couleur du bouton en fonction de son texte
                final String buttonColor;
                if (buttonText.equals("OK") || buttonText.equals("Oui") || buttonText.equals("Yes")) {
                    buttonColor = "#4caf50"; // Vert pour OK/Oui
                } else if (buttonText.equals("Annuler") || buttonText.equals("Non") || buttonText.equals("Cancel") || buttonText.equals("No")) {
                    buttonColor = "#f44336"; // Rouge pour Annuler/Non
                } else {
                    buttonColor = headerColor; // Couleur par défaut
                }
                
                // Appliquer le style de base
                applyButtonStyle(button, buttonColor, false);
                
                // Ajouter un effet de survol
                button.setOnMouseEntered(e -> applyButtonStyle(button, buttonColor, true));
                
                // Restaurer le style normal lorsque la souris quitte le bouton
                button.setOnMouseExited(e -> applyButtonStyle(button, buttonColor, false));
            }
        });
        
        // Styliser le texte du contenu
        dialogPane.lookupAll(".content").forEach(node -> {
            node.setStyle("-fx-text-fill: " + textColor + "; -fx-font-size: 14px;");
        });
        
        // Styliser le texte de l'en-tête
        dialogPane.lookupAll(".header-panel").forEach(node -> {
            node.setStyle("-fx-background-color: " + headerColor + "; -fx-text-fill: white;");
        });
        
        // Ajouter une icône à la fenêtre
        Stage stage = (Stage) dialogPane.getScene().getWindow();
        try {
            stage.getIcons().add(new Image(
                AlertUtil.class.getResourceAsStream("/ihm/pendu/images/icon.png")));
        } catch (Exception e) {
            // Ignorer si l'icône n'est pas disponible
        }
        
        // Ajouter une icône graphique selon le type
        try {
            String iconPath = "/ihm/pendu/images/icons/";
            switch (type) {
                case "success":
                    iconPath += "success.png";
                    break;
                case "error":
                    iconPath += "error.png";
                    break;
                case "warning":
                    iconPath += "warning.png";
                    break;
                default:
                    iconPath += "info.png";
                    break;
            }
            
            ImageView icon = new ImageView(new Image(
                AlertUtil.class.getResourceAsStream(iconPath)));
            icon.setFitHeight(48);
            icon.setFitWidth(48);
            alert.setGraphic(icon);
        } catch (Exception e) {
            // Ignorer si l'icône n'est pas disponible
        }
    }
    
    /**
     * Applique un style à un bouton
     * 
     * @param button Le bouton à styliser
     * @param color La couleur du bouton
     * @param hover Si true, applique le style de survol
     */
    private static void applyButtonStyle(Button button, String color, boolean hover) {
        String scale = hover ? "-fx-scale-x: 1.05; -fx-scale-y: 1.05;" : "-fx-scale-x: 1.0; -fx-scale-y: 1.0;";
        String shadow = hover ? 
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 8, 0, 0, 3);" : 
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 2);";
        
        button.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 10px 20px;" +
            "-fx-background-radius: 30px;" +
            shadow +
            "-fx-font-size: 14px;" +
            "-fx-border-width: 0;" +
            scale
        );
    }

    /**
     * Affiche une alerte qui se ferme automatiquement après un délai spécifié
     * 
     * @param alertType         Le type d'alerte
     * @param title             Le titre de l'alerte
     * @param headerText        Le texte d'en-tête (peut être null)
     * @param contentText       Le contenu de l'alerte
     * @param durationInSeconds La durée en secondes avant fermeture automatique
     */
    public static void showAutoCloseAlert(AlertType alertType, String title, String headerText, String contentText,
            double durationInSeconds) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        
        // Déterminer le type de style à appliquer
        String styleType;
        switch (alertType) {
            case ERROR:
                styleType = "error";
                break;
            case WARNING:
                styleType = "warning";
                break;
            case INFORMATION:
                styleType = "info";
                break;
            case CONFIRMATION:
                styleType = "info";
                break;
            default:
                styleType = "info";
                break;
        }
        
        // Appliquer le style
        styleAlert(alert, styleType);
        
        // Créer une transition de pause qui fermera l'alerte après le délai spécifié
        javafx.animation.PauseTransition delay = new javafx.animation.PauseTransition(
                javafx.util.Duration.seconds(durationInSeconds));
        delay.setOnFinished(event -> alert.close());
        
        // Démarrer la transition dès que l'alerte est affichée
        alert.setOnShown(event -> delay.play());
        
        alert.show();
    }

    /**
     * Crée une alerte stylisée de type information
     * 
     * @param title   Titre de l'alerte
     * @param header  En-tête de l'alerte (peut être null)
     * @param content Contenu de l'alerte
     * @return L'alerte stylisée
     */
    public static Alert createInfoAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        styleAlert(alert, "info");
        return alert;
    }

    /**
     * Crée une alerte stylisée de type erreur
     * 
     * @param title   Titre de l'alerte
     * @param header  En-tête de l'alerte (peut être null)
     * @param content Contenu de l'alerte
     * @return L'alerte stylisée
     */
    public static Alert createErrorAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        styleAlert(alert, "error");
        return alert;
    }

    /**
     * Crée une alerte stylisée de type avertissement
     * 
     * @param title   Titre de l'alerte
     * @param header  En-tête de l'alerte (peut être null)
     * @param content Contenu de l'alerte
     * @return L'alerte stylisée
     */
    public static Alert createWarningAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        styleAlert(alert, "warning");
        return alert;
    }

    /**
     * Crée une alerte stylisée de type confirmation
     * 
     * @param title   Titre de l'alerte
     * @param header  En-tête de l'alerte (peut être null)
     * @param content Contenu de l'alerte
     * @return L'alerte stylisée
     */
    public static Alert createConfirmationAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        styleAlert(alert, "info");
        return alert;
    }

    /**
     * Crée une alerte stylisée de type confirmation avec des boutons personnalisés
     * 
     * @param title   Titre de l'alerte
     * @param header  En-tête de l'alerte (peut être null)
     * @param content Contenu de l'alerte
     * @param buttonTypes Types de boutons à afficher
     * @return L'alerte stylisée
     */
    public static Alert createConfirmationAlert(String title, String header, String content, ButtonType... buttonTypes) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.getButtonTypes().setAll(buttonTypes);
        styleAlert(alert, "info");
        return alert;
    }

    /**
     * Crée une alerte stylisée de type succès (utilise le type INFORMATION)
     * 
     * @param title   Titre de l'alerte
     * @param header  En-tête de l'alerte (peut être null)
     * @param content Contenu de l'alerte
     * @return L'alerte stylisée
     */
    public static Alert createSuccessAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        styleAlert(alert, "success");
        return alert;
    }
    
    /**
     * Crée un bouton personnalisé pour les alertes
     * 
     * @param text Le texte du bouton
     * @param buttonData Le type de bouton (OK, CANCEL, etc.)
     * @return Le ButtonType personnalisé
     */
    public static ButtonType createButton(String text, ButtonData buttonData) {
        return new ButtonType(text, buttonData);
    }
}