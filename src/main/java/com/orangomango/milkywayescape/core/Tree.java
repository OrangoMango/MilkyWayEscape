package com.orangomango.milkywayescape.core;

import javafx.scene.canvas.GraphicsContext;

import java.util.Random;

import com.orangomango.milkywayescape.AssetLoader;

public class Tree{
	private double x, y;
	private int frameIndex;

	public Tree(double x, double y){
		this.x = x;
		this.y = y;
		Random random = new Random();
		this.frameIndex = random.nextInt(3);
	}

	public boolean update(double speed, double screenHeight){
		this.y += speed * 0.5;
		if (this.y >= screenHeight){
			return false;
		}

		return true;
	}

	public void render(GraphicsContext gc){
		gc.drawImage(AssetLoader.getInstance().getImage("tree.png"), 1+34*this.frameIndex, 1, 32, 32, this.x-32, this.y-32, 64, 64);
	}
}