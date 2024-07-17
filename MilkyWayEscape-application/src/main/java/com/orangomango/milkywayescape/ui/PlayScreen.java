package com.orangomango.milkywayescape.ui;

import com.orangomango.milkywayescape.Util;
import dev.webfx.platform.resource.Resource;
import dev.webfx.platform.scheduler.Scheduler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.input.KeyCode;
import javafx.geometry.Rectangle2D;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random;

import com.orangomango.milkywayescape.AssetLoader;
import com.orangomango.milkywayescape.core.Car;
import com.orangomango.milkywayescape.core.Tree;

public class PlayScreen extends GameScreen{	
	private static final Font FONT = Font.loadFont(Resource.toUrl("/font/font.ttf", PlayScreen.class), 30);

	private Car player;
	private ArrayList<Car> cars = new ArrayList<>();
	private ArrayList<Tree> trees = new ArrayList<>();
	private double distanceTravelled;
	private int score;
	private double speed = Util.MIN_SPEED;
	private volatile boolean gameIsRunning = true;
	private long startTime, diff = -1;
	private long oppoTime = -1, oppoDiff;
	private boolean soundCooldown = false, soundCooldown2 = false;

	public PlayScreen(int w, int h, HashMap<KeyCode, Boolean> keys){
		super(w, h, keys);

		Random random = new Random();
		this.player = new Car(480, random.nextInt(4));
		this.startTime = System.currentTimeMillis();

		Runnable spawner = new Runnable() {
			@Override
			public void run() {
				if (gameIsRunning){
					// New car
					Car car = new Car(-150, random.nextInt(4));
					car.setLane(random.nextInt(4));
					car.setSpeed(speed);

					cars.add(car);
					Scheduler.scheduleDelay(speed < 7 ? 1000 : 550, this);
				}
			}
		};
		spawner.run();

		Scheduler.schedulePeriodic(100, scheduled -> {
			if (!gameIsRunning){
				scheduled.cancel();
			} else if (speed >= 7)
				score += 1 * (player.getLane() <= 1 ? 2 : 1) * (speed >= 10 ? 2 : 1);
		});

		Runnable treeSpawner = new Runnable() {
			@Override
			public void run() {
				if (gameIsRunning){
					Tree tree1 = new Tree(random.nextInt(180), -50);
					Tree tree2 = new Tree(800+random.nextInt(180), -50);

					trees.add(tree1);
					trees.add(tree2);

					Scheduler.scheduleDelay(speed < 7 ? 1500 : 1000, this);
				}
			}
		};
		treeSpawner.run();
	}

