package mines;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

//A class that manages the user interface of the Minesweeper game
public class MinesFX extends Application {
	private Mines game; // A game whose logic is in the mines class
	private Stage stage;
	private ControllerMines controller;
	private GridPane buttons = new GridPane(); // Layer for arranging game buttons
	private int height, width, mines;
	private final Image imageLose = new Image(getClass().getResourceAsStream("losing.gif"), 300, 300, false, false);
	private final Image imageWin = new Image(getClass().getResourceAsStream("win.gif"), 300, 300, false, false);
	private boolean flagWindow = false;

	// main for running a user interface
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		// This class manages the scene and presents
		this.stage = stage;
		HBox hbox;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("MinesFxml.fxml"));
			hbox = loader.load(); // Management of the hbox by fxml
			controller = loader.getController(); // controller for controlling display changes
			controller.getGrid().getChildren().add(buttons); // Inserting the grid of the game buttons on the display
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		resetClick(); // In case someone presses the reset button
		createGame(); // To start a new game
		Scene scene = new Scene(hbox); // Set up a new scene using hbox obtained in fxml
		stage.setScene(scene);
		stage.setTitle("The Amazing Mines Sweeper");
		HBox.setHgrow(controller.getGrid(), Priority.ALWAYS);
		stage.show();
		stage.setMinHeight(stage.getHeight());
		stage.setMinWidth(stage.getWidth());
	}

	private void createGame() {
		// initialize the game board
		buttons.getChildren().clear(); // Grid cleaning in case of reset
		height = Integer.parseInt(controller.getHeight().getText());
		width = Integer.parseInt(controller.getWidth().getText());
		mines = Integer.parseInt(controller.getMines().getText());
		// Designed to handle in case the number of mines exceeds the size of the board
		mines = Math.min(mines, height * width);
		game = new Mines(height, width, mines); // Initialize a game that will handle logic
		for (int i = 0; i < height; i++) { // passes on row of buttons
			for (int j = 0; j < width; j++) { // passes on column of buttons
				ButtonBoard button = new ButtonBoard(i, j);
				buttonClick(button); // Dealing with a button-pressed situation
				button.setText(game.get(i, j));
				button.setMinSize(40, 40);
				button.prefWidthProperty().bind(buttons.widthProperty());
				button.prefHeightProperty().bind(buttons.heightProperty());
				buttons.add(button, j, i); // Add the button to the grid
			}
		}
	}

	// method that dealing with a button-pressed situation
	private void buttonClick(ButtonBoard button) {
		button.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// Handling click cases
				if (event.getButton() == MouseButton.PRIMARY && !flagWindow) { // Using a left mouse button, press any
																				// button
					// A case where there is a mine with a button pressed
					if (!game.open(button.getX(), button.getY())) {
						game.setShowAll(true); // Opening the entire board
						newWindow(imageLose, "OMG, you just lose");
					} else if (game.isDone()) { // In case the user wins the game
						newWindow(imageWin, "OMG, you just won!!");
					}
				} else if (event.getButton() == MouseButton.SECONDARY && !flagWindow) // Using a right mouse button,
																						// press any button
					game.toggleFlag(button.getX(), button.getY()); // Marking or lowering a flag
				openTheButtons(); // Opening buttons that are in the open position
			}
		});
	}

	// Bounces a new window in case the player wins or loses
	private void newWindow(Image image, String str) {
		if (flagWindow) // If a window is already opened in the same game - it will not open again
			return;
		Label secondLabel = new Label(str);
		secondLabel.setFont(new Font("Cambria", 50));
		secondLabel.setPadding(new Insets(50, 10, 20, 10));
		secondLabel.setGraphic(new ImageView(image)); // Set up an image for a win or loss
		StackPane secondaryLayout = new StackPane();
		secondaryLayout.getChildren().add(secondLabel);
		Scene secondScene = new Scene(secondaryLayout, 800, 350);
		// New window (Stage)
		Stage newWindow = new Stage();
		newWindow.setTitle("Game Over");
		newWindow.setScene(secondScene);
		// Set position of second window, related to primary window.
		newWindow.setX(stage.getX() + 200);
		newWindow.setY(stage.getY() + 100);
		flagWindow = true; // Warns that a window will open in this game so that it does not open again
		newWindow.show();
	}

	// Opening buttons that are in the open position
	private void openTheButtons() {
		for (Node button : buttons.getChildren()) { // passes on all buttons
			ButtonBoard btn = (ButtonBoard) button;
			// Defines a suitable character according to the button position
			btn.setText(game.get(btn.getX(), btn.getY()));
		}
	}

	// In case someone presses the reset button
	private void resetClick() {
		controller.resetGame().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// Handles reset mode by starting a new game
				flagWindow = false;
				createGame();
				stage.sizeToScene();
				stage.setMinHeight(stage.getHeight());
				stage.setMinWidth(stage.getWidth());
			}
		});
	}

}
