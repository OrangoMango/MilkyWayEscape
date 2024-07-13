package com.orangomango.milkywayescape.ui;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.input.KeyCode;
import javafx.geometry.Point2D;

import java.util.HashMap;
import java.util.Random;
import java.util.ArrayList;

import com.orangomango.milkywayescape.core.*;

public class PlayScreen extends GameScreen{
	private ArrayList<Obstacle> currentObstacles = new ArrayList<>();
	private Player player;
	private double currentSpeed = 23.5;

	private static final Font FONT = Font.loadFont(PlayScreen.class.getResourceAsStream("/font/font.ttf"), 40);
	private static final Font FONT_SMALL = Font.loadFont(PlayScreen.class.getResourceAsStream("/font/font.ttf"), 25);

	public PlayScreen(int w, int h, HashMap<KeyCode, Boolean> keys){
		super(w, h, keys);

		Random random = new Random();
		for (int i = 0; i < Obstacle.COUNT; i++){
			this.currentObstacles.add(new Obstacle(random.nextInt(Obstacle.TYPE_COUNT), this.currentSpeed, (i+1)*3500));
		}
		this.player = new Player(new Point2D(this.width/2, this.height*7/8), 64);
	}

	private void rotateScene(double angle){
		for (int i = 0; i < this.currentObstacles.size(); i++){
			this.currentObstacles.get(i).rotate(angle);
		}
	}

	@Override
	public void update(GraphicsContext gc, double scale){
		super.update(gc, scale);

		if (this.keys.getOrDefault(KeyCode.LEFT, false)){
			rotateScene(-4);
		} else if (this.keys.getOrDefault(KeyCode.RIGHT, false)){
			rotateScene(4);
		}

		gc.save();
		gc.scale(scale, scale);
		//gc.drawImage(AssetLoader.getInstance().getImage("background.jpg"), 0, 0, this.width, this.height);

		for (int i = 0; i < this.currentObstacles.size(); i++){
			this.currentObstacles.get(i).update(this.currentSpeed);
		}

		this.currentObstacles.sort((o1, o2) -> -Double.compare(o1.getZpos(), o2.getZpos()));

		for (int i = 0; i < this.currentObstacles.size(); i++){
			this.currentObstacles.get(i).render(gc, this.width, this.height);
		}
		this.player.render(gc);

		gc.restore();
	}
}