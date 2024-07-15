package com.orangomango.milkywayescape.ui;

import javafx.scene.canvas.GraphicsContext;
import javafx.geometry.Rectangle2D;

import com.orangomango.milkywayescape.AssetLoader;
import com.orangomango.milkywayescape.Util;

public class UiButton{
	private String imageName;
	private double x, y, width, height;
	private Runnable onClick;

	public UiButton(String imageName, double x, double y, double w, double h, Runnable r){
		this.imageName = imageName;
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
		this.onClick = r;
	}

	public void click(double mx, double my){
		Rectangle2D rect = new Rectangle2D(this.x, this.y, this.width, this.height);
		if (rect.contains(mx, my)){
			Util.playSound("click.mp3");
			this.onClick.run();
		}
	}

	public void render(GraphicsContext gc){
		gc.drawImage(AssetLoader.getInstance().getImage(this.imageName), this.x, this.y, this.width, this.height);
	}

	public void render(GraphicsContext gc, int frameIndex){
		gc.drawImage(AssetLoader.getInstance().getImage(this.imageName), 1+frameIndex*34, 1, 32, 32, this.x, this.y, this.width, this.height);
	}
}