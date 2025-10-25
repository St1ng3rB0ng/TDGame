package UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import logic.GameState;
import logic.ActionMode;

import java.util.HashMap;
import java.util.Map;

public class ActionPanel extends VBox {

    // --- Константи для стилів ---
    private static final String NORMAL_STYLE = "-fx-font-size: 9px; -fx-padding: 3 6;";
    private static final String ACTIVE_STYLE = NORMAL_STYLE + " -fx-background-color: #4CAF50; -fx-text-fill: white;";
    
    private final GameState gameState;
    private final Label goldLabel;
    // Використовуємо Map для керування кнопками
    private final Map<ActionMode, Button> actionButtons = new HashMap<>();

    public ActionPanel(GameState gameState, Runnable onMove, Runnable onAttack, Runnable onDefend) {
        this.gameState = gameState;
        
        // Налаштування VBox
        setAlignment(Pos.TOP_RIGHT);
        setPadding(new Insets(6));
        setSpacing(4);
        setStyle("-fx-background-color: transparent;");
        setPrefWidth(100);
        setMaxWidth(100);

        Label titleLabel = new Label("Дії");
        titleLabel.setStyle("-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-fill: white;");

        goldLabel = new Label("Золото: " + gameState.getGold());
        goldLabel.setStyle("-fx-font-size: 9px; -fx-text-fill: gold;");

        // Створення кнопок через допоміжний метод
        Button moveButton = createActionButton("Рух", ActionMode.MOVE, onMove);
        Button attackButton = createActionButton("Атака", ActionMode.ATTACK, onAttack);
        Button defendButton = createActionButton("Захист", ActionMode.DEFEND, onDefend);

        getChildren().addAll(titleLabel, goldLabel, moveButton, attackButton, defendButton);
        updateButtonStyles();
    }

    /**
     * Допоміжний метод для створення та налаштування кнопки дії.
     */
    private Button createActionButton(String text, ActionMode mode, Runnable onAction) {
        Button button = new Button(text);
        button.setPrefWidth(85);
        button.setMaxWidth(85);
        
        button.setOnAction(e -> {
            gameState.setCurrentMode(mode);
            updateButtonStyles(); // Оновити стилі всіх кнопок
            if (onAction != null) {
                onAction.run();
            }
        });
        
        actionButtons.put(mode, button); // Додаємо кнопку до Map
        return button;
    }

    /**
     * Оновлює стилі всіх кнопок на основі поточного режиму гри.
     */
    private void updateButtonStyles() {
        actionButtons.forEach((mode, button) -> {
            boolean isActive = (gameState.getCurrentMode() == mode);
            button.setStyle(isActive ? ACTIVE_STYLE : NORMAL_STYLE);
        });
    }

    public void updateGold() {
        goldLabel.setText("Золото: " + gameState.getGold());
    }

    // Методи set...Enabled тепер використовують Map
    public void setMoveEnabled(boolean enabled) {
        actionButtons.get(ActionMode.MOVE).setDisable(!enabled);
    }

    public void setAttackEnabled(boolean enabled) {
        actionButtons.get(ActionMode.ATTACK).setDisable(!enabled);
    }

    public void setDefendEnabled(boolean enabled) {
        actionButtons.get(ActionMode.DEFEND).setDisable(!enabled);
    }
}