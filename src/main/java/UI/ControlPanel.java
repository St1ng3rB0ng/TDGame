package UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import logic.GameState;
import logic.ActionMode;
import logic.entities.Unit;
import java.util.HashMap;
import java.util.Map;

/**
 * Нова "Панель Долі", яка замінює ActionPanel, MenuBarPane та ButtonBarPane.
 */
public class ControlPanel extends BorderPane {
    
    private GameState gameState;
    
    // --- UI Секції ---
    private PlayerInfoPanel playerInfoPanel;
    private TargetInfoPanel targetInfoPanel;
    private Label goldLabel;
    
    // --- Кнопки ---
    private Map<ActionMode, Button> actionButtons = new HashMap<>();

    // --- Обробники (Callbacks) ---
    private Runnable onMove;
    private Runnable onAttack;
    private Runnable onDefend;
    private Runnable onMenu;
    private Runnable onBuy;

    public ControlPanel(GameState gameState, Runnable onMove, Runnable onAttack, Runnable onDefend, Runnable onMenu, Runnable onBuy) {
        this.gameState = gameState;
        this.onMove = onMove;
        this.onAttack = onAttack;
        this.onDefend = onDefend;
        this.onMenu = onMenu;
        this.onBuy = onBuy;

        // Налаштування самої панелі
        getStyleClass().add("control-panel");
        setPadding(new Insets(10, 20, 10, 20));
        setMinHeight(120); // Задаємо мінімальну висоту

        // --- ЛІВА СЕКЦЯ (Інфо про гравця) ---
        playerInfoPanel = new PlayerInfoPanel();
        setLeft(playerInfoPanel);
        BorderPane.setAlignment(playerInfoPanel, Pos.CENTER_LEFT);

        // --- ЦЕНТРАЛЬНА СЕКЦІЯ (Кнопки) ---
        VBox centerBox = createCenterBox();
        setCenter(centerBox);

        // --- ПРАВА СЕКЦІЯ (Інфо про ціль) ---
        targetInfoPanel = new TargetInfoPanel();
        setRight(targetInfoPanel);
        BorderPane.setAlignment(targetInfoPanel, Pos.CENTER_RIGHT);
    }

    private VBox createCenterBox() {
        VBox centerBox = new VBox(10);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(0, 20, 0, 20));

        // --- Верхній ряд (Дії) ---
        HBox actionButtonsBox = new HBox(10);
        actionButtonsBox.setAlignment(Pos.CENTER);
        
        Button moveBtn = createActionButton("Рух", ActionMode.MOVE, onMove);
        Button attackBtn = createActionButton("Атака", ActionMode.ATTACK, onAttack);
        Button defendBtn = createActionButton("Захист", ActionMode.DEFEND, onDefend);
        
        actionButtonsBox.getChildren().addAll(moveBtn, attackBtn, defendBtn);

        // --- Нижній ряд (Гра) ---
        HBox gameButtonsBox = new HBox(20);
        gameButtonsBox.setAlignment(Pos.CENTER);
        
        Button menuButton = new Button("Меню");
        menuButton.setOnAction(e -> onMenu.run());
        
        goldLabel = new Label("Золото: " + gameState.getGold());
        goldLabel.getStyleClass().add("gold-label");

        Button buyButton = new Button("Магазин");
        buyButton.setOnAction(e -> onBuy.run());
        
        gameButtonsBox.getChildren().addAll(menuButton, goldLabel, buyButton);
        
        centerBox.getChildren().addAll(actionButtonsBox, gameButtonsBox);
        return centerBox;
    }

    private Button createActionButton(String text, ActionMode mode, Runnable onAction) {
        Button button = new Button(text);
        button.setPrefWidth(90);
        
        button.setOnAction(e -> {
            // Скидаємо режим, якщо натиснули ту саму кнопку
            if (gameState.getCurrentMode() == mode) {
                gameState.setCurrentMode(ActionMode.NONE);
            } else {
                gameState.setCurrentMode(mode);
            }
            updateButtonStyles();
            if (onAction != null) onAction.run();
        });
        
        actionButtons.put(mode, button);
        return button;
    }

    // --- Публічні методи для оновлення UI ---

    public void updateButtonStyles() {
        actionButtons.forEach((mode, button) -> {
            boolean isActive = (gameState.getCurrentMode() == mode);
            button.getStyleClass().remove("button-active"); // Скидаємо
            if (isActive) {
                button.getStyleClass().add("button-active"); // Додаємо
            }
        });
    }

    public void updateGold() {
        goldLabel.setText("Золото: " + gameState.getGold());
    }

    public void updateSelectedUnit(Unit unit) {
        playerInfoPanel.update(unit);
        
        boolean disabled = (unit == null);
        actionButtons.values().forEach(button -> button.setDisable(disabled));
        
        if (disabled) {
            gameState.setCurrentMode(ActionMode.NONE);
            updateButtonStyles();
        }
    }
    
    public void updateTargetUnit(Unit target) {
        targetInfoPanel.update(target);
    }
}