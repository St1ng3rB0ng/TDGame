package logic.actions;

import logic.entities.Unit;

public interface Attackable {
    boolean attack(Unit target);
}