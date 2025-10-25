package UI;

import javafx.scene.layout.GridPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import logic.HeroType;

import java.util.HashMap;
import java.util.Map;

public class HeroView extends StackPane {

    private static final Map<HeroType, Image> IMAGE_TYPE = new HashMap<>();
    private static final double IMAGE_WIDTH = 65;
    private static final double IMAGE_HEIGHT = 50;
    private static final double SELECTION_RADIUS = 35;
    private static final double SELECTION_STROKE_WIDTH = 3;
    private static final Color SELECTION_COLOR = Color.LIME;

    private static Image getCharacterImage(HeroType type) {
        // Check if the image is already in the cache
        if (!IMAGE_TYPE.containsKey(type)) {
            // If not in cache, load it and put it into the cache
            Image image = new Image(
                    HeroView.class.getResourceAsStream(type.getImagePath())
            );
            IMAGE_TYPE.put(type, image);
        }
        return IMAGE_TYPE.get(type);
    }

    /**
     * Creates a view for a hero, with an optional selection indicator.
     * @param isSelected true to show the selection circle, false otherwise.
     */
    public HeroView(HeroType heroType, boolean isSelected) {
        Image heroImage = getCharacterImage(heroType);
        // Create the image view using the static image
        ImageView imageView = new ImageView(heroImage);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(IMAGE_WIDTH);
        imageView.setFitHeight(IMAGE_HEIGHT);


        if (isSelected) {
            // Create and add the selection circle (will be layered below the image)
            Circle selectionCircle = new Circle(SELECTION_RADIUS);
            selectionCircle.setFill(Color.TRANSPARENT);
            selectionCircle.setStroke(SELECTION_COLOR);
            selectionCircle.setStrokeWidth(SELECTION_STROKE_WIDTH);
            getChildren().add(selectionCircle);
        }

        // Add the hero image (will be layered on top)
        getChildren().add(imageView);
    }

    /**
     * Convenience constructor for creating a non-selected hero view.
     */

}