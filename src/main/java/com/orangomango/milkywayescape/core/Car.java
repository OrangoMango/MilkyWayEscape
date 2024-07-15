package com.orangomango.milkywayescape.core;

import javafx.scene.canvas.GraphicsContext;
import javafx.geometry.Rectangle2D;

import com.orangomango.milkywayescape.AssetLoader;

public class Car{
	private double lane;
	private double tempRot = 0;
	private double position;
	private boolean moving;
	private double speed;
	private int frameIndex;

	public Car(double startPos, int fi){
		this.lane = 2;
		this.position = startPos;
		this.frameIndex = fi;
	}

	public void setLane(double lane){
		this.lane = lane;
	}

	public void setSpeed(double speed){
		this.moving = true;
		this.speed = speed;
	}

	public boolean update(double screenHeight){
		if (this.moving){
			this.position += this.speed * (this.lane <= 1 ? 1.3 : 0.8);
			if (this.position >= screenHeight+100){
				return false;
			}
		}

		return true;
	}

	public void turnRight(){
		this.lane += 0.1;
		this.tempRot = 12;
	}

	public void turnLeft(){
		this.lane -= 0.1;
		this.tempRot = -12;
	}

	public void resetRot(){
		this.tempRot = 0;
	}

	public double getLane(){
		return this.lane;
	}

	public Rectangle2D getRect(){
		return new Rectangle2D(320+this.lane*100, this.position, 65, 80);
	}

	public void render(GraphicsContext gc){
		gc.save();

		final double width = 65;
		final double height = 80;

		gc.translate(320+this.lane*100+width/2, this.position+height/2);
		gc.rotate(this.tempRot);
		if (this.moving && this.lane <= 1){
			gc.rotate(180);
		}
		gc.drawImage(AssetLoader.getInstance().getImage("car.png"), 1+(width+2)*this.frameIndex, 1, width, height, -width/2, -height/2, width, height);
		gc.restore();

		//gc.setStroke(javafx.scene.paint.Color.RED);
		//Rectangle2D rect = getRect();
		//gc.strokeRect(rect.getMinX(), rect.getMinY(), rect.getWidth(), rect.getHeight());
	}
}