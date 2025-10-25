package UI;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.Priority; // Потрібно для setupGridConstraints
import java.util.*;

import logic.HeroType;
import logic.entities.Map;
import logic.entities.Unit;
import logic.entities.Enemy;
import logic.GameState;
import logic.ActionMode;
import UI.HeroView;
import logic.entities.Swordsman; // <-- Додайте
import logic.entities.Archer;   // <-- Додайте
import logic.entities.Mage;      // <-- Додайте

public class GameFieldPane extends GridPane {

    private List<List<GameCellPane>> cellPanes;
    private Map map;
    private GameState gameState;
    private ControlPanel controlPanel;

    public GameFieldPane(Map map, GameState gameState, ControlPanel controlPanel) {
        this.map = map;
        this.gameState = gameState;
        this.controlPanel = controlPanel;
        buildField();
    }

    /**
     * Створює поле та клітинки.
     */
    private void buildField() {
        // setAlignment(javafx.geometry.Pos.CENTER); // Видалено
        // setPrefSize(500, 500); // Видалено

        setupGridConstraints();

        cellPanes = new ArrayList<>();
        for (int y = 0; y < map.getHeight(); y++) {
            List<GameCellPane> row = new ArrayList<>();
            for (int x = 0; x < map.getLength(); x++) {
                final int cellY = y;
                final int cellX = x;

                GameCellPane cellPane = new GameCellPane(cellX, cellY, gameState, controlPanel, () -> handleCellClick(cellX, cellY));

                add(cellPane, x, y);
                row.add(cellPane);
            }
            cellPanes.add(row);
        }
        updateAllUnits();
    }

    /**
     * Налаштовує колонки та рядки, щоб вони розтягувалися.
     */
    private void setupGridConstraints() {
        getColumnConstraints().clear();
        getRowConstraints().clear();

        double colPercent = 100.0 / map.getLength();
        for (int i = 0; i < map.getLength(); i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(colPercent);
            col.setHgrow(Priority.ALWAYS);
            col.setFillWidth(true);
            getColumnConstraints().add(col);
        }

        double rowPercent = 100.0 / map.getHeight();
        for (int i = 0; i < map.getHeight(); i++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(rowPercent);
            row.setVgrow(Priority.ALWAYS);
            row.setFillHeight(true);
            getRowConstraints().add(row);
        }
    }

    /**
     * Головний обробник кліків по клітинках.
     */
    private void handleCellClick(int cellX, int cellY) {
        ActionMode mode = gameState.getCurrentMode();

        // 1. РЕЖИМ РОЗМІЩЕННЯ
        if (mode == ActionMode.PLACEMENT) {
            handlePlacement(cellX, cellY);
            updateAllUI();
            return;
        }

        // 2. Спроба вибрати юніта
        for (Unit unit : gameState.getPlayerUnits()) {
            if (unit.getX() == cellX && unit.getY() == cellY) {
                gameState.setSelectedUnit(unit);
                updateAllUI();
                return;
            }
        }

        // 3. Спроба зняти виділення
        Unit selectedUnit = gameState.getSelectedUnit();
        if (selectedUnit != null && selectedUnit.getX() == cellX && selectedUnit.getY() == cellY) {
             gameState.setSelectedUnit(null);
             updateAllUI();
             return;
        }

        // 4. Виконання дії
        if (selectedUnit == null) {
            System.out.println("Юніт не вибраний");
            return;
        }
        if (mode == ActionMode.NONE) {
            System.out.println("Дія не вибрана");
            return;
        }

        // Обчислення відстані (тільки для атаки)
        int dy = Math.abs(cellY - selectedUnit.getY());
        int dx = Math.abs(cellX - selectedUnit.getX());
        // 'isAdjacent' тут використовується лише для АТАКИ (оскільки move сам перевіряє відстань)
        boolean isAdjacent = (dx <= 1 && dy <= 1) && (dx + dy > 0);

        boolean actionTaken = false;
        switch (mode) {
            case MOVE:
                // Передаємо цільові координати, 'isAdjacent' тут не потрібен
                actionTaken = handleMove(selectedUnit, cellX, cellY);
                break;
            case ATTACK:
                actionTaken = handleAttack(selectedUnit, cellX, cellY, isAdjacent);
                break;
            case DEFEND:
                actionTaken = handleDefend(selectedUnit);
                break;
            default: break;
        }

        if (actionTaken) {
            gameState.setCurrentMode(ActionMode.NONE);
            updateAllUI();
        }
    }

    /**
     * Оновлює всі елементи UI (панель та поле).
     */
    private void updateAllUI() {
        controlPanel.updateSelectedUnit(gameState.getSelectedUnit());
        controlPanel.updateGold();
        controlPanel.updateButtonStyles();
        updateAllUnits(); // Перемальовує поле
    }

