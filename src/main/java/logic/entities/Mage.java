package logic.entities;

import logic.actions.Calculation;

public class Mage extends Unit {
    public Mage(String name, int y, int x, Map map) {
        super(name, 100, 5, 10, 18, 2, 15, 5, 2, y, x, map);
    }

    @Override
    public boolean attack(Unit target) {
        if (getActions() > 0 && target != null && Calculation.difference(getX(), target.getX()) <= getDistanceOfAtk() && Calculation.difference(getY(), target.getX()) <= getDistanceOfAtk()) {
            target.setHp(target.getHp() - getDamage() + getIntelligence());
            setActions(getActions() - 1);
            return true;
        }
        return false;
    }
}
