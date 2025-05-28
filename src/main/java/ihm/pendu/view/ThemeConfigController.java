package ihm.pendu.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import ihm.pendu.PenduFXapp;
import ihm.pendu.model.CategorieMot;
import ihm.pendu.util.SoundManager;

public class ThemeConfigController {
    
    private PenduFXapp penduFXapp;
    private static CategorieMot categorieSelectionnee = CategorieMot.TOUTES;
    
    @FXML
    private Button btnJavaFX;
    
    @FXML
    private Button btnAnimaux;
    
    @FXML
    private Button btnFruits;
    
    @FXML
    private Button btnCouleurs;
    
    @FXML
    private Button btnPays;
    
    @FXML
    private Button btnMetiers;
    
    @FXML
    private Button btnToutes;
    
    /**
    * Initialise le contrôleur.
    */
    @FXML
    private void initialize() {
        // Initialiser les gestionnaires d'événements pour les boutons
        btnJavaFX.setOnAction(e -> {
            SoundManager.getInstance().playSound(SoundManager.KEYBOARD_CLICK);
            handleThemeSelection(CategorieMot.JAVA_FX);
        });
        
        btnAnimaux.setOnAction(e -> {
            SoundManager.getInstance().playSound(SoundManager.KEYBOARD_CLICK);
            handleThemeSelection(CategorieMot.ANIMAUX);
        });
        
        btnFruits.setOnAction(e -> {
            SoundManager.getInstance().playSound(SoundManager.KEYBOARD_CLICK);
            handleThemeSelection(CategorieMot.FRUITS);
        });
        
        btnCouleurs.setOnAction(e -> {
            SoundManager.getInstance().playSound(SoundManager.KEYBOARD_CLICK);
            handleThemeSelection(CategorieMot.COULEURS);
        });
        
        btnPays.setOnAction(e -> {
            SoundManager.getInstance().playSound(SoundManager.KEYBOARD_CLICK);
            handleThemeSelection(CategorieMot.PAYS);
        });
        
        btnMetiers.setOnAction(e -> {
            SoundManager.getInstance().playSound(SoundManager.KEYBOARD_CLICK);
            handleThemeSelection(CategorieMot.METIERS);
        });
        
        btnToutes.setOnAction(e -> {
            SoundManager.getInstance().playSound(SoundManager.KEYBOARD_CLICK);
            handleThemeSelection(CategorieMot.TOUTES);
        });
    }
    
    /**
    * Est appelé par l'application principale pour donner une référence à elle-même.
    */
    public void setPenduFXapp(PenduFXapp penduFXapp) {
        this.penduFXapp = penduFXapp;
    }
    
    /**
    * Gère la sélection d'un thème
    * @param categorie La catégorie sélectionnée
    */
    private void handleThemeSelection(CategorieMot categorie) {
        // Enregistrer la catégorie sélectionnée
        categorieSelectionnee = categorie;
        
        // Après sélection, aller aux paramètres
        penduFXapp.showSettingsScreen();
    }
    
    /**
    * Retourne à l'écran d'accueil
    */
    @FXML
    private void handleBackToMenu() {
        SoundManager.getInstance().playSound(SoundManager.KEYBOARD_CLICK);
        penduFXapp.showWelcomeScreen();
    }
    
    /**
    * Retourne la catégorie sélectionnée
    */
    public static CategorieMot getCategorieSelectionnee() {
        return categorieSelectionnee;
    }
    
    /**
    * Retourne le nom du fichier d'arrière-plan correspondant à la catégorie
    */
    public static String getBackgroundFileName() {
        switch (categorieSelectionnee) {
            case JAVA_FX:
                return "javafx.png";
            case ANIMAUX:
                return "animals.png";
            case FRUITS:
                return "fruits.png";
            case COULEURS:
                return "colors.png";
            case PAYS:
                return "pays.png";
            case METIERS:
                return "metiers.png";
            case TOUTES:
            default:
                return "tous.png";
        }
    }
}