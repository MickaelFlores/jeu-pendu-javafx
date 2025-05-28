# 🎮 Jeu du Pendu JavaFX

Un jeu du pendu moderne et interactif développé en JavaFX lors d'un projet de 2 jours à l'IUT.

![JavaFX](https://img.shields.io/badge/JavaFX-17+-blue.svg)
![Java](https://img.shields.io/badge/Java-17+-orange.svg)
![License](https://img.shields.io/badge/License-MIT-green.svg)

## 📸 Aperçu

*Ajoutez ici vos captures d'écran du jeu*

## 🚀 Fonctionnalités

### 🎯 Gameplay
- **Système de difficulté** : Choix du nombre d'erreurs autorisées
- **Catégories de mots** : Différents thèmes pour varier les parties
- **Mode triche** : Affichage optionnel du mot à deviner
- **Système d'indices** : Aide ponctuelle pour débloquer les situations
- **Propositions multiples** : Lettres individuelles ou mots entiers

### 🎨 Interface
- **Thèmes personnalisés** : Sélection de thèmes graphiques variés
- **Animations dynamiques** : Affichage évolutif du pendu selon les erreurs
- **Alertes colorées** : Pop-ups animées pour les événements de jeu
- **Transitions fluides** : Animations entre les écrans
- **Boutons interactifs** : Effets visuels (rebond, shake, fade)

### 🔊 Audio & Effets
- **Effets sonores** : Sons pour les actions, victoires et défaites
- **Feedback visuel** : Animations et couleurs pour guider l'utilisateur
- **Personnalisation** : Images de fond et couleurs dynamiques

## 🛠️ Technologies utilisées

- **JavaFX** : Framework d'interface utilisateur
- **Java 17+** : Langage de programmation
- **FXML** : Définition des interfaces
- **CSS** : Stylisation des composants
- **Architecture MVC** : Séparation des responsabilités

## 📋 Prérequis

- Java 17 ou supérieur
- JavaFX SDK
- IDE compatible (IntelliJ IDEA, Eclipse, VS Code)

## 🚀 Installation et lancement

### 1. Cloner le repository
```bash
git clone https://github.com/VOTRE_USERNAME/jeu-pendu-javafx.git
cd jeu-pendu-javafx
```

### 2. Configuration JavaFX
Assurez-vous d'avoir JavaFX configuré dans votre IDE ou ajoutez les modules JavaFX :
```bash
--module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml,javafx.media
```

### 3. Compilation et exécution
```bash
javac -cp ".:lib/*" src/*.java
java -cp ".:lib/*" --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml,javafx.media Main
```

## 🎮 Comment jouer

1. **Lancez l'application** et configurez votre partie
2. **Choisissez la difficulté** (nombre d'erreurs autorisées)
3. **Sélectionnez un thème** et une catégorie de mots
4. **Devinez le mot** en proposant des lettres ou le mot entier
5. **Utilisez les indices** si vous êtes bloqué
6. **Gagnez** en trouvant le mot avant que le pendu soit complet !

## 🏗️ Architecture du projet

```
src/
├── controllers/          # Contrôleurs JavaFX (MVC)
├── models/              # Modèles de données
├── views/               # Fichiers FXML
├── utils/               # Classes utilitaires
├── resources/           # Images, sons, CSS
└── Main.java           # Point d'entrée de l'application
```

## ✨ Fonctionnalités techniques

- **Architecture MVC** : Séparation claire entre logique métier et interface
- **Gestion d'événements** : Interactions complexes (double-clic, raccourcis)
- **Animations FXML** : Transitions cross-fade, blur et effets visuels
- **Système audio** : Intégration d'effets sonores configurables
- **Personnalisation dynamique** : Changement de thème en temps réel
- **Gestion fine des erreurs** : Logique de jeu adaptative

## 🎯 Défis relevés

Ce projet de 2 jours m'a permis de :
- Maîtriser JavaFX et l'architecture MVC
- Implémenter des animations et effets visuels avancés
- Gérer la persistance des données et la configuration
- Créer une interface utilisateur intuitive et moderne
- Intégrer des éléments multimédias (sons, images)

## 🤝 Contribution

Les contributions sont les bienvenues ! N'hésitez pas à :
1. Fork le projet
2. Créer une branche pour votre fonctionnalité
3. Commiter vos changements
4. Pousser vers la branche
5. Ouvrir une Pull Request

## 📝 License

Ce projet est sous licence MIT. Voir le fichier [LICENSE](LICENSE) pour plus de détails.

## 👨‍💻 Auteur

**Mickael Flores**
- LinkedIn : [Mickael Flores](https://www.linkedin.com/in/mickael-flores-2026692a6/)
- Portfolio : [mickaelflores.dev](https://votre-portfolio.com)

---

⭐ N'hésitez pas à mettre une étoile si ce projet vous a plu !
