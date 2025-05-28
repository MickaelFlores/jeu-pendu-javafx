package ihm.pendu.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import ihm.pendu.PenduFXapp;
import ihm.pendu.util.SoundManager;

import java.util.Optional;

public class SettingsController {

    private PenduFXapp penduFXapp;
    private static int nbErreursMax = 6;
    private static int nombreLettres = 0; // 0 signifie aléatoire
    private static String difficulte = "Normal";
    private static boolean sonActive = true;
    private static boolean animationActive = true;
    public static boolean modeTricheActive = false;

    @FXML
    private Slider errorSlider;

    @FXML
    private TextField errorTextField;

    @FXML
    private MenuButton difficultyMenu;

    @FXML
    private CheckBox soundOption;

    @FXML
    private CheckBox animationOption;

    @FXML
    private CheckBox cheatOption;

    @FXML
    private Button previousButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button nextButton;

    /**
     * Met à jour le style du menu de difficulté en fonction de la difficulté sélectionnée
     * @param difficulty La difficulté sélectionnée
     */
    private void updateDifficultyMenuStyle(String difficulty) {
        switch (difficulty) {
            case "Facile":
                difficultyMenu.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                break;
            case "Normal":
                difficultyMenu.setStyle("-fx-background-color: #FFC107; -fx-text-fill: black;");
                break;
            case "Difficile":
                difficultyMenu.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white;");
                break;
            case "Expert":
                difficultyMenu.setStyle("-fx-background-color: #F44336; -fx-text-fill: white;");
                break;
            default:
                difficultyMenu.setStyle("");
                break;
        }
    }