    /**
     * Обробка розміщення нового юніта.
     */
    private void handlePlacement(int cellX, int cellY) {
        Unit unitToPlace = gameState.getUnitPendingPlacement();

        // map.isValidPosition(cellX, cellY) -> перевіряє і межі, і зайнятість
        if (unitToPlace != null && map.isValidPosition(cellX, cellY)) {

            unitToPlace.setCoordinate(cellY, cellX, map);
            gameState.addUnit(unitToPlace);
            gameState.setUnitPendingPlacement(null);
            gameState.setCurrentMode(ActionMode.NONE);

            System.out.println(unitToPlace.getName() + " розміщено на (" + cellX + ", " + cellY + ")");

        } else if (unitToPlace == null) {
            gameState.setCurrentMode(ActionMode.NONE);
        } else {
            System.out.println("Неможливо розмістити персонажа. Клітинка зайнята.");
        }
    }

    // --- Методи дій (викликають логіку Unit) ---

    private boolean handleMove(Unit unit, int x, int y) {
        // unit.move() сам перевірить ОД, відстань та вільність клітинки
        if (unit.move(y, x, map)) {
            System.out.println("Персонаж переміщено на (" + x + ", " + y + ")");
            return true;
        } else {
            System.out.println("Неможливо перемістити персонажа сюди (клітинка зайнята, далеко, або немає ОД)");
            return false;
        }
    }

    private boolean handleAttack(Unit unit, int x, int y, boolean isAdjacent) {
        // 'isAdjacent' - це швидка перевірка UI, щоб не шукати ворога даремно
        if (!isAdjacent) {
            System.out.println("Ціль занадто далеко (UI check)");
            return false;
        }

        Enemy enemy = gameState.getEnemyAt(x, y);
        if (enemy != null) {
            // unit.attack() сам перевірить ОД та точну відстань атаки
            if (unit.attack(enemy)) {
                System.out.println("Атака на ворога! HP ворога: " + enemy.getHp());
                if (enemy.getHp() <= 0) {
                    gameState.getEnemies().remove(enemy);
                    map.removeUnit(enemy.getX(), enemy.getY());
                    System.out.println("Ворога знищено!");
                }
                return true;
            } else {
                System.out.println("Атака не вдалася (ціль занадто далеко або немає ОД)");
                return false;
            }
        } else {
            System.out.println("На цій клітинці немає ворога");
            return false;
        }
    }

    private boolean handleDefend(Unit unit) {
        // unit.defend() сам перевірить ОД
        if (unit.defend()) {
            return true;
        } else {
            System.out.println(unit.getName() + " не може захищатися (немає ОД)");
            return false;
        }
    }

    /**
     * Повністю перемальовує поле.
     */
    public void updateAllUnits() {
        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getLength(); x++) {
                GameCellPane cell = cellPanes.get(y).get(x);
                cell.getChildren().clear();
                cell.setHighlight(null);
            }
        }

        Unit selectedUnit = gameState.getSelectedUnit(); // <-- Додано оголошення

        // Малюємо юнітів гравця
        for (Unit unit : gameState.getPlayerUnits()) {
            // Використовуємо 'false', щоб малювати, навіть якщо клітинка "зайнята"
            if (map.isValidPosition(unit.getX(), unit.getY(), false)) {

                HeroType heroType = getHeroType(unit); // Новий допоміжний метод
                boolean isSelected = (unit == selectedUnit);

                cellPanes.get(unit.getY()).get(unit.getX()).getChildren().add(
                        new HeroView(heroType, isSelected) // Передаємо HeroType та boolean
                );
            }
        }

        // Малюємо ворогів
        for (Enemy enemy : gameState.getEnemies()) {
            if (map.isValidPosition(enemy.getX(), enemy.getY(), false)) {
                cellPanes.get(enemy.getY()).get(enemy.getX()).getChildren().add(new EnemyView());
            }
        }

        // TODO: Додати логіку підсвітки клітинок для руху/атаки
        // showActionHighlights(selectedUnit);
    }

    private HeroType getHeroType(Unit unit) {
        if (unit instanceof Swordsman) {
            return HeroType.SWORDSMAN;
        } else if (unit instanceof Archer) {
            return HeroType.ARCHER;
        } else if (unit instanceof Mage) {
            return HeroType.MAGE;
        }
        // Повертаємо Swordsman або інший дефолтний, якщо тип невідомий
        return HeroType.SWORDSMAN;
    }

    public void redrawField() {
        updateAllUnits();
    }
}