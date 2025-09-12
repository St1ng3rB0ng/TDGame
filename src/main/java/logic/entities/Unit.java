package logic.entities;

import logic.actions.*;
import logic.actions.Calculation;
public class Unit implements Movable, Attackable {
    private int y; // position Height
    private int x; // position Length
    private String name;
    private int strength;
    private int agility;
    private int intelligence;
    private int hp;
    private int actions;
    private int damage;
    private int resistance;
    private int distanceOfAtk;

    public Unit(String name, int hp, int strength, int agility, int intelligence, int actions, int damage,int resistance,int distanceOfAtk, int y, int x, Map map) {
        setName(name);
        setHp(hp);
        setStrength(strength);
        setAgility(agility);
        setIntelligence(intelligence);
        setActions(actions);
        setDamage(damage);
        setResistance(resistance);
        setDistanceOfAtk(distanceOfAtk);
        setCoordinate(y, x, map);
    }

    public void setCoordinate(int y, int x, Map map) {
        if (map == null) return;
        map.removeUnit(this.x, this.y);
        if (map.isValidPosition(x, y)) {
            this.y = y;
            this.x = x;
            map.placeUnit(x, y);
        }
    }

    public void setDamage(int damage) {
        if (damage >= 0) this.damage = damage;
    }

    public void setActions(int actions) {
        if (actions >= 0) this.actions = actions;
    }

    public void setHp(int hp) {
        if (hp >= 0) this.hp = hp;
    }

    public void setAgility(int agility) {
        if (agility >= 0) this.agility = agility;
    }

    public void setIntelligence(int intelligence) {
        if (intelligence >= 0) this.intelligence = intelligence;
    }

    public void setName(String name) {
        if (name != null) this.name = name;
    }

    public void setStrength(int strength) {
        if (strength >= 0) this.strength = strength;
    }

    public void setResistance(int resistance) {if (resistance >= 0) this.resistance = resistance;}

    public void setDistanceOfAtk(int distanceOfAtk) {if(distanceOfAtk>0)this.distanceOfAtk = distanceOfAtk;}

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getName() {
        return name;
    }

    public int getActions() {
        return actions;
    }

    public int getDamage() {
        return damage;
    }

    public int getStrength() {
        return strength;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public int getHp() {
        return hp;
    }

    public int getAgility() {
        return agility;
    }

    public int getResistance() { return resistance; }

    public int getDistanceOfAtk() {return distanceOfAtk;}

    public boolean move(int newY, int newX, Map map) {
        if (actions > 0 && map.isValidPosition(newX, newY) && Calculation.difference(getX(),newX)<2 &&Calculation.difference(getY(),newY)<2) {
            setCoordinate(newY, newX, map);
            actions--;
            return true;
        }
        return false;
    }

    public boolean attack(Unit target) { //in development...
        if (actions > 0 && target != null) {
            target.setHp(target.getHp() - damage);
            actions--;
            return true;
        }
        return false;
    }
}