package UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import logic.entities.Unit; // Використовуємо Unit, оскільки Enemy наслідує його

public class TargetInfoPanel extends VBox {

    private Label unitName;
    private Label hpText;

    public TargetInfoPanel() {
        setPrefWidth(220); // Фіксована ширина
        setPadding(new Insets(10));
        setSpacing(5);
        setAlignment(Pos.CENTER_RIGHT);
        getStyleClass().add("info-panel");

        unitName = new Label("Ціль");
        unitName.getStyleClass().add("label-title");

        hpText = new Label("HP: 0 / 0");

        getChildren().addAll(unitName, hpText);
        setVisible(false); // Сховано за замовчуванням
    }

    /** Оновлює панель на основі цілі */
    public void update(Unit target) {
        if (target != null) {
            unitName.setText(target.getName());
            
            // TODO: Додайте метод getMaxHp() у ваш клас Unit
            int maxHp = 100; // Тимчасова заглушка
            
            hpText.setText("HP: " + target.getHp() + " / " + maxHp);
            setVisible(true);
        } else {
            setVisible(false);
        }
    }
}