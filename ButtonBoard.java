package mines;

import javafx.scene.control.Button;

//A class that inherits from a button class, in addition to saving the position of the button
public class ButtonBoard extends Button {
	private int x, y;

	// A constructor that initializes the position of the button
	public ButtonBoard(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
