import logic.entities.*;

public class Tester {
    public static void main(String[] args) {
        // Створення карти
        Map map = new Map(20, 10);

        // Створення юнітів
        Swordsman unit1 = new Swordsman("Swordsman", 5, 15, map);
        Unit unit2 = new Unit("Enemy", 80, 7, 8, 4, 2, 6,1, 2,2,16, map);

        // Вивід карти
        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getLength(); x++) {
                if (y == unit1.getY() && x == unit1.getX()) {
                    System.out.print(" H ");
                } else if (y == unit2.getY() && x == unit2.getX()) {
                    System.out.print(" E ");
                } else {
                    System.out.print(" . ");
                }
            }
            System.out.println();
        }

        // Приклад використання лідера
        Leader leader = new Leader("King", 50, 20, 5) {
            // Анонімний клас для абстрактного Leader
        };
        leader.applyBonuses(unit1);
        System.out.println("After bonuses: HP = " + unit1.getHp() + ", Strength = " + unit1.getStrength());

        // Приклад руху
        unit1.move(6, 16, map);
        System.out.println("Swordsman new position: Y=" + unit1.getY() + ", X=" + unit1.getX());

        // Приклад атаки
        unit1.attack(unit2);
        System.out.println("Enemy HP after attack: " + unit2.getHp());
    }
}