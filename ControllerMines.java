package mines;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

//This class allows control of the display
public class ControllerMines {

	@FXML
	private StackPane gridID;
	@FXML
	private TextField height;
	@FXML
	private TextField mines;
	@FXML
	private Button resetButton;
	@FXML
	private TextField width;

	// Returns the width that found in a text area
	public TextField getWidth() {
		return width;
	}

	// Returns the number of mines found in a text area
	public TextField getMines() {
		return mines;
	}

	// Returns the height that found in a text area
	public TextField getHeight() {
		return height;
	}

	// Returns the StackPane identifier for the purpose of placing the relevant grid
	public StackPane getGrid() {
		return gridID;
	}

	// Returns the ID of the reset button for reset
	public Button resetGame() {
		return resetButton;
	}
}
