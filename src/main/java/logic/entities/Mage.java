package logic.entities;

public class Mage extends Unit {

    // Старий конструктор
    public Mage(String name, int y, int x, Map map) {
        super(name, 100, 5, 10, 18, 2, 15, 5, 2, y, x, map);
    }

    /**
     * Новий конструктор для "покупки" (без координат).
     */
    public Mage(String name, Map map) {
        this(name, -1, -1, map);
    }

    @Override
    public boolean attack(Unit target) {
        if (getActions() > 0 && target != null) {
            int distance = Math.max(
                Math.abs(getX() - target.getX()),
                Math.abs(getY() - target.getY()) // Виправлено з target.getX()
            );

            if (distance <= getDistanceOfAtk()) {
                target.setHp(target.getHp() - (getDamage() + getIntelligence()));
                setActions(getActions() - 1);
                return true;
            }
        }
        return false;
    }
}
