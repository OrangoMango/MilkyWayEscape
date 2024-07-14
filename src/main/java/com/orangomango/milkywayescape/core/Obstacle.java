package com.orangomango.milkywayescape.core;

import javafx.scene.canvas.GraphicsContext;
import javafx.geometry.Point2D;

import java.util.Random;
import java.util.ArrayList;

import com.orangomango.milkywayescape.AssetLoader;
import com.orangomango.milkywayescape.Util;

public class Obstacle{
	private int type;
	private double speed, size, rotAngle, zPos;
	private int colorIndex = 0;
	private boolean checked = false;
	private Player player;

	public static final int TYPE_COUNT = 3;
	public static final int COUNT = 7;
	private static final Util.Polygon[] HITBOXES = Util.loadHitboxData();

	public Obstacle(Player player, int type, double speed, double zPos){
		this.player = player;
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

		if (this.zPos <= 0){
			// Create new obstacle
			this.zPos = factor;
			this.size = 0;
			Random random = new Random();
			this.type = random.nextInt(TYPE_COUNT);
			this.speed = newSpeed;
			this.colorIndex = random.nextInt(3);
			this.rotAngle = random.nextInt(360);
			this.checked = false;
		} else if (this.zPos <= 1000 && !this.checked){
			this.checked = true;

			// Check collision
			//if (checkCollision()) try { Thread.sleep(2000); } catch (Exception ex){}
		}
	}

	private boolean checkCollision(){
		ArrayList<Util.Polygon> boxes = Util.getHitboxes(HITBOXES, this.type);
		for (Util.Polygon pol : boxes){
			pol.scale(this.size/32);
			pol.rotate(this.rotAngle);
		}

		Util.Polygon ply = new Util.Polygon(new Point2D[]{new Point2D(14, 23), new Point2D(17, 23), new Point2D(17, 26), new Point2D(14, 26)});
		ply.scale(this.size/32);

		for (Util.Polygon pol : boxes){
			if (pol.collided(ply)){
				return true;
			}
		}

		return false;
	}

	public void render(GraphicsContext gc, double screenWidth, double screenHeight, boolean first){
		if (this.zPos <= 0) return;

		gc.save();
		gc.translate(screenWidth/2, screenHeight/2);
		gc.rotate(this.rotAngle);
		gc.drawImage(AssetLoader.getInstance().getImage("obstacle_"+this.type+".png"), 1+34*this.colorIndex, 1, 32, 32, -this.size/2, -this.size/2, this.size, this.size);
		gc.restore();

		if (first){ // DEBUG
			ArrayList<Util.Polygon> boxes = Util.getHitboxes(HITBOXES, this.type);
			for (Util.Polygon pol : boxes){
				pol.scale(2); //pol.scale(this.size/32);
				pol.rotate(this.rotAngle);
			}

			Util.Polygon ply = new Util.Polygon(new Point2D[]{new Point2D(14, 23), new Point2D(17, 23), new Point2D(17, 26), new Point2D(14, 26)});
			ply.scale(2); //ply.scale(this.size/32);

			boolean collided = false;
			for (Util.Polygon pol : boxes){
				if (pol.collided(ply)){
					collided = true;
				}
			}

			gc.setFill(collided ? javafx.scene.paint.Color.LIME : javafx.scene.paint.Color.YELLOW);
			for (Util.Polygon pol : boxes){
				renderPolygon(gc, pol);
			}
			renderPolygon(gc, ply);
		}
	}

	private static void renderPolygon(GraphicsContext gc, Util.Polygon pol){
		Point2D[] vertices = pol.getVertices();

		double[] xpoints = new double[vertices.length];
		double[] ypoints = new double[vertices.length];
		for (int i = 0; i < vertices.length; i++){
			xpoints[i] = vertices[i].getX();
			ypoints[i] = vertices[i].getY();
		}

		gc.fillPolygon(xpoints, ypoints, vertices.length);
	}
}