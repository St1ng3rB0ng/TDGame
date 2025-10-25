package logic;

import UI.HeroView;
import logic.entities.Unit;
import logic.entities.Enemy;
import java.util.ArrayList;
import java.util.List;
import UI.CharacterSelectionDialog;

public class GameState {
    private int gold;
    private List<Unit> playerUnits;
    private List<Enemy> enemies;
    private ActionMode currentMode;
    public CharacterSelectionDialog charSelDialog;
    private Unit selectedUnit;

    // ДОДАНО: Поле для юніта, що очікує розміщення
    private Unit unitPendingPlacement;


    public GameState() {
        this.gold = 500;
        this.playerUnits = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.currentMode = ActionMode.NONE;
        this.unitPendingPlacement = null; // Ініціалізація
    }

    public int getGold() { return gold; }
    
    public void setGold(int gold) { this.gold = gold; }
    
    public boolean spendGold(int amount) {
        if (gold >= amount) {
            gold -= amount;
            return true;
        }
        return false;
    }

    public void addUnit(Unit unit) {
        playerUnits.add(unit);
    }
    
    public void addEnemy(Enemy enemy) {
        enemies.add(enemy);
    }
    
    public List<Enemy> getEnemies() { return enemies; }

    public List<Unit> getPlayerUnits() { return playerUnits; }

    public Unit getSelectedUnit() { return selectedUnit; }
    
    public void setSelectedUnit(Unit unit) { 
        this.selectedUnit = unit;
        // Видалено System.out.println для чистоти логіки
    }

    public ActionMode getCurrentMode() { return currentMode; }
    
    public void setCurrentMode(ActionMode mode) { this.currentMode = mode; }
    
    public Enemy getEnemyAt(int x, int y) {
        for (Enemy enemy : enemies) {
            if (enemy.getX() == x && enemy.getY() == y) {
                return enemy;
            }
        }
        return null;
    }

    // --- Додані методи для розміщення ---
    
    public Unit getUnitPendingPlacement() {
        return unitPendingPlacement;
    }

    public void setUnitPendingPlacement(Unit unit) {
        this.unitPendingPlacement = unit;
    }
}