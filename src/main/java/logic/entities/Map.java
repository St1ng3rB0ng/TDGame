package logic.entities;

public class Map {
    private int length;
    private int height;
    private boolean[][] occupiedCells;

    public Map(int length, int height) {
        this.length = length;
        this.height = height;
        this.occupiedCells = new boolean[height][length];
    }

    public int getLength() { return length; }
    public int getHeight() { return height; }

    /**
     * НОВИЙ (перевантажений) метод.
     * Дозволяє перевірити, чи валідна позиція,
     * ігноруючи, чи вона "зайнята" (потрібно для малювання).
     */
    public boolean isValidPosition(int x, int y, boolean checkOccupied) {
        // 1. Перевірка, чи координати в межах карти
        if (x < 0 || x >= length || y < 0 || y >= height) {
            return false;
        }
        // 2. Перевірка, чи клітинка зайнята (тільки якщо checkOccupied = true)
        if (checkOccupied && occupiedCells[y][x]) {
            return false;
        }
        // Позиція валідна
        return true;
    }

    /**
     * Старий метод, тепер він просто викликає новий
     * (перевіряє і межі, і зайнятість).
     */
    public boolean isValidPosition(int x, int y) {
        return isValidPosition(x, y, true); // true = перевірити зайнятість
    }

    public void placeUnit(int x, int y) {
        // Використовуємо isValidPosition(x, y, false), щоб дозволити розміщення
        // (isValidPosition(x, y, true) завжди поверне false для вільної клітинки)
        if (isValidPosition(x, y, false)) {
            occupiedCells[y][x] = true;
        }
    }

    public void removeUnit(int x, int y) {
        if (isValidPosition(x, y, false)) { // Перевіряємо лише межі
            occupiedCells[y][x] = false;
        }
    }
}
