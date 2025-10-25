package UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import logic.entities.Unit;

public class PlayerInfoPanel extends VBox {

    private Label unitName;
    private ProgressBar hpBar;
    private Label hpText;
    private Label actionsText;

    public PlayerInfoPanel() {
        setPrefWidth(220); // Фіксована ширина
        setPadding(new Insets(10));
        setSpacing(5);
        getStyleClass().add("info-panel"); // Стиль з CSS

        // Припустимо, у вас є портрет (можна додати ImageView)
        
        unitName = new Label("Юніт не вибраний");
        unitName.getStyleClass().add("label-title");

        hpBar = new ProgressBar(0);
        hpBar.setPrefWidth(200);

        hpText = new Label("HP: 0 / 0");
        actionsText = new Label("Очки Дії: 0");

        getChildren().addAll(unitName, hpBar, hpText, actionsText);
        update(null); // Початковий стан
    }

    /** Оновлює панель на основі вибраного юніта */
    public void update(Unit unit) {
        if (unit != null) {
            unitName.setText(unit.getName());
            
            // TODO: Додайте метод getMaxHp() у ваш клас Unit
            // int maxHp = unit.getMaxHp(); 
            int maxHp = 100; // Тимчасова заглушка
            if(unit.getName().equals("Мечник")) maxHp = 150;
            
            double hpPercent = (double) unit.getHp() / maxHp;
            hpBar.setProgress(hpPercent);
            hpText.setText("HP: " + unit.getHp() + " / " + maxHp);
            
            // Зміна стилю HP бару
            hpBar.getStyleClass().removeAll("warning", "danger");
            if (hpPercent < 0.3) {
                hpBar.getStyleClass().add("danger");
            } else if (hpPercent < 0.7) {
                hpBar.getStyleClass().add("warning");
            }

            actionsText.setText("Очки Дії: " + unit.getActions());
            setVisible(true);
        } else {
            setVisible(false);
        }
    }
}