    /**
    * Initialise le contrôleur.
    */
    @FXML
    private void initialize() {
        // Initialiser avec les valeurs actuelles
        errorSlider.setValue(nbErreursMax);
        errorTextField.setText(String.valueOf(nbErreursMax));
        difficultyMenu.setText(difficulte);
        soundOption.setSelected(sonActive);
        animationOption.setSelected(animationActive);
        cheatOption.setSelected(modeTricheActive);
        
        // Appliquer la couleur correspondant à la difficulté actuelle
        updateDifficultyMenuStyle(difficulte);

        // Liaison bidirectionnelle entre le slider et le champ de texte
        errorSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            errorTextField.setText(String.valueOf(newValue.intValue()));
        });

        // Écouteur pour le champ de texte
        errorTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                // Vérifier si la valeur est un nombre
                if (!newValue.matches("\\d*")) {
                    // Si ce n'est pas un nombre, restaurer l'ancienne valeur
                    errorTextField.setText(oldValue);
                    return;
                }

                // Si le champ est vide, ne rien faire
                if (newValue.isEmpty()) {
                    return;
                }

                // Convertir en entier
                int value = Integer.parseInt(newValue);

                // Vérifier les limites
                if (value < 0) {
                    errorTextField.setText("0");
                    errorSlider.setValue(0);
                } else if (value > 26) {
                    errorTextField.setText("26");
                    errorSlider.setValue(26);
                } else {
                    // Mettre à jour le slider
                    errorSlider.setValue(value);
                }
            } catch (NumberFormatException e) {
                // En cas d'erreur, restaurer l'ancienne valeur
                errorTextField.setText(oldValue);
            }
        });

        // Ajouter un gestionnaire pour la perte de focus
        errorTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            // Quand le champ perd le focus
            if (!newValue) {
                // Si le champ est vide, le remplir avec la valeur du slider
                if (errorTextField.getText().isEmpty()) {
                    errorTextField.setText(String.valueOf((int) errorSlider.getValue()));
                }
            }
        });

        // Synchroniser le slider et le champ texte
        errorSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            int value = newValue.intValue();
            errorTextField.setText(String.valueOf(value));
            // Stocker la valeur réelle sélectionnée par l'utilisateur
            nbErreursMax = value;
        });

        errorTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int value = Integer.parseInt(newValue);
                if (value >= 0 && value <= 10) { // Permettre 0 comme valeur minimale
                    errorSlider.setValue(value);
                    nbErreursMax = value;
                }
            } catch (NumberFormatException e) {
                // Ignorer les entrées non numériques
            }
        });

        // Configurer les options du menu de difficulté
        for (MenuItem item : difficultyMenu.getItems()) {
            item.setOnAction(e -> {
                difficultyMenu.setText(item.getText());
                difficulte = item.getText();

                // Mettre à jour le nombre de lettres en fonction de la difficulté
                switch (difficulte) {
                    case "Facile":
                        nombreLettres = 5;
                        break;
                    case "Normal":
                        nombreLettres = 7;
                        break;
                    case "Difficile":
                        nombreLettres = 9;
                        break;
                    case "Expert":
                        nombreLettres = 11;
                        break;
                    default:
                        nombreLettres = 0;
                        break;
                }
                
                // Mettre à jour le style du menu
                updateDifficultyMenuStyle(difficulte);
            });
        }

        // Configurer les options checkbox
        soundOption.setOnAction(e -> {
            sonActive = soundOption.isSelected();
            SoundManager.getInstance().setSoundEnabled(sonActive);

            // Jouer un son de test si activé
            if (sonActive) {
                SoundManager.getInstance().playSound(SoundManager.KEYBOARD_CLICK);
            }
        });

        animationOption.setOnAction(e -> {
            SoundManager.getInstance().playSound(SoundManager.KEYBOARD_CLICK);
            animationActive = animationOption.isSelected();
        });

        cheatOption.setOnAction(e -> {
            SoundManager.getInstance().playSound(SoundManager.KEYBOARD_CLICK);
            modeTricheActive = cheatOption.isSelected();
        });

        // Configurer les gestionnaires d'événements pour les boutons
        previousButton.setOnAction(e -> {
            SoundManager.getInstance().playSound(SoundManager.KEYBOARD_CLICK);
            handlePrevious();
        });

        saveButton.setOnAction(e -> {
            SoundManager.getInstance().playSound(SoundManager.KEYBOARD_CLICK);
            handleSave();
        });

        nextButton.setOnAction(e -> {
            SoundManager.getInstance().playSound(SoundManager.KEYBOARD_CLICK);
            handleNext();
        });
    }

    /**
    * Est appelé par l'application principale pour donner une référence à
    * elle-même.
    */
    public void setPenduFXapp(PenduFXapp penduFXapp) {
        this.penduFXapp = penduFXapp;
    }

    /**
    * Gère le clic sur le bouton Précédent
    */
    @FXML
    private void handlePrevious() {
        penduFXapp.showThemeConfigScreen();
    }

    /**
    * Gère le clic sur le bouton Enregistrer
    */
    @FXML
    private void handleSave() {
        try {
            // Récupérer et valider la valeur du nombre d'erreurs
            int errorCount = Integer.parseInt(errorTextField.getText());
            if (errorCount < 0)
                errorCount = 0;
            if (errorCount > 26)
                errorCount = 26;

            // Autres sauvegardes...

            //Lance le jeu
            penduFXapp.showWelcomeScreen();

        } catch (NumberFormatException e) {
            // Gérer l'erreur de format
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Le nombre d'erreurs doit être un nombre entier valide.");
            alert.showAndWait();
        }
    }

    /**
    * Gère le clic sur le bouton Suivant
    */
    @FXML
    private void handleNext() {
        // Les paramètres sont déjà sauvegardés via les listeners
        // Le mode triche est maintenant géré par la checkbox
        penduFXapp.showGameScreen();
    }

    /**
    * Retourne le nombre d'erreurs maximum configuré par l'utilisateur
    */
    public static int getNbErreursMax() {
        return nbErreursMax;
    }

    /**
    * Retourne le nombre de lettres configuré
    */
    public static int getNombreLettres() {
        return nombreLettres;
    }

    /**
    * Retourne si le son est activé
    */
    public static boolean isSonActive() {
        return sonActive;
    }

    /**
    * Retourne si les animations sont activées
    */
    public static boolean isAnimationActive() {
        return animationActive;
    }

    /**
    * Retourne si le mode triche est activé
    */
    public static boolean isModeTricheActive() {
        return modeTricheActive;
    }

    /**
    * Retourne le nombre d'erreurs maximum configuré pour le modèle
    * Ajuste la valeur pour le modèle (ajoute 1 si nbErreursMax est 0)
    */
    public static int getNbErreursMaxForModel() {
        // Si l'utilisateur a choisi 0, on retourne 0 au modèle
        // Sinon, on retourne la valeur choisie
        if (getNbErreursMax() == 0) {
            return nbErreursMax;
        }
        return nbErreursMax + 1;
    }
}