package logic.entities;

public class Archer extends Unit {
    
    // Старий конструктор
    public Archer(String name, int y, int x, Map map) {
        super(name, 110, 12, 18, 8, 3, 7, 7, 2, y, x, map);
    }

    /**
     * Новий конструктор для "покупки" (без координат).
     */
    public Archer(String name, Map map) {
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
                target.setHp(target.getHp() - (getDamage() + getAgility()));
                setActions(getActions() - 1);
                return true;
            }
        }
        return false;
    }
}
