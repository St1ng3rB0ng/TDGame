package logic.entities;

public class Enemy extends Unit {
    public Enemy(String name, int y, int x, Map map) {
        super(name, 100, 15, 8, 5, 2, 12, 8, 1, y, x, map);
    }

    @Override
    public boolean attack(Unit target) {
        if (getActions() > 0 && target != null) {
            int distance = Math.max(
                Math.abs(getX() - target.getX()),
                Math.abs(getY() - target.getY())
            );
            if (distance <= getDistanceOfAtk()) {
                target.setHp(target.getHp() - (getDamage() + getStrength() - target.getResistance()));
                setActions(getActions() - 1);
                return true;
            }
        }
        return false;
    }
}
