package UI;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class EnemyView extends StackPane {

    // --- Константи ---

    // Шлях до зображення
    private static final String IMAGE_PATH = "/enemy.jfif"; // Припустимий шлях

    // Завантажуємо зображення ОДИН РАЗ і зберігаємо його.
    private static final Image ENEMY_IMAGE = new Image(
        EnemyView.class.getResourceAsStream(IMAGE_PATH)
    );

    // Розміри
    private static final double IMAGE_WIDTH = 65;
    private static final double IMAGE_HEIGHT = 50;

    /**
     * Конструктор для відображення ворога.
     */
    public EnemyView() {
        // Створюємо ImageView, використовуючи вже завантажене static зображення
        ImageView imageView = new ImageView(ENEMY_IMAGE);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(IMAGE_WIDTH);
        imageView.setFitHeight(IMAGE_HEIGHT);
        
        getChildren().add(imageView);
    }
}