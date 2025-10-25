package UI;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import logic.GameState;
import logic.entities.Unit;

/**
 * Клітинка, яка тепер знає про ControlPanel для показу інфо про ціль.
 */
public class GameCellPane extends StackPane {

    private int x, y;
    private GameState gameState;
    private ControlPanel controlPanel; // Посилання на головну панель

    public GameCellPane(int x, int y, GameState gameState, ControlPanel controlPanel, Runnable onClick) {
        this.x = x;
        this.y = y;
        this.gameState = gameState;
        this.controlPanel = controlPanel;

        setAlignment(Pos.CENTER);
        getStyleClass().add("game-cell"); // Стиль з CSS

        // Обробник кліку
        setOnMouseClicked(e -> onClick.run());

        // Обробник наведення миші
        setOnMouseEntered(e -> {
            Unit target = gameState.getEnemyAt(x, y);
            // TODO: Також перевіряти, чи є тут 'Unit' гравця
            if (target == null) {
                for(Unit unit : gameState.getPlayerUnits()) {
                    if (unit.getX() == x && unit.getY() == y) {
                        target = unit;
                        break;
                    }
                }
            }
            controlPanel.updateTargetUnit(target); // Оновлюємо панель цілі
        });

        // Обробник виходу миші
        setOnMouseExited(e -> {
            controlPanel.updateTargetUnit(null); // Ховаємо панель
        });
    }

    // (Можна додати методи для оновлення стилів: setMoveHighlight, setAttackHighlight)
    public void setHighlight(String styleClass) {
        // Спершу очистити старі
        getStyleClass().removeAll("game-cell-move", "game-cell-attack");
        if (styleClass != null) {
            getStyleClass().add(styleClass);
        }
    }
}