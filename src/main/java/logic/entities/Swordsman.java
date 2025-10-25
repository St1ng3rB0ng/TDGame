package logic.entities;

public class Swordsman extends Unit {
    
    // Старий конструктор (для початкових юнітів)
    public Swordsman(String name, int y, int x, Map map) {
        super(name, 150, 20, 10, 5, 2, 10, 15, 1, y, x, map);
    }

    /**
     * Новий конструктор для "покупки" (без координат).
     */
    public Swordsman(String name, Map map) {
        // Викликає головний конструктор з координатами "в лімбі"
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
                target.setHp(target.getHp() - (getDamage() + getStrength()));
                setActions(getActions() - 1);
                return true;
            }
        }
        return false;
    }
}
