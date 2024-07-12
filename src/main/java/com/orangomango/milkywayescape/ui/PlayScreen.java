package com.orangomango.milkywayescape.ui;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.input.KeyCode;

import java.util.HashMap;

public class PlayScreen extends GameScreen{
	private static final Font FONT = Font.loadFont(PlayScreen.class.getResourceAsStream("/font/font.ttf"), 40);
	private static final Font FONT_SMALL = Font.loadFont(PlayScreen.class.getResourceAsStream("/font/font.ttf"), 25);

	public PlayScreen(int w, int h, HashMap<KeyCode, Boolean> keys){
		super(w, h, keys);
	}

	@Override
	public void update(GraphicsContext gc, double scale){
		super.update(gc, scale);

		gc.save();
		gc.scale(scale, scale);
		//gc.drawImage(AssetLoader.getInstance().getImage("background.jpg"), 0, 0, this.width, this.height);

		gc.restore();
	}
}