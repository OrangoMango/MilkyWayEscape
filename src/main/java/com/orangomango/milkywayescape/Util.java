package com.orangomango.milkywayescape;

import javafx.geometry.Point2D;

import java.util.Collections;
import java.util.ArrayList;
import java.io.*;

public class Util{
	public static class Polygon{
		private Point2D[] vertices;
		private double rotX, rotY;

		public Polygon(Point2D... v){
			this.vertices = v;
		}

		public Polygon copy(){
			Point2D[] copy = new Point2D[this.vertices.length];
			System.arraycopy(this.vertices, 0, copy, 0, copy.length);
			Polygon output = new Polygon(copy);
			output.rotX = this.rotX;
			output.rotY = this.rotY;
			return output;
		}

		public Point2D[] getVertices(){
			return this.vertices;
		}

		public void setRotationPivot(double x, double y){
			this.rotX = x;
			this.rotY = y;
		}

		public void rotate(double rot){
			for (int i = 0; i < this.vertices.length; i++){
				this.vertices[i] = rotatePoint(this.vertices[i], rot, this.rotX, this.rotY);
			}
		}

		public void scale(double factor){
			for (int i = 0; i < this.vertices.length; i++){
				this.vertices[i] = this.vertices[i].multiply(factor);
			}

			this.rotX *= factor;
			this.rotY *= factor;
		}

		public boolean collided(Polygon other){
			ArrayList<Point2D[]> edges = new ArrayList<>();
			for (int i = 0; i < this.vertices.length; i++){
				Point2D a = this.vertices[i];
				Point2D b = this.vertices[(i+1)%this.vertices.length];
				edges.add(new Point2D[]{a, b});
			}
			for (int i = 0; i < other.vertices.length; i++){
				Point2D a = other.vertices[i];
				Point2D b = other.vertices[(i+1)%other.vertices.length];
				edges.add(new Point2D[]{a, b});
			}

			for (Point2D[] edge : edges){
				Point2D vector = edge[1].subtract(edge[0]);
				Point2D axis = new Point2D(-vector.getY(), vector.getX());
				ArrayList<Double> proj1 = new ArrayList<>();
				ArrayList<Double> proj2 = new ArrayList<>();
				for (Point2D v : this.vertices){
					proj1.add(axis.dotProduct(v));
				}
				for (Point2D v : other.vertices){
					proj2.add(axis.dotProduct(v));
				}
				double min1 = Collections.min(proj1);
				double min2 = Collections.min(proj2);
				double max1 = Collections.max(proj1);
				double max2 = Collections.max(proj2);
				if (!(min1 <= max2 && max1 >= min2)){
					return false;
				}
			}
			return true;
		}

		private Point2D rotatePoint(Point2D point, double rot, double px, double py){
			rot = Math.toRadians(rot);
			double x = point.getX();
			double y = point.getY();
			x -= px;
			y -= py;
			double nx = x*Math.cos(rot)-y*Math.sin(rot);
			double ny = y*Math.cos(rot)+x*Math.sin(rot);
			return new Point2D(nx+px, ny+py);
		}
	}

	public static Polygon[] loadHitboxData(){
		Polygon[] output = new Polygon[4];
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(Util.class.getResourceAsStream("/files/hitboxes.data")));
			ArrayList<Point2D> currentPoints = new ArrayList<>();
			int currentObstacle = 0;
			String line;
			while ((line = reader.readLine()) != null){
				if (line.equals("")){
					Point2D[] points = currentPoints.toArray(new Point2D[currentPoints.size()]);
					currentPoints.clear();
					Polygon pol = new Polygon(points);
					pol.setRotationPivot(15.5, 15.5);
					output[currentObstacle++] = pol;
				} else {
					int xp = Integer.parseInt(line.split(" ")[0]);
					int yp = Integer.parseInt(line.split(" ")[1]);
					currentPoints.add(new Point2D(xp, yp));
				}
			}
			reader.close();
		} catch (IOException ex){
			ex.printStackTrace();
		}

		return output;
	}

	public static ArrayList<Polygon> getHitboxes(Polygon[] data, int type){
		ArrayList<Polygon> output = new ArrayList<>();
		if (type != 2){
			output.add(data[type].copy());
		} else {
			output.add(data[2].copy());
			output.add(data[3].copy());
		}

		return output;
	}


	public static void playSound(String name){
		AssetLoader.getInstance().getAudio(name).play();
	}

	public static void schedule(Runnable r, int delay){
		new Thread(() -> {
			try {
				Thread.sleep(delay);
				r.run();
			} catch (InterruptedException ex){
				ex.printStackTrace();
			}
		}).start();
	}
}