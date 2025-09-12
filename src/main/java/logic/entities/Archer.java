package logic.entities;

import logic.actions.Calculation;

public class Archer extends Unit {
    public Archer(String name, int y, int x, Map map) {
        super(name, 110, 12, 18, 8, 3, 7, 7, 2, y, x, map);
    }

    @Override
    public boolean attack(Unit target) {
        if (getActions() > 0 && target != null && Calculation.difference(getX(), target.getX()) <= getDistanceOfAtk() && Calculation.difference(getY(), target.getX()) <= getDistanceOfAtk()) {
            target.setHp(target.getHp() - getDamage() + getAgility());
            setActions(getActions() - 1);
            return true;
        }
        return false;
    }
}
