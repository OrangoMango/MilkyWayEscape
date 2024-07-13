package com.orangomango.milkywayescape.core;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.geometry.Point2D;

public class Player{
	private Point2D pos;
	private double size;

	public Player(Point2D pos, double size){
		this.pos = pos;
		this.size = size;
	}

	public void render(GraphicsContext gc){
		gc.setFill(Color.BLUE);
		gc.fillRect(this.pos.getX()-this.size/2, this.pos.getY()-this.size/2, this.size, this.size);
	}
}