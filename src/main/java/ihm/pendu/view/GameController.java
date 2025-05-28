package ihm.pendu.view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

import ihm.pendu.PenduFXapp;
import ihm.pendu.model.CategorieMot;
import ihm.pendu.model.EtatPartie;
import ihm.pendu.model.JeuPendu;
import ihm.pendu.model.JeuPenduBuilder;
import ihm.pendu.model.JeuPenduException;
import ihm.pendu.model.ResultatProposition;
import ihm.pendu.util.AlertUtil;
import ihm.pendu.util.AnimationUtil;
import ihm.pendu.util.SoundManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class GameController {

    private PenduFXapp penduFXapp;
    private JeuPendu jeuPendu;
    private Map<Character, Button> lettresBoutons = new HashMap<>();

    // Nombre total d'étapes d'images disponibles (de 1 à 7)
    private static final int TOTAL_ETAPES_IMAGES = 7;

    @FXML
    private ImageView hangmanImage;

    @FXML
    private Label wordLabel;

    @FXML
    private TextField guessTextField;

    @FXML
    private TextField cheatTextField; // Champ pour le mode triche

    @FXML
    private Button guessButton;

    @FXML
    private Button newGameButton;

    @FXML
    private Button hintButton;

    @FXML
    private Button statsButton;

    @FXML
    private Button quitButton; // Bouton Quitter

    @FXML
    private Label attemptsLabel;

    @FXML
    private ImageView backgroundImage;

    // Références aux boutons de lettres
    @FXML
    private Button btnA;
    @FXML
    private Button btnB;
    @FXML
    private Button btnC;
    @FXML
    private Button btnD;
    @FXML
    private Button btnE;
    @FXML
    private Button btnF;
    @FXML
    private Button btnG;
    @FXML
    private Button btnH;
    @FXML
    private Button btnI;
    @FXML
    private Button btnJ;
    @FXML
    private Button btnK;
    @FXML
    private Button btnL;
    @FXML
    private Button btnM;
    @FXML
    private Button btnN;
    @FXML
    private Button btnO;
    @FXML
    private Button btnP;
    @FXML
    private Button btnQ;
    @FXML
    private Button btnR;
    @FXML
    private Button btnS;
    @FXML
    private Button btnT;
    @FXML
    private Button btnU;
    @FXML
    private Button btnV;
    @FXML
    private Button btnW;
    @FXML
    private Button btnX;
    @FXML
    private Button btnY;
    @FXML
    private Button btnZ;

    // Liste des mots complets incorrects déjà proposés
    private List<String> motsProposesIncorrects = new ArrayList<>();

    // Variables pour gérer le double-clic
    private final AtomicLong lastClickTime = new AtomicLong(0);
    private static final long DOUBLE_CLICK_TIME_DELTA = 300; // millisecondes

    /**
     * Initialise le contrôleur.
     */
    @FXML
    private void initialize() {
        // Initialiser la map des boutons de lettres
        initLettresBoutons();

        // Configurer les gestionnaires d'événements pour les boutons de lettres
        for (Map.Entry<Character, Button> entry : lettresBoutons.entrySet()) {
            Character lettre = entry.getKey();
            Button button = entry.getValue();

            button.setOnAction(e -> {
                // Jouer le son de clic
                SoundManager.getInstance().playSound(SoundManager.KEYBOARD_CLICK);
                proposerLettre(lettre);
            });
        }

        // Configurer le bouton de validation avec détection de double-clic
        guessButton.setOnAction(null);
        guessButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            SoundManager.getInstance().playSound(SoundManager.KEYBOARD_CLICK);

            long currentTime = System.currentTimeMillis();
            long lastClick = lastClickTime.get();

            if (currentTime - lastClick <= DOUBLE_CLICK_TIME_DELTA) {
                // Double-clic détecté
                toggleCheatMode();
                lastClickTime.set(0); // Réinitialiser pour éviter de détecter plusieurs doubles clics consécutifs
            } else {
                // Clic simple - traiter la proposition
                String proposition = guessTextField.getText().trim().toUpperCase();
                if (proposition.length() == 1) {
                    proposerLettre(proposition.charAt(0));
                } else if (proposition.length() > 1) {
                    // Proposer un mot entier
                    proposerMot(proposition);
                }
                guessTextField.clear();

                // Enregistrer le temps du clic
                lastClickTime.set(currentTime);
            }
        });

        // Ajouter un gestionnaire pour les événements clavier après que la scène est
        // chargée
        Platform.runLater(() -> {
            Scene scene = guessTextField.getScene();
            if (scene != null) {
                scene.setOnKeyPressed(this::handleKeyPress);
            }
        });

        // Configurer les boutons d'action
        newGameButton.setOnAction(e -> {
            SoundManager.getInstance().playSound(SoundManager.KEYBOARD_CLICK);
            handleNewGame();
        });

        // Remettre le bouton indice comme avant
        hintButton.setOnAction(e -> {
            SoundManager.getInstance().playSound(SoundManager.KEYBOARD_CLICK);
            handleHint();
        });

        statsButton.setOnAction(e -> {
            SoundManager.getInstance().playSound(SoundManager.KEYBOARD_CLICK);
            handleStats();
        });

        quitButton.setOnAction(e -> {
            SoundManager.getInstance().playSound(SoundManager.KEYBOARD_CLICK);
            handleBackToMenu();
        });

        // Charger l'image de fond correspondant au thème
        chargerImageFond();

        // Démarrer une nouvelle partie
        demarrerNouvellePartie();
    }

    /**
     * Active ou désactive le mode triche
     */
    private void toggleCheatMode() {
        // Inverser l'état du mode triche
        boolean newCheatMode = !SettingsController.isModeTricheActive();
        SettingsController.modeTricheActive = newCheatMode;

        // Mettre à jour l'affichage du champ de triche
        if (newCheatMode) {
            cheatTextField.setVisible(true);
            cheatTextField.setText("Mot à trouver : " + jeuPendu.getMotATrouver());

            // Afficher un message discret
            AlertUtil.showAutoCloseAlert(
                    AlertType.INFORMATION,
                    "Mode triche",
                    null,
                    "Mode triche activé",
                    2);
        } else {
            cheatTextField.setVisible(false);

            // Afficher un message discret
            AlertUtil.showAutoCloseAlert(
                    AlertType.INFORMATION,
                    "Mode triche",
                    null,
                    "Mode triche desactivé",
                    0.7);
        }
    }

    /**
     * Initialise la map des boutons de lettres.
     */
    private void initLettresBoutons() {
        lettresBoutons.put('A', btnA);
        lettresBoutons.put('B', btnB);
        lettresBoutons.put('C', btnC);
        lettresBoutons.put('D', btnD);
        lettresBoutons.put('E', btnE);
        lettresBoutons.put('F', btnF);
        lettresBoutons.put('G', btnG);
        lettresBoutons.put('H', btnH);
        lettresBoutons.put('I', btnI);
        lettresBoutons.put('J', btnJ);
        lettresBoutons.put('K', btnK);
        lettresBoutons.put('L', btnL);
        lettresBoutons.put('M', btnM);
        lettresBoutons.put('N', btnN);
        lettresBoutons.put('O', btnO);
        lettresBoutons.put('P', btnP);
        lettresBoutons.put('Q', btnQ);
        lettresBoutons.put('R', btnR);
        lettresBoutons.put('S', btnS);
        lettresBoutons.put('T', btnT);
        lettresBoutons.put('U', btnU);
        lettresBoutons.put('V', btnV);
        lettresBoutons.put('W', btnW);
        lettresBoutons.put('X', btnX);
        lettresBoutons.put('Y', btnY);
        lettresBoutons.put('Z', btnZ);
    }

    /**
     * Est appelé par l'application principale pour donner une référence à
     * elle-même.
     */
    public void setPenduFXapp(PenduFXapp penduFXapp) {
        this.penduFXapp = penduFXapp;
    }

    /**
     * Démarre une nouvelle partie.
     */
    private void demarrerNouvellePartie() {
        // Réinitialiser la liste des mots proposés incorrects
        motsProposesIncorrects.clear();

        // Récupérer les paramètres configurés
        CategorieMot categorie = ThemeConfigController.getCategorieSelectionnee();
        int nbErreursMax = SettingsController.getNbErreursMaxForModel();
        int nombreLettres = SettingsController.getNombreLettres();

        // Créer une nouvelle partie
        jeuPendu = JeuPenduBuilder.creer()
                .avecCategorie(categorie)
                .avecNombreLettres(nombreLettres)
                .avecNbErreursMax(nbErreursMax)
                .construire();

        // Réinitialiser l'interface
        mettreAJourAffichage();

        // Réactiver tous les boutons de lettres
        for (Button button : lettresBoutons.values()) {
            button.setDisable(false);
            button.setStyle("-fx-background-color: #3498db;");
        }

        // Mettre à jour l'image du pendu (initialiser avec l'image vide)
        mettreAJourImagePendu();

        // Gérer le mode triche
        if (SettingsController.isModeTricheActive()) {
            cheatTextField.setVisible(true);
            cheatTextField.setText("Mot à trouver : " + jeuPendu.getMotATrouver());
        } else {
            cheatTextField.setVisible(false);
        }
    }

    /**
     * Gère les événements clavier pour toute la scène
     * 
     * @param event L'événement clavier
     */
    private void handleKeyPress(KeyEvent event) {
        // Ignorer les événements si le jeu est terminé
        if (jeuPendu.isPartieTerminee()) {
            return;
        }

        // Ignorer les événements si un TextField a le focus
        if (guessTextField.isFocused() || cheatTextField.isFocused()) {
            return;
        }

        String character = event.getText().toUpperCase();

        // Vérifier si c'est une lettre de A à Z
        if (character.length() == 1 && character.matches("[A-Z]")) {
            // Ajouter la lettre au TextField
            guessTextField.setText(guessTextField.getText() + character);

            // Optionnel : donner le focus au TextField
            // guessTextField.requestFocus();

            // Optionnel : positionner le curseur à la fin du texte
            guessTextField.positionCaret(guessTextField.getText().length());

            // Consommer l'événement pour éviter qu'il soit traité ailleurs
            event.consume();
        } else if (event.getCode() == KeyCode.ENTER) {
            // Si l'utilisateur appuie sur Entrée, simuler un clic sur le bouton de
            // validation
            guessButton.fire();
            event.consume();
        } else if (event.getCode() == KeyCode.BACK_SPACE) {
            // Si l'utilisateur appuie sur Retour arrière, supprimer le dernier caractère
            String currentText = guessTextField.getText();
            if (!currentText.isEmpty()) {
                guessTextField.setText(currentText.substring(0, currentText.length() - 1));

                // Optionnel : donner le focus au TextField
                guessTextField.requestFocus();

                // Optionnel : positionner le curseur à la fin du texte
                guessTextField.positionCaret(guessTextField.getText().length());
            }
            event.consume();
        }
    }

    /**
     * Met à jour l'affichage du mot et des tentatives restantes.
     */
    private void mettreAJourAffichage() {
        // Afficher le mot courant avec des espaces entre les lettres
        char[] motCourant = jeuPendu.getMotCourant();
        StringBuilder sb = new StringBuilder();
        for (char c : motCourant) {
            sb.append(c).append(" ");
        }
        wordLabel.setText(sb.toString().trim());

        // Mettre à jour le nombre de tentatives restantes
        // Ajuster l'affichage en fonction de la valeur réelle choisie par l'utilisateur
        int nbErreursUtilisateur = SettingsController.getNbErreursMax();
        int tentativesRestantes = nbErreursUtilisateur - jeuPendu.getNbErreurs();
        if (tentativesRestantes < 0)
            tentativesRestantes = 0;
        attemptsLabel.setText("Tentatives restantes: " + tentativesRestantes);
    }

    /**
     * Met à jour l'image du pendu en fonction du nombre d'erreurs.
     * Cette méthode adapte l'affichage des images en fonction du nombre d'erreurs
     * maximum.
     */
    private void mettreAJourImagePendu() {
        int nbErreurs = jeuPendu.getNbErreurs();
        int nbErreursUtilisateur = SettingsController.getNbErreursMax();

        // Si aucune erreur, afficher une image vide
        if (nbErreurs == 0) {
            try {
                Image image = new Image(getClass().getResourceAsStream("/ihm/pendu/images/pendu/pendu_0.png"));
                hangmanImage.setImage(image);
            } catch (Exception e) {
                // Si l'image vide n'existe pas, laisser l'image vide
                hangmanImage.setImage(null);
            }
            return;
        }

        // Calculer quelle étape d'image afficher en fonction du ratio d'erreurs
        int etapeImage;

        // Si l'utilisateur a choisi 0 erreurs, on perd dès la première erreur
        if (nbErreursUtilisateur == 0 && nbErreurs > 0) {
            etapeImage = 7; // Dernière image (pendu complet)
        } else if (nbErreurs > nbErreursUtilisateur) {
            // Si le joueur a dépassé le nombre d'erreurs autorisées
            etapeImage = 7; // Dernière image (pendu complet)
        } else {
            // Calculer l'étape proportionnellement au nombre d'erreurs maximum
            // Pour éviter division par zéro si nbErreursUtilisateur est 0
            int diviseur = Math.max(1, nbErreursUtilisateur);
            double ratio = (double) nbErreurs / diviseur;
            etapeImage = (int) Math.ceil(ratio * (TOTAL_ETAPES_IMAGES - 1));

            // S'assurer que l'étape est dans les limites (1-7)
            etapeImage = Math.max(1, Math.min(etapeImage, TOTAL_ETAPES_IMAGES));
        }

        // Charger l'image correspondante
        String imagePath = "/ihm/pendu/images/pendu/pendu_" + etapeImage + ".png";

        try {
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            hangmanImage.setImage(image);
        } catch (Exception e) {
            // Si l'image n'existe pas, utiliser une image par défaut
            try {
                Image image = new Image(getClass().getResourceAsStream("/ihm/pendu/images/pendu/etape_1.png"));
                hangmanImage.setImage(image);
            } catch (Exception ex) {
                // Si aucune image n'est disponible, laisser l'image vide
                hangmanImage.setImage(null);
            }
        }
    }

    /**
     * Propose une lettre.
     * 
     * @param lettre La lettre proposée
     */
    private void proposerLettre(char lettre) {
        try {
            ResultatProposition resultat = jeuPendu.proposerLettre(lettre);

            // Mettre à jour l'interface
            mettreAJourAffichage();
            mettreAJourImagePendu();

            // Désactiver le bouton de la lettre proposée
            Button button = lettresBoutons.get(lettre);
            if (button != null) {
                button.setDisable(true);

                // Changer la couleur du bouton en fonction du résultat
                if (resultat == ResultatProposition.BIEN_JOUEE) {
                    button.setStyle(
                            "-fx-background-color: #2ecc71; -fx-text-fill: #000000; -fx-font-size: 16; -fx-opacity: 1;"); // Vert
                                                                                                                          // pour
                                                                                                                          // les
                                                                                                                          // bonnes
                                                                                                                          // lettres

                    // Animation pour une bonne lettre
                    AnimationUtil.bounce(button, 200);
                } else {
                    button.setStyle(
                            "-fx-background-color: #e74c3c; -fx-text-fill: #000000; -fx-font-size: 16; -fx-opacity: 1;"); // Rouge
                                                                                                                          // pour
                                                                                                                          // les
                                                                                                                          // mauvaises
                                                                                                                          // lettres

                    // Animation pour une mauvaise lettre
                    AnimationUtil.shake(button);
                }
            }

            // Vérifier si la partie est terminée selon nos règles personnalisées
            // (y compris le cas où l'utilisateur a choisi 0 erreurs)
            int nbErreursUtilisateur = SettingsController.getNbErreursMax();
            if (nbErreursUtilisateur == 0 && jeuPendu.getNbErreurs() > 0) {
                // Cas spécial: l'utilisateur a choisi 0 erreurs et a fait une erreur
                verifierFinPartie();
            } else {
                // Vérification normale
                verifierFinPartie();
            }

        } catch (JeuPenduException e) {
            // Afficher un message d'erreur stylisé
            Alert alert = AlertUtil.createWarningAlert(
                    "Erreur",
                    null,
                    e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Propose un mot entier.
     * 
     * @param mot Le mot proposé
     */
    private void proposerMot(String mot) {
        // Normaliser le mot en majuscules pour la comparaison
        String motNormalise = mot.toUpperCase();

        // Vérifier si le mot proposé est correct
        if (motNormalise.equals(jeuPendu.getMotATrouver())) {
            // Révéler toutes les lettres
            for (char lettre : motNormalise.toCharArray()) {
                try {
                    jeuPendu.proposerLettre(lettre);
                } catch (JeuPenduException e) {
                    // Ignorer les erreurs (lettres déjà proposées)
                }
            }

            // Mettre à jour l'interface
            mettreAJourAffichage();

            // Jouer le son de victoire
            SoundManager.getInstance().playSound(SoundManager.VICTORY);

            // Afficher un message de victoire
            Alert alert = AlertUtil.createSuccessAlert(
                    "Victoire !",
                    "Félicitations !",
                    "Vous avez trouvé le mot : " + jeuPendu.getMotATrouver());
            alert.showAndWait();

            // Proposer une nouvelle partie
            proposerNouvellePartie();
        } else {
            // Vérifier si ce mot a déjà été proposé
            boolean motDejaPropose = motsProposesIncorrects.contains(motNormalise);

            // Ajouter le mot à la liste des mots proposés incorrects s'il n'y est pas déjà
            if (!motDejaPropose) {
                motsProposesIncorrects.add(motNormalise);
            }

            // Mettre à jour l'interface
            mettreAJourAffichage();
            mettreAJourImagePendu();

            // Si le mot a déjà été proposé, informer l'utilisateur
            if (motDejaPropose) {
                AlertUtil.showAutoCloseAlert(
                        AlertType.WARNING,
                        "Mot déjà proposé",
                        null,
                        "Ce mot a déjà été proposé et est incorrect.",
                        1.5);
            }

            // Vérifier si la partie est terminée
            verifierFinPartie();
        }
    }

    /**
     * Vérifie si la partie est terminée et affiche un message approprié.
     */
    private void verifierFinPartie() {
        // Vérifier si la partie est terminée selon le modèle
        boolean partieTermineeSelonModele = jeuPendu.isPartieTerminee();

        // Vérifier si la partie devrait être terminée selon nos règles personnalisées
        int nbErreursUtilisateur = SettingsController.getNbErreursMax();
        boolean partiePerdueSurErreurs = nbErreursUtilisateur == 0 && jeuPendu.getNbErreurs() > 0;

        if (partieTermineeSelonModele || partiePerdueSurErreurs) {
            EtatPartie etat;

            if (partiePerdueSurErreurs && !partieTermineeSelonModele) {
                // Cas spécial: l'utilisateur a choisi 0 erreurs et a fait une erreur
                etat = EtatPartie.PERDUE;
            } else {
                etat = jeuPendu.getEtatPartie();
            }

            // Jouer le son approprié
            if (etat == EtatPartie.GAGNEE) {
                SoundManager.getInstance().playSound(SoundManager.VICTORY);

                // Utiliser une alerte de succès
                Alert alert = AlertUtil.createSuccessAlert(
                        "Victoire !",
                        "Félicitations !",
                        "Vous avez trouvé le mot : " + jeuPendu.getMotATrouver());
                alert.showAndWait();
            } else {
                SoundManager.getInstance().playSound(SoundManager.FAIL);

                // Utiliser une alerte d'erreur
                Alert alert = AlertUtil.createErrorAlert(
                        "Défaite !",
                        "Dommage !",
                        "Le mot à trouver était : " + jeuPendu.getMotATrouver());
                alert.showAndWait();
            }

            // Proposer une nouvelle partie
            proposerNouvellePartie();
        }
    }

    /**
     * Propose de démarrer une nouvelle partie.
     */
    private void proposerNouvellePartie() {
        Alert alert = AlertUtil.createConfirmationAlert(
                "Nouvelle partie",
                "Voulez-vous démarrer une nouvelle partie ?",
                "Choisissez une option.");

        ButtonType buttonTypeOui = new ButtonType("Oui");
        ButtonType buttonTypeNon = new ButtonType("Non");
        ButtonType buttonTypeMenu = new ButtonType("Retour au menu");

        alert.getButtonTypes().setAll(buttonTypeOui, buttonTypeNon, buttonTypeMenu);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOui) {
            demarrerNouvellePartie();
        } else if (result.get() == buttonTypeMenu) {
            penduFXapp.showWelcomeScreen();
        }
    }

    /**
     * Gère le clic sur le bouton Nouvelle partie
     */
    @FXML
    private void handleNewGame() {
        demarrerNouvellePartie();
    }

    /**
     * Gère le clic sur le bouton Indice
     */
    @FXML
    private void handleHint() {

        // Donner un indice en révélant une lettre non encore trouvée
        String motATrouver = jeuPendu.getMotATrouver();
        char[] motCourant = jeuPendu.getMotCourant();

        for (int i = 0; i < motATrouver.length(); i++) {
            if (motCourant[i] == '-') {
                char lettre = motATrouver.charAt(i);

                // Proposer cette lettre
                proposerLettre(lettre);

                if (jeuPendu.isPartieTerminee()) {
                    return;
                }
                // Afficher un message

                // Afficher un message discret
                AlertUtil.showAutoCloseAlert(
                        AlertType.INFORMATION,
                        "Indice",
                        null,
                        "Indice : la lettre '" + lettre + "' est dans le mot.",
                        3);

                return;
            }
        }

        // Vérifier si la partie est terminée
        if (jeuPendu.isPartieTerminee()) {
            Alert alert = AlertUtil.createWarningAlert(
                    "Indice",
                    null,
                    "La partie est terminée !");
            alert.showAndWait();
            return;
        }

        // Si toutes les lettres sont déjà trouvées
        Alert alert = AlertUtil.createInfoAlert(
                "Indice",
                null,
                "Toutes les lettres ont déjà été trouvées !");
        alert.showAndWait();
    }

    /**
     * Gère le clic sur le bouton Statistiques
     */
    @FXML
    private void handleStats() {
        // Afficher les statistiques
        StringBuilder sb = new StringBuilder();
        sb.append("Catégorie : ").append(jeuPendu.getCategorie().getLibelle()).append("\n");
        sb.append("Nombre de lettres : ").append(jeuPendu.getNombreLettres()).append("\n");
        sb.append("Erreurs : ").append(jeuPendu.getNbErreurs()).append("/").append(jeuPendu.getNbErreursMax())
                .append("\n");
        sb.append("État de la partie : ").append(jeuPendu.getEtatPartie().getLibelle()).append("\n");

        if (jeuPendu.isPartieTerminee()) {
            sb.append("\nLe mot était : ").append(jeuPendu.getMotATrouver());
        }

        Alert alert = AlertUtil.createInfoAlert(
                "Statistiques",
                "Informations sur la partie en cours",
                sb.toString());
        alert.showAndWait();
    }

    /**
     * Retourne à l'écran d'accueil
     */
    @FXML
    private void handleBackToMenu() {
        // Demander confirmation si la partie est en cours
        if (jeuPendu != null && !jeuPendu.isPartieTerminee()) {
            Alert alert = AlertUtil.createConfirmationAlert(
                    "Retour au menu",
                    "Voulez-vous vraiment quitter la partie en cours ?",
                    "Votre progression sera perdue.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() != ButtonType.OK) {
                return;
            }
        }

        penduFXapp.showWelcomeScreen();
    }

    /**
     * Charge l'image de fond correspondant au thème sélectionné
     */
    private void chargerImageFond() {
        String backgroundFileName = ThemeConfigController.getBackgroundFileName();
        String imagePath = "/ihm/pendu/images/backgrounds/" + backgroundFileName;

        try {
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            backgroundImage.setImage(image);
        } catch (Exception e) {
            // Si l'image n'existe pas, utiliser l'image par défaut
            try {
                Image image = new Image(getClass().getResourceAsStream("/ihm/pendu/images/backgrounds/tous.png"));
                backgroundImage.setImage(image);
            } catch (Exception ex) {
                // Si aucune image n'est disponible, ne rien faire
            }
        }
    }
}