package UI;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class MenuBarPane extends HBox {

    public MenuBarPane(Runnable onMenu) {
        // Створення та налаштування кнопки
        Button menuBtn = new Button("Меню");
        menuBtn.setFocusTraversable(false);
        menuBtn.getStyleClass().add("menu-btn");
        
        menuBtn.setOnAction(_ -> {
            if (onMenu != null) {
                onMenu.run();
            }
        });

        // Налаштування самого HBox
        setAlignment(Pos.CENTER); // 'this.' прибрано
        setPadding(new Insets(30, 0, 0, 0)); // 'this.' прибрано
        getChildren().add(menuBtn); // 'this.' прибрано
    }
}