package com.orangomango.milkywayescape.ui;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.canvas.GraphicsContext;

import java.util.HashMap;

import com.orangomango.milkywayescape.AssetLoader;

public class HomeScreen extends GameScreen{
	private UiButton playButton, creditsButton;

	private static final double BUTTON_WIDTH = 300;
	private static final double BUTTON_HEIGHT = 100;

	public HomeScreen(int w, int h, HashMap<KeyCode, Boolean> keys){
		super(w, h, keys);
		this.playButton = new UiButton("button_play.png", w*0.5-BUTTON_WIDTH*0.5, h*0.5-BUTTON_HEIGHT*0.5, BUTTON_WIDTH, BUTTON_HEIGHT, () -> SCREEN_SWITCHER.accept(new PlayScreen(this.width, this.height, this.keys)));
		this.creditsButton = new UiButton("button_credits.png", w*0.5-BUTTON_WIDTH*0.5, h*0.5-BUTTON_HEIGHT*0.5+BUTTON_HEIGHT*1.3, BUTTON_WIDTH, BUTTON_HEIGHT, () -> SCREEN_SWITCHER.accept(new CreditsScreen(this.width, this.height, this.keys)));
	}

	@Override
	public void handleMouseInput(MouseEvent e, double scale, double offsetX){
		this.playButton.click((e.getX()-offsetX)/scale, e.getY()/scale);
		this.creditsButton.click((e.getX()-offsetX)/scale, e.getY()/scale);
	}

	@Override
	public void update(GraphicsContext gc, double scale){
		super.update(gc, scale);

		gc.save();
		gc.scale(scale, scale);
		//gc.drawImage(AssetLoader.getInstance().getImage("background.jpg"), 0, 0, this.width, this.height);
		//gc.drawImage(AssetLoader.getInstance().getImage("logo.png"), (this.width-600)/2, -20, 600, 200);
		gc.save();
		gc.setGlobalAlpha(0.8);
		this.playButton.render(gc);
		this.creditsButton.render(gc);
		gc.restore();
		gc.restore();
	}
}