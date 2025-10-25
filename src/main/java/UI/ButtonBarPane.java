package UI;

import javafx.scene.layout.VBox;

public class ButtonBarPane extends VBox {
    public ButtonBarPane(MenuBarPane menuBar) {
        super(8, menuBar);
        this.setAlignment(javafx.geometry.Pos.CENTER);
        this.setPadding(new javafx.geometry.Insets(0, 0, 0, 0));
        this.setMinHeight(80);
    }
}
