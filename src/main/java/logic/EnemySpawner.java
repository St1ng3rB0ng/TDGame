package logic;

import logic.entities.Enemy;
import logic.entities.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Клас для керування спавном ворогів на карті.
 */
public class EnemySpawner {

    private final Map map;
    private final Random random;

    public EnemySpawner(Map map) {
        this.map = map;
        this.random = new Random();
    }

    /**
     * Створює дефолтний набір ворогів для початку гри.
     * @return список створених ворогів
     */
    public List<Enemy> spawnDefaultEnemies() {
        List<Enemy> enemies = new ArrayList<>();

        // Створюємо 3-5 ворогів у випадкових позиціях
        int enemyCount = 3 + random.nextInt(3); // 3-5 ворогів

        for (int i = 0; i < enemyCount; i++) {
            Enemy enemy = spawnEnemyAtRandomPosition("Ворог-" + (i + 1));
            if (enemy != null) {
                enemies.add(enemy);
            }
        }

        return enemies;
    }

    /**
     * Створює ворога у випадковій вільній позиції на правій половині карти.
     * @param name ім'я ворога
     * @return створений ворог або null, якщо не вдалося знайти вільне місце
     */
    public Enemy spawnEnemyAtRandomPosition(String name) {
        int attempts = 0;
        int maxAttempts = 50;

        // Спавнимо ворогів на правій половині карти
        int minX = map.getLength() / 2;
        int maxX = map.getLength();

        while (attempts < maxAttempts) {
            int x = minX + random.nextInt(maxX - minX);
            int y = random.nextInt(map.getHeight());

            if (map.isValidPosition(x, y, true)) {
                Enemy enemy = new Enemy(name, y, x, map);
                map.placeUnit(x, y);
                return enemy;
            }
            attempts++;
        }

        System.out.println("Не вдалося знайти вільне місце для спавну ворога: " + name);
        return null;
    }

    /**
     * Спавнить ворога на конкретній позиції.
     * @param name ім'я ворога
     * @param x координата X
     * @param y координата Y
     * @return створений ворог або null, якщо позиція зайнята
     */
    public Enemy spawnEnemyAt(String name, int x, int y) {
        if (map.isValidPosition(x, y, true)) {
            Enemy enemy = new Enemy(name, y, x, map);
            map.placeUnit(x, y);
            return enemy;
        }
        return null;
    }

    /**
     * Спавнить хвилю ворогів (для можливого розширення гри).
     * @param waveNumber номер хвилі
     * @return список створених ворогів
     */
    public List<Enemy> spawnWave(int waveNumber) {
        List<Enemy> enemies = new ArrayList<>();
        int enemyCount = 2 + waveNumber; // Кількість збільшується з хвилею

        for (int i = 0; i < enemyCount; i++) {
            Enemy enemy = spawnEnemyAtRandomPosition("Ворог-Х" + waveNumber + "-" + (i + 1));
            if (enemy != null) {
                enemies.add(enemy);
            }
        }

        return enemies;
    }
}