	@Override
	public void update(GraphicsContext gc, double scale){
		super.update(gc, scale);

		if (this.gameIsRunning){
			Rectangle2D rect = this.player.getRect();

			if (this.keys.getOrDefault(KeyCode.LEFT, false)){
				if (rect.getMinX() > 300) this.player.turnLeft();
			} else if (this.keys.getOrDefault(KeyCode.RIGHT, false)){
				if (rect.getMaxX() < 700) this.player.turnRight();
			} else {
				this.player.resetRot();
			}

			if (this.keys.getOrDefault(KeyCode.UP, false)){
				this.speed += 0.1;
				if (!this.soundCooldown){
					Util.playSound("speed.wav");
					this.soundCooldown = true;
					Util.schedule(() -> this.soundCooldown = false, 3500);
				}
			} else if (this.keys.getOrDefault(KeyCode.DOWN, false)){
				this.speed -= 0.2;
				if (!this.soundCooldown2){
					Util.playSound("break.wav");
					this.soundCooldown2 = true;
					Util.schedule(() -> this.soundCooldown2 = false, 750);
				}
			} else {
				this.speed -= 0.01;
			}
		} else {
			if (this.keys.getOrDefault(KeyCode.SPACE, false)){
				SCREEN_SWITCHER.accept(new HomeScreen(this.width, this.height, this.keys));
				return;
			}
		}

		this.speed = Math.max(Util.MIN_SPEED, Math.min(Util.MAX_SPEED, this.speed));

		for (int i = 0; i < this.cars.size(); i++){ // Update the speed
			this.cars.get(i).setSpeed(this.speed);
		}

		gc.save();
		gc.scale(scale, scale);
		gc.drawImage(AssetLoader.getInstance().getImage("background.png"), 0, 0, this.width, this.height);

		ArrayList<Car> toRemove = new ArrayList<>();
		for (int i = 0; i < this.cars.size(); i++){
			Car car = this.cars.get(i);
			boolean existing = this.gameIsRunning ? car.update(this.height) : true;
			car.render(gc);

			if (!existing){
				toRemove.add(car);
			}
		}
		this.cars.removeAll(toRemove);

		this.player.render(gc);

		if (this.gameIsRunning){
			if (this.player.getLane() <= 1){
				if (this.oppoTime == -1){
					this.oppoTime = System.currentTimeMillis();
				}
			} else {
				if (this.oppoTime != -1){
					this.oppoDiff += System.currentTimeMillis()-this.oppoTime;
					this.oppoTime = -1;
				}
			}
		}

		ArrayList<Tree> treesToRemove = new ArrayList<>();
		for (int i = 0; i < this.trees.size(); i++){
			Tree tree = this.trees.get(i);
			boolean existing = this.gameIsRunning ? tree.update(this.speed, this.height) : true;
			tree.render(gc);

			if (!existing){
				treesToRemove.add(tree);
			}
		}
		this.trees.removeAll(treesToRemove);

		// Check gameover
		if (this.gameIsRunning){
			for (int i = 0; i < this.cars.size(); i++){
				Car car = this.cars.get(i);
				if (car.getRect().intersects(this.player.getRect())){
					this.gameIsRunning = false;
					Util.playSound("crash.wav");
					if (this.oppoTime != -1){
						this.oppoDiff += System.currentTimeMillis()-this.oppoTime;
					}
				}
			}
		}

		if (this.gameIsRunning) this.distanceTravelled += 0.1;
		gc.save();
		gc.setFill(Color.BLACK);
		gc.setGlobalAlpha(0.7);
		gc.fillRect(10, 20, 150, 120);
		gc.restore();

		gc.setFill(Color.WHITE);
		gc.setFont(FONT);
		String string = "";
		if (this.player.getLane() <= 1 && this.speed >= 10){
			string = "(x4)";
		} else if (this.player.getLane() <= 1 || this.speed >= 10){
			string = "(x2)";
		}
		//gc.fillText(String.format("%d %s\n%.1fm\n%.1fkm/h", this.score, string, this.distanceTravelled, Util.mapSpeed(this.speed)), 20, 50);
		gc.fillText(score + " " + string + "\n" + format(this.distanceTravelled, 1) + "m\n" + format(Util.mapSpeed(this.speed), 2) + "km/h", 20, 50);

		if (!this.gameIsRunning){
			if (this.diff == -1) this.diff = System.currentTimeMillis()-this.startTime;

			gc.setFill(Color.BLACK);
			gc.save();
			gc.setGlobalAlpha(0.85);
			gc.fillRect(0, 0, this.width, this.height);
			gc.setFill(Color.WHITE);
			gc.setTextAlign(TextAlignment.CENTER);

			double oppo = (double)this.oppoDiff/this.diff*100;
			//gc.fillText(String.format("GAME OVER!\nScore: %d\nDistance travelled: %.1fm\nOpposite lane time: %.2f/100\n\n-----\nPress SPACE to continue", this.score, this.distanceTravelled, oppo), this.width/2, this.height/2-100);
			gc.fillText("GAME OVER!\nScore: " + this.score + "\nDistance travelled: " + format(this.distanceTravelled, 1) + "m\nOpposite lane time: " + format(oppo, 2) + "/100\n\n-----\nPress SPACE to continue", this.width/2, this.height/2-100);
			gc.restore();
		}

		gc.restore();
	}

	private static String format(double d, int n) {
		int tens = (int) Math.pow(10, n);
		int left = (int) d;
		int right = ((int) d * tens) % tens;
		StringBuilder s = new StringBuilder("" + right);
		while (s.length() < n) {
			s.append("0");
		}
		return left + "." + s;
	}
}