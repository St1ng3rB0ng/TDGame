package logic.entities;

public abstract class Leader {
    private String name;
    private int bonusMoney;
    private int bonusHp;
    private int bonusAtk;

    public Leader(String name, int bonusMoney, int bonusHp, int bonusAtk) {
        if (name != null) {
            this.name = name;
        }
        this.bonusMoney = bonusMoney;
        this.bonusHp = bonusHp;
        this.bonusAtk = bonusAtk;
    }

    public void applyBonuses(Unit unit) {
        if (unit != null) {
            unit.setHp(unit.getHp() + bonusHp);
            unit.setDamage(unit.getDamage() + bonusAtk);
        }
    }

    public String getName() {
        return name;
    }

    public int getBonusMoney() {
        return bonusMoney;
    }

    public int getBonusHp() {
        return bonusHp;
    }

    public int getBonusAtk() {
        return bonusAtk;
    }

    public void setName(String name) {
        if (name != null) this.name = name;
    }

    public void setBonusMoney(int bonusMoney) {
        this.bonusMoney = bonusMoney;
    }

    public void setBonusHp(int bonusHp) {
        this.bonusHp = bonusHp;
    }

    public void setBonusAtk(int bonusAtk) {
        this.bonusAtk = bonusAtk;
    }
}