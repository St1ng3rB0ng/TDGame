// File: logic/entities/CharacterType.java

package logic;

public enum HeroType {
    SWORDSMAN("swordsman.png"), // Example image file for the Knight
    ARCHER("archer.png"), // Example image file for the Archer
    MAGE("mage.png"); // Example image file for the Wizard

    private final String imagePath;

    HeroType(String imagePath) {
        // Assuming images are in a resources folder relative to the root package,
        // e.g., src/main/resources/knight.png
        this.imagePath = "/" + imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }
}