package com.orangomango.milkywayescape.ui;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.canvas.GraphicsContext;

import java.util.HashMap;
import java.util.function.Consumer;

public abstract class GameScreen{
	protected int width, height;
	protected HashMap<KeyCode, Boolean> keys;

	public static Consumer<GameScreen> SCREEN_SWITCHER;

	public GameScreen(int w, int h, HashMap<KeyCode, Boolean> keys){
		this.width = w;
		this.height = h;
		this.keys = keys;
	}

	public void handleMouseInput(MouseEvent e, double scale, double offsetX){
		// Do nothing
	}

	public void handleMouseMovement(MouseEvent e, double scale, double offsetX){
		// Do nothing
	}
	
	public void update(GraphicsContext gc, double scale){
		if (this.keys.getOrDefault(KeyCode.ESCAPE, false)){
			SCREEN_SWITCHER.accept(new HomeScreen(this.width, this.height, this.keys)); // Return to home screen
		}
	}
}