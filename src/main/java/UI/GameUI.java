package UI;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.Screen;
import logic.GameState;
import logic.entities.Map;
import logic.entities.Unit;
import logic.entities.Enemy;
import logic.ActionMode;
import logic.EnemySpawner;
import logic.EnemyAI;
import logic.GameSaveSystem;

import java.util.Optional;

public class GameUI extends Application {

    private static final int MAP_HEIGHT = 10;
    private static final int MAP_LENGTH = 10;

    private StackPane root;
    private BorderPane mainLayout;
    private Scene scene;
    private GameFieldPane gameFieldPane;
    private ControlPanel controlPanel;
    private GameState gameState;
    private Map map;
    private EnemySpawner enemySpawner;
    private EnemyAI enemyAI;

    @Override
    public void start(Stage stage) {
        root = new StackPane();
        mainLayout = new BorderPane();
        root.getChildren().add(mainLayout);

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double width = screenBounds.getWidth() * 0.7;
        double height = screenBounds.getHeight() * 0.9;

        if (width < 900) width = 900;
        if (height < 700) height = 700;

        scene = new Scene(root, width, height);

        setupCoreGameLogic();
        renderGameScreen();
        loadResources(stage, scene);

        stage.setScene(scene);
        stage.setTitle("DANGEON MASTER - The Fading Covenant");
        stage.show();

        controlPanel.updateSelectedUnit(gameState.getSelectedUnit());

        // Пропонуємо завантажити гру, якщо є збереження
        if (GameSaveSystem.saveFileExists()) {
            showLoadGameDialog();
        }
    }

    private void setupCoreGameLogic() {
        gameState = new GameState();
        map = new Map(MAP_LENGTH, MAP_HEIGHT);
        enemySpawner = new EnemySpawner(map);
        enemyAI = new EnemyAI(map);

        // Створюємо дефолтних ворогів
        for (Enemy enemy : enemySpawner.spawnDefaultEnemies()) {
            gameState.addEnemy(enemy);
        }

        System.out.println("Гру ініціалізовано. Ворогів створено: " + gameState.getEnemies().size());
    }

    private void renderGameScreen() {
        controlPanel = new ControlPanel(
                gameState,
                () -> System.out.println("Режим руху активовано."),
                () -> System.out.println("Режим атаки активовано."),
                () -> System.out.println("Режим захисту активовано."),
                () -> showGameMenu(),
                () -> showCharacterSelectionDialog()
        );
        controlPanel.setMaxWidth(Double.MAX_VALUE);

        gameFieldPane = new GameFieldPane(map, gameState, controlPanel);
        gameFieldPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        gameFieldPane.setMinSize(0, 0);

        mainLayout.setCenter(gameFieldPane);
        mainLayout.setBottom(controlPanel);

        BorderPane.setMargin(gameFieldPane, new Insets(20, 20, 0, 20));
    }

    private void showCharacterSelectionDialog() {
        CharacterSelectionDialog dialog = new CharacterSelectionDialog(
                gameState,
                map,
                () -> {
                    gameFieldPane.redrawField();
                    controlPanel.updateGold();

                    if (gameState.getCurrentMode() == ActionMode.PLACEMENT) {
                        System.out.println("РЕЖИМ РОЗМІЩЕННЯ: Виберіть вільну клітинку...");
                    }
                }
        );
        dialog.show();
    }

    /**
     * НОВЕ: Показує меню гри з опціями збереження/завантаження.
     */
    private void showGameMenu() {
        Alert menuAlert = new Alert(Alert.AlertType.NONE);
        menuAlert.setTitle("Меню Гри");
        menuAlert.setHeaderText("Виберіть дію:");

        ButtonType saveButton = new ButtonType("Зберегти Гру");
        ButtonType loadButton = new ButtonType("Завантажити Гру");
        ButtonType endTurnButton = new ButtonType("Завершити Хід");
        ButtonType cancelButton = new ButtonType("Скасувати");

        menuAlert.getButtonTypes().setAll(saveButton, loadButton, endTurnButton, cancelButton);

        Optional<ButtonType> result = menuAlert.showAndWait();

        if (result.isPresent()) {
            if (result.get() == saveButton) {
                saveGame();
            } else if (result.get() == loadButton) {
                loadGame();
            } else if (result.get() == endTurnButton) {
                endPlayerTurn();
            }
        }
    }

    /**
     * НОВЕ: Зберігає гру.
     */
    private void saveGame() {
        boolean success = GameSaveSystem.saveGame(gameState, map);

        Alert alert = new Alert(success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(success ? "Успіх" : "Помилка");
        alert.setHeaderText(null);
        alert.setContentText(success ? "Гру успішно збережено!" : "Не вдалося зберегти гру.");
        alert.showAndWait();
    }

    /**
     * НОВЕ: Завантажує гру.
     */
    private void loadGame() {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Завантаження Гри");
        confirmAlert.setHeaderText("Завантажити збережену гру?");
        confirmAlert.setContentText("Поточний прогрес буде втрачено.");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = GameSaveSystem.loadGame(gameState, map);

            if (success) {
                gameFieldPane.updateAllUnits();
                controlPanel.updateGold();
                controlPanel.updateSelectedUnit(null);

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Успіх");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Гру успішно завантажено!");
                successAlert.showAndWait();
            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Помилка");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Не вдалося завантажити гру.");
                errorAlert.showAndWait();
            }
        }
    }

    /**
     * НОВЕ: Пропонує завантажити гру при старті.
     */
    private void showLoadGameDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Знайдено Збереження");
        alert.setHeaderText("Знайдено збережену гру!");
        alert.setContentText("Бажаєте завантажити її?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            loadGame();
        }
    }

    /**
     * НОВЕ: Завершує хід гравця та передає хід ворогам.
     */
    private void endPlayerTurn() {
        System.out.println("=== ХІД ВОРОГІВ ===");

        // Відновлюємо очки дії для юнітів гравця
        for (Unit unit : gameState.getPlayerUnits()) {
            unit.setActions(2);
        }

        // Виконуємо хід ворогів
        enemyAI.processEnemyTurn(gameState.getEnemies(), gameState.getPlayerUnits());

        // Видаляємо мертвих юнітів гравця
        gameState.getPlayerUnits().removeIf(unit -> {
            if (unit.getHp() <= 0) {
                map.removeUnit(unit.getX(), unit.getY());
                System.out.println(unit.getName() + " загинув!");
                return true;
            }
            return false;
        });

        // Оновлюємо UI
        gameFieldPane.updateAllUnits();
        controlPanel.updateSelectedUnit(null);

        // Перевіряємо умови перемоги/поразки
        checkGameState();

        System.out.println("=== ХІД ГРАВЦЯ ===");
    }

    /**
     * НОВЕ: Перевіряє стан гри (перемога/поразка).
     */
    private void checkGameState() {
        if (gameState.getPlayerUnits().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Поразка");
            alert.setHeaderText("Гра Завершена");
            alert.setContentText("Всі ваші юніти загинули. Ви програли!");
            alert.showAndWait();
        } else if (gameState.getEnemies().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Перемога");
            alert.setHeaderText("Гра Завершена");
            alert.setContentText("Ви перемогли всіх ворогів!");
            alert.showAndWait();
        }
    }

    private void loadResources(Stage stage, Scene scene) {
        try {
            String cssResource = getClass().getResource("/dark_fantasy.css").toExternalForm();
            scene.getStylesheets().add(cssResource);
        } catch (Exception e) {
            System.out.println("CSS file 'dark_fantasy.css' not found: " + e.getMessage());
        }

        try {
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));
        } catch (Exception e) {
            System.out.println("Icon not found: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}