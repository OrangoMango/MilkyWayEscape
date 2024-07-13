package com.orangomango.milkywayescape.core;

import javafx.scene.canvas.GraphicsContext;

import java.util.Random;

import com.orangomango.milkywayescape.AssetLoader;

public class Obstacle{
	private int type;
	private double speed;
	private double size;
	private double rotAngle;
	private int colorIndex = 0;
	private double zPos;

	public static final int TYPE_COUNT = 3;
	public static final int COUNT = 7;

	public Obstacle(int type, double speed, double zPos){
		this.type = type;
		this.speed = speed;
		this.zPos = zPos;
		Random random = new Random();
		this.colorIndex = random.nextInt(3);
		this.rotAngle = random.nextInt(360);
	}

	public void rotate(double angle){
		this.rotAngle += angle;
	}

	public double getZpos(){
		return this.zPos;
	}

	public void update(double newSpeed){
		final double factor = (COUNT+1)*3500;
		this.zPos -= this.speed * factor / (this.zPos+1);
		this.size = 50 * factor / (this.zPos+1);

		if (this.zPos <= -25){
			// Create new obstacle
			this.zPos = factor;
			this.size = 0;
			Random random = new Random();
			this.type = random.nextInt(TYPE_COUNT);
			this.speed = newSpeed;
			this.colorIndex = random.nextInt(3);
			this.rotAngle = random.nextInt(360);
		}
	}

	public void render(GraphicsContext gc, double screenWidth, double screenHeight){
		if (this.zPos <= 0) return;

		gc.save();
		gc.translate(screenWidth/2, screenHeight/2);
		gc.rotate(this.rotAngle);
		gc.drawImage(AssetLoader.getInstance().getImage("obstacle_"+this.type+".png"), 1+34*this.colorIndex, 1, 32, 32, -this.size/2, -this.size/2, this.size, this.size);
		gc.restore();
	}
}