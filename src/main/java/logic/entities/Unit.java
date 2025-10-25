package logic.entities;

import UI.HeroView;
import javafx.scene.image.Image;
import logic.actions.Movable;
import logic.actions.Attackable;

public class Unit implements Movable, Attackable {
    private String name;
    private int hp;
    // TODO: Додайте поле maxHp для коректної роботи UI
    // private int maxHp; 
    private int strength;
    private int agility;
    private int intelligence;
    private int actions;
    private int damage;
    private int resistance;
    private int distanceOfAtk;
    private int y;
    private int x;
    private Map map;


    public Unit(String name, int hp, int strength, int agility, int intelligence, int actions, int damage, int resistance, int distanceOfAtk, int y, int x, Map map) {
        this.name = name;
        this.hp = hp;
        // this.maxHp = hp; // Ініціалізуйте maxHp тут
        this.strength = strength;
        this.agility = agility;
        this.intelligence = intelligence;
        this.actions = actions;
        this.damage = damage;
        this.resistance = resistance;
        this.distanceOfAtk = distanceOfAtk;
        this.y = y;
        this.x = x;
        this.map = map;
    }

    // --- Геттери ---
    public String getName() { return name; }
    public int getHp() { return hp; }
    // public int getMaxHp() { return maxHp; } // TODO: Додайте це для UI
    public int getStrength() { return strength; }
    public int getAgility() { return agility; }
    public int getIntelligence() { return intelligence; }
    public int getActions() { return actions; }
    public int getDamage() { return damage; }
    public int getResistance() { return resistance; }
    public int getDistanceOfAtk() { return distanceOfAtk; }
    public int getY() { return y; }
    public int getX() { return x; }

    // --- Сеттери ---
    public void setHp(int hp) { this.hp = hp; }
    public void setDamage(int damage) { this.damage = damage; }

    /**
     * Встановлює очки дії, гарантуючи, що вони ніколи не будуть < 0.
     */
    public void setActions(int actions) {
        this.actions = Math.max(0, actions);
    }

    /**
     * Встановлює координати юніта ТА оновлює карту.
     * Повертає true, якщо вдалося, і false, якщо клітинка зайнята.
     */
    public boolean setCoordinate(int newY, int newX, Map map) {
        // Перевіряємо, чи позиція валідна (в межах) І вільна
        if (map.isValidPosition(newX, newY, true)) {
            map.removeUnit(this.x, this.y); // Звільнити стару позицію
            this.y = newY;
            this.x = newX;
            map.placeUnit(this.x, this.y); // Зайняти нову позицію
            return true;
        }
        return false;
    }

    /**
     * Логіка руху. Перевіряє ОД та відстань, потім викликає setCoordinate.
     * @return true, якщо рух відбувся.
     */
    @Override
    public boolean move(int newY, int newX, Map map) {
        if (actions > 0) {
            // Перевірка відстані (сусідня клітинка)
            int dy = Math.abs(newY - y);
            int dx = Math.abs(newX - x);
            boolean isAdjacent = (dx <= 1 && dy <= 1) && (dx + dy > 0);
            
            // setCoordinate поверне true, якщо клітинка вільна
            if (isAdjacent && setCoordinate(newY, newX, map)) {
                actions--; // Витрачаємо ОД
                return true;
            }
        }
        return false; // Недостатньо ОД або клітинка зайнята
    }

    /**
     * Логіка атаки. Перевіряє ОД, відстань та ціль.
     * @return true, якщо атака відбулася.
     */
    @Override
    public boolean attack(Unit target) {
        if (actions > 0 && target != null) {
            // Використовуємо відстань Чебишева (атака "по квадрату")
            int distance = Math.max(
                Math.abs(x - target.getX()),
                Math.abs(y - target.getY())
            );

            if (distance <= distanceOfAtk) {
                // TODO: Ця базова формула має бути в підкласах (Swordsman, Mage...)
                target.setHp(target.getHp() - (damage + strength - target.getResistance()));
                actions--; // Витрачаємо ОД
                return true;
            }
        }
        return false; // Недостатньо ОД або ціль занадто далеко
    }

    /**
     * Логіка захисту. Перевіряє ОД.
     * @return true, якщо захист активовано.
     */
    public boolean defend() {
        if (actions > 0) {
            // TODO: Додайте реальну логіку захисту (напр., +resistance)
            System.out.println(name + " захищається.");
            actions--; // Витрачаємо ОД
            return true;
        }
        return false; // Недостатньо ОД
    }
}
