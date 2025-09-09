package logic.actions;

import logic.entities.Map;

public interface Movable {
    boolean move(int newY, int newX, Map map);
}
