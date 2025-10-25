package UI;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.Screen;
import logic.GameState;
import logic.entities.Map;
import logic.entities.Unit;
import logic.entities.Enemy;
import logic.ActionMode;

public class GameUI extends Application {

    // (Константи залишаються)
    private static final int MAP_HEIGHT = 10;
    private static final int MAP_LENGTH = 10;

    // --- Поля Класу ---
    private StackPane root; // Головний контейнер (для фону)
    private BorderPane mainLayout; // Новий головний макет
    private Scene scene;
    private GameFieldPane gameFieldPane;
    private ControlPanel controlPanel; // Нова панель
    private GameState gameState;
    private Map map;

    @Override
    public void start(Stage stage) {
        root = new StackPane();
        mainLayout = new BorderPane(); // Використовуємо BorderPane
        root.getChildren().add(mainLayout); // mainLayout поверх фону
        
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double width = screenBounds.getWidth() * 0.7;
        double height = screenBounds.getHeight() * 0.9;
        
        // Ініціалізуємо розміри
        if (width < 900) width = 900;
        if (height < 700) height = 700;

        scene = new Scene(root, width, height);

        setupCoreGameLogic();
        renderGameScreen(); // Викликаємо оновлений метод
        loadResources(stage, scene); // Завантажуємо новий CSS

        stage.setScene(scene);
        stage.setTitle("DANGEON MASTER - The Fading Covenant");
        // ... (решта налаштувань stage)
        stage.show();
        
        // Ініціалізуємо стан UI
        controlPanel.updateSelectedUnit(gameState.getSelectedUnit());
    }

    // (setupCoreGameLogic залишається без змін)
    private void setupCoreGameLogic() {
        gameState = new GameState();
        map = new Map(MAP_LENGTH, MAP_HEIGHT);
        // ... (створення героя та ворогів)
    }

    /**
     * ОНОВЛЕНО: Використовує BorderPane та ControlPanel
     */
private void renderGameScreen() {
        // 1. Налаштування фону

        // 2. Створюємо ControlPanel (нижня панель)
        controlPanel = new ControlPanel(
            gameState,
            () -> System.out.println("Режим руху активовано."),
            () -> System.out.println("Режим атаки активовано."),
            () -> System.out.println("Режим захисту активовано."),
            () -> System.out.println("Меню натиснуто"), 
            () -> showCharacterSelectionDialog() 
        );
        controlPanel.setMaxWidth(Double.MAX_VALUE);


        // 3. Створюємо Ігрове поле
        gameFieldPane = new GameFieldPane(map, gameState, controlPanel);
        
        // Дозволяємо полю рости до нескінченності
        gameFieldPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        // --- ОСЬ ВИРІШЕННЯ ---
        // Забороняємо полю схлопуватися. 
        // minSize(0, 0) змушує його слухатися батьківського контейнера.
        gameFieldPane.setMinSize(0, 0); 

        
        // 4. Розміщуємо елементи в BorderPane
        mainLayout.setCenter(gameFieldPane);
        mainLayout.setBottom(controlPanel);

        // Додаємо відступи
        BorderPane.setMargin(gameFieldPane, new Insets(20, 20, 0, 20)); 
    }
    /**
     * ОНОВЛЕНО: Викликає методи оновлення ControlPanel
     */
    private void showCharacterSelectionDialog() {
        CharacterSelectionDialog dialog = new CharacterSelectionDialog(
            gameState,
            map,
            () -> {
                // Цей код виконується ПІСЛЯ натискання "OK"
                gameFieldPane.redrawField(); 
                controlPanel.updateGold(); // Оновлюємо золото на панелі
                
                if (gameState.getCurrentMode() == ActionMode.PLACEMENT) {
                    System.out.println("РЕЖИМ РОЗМІЩЕННЯ: Виберіть вільну клітинку...");
                }
            }
        );
        dialog.show();
    }

    /**
     * ОНОВЛЕНО: Завантажує новий 'dark_fantasy.css'
     */
    private void loadResources(Stage stage, Scene scene) {
        try {
            // Завантажуємо новий стиль
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