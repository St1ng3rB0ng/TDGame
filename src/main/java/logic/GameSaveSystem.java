package logic;

import logic.entities.Unit;
import logic.entities.Enemy;
import logic.entities.Swordsman;
import logic.entities.Archer;
import logic.entities.Mage;
import logic.entities.Map;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Система збереження та завантаження стану гри.
 */
public class GameSaveSystem {

    private static final String SAVE_DIRECTORY = "saves";
    private static final String SAVE_FILE = "game_save.dat";

    /**
     * Дані для збереження стану гри.
     */
    public static class SaveData implements Serializable {
        private static final long serialVersionUID = 1L;

        public int gold;
        public int mapLength;
        public int mapHeight;
        public List<UnitData> playerUnits;
        public List<UnitData> enemies;

        public SaveData() {
            playerUnits = new ArrayList<>();
            enemies = new ArrayList<>();
        }
    }

    /**
     * Дані для збереження інформації про юніта.
     */
    public static class UnitData implements Serializable {
        private static final long serialVersionUID = 1L;

        public String name;
        public String type; // "Swordsman", "Archer", "Mage", "Enemy"
        public int hp;
        public int x;
        public int y;
        public int actions;

        public UnitData(Unit unit, String type) {
            this.name = unit.getName();
            this.type = type;
            this.hp = unit.getHp();
            this.x = unit.getX();
            this.y = unit.getY();
            this.actions = unit.getActions();
        }
    }

    /**
     * Зберігає поточний стан гри у файл.
     * @param gameState стан гри
     * @param map карта
     * @return true, якщо збереження успішне
     */
    public static boolean saveGame(GameState gameState, Map map) {
        try {
            // Створюємо директорію для збережень, якщо її немає
            File saveDir = new File(SAVE_DIRECTORY);
            if (!saveDir.exists()) {
                saveDir.mkdirs();
            }

            SaveData saveData = new SaveData();
            saveData.gold = gameState.getGold();
            saveData.mapLength = map.getLength();
            saveData.mapHeight = map.getHeight();

            // Зберігаємо юнітів гравця
            for (Unit unit : gameState.getPlayerUnits()) {
                String type = getUnitType(unit);
                saveData.playerUnits.add(new UnitData(unit, type));
            }

            // Зберігаємо ворогів
            for (Enemy enemy : gameState.getEnemies()) {
                saveData.enemies.add(new UnitData(enemy, "Enemy"));
            }

            // Записуємо у файл
            File saveFile = new File(SAVE_DIRECTORY, SAVE_FILE);
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(saveFile))) {
                oos.writeObject(saveData);
            }

            System.out.println("Гру збережено успішно: " + saveFile.getAbsolutePath());
            return true;

        } catch (IOException e) {
            System.err.println("Помилка при збереженні гри: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Завантажує стан гри з файлу.
     * @param gameState стан гри для заповнення
     * @param map карта для заповнення
     * @return true, якщо завантаження успішне
     */
    public static boolean loadGame(GameState gameState, Map map) {
        try {
            File saveFile = new File(SAVE_DIRECTORY, SAVE_FILE);

            if (!saveFile.exists()) {
                System.out.println("Файл збереження не знайдено: " + saveFile.getAbsolutePath());
                return false;
            }

            SaveData saveData;
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(saveFile))) {
                saveData = (SaveData) ois.readObject();
            }

            // Перевіряємо розміри карти
            if (saveData.mapLength != map.getLength() || saveData.mapHeight != map.getHeight()) {
                System.err.println("Розміри карти у збереженні не співпадають з поточною картою!");
                return false;
            }

            // Очищуємо поточний стан
            gameState.getPlayerUnits().clear();
            gameState.getEnemies().clear();

            // Відновлюємо золото
            gameState.setGold(saveData.gold);

            // Відновлюємо юнітів гравця
            for (UnitData unitData : saveData.playerUnits) {
                Unit unit = createUnitFromData(unitData, map);
                if (unit != null) {
                    gameState.addUnit(unit);
                    map.placeUnit(unit.getX(), unit.getY());
                }
            }

            // Відновлюємо ворогів
            for (UnitData enemyData : saveData.enemies) {
                Enemy enemy = new Enemy(enemyData.name, enemyData.y, enemyData.x, map);
                enemy.setHp(enemyData.hp);
                enemy.setActions(enemyData.actions);
                gameState.addEnemy(enemy);
                map.placeUnit(enemy.getX(), enemy.getY());
            }

            System.out.println("Гру завантажено успішно!");
            System.out.println("Золото: " + gameState.getGold());
            System.out.println("Юнітів гравця: " + gameState.getPlayerUnits().size());
            System.out.println("Ворогів: " + gameState.getEnemies().size());
            return true;

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Помилка при завантаженні гри: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Перевіряє, чи існує файл збереження.
     */
    public static boolean saveFileExists() {
        File saveFile = new File(SAVE_DIRECTORY, SAVE_FILE);
        return saveFile.exists();
    }

    /**
     * Видаляє файл збереження.
     */
    public static boolean deleteSaveFile() {
        File saveFile = new File(SAVE_DIRECTORY, SAVE_FILE);
        if (saveFile.exists()) {
            return saveFile.delete();
        }
        return false;
    }

    /**
     * Визначає тип юніта за його класом.
     */
    private static String getUnitType(Unit unit) {
        if (unit instanceof Swordsman) return "Swordsman";
        if (unit instanceof Archer) return "Archer";
        if (unit instanceof Mage) return "Mage";
        return "Unknown";
    }

    /**
     * Створює юніта з збережених даних.
     */
    private static Unit createUnitFromData(UnitData data, Map map) {
        Unit unit = null;

        switch (data.type) {
        case "Swordsman":
            unit = new Swordsman(data.name, data.y, data.x, map);
            break;
        case "Archer":
            unit = new Archer(data.name, data.y, data.x, map);
            break;
        case "Mage":
            unit = new Mage(data.name, data.y, data.x, map);
            break;
        default:
            System.err.println("Невідомий тип юніта: " + data.type);
            return null;
        }

        unit.setHp(data.hp);
        unit.setActions(data.actions);
        return unit;
    }
}