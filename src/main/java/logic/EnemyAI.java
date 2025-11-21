package logic;

import logic.entities.Enemy;
import logic.entities.Unit;
import logic.entities.Map;
import java.util.List;

/**
 * Примітивна AI для ворогів.
 * Ворог обирає найближчу ціль та намагається атакувати або наблизитися до неї.
 */
public class EnemyAI {

    private final Map map;

    public EnemyAI(Map map) {
        this.map = map;
    }

    /**
     * Виконує хід для всіх ворогів.
     * @param enemies список ворогів
     * @param playerUnits список юнітів гравця
     */
    public void processEnemyTurn(List<Enemy> enemies, List<Unit> playerUnits) {
        if (playerUnits.isEmpty()) {
            return; // Немає цілей
        }

        for (Enemy enemy : enemies) {
            // Відновлюємо очки дії для ворога на початку ходу
            enemy.setActions(2); // Стандартна кількість ОД

            // Виконуємо дії поки є очки
            while (enemy.getActions() > 0) {
                if (!processEnemyAction(enemy, playerUnits)) {
                    break; // Якщо не можемо нічого зробити - виходимо
                }
            }
        }
    }

    /**
     * Виконує одну дію для ворога.
     * @param enemy ворог
     * @param playerUnits список юнітів гравця
     * @return true, якщо дію виконано
     */
    private boolean processEnemyAction(Enemy enemy, List<Unit> playerUnits) {
        // Знаходимо найближчу ціль
        Unit target = findNearestTarget(enemy, playerUnits);

        if (target == null) {
            return false; // Немає цілей
        }

        int distance = calculateDistance(enemy, target);

        // Якщо ціль в межах атаки - атакуємо
        if (distance <= enemy.getDistanceOfAtk()) {
            boolean attacked = enemy.attack(target);
            if (attacked) {
                System.out.println(enemy.getName() + " атакує " + target.getName() + "! HP цілі: " + target.getHp());
                return true;
            }
            return false;
        }

        // Інакше - рухаємося до цілі
        return moveTowardsTarget(enemy, target);
    }

    /**
     * Знаходить найближчого юніта гравця.
     * @param enemy ворог
     * @param playerUnits список юнітів гравця
     * @return найближчий юніт або null
     */
    private Unit findNearestTarget(Enemy enemy, List<Unit> playerUnits) {
        Unit nearest = null;
        int minDistance = Integer.MAX_VALUE;

        for (Unit unit : playerUnits) {
            int distance = calculateDistance(enemy, unit);
            if (distance < minDistance) {
                minDistance = distance;
                nearest = unit;
            }
        }

        return nearest;
    }

    /**
     * Обчислює відстань між двома юнітами (Манхеттенська метрика).
     * @param unit1 перший юніт
     * @param unit2 другий юніт
     * @return відстань
     */
    private int calculateDistance(Unit unit1, Unit unit2) {
        return Math.abs(unit1.getX() - unit2.getX()) + Math.abs(unit1.getY() - unit2.getY());
    }

    /**
     * Рухає ворога на одну клітинку ближче до цілі.
     * @param enemy ворог
     * @param target ціль
     * @return true, якщо рух виконано
     */
    private boolean moveTowardsTarget(Enemy enemy, Unit target) {
        int dx = target.getX() - enemy.getX();
        int dy = target.getY() - enemy.getY();

        // Нормалізуємо напрямок до -1, 0, або 1
        int moveX = Integer.compare(dx, 0);
        int moveY = Integer.compare(dy, 0);

        int newX = enemy.getX() + moveX;
        int newY = enemy.getY() + moveY;

        // Спочатку пробуємо рух по діагоналі
        if (enemy.move(newY, newX, map)) {
            System.out.println(enemy.getName() + " рухається до (" + newX + ", " + newY + ")");
            return true;
        }

        // Якщо діагональ неможлива, пробуємо рух по X
        if (moveX != 0) {
            newX = enemy.getX() + moveX;
            newY = enemy.getY();
            if (enemy.move(newY, newX, map)) {
                System.out.println(enemy.getName() + " рухається до (" + newX + ", " + newY + ")");
                return true;
            }
        }

        // Якщо X неможливий, пробуємо рух по Y
        if (moveY != 0) {
            newX = enemy.getX();
            newY = enemy.getY() + moveY;
            if (enemy.move(newY, newX, map)) {
                System.out.println(enemy.getName() + " рухається до (" + newX + ", " + newY + ")");
                return true;
            }
        }

        // Не можемо рухатися - шлях заблоковано
        System.out.println(enemy.getName() + " не може рухатися (шлях заблоковано)");
        return false;
    }

    /**
     * Перевіряє, чи може ворог атакувати будь-якого юніта гравця.
     * @param enemy ворог
     * @param playerUnits список юнітів гравця
     * @return true, якщо є ціль в межах атаки
     */
    public boolean canAttackAnyTarget(Enemy enemy, List<Unit> playerUnits) {
        for (Unit unit : playerUnits) {
            int distance = calculateDistance(enemy, unit);
            if (distance <= enemy.getDistanceOfAtk()) {
                return true;
            }
        }
        return false;
    }
}