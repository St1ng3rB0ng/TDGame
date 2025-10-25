package UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.GameState;
import logic.entities.Map;
import logic.entities.Swordsman;
import logic.entities.Archer;
import logic.entities.Mage;
import logic.entities.Unit;
import logic.ActionMode;

import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class CharacterSelectionDialog {

    // --- Константи для CSS ---
    private static final String STYLE_BG_DIALOG = "-fx-background-color: #2c2c2c;";
    private static final String STYLE_BG_CARD = "-fx-background-color: #3c3c3c; -fx-border-color: #555; -fx-border-width: 2; -fx-background-radius: 10; -fx-border-radius: 10;";
    private static final String STYLE_BG_CARD_SELECTED = "-fx-background-color: #4CAF50; -fx-border-color: #2E7D32; -fx-border-width: 3; -fx-background-radius: 10; -fx-border-radius: 10;";
    private static final String STYLE_TEXT_TITLE = "-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;";
    private static final String STYLE_TEXT_GOLD = "-fx-font-size: 18px; -fx-text-fill: gold;";
    private static final String STYLE_TEXT_CARD_NAME = "-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;";
    private static final String STYLE_TEXT_CARD_STATS = "-fx-font-size: 12px; -fx-text-fill: #ccc; -fx-text-alignment: center;";
    private static final String STYLE_BUTTON_OK = "-fx-font-size: 16px; -fx-padding: 10 30; -fx-background-color: #4CAF50; -fx-text-fill: white;";
    private static final String STYLE_BUTTON_CLOSE = "-fx-font-size: 14px; -fx-padding: 10 20;";
    private static final String STYLE_BUTTON_SELECT = "-fx-font-size: 12px; -fx-padding: 8 15;";

    /**
     * Внутрішній record для зберігання шаблону персонажа.
     */
    public record CharacterPrototype(
            String name,
            String stats,
            int cost,
            Supplier<Unit> creator
    ) {}

    /**
     * Внутрішній record для зберігання UI-елементів картки.
     */
    public record CharacterCardUI(VBox cardPane, Button selectButton) {}

    // --- Поля Класу ---
    private final GameState gameState;
    private final Map map;
    private final Runnable onCharacterPlaced;
    private Stage dialog;

    public List<CharacterPrototype> prototypes;
    private final java.util.Map<CharacterPrototype, CharacterCardUI> visualMap = new HashMap<>();
    private CharacterPrototype selectedPrototype = null;

    public CharacterSelectionDialog(GameState gameState, Map map, Runnable onCharacterPlaced) {
        this.gameState = gameState;
        this.map = map;
        this.onCharacterPlaced = onCharacterPlaced;
        initializePrototypes();
    }

    /**
     * Ініціалізує список доступних для вибору персонажів.
     */
    private void initializePrototypes() {
        prototypes = List.of(
                new CharacterPrototype(
                        "Мечник",
                        "HP: 150\nСила: 20\nВартість: 100",
                        100,
                        () -> new Swordsman("Мечник", map)
                ),
                new CharacterPrototype(
                        "Лучник",
                        "HP: 110\nСпритність: 18\nВартість: 120",
                        120,
                        () -> new Archer("Лучник", map)
                ),
                new CharacterPrototype(
                        "Маг",
                        "HP: 100\nІнтелект: 18\nВартість: 150",
                        150,
                        () -> new Mage("Маг", map)
                )
        );
    }

    /**
     * Створює та показує модальне вікно.
     */
    public void show() {
        // Скидаємо попередній вибір при відкритті діалогу
        selectedPrototype = null;
        visualMap.clear();

        dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Вибір персонажа");

        VBox mainLayout = new VBox(20);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(30));
        mainLayout.setStyle(STYLE_BG_DIALOG);

        Label titleLabel = new Label("Виберіть персонажа");
        titleLabel.setStyle(STYLE_TEXT_TITLE);

        Label goldLabel = new Label("Золото: " + gameState.getGold());
        goldLabel.setStyle(STYLE_TEXT_GOLD);

        HBox charactersBox = new HBox(20);
        charactersBox.setAlignment(Pos.CENTER);
        for (CharacterPrototype proto : prototypes) {
            charactersBox.getChildren().add(createCharacterCard(proto));
        }

        Button okButton = new Button("OK");
        okButton.setStyle(STYLE_BUTTON_OK);
        okButton.setOnAction(e -> handleOkClick());

        Button closeButton = new Button("Закрити");
        closeButton.setStyle(STYLE_BUTTON_CLOSE);
        closeButton.setOnAction(e -> dialog.close());

        HBox buttonBox = new HBox(15, okButton, closeButton);
        buttonBox.setAlignment(Pos.CENTER);

        mainLayout.getChildren().addAll(titleLabel, goldLabel, charactersBox, buttonBox);

        Scene scene = new Scene(mainLayout, 700, 500);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    /**
     * Створює картку персонажа на основі прототипу.
     */
    private VBox createCharacterCard(CharacterPrototype proto) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20));
        card.setStyle(STYLE_BG_CARD);
        card.setPrefWidth(180);

        Label nameLabel = new Label(proto.name());
        nameLabel.setStyle(STYLE_TEXT_CARD_NAME);

        Label statsLabel = new Label(proto.stats());
        statsLabel.setStyle(STYLE_TEXT_CARD_STATS);

        Button selectButton = new Button("Вибрати (" + proto.cost() + " золота)");
        selectButton.setStyle(STYLE_BUTTON_SELECT);

        selectButton.setOnAction(e -> {
            selectedPrototype = proto;
            updateVisualState();
        });

        visualMap.put(proto, new CharacterCardUI(card, selectButton));

        card.getChildren().addAll(nameLabel, statsLabel, selectButton);
        return card;
    }

    /**
     * Оновлює вигляд всіх карток, підсвічуючи обрану та скидаючи інші.
     */
    private void updateVisualState() {
        visualMap.forEach((proto, ui) -> {
            boolean isSelected = (proto == selectedPrototype);

            ui.cardPane().setStyle(isSelected ? STYLE_BG_CARD_SELECTED : STYLE_BG_CARD);
            ui.selectButton().setText(isSelected ? "Вибрано" : "Вибрати (" + proto.cost() + " золота)");
            ui.selectButton().setDisable(isSelected);
        });
    }

    /**
     * Обробляє натискання "OK". Переводить гру в режим PLACEMENT.
     */
    private void handleOkClick() {
        if (selectedPrototype == null) {
            System.out.println("Персонажа не вибрано!");
            return;
        }

        if (gameState.spendGold(selectedPrototype.cost())) {
            // Створюємо новий юніт
            Unit newUnit = selectedPrototype.creator().get();

            // Зберігаємо його в стані гри
            gameState.setUnitPendingPlacement(newUnit);
            gameState.setCurrentMode(ActionMode.PLACEMENT);

            System.out.println("Створено юніта: " + newUnit.getName() + " типу " + newUnit.getClass().getSimpleName());
            System.out.println("Режим гри: " + gameState.getCurrentMode());

            // Викликаємо callback
            if (onCharacterPlaced != null) {
                onCharacterPlaced.run();
            }

            dialog.close();
        } else {
            showInsufficientGoldWarning();
        }
    }

    /**
     * Допоміжний метод для показу попередження про нестачу золота.
     */
    private void showInsufficientGoldWarning() {
        Stage warningDialog = new Stage();
        warningDialog.initModality(Modality.APPLICATION_MODAL);
        warningDialog.setTitle("Недостатньо золота");

        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));
        layout.setStyle(STYLE_BG_DIALOG);

        Label message = new Label("Недостатньо золота для покупки!");
        message.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");

        Button okButton = new Button("OK");
        okButton.setStyle("-fx-font-size: 14px; -fx-padding: 8 20;");
        okButton.setOnAction(e -> warningDialog.close());

        layout.getChildren().addAll(message, okButton);

        Scene scene = new Scene(layout, 300, 150);
        warningDialog.setScene(scene);
        warningDialog.showAndWait();
    }
}