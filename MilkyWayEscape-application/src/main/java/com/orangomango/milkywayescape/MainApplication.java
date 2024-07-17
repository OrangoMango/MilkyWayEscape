package com.orangomango.milkywayescape;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.canvas.*;
import javafx.animation.*;
import javafx.util.Duration;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.media.MediaPlayer;

import java.util.HashMap;

import com.orangomango.milkywayescape.ui.GameScreen;
import com.orangomango.milkywayescape.ui.HomeScreen;

public class MainApplication extends Application{
	private GameScreen currentScreen;
	private HashMap<KeyCode, Boolean> keys = new HashMap<>();
	private int fps, frames;
	private MediaPlayer player;

	private static final int WIDTH = 1000;
	private static final int HEIGHT = 600;
	private static final int WINDOW_WIDTH = 1000;
	private static final int WINDOW_HEIGHT = 600;
	private static final double SCALE = (double)WINDOW_HEIGHT/HEIGHT;
	private static final double OFFSET_X = (WINDOW_WIDTH-WIDTH*SCALE)/2;

	@Override
	public void start(Stage stage){
		GameScreen.SCREEN_SWITCHER = screen -> this.currentScreen = screen;
		this.currentScreen = new HomeScreen(WIDTH, HEIGHT, this.keys);
		this.player = new MediaPlayer(AssetLoader.getInstance().getMusic("background.wav"));
		this.player.setCycleCount(MediaPlayer.INDEFINITE);
		this.player.play();

		Thread frameCounter = new Thread(() -> {
			while (true){
				try {
					this.fps = this.frames;
					this.frames = 0;
					Thread.sleep(1000);
				} catch (InterruptedException ex){
					ex.printStackTrace();
				}
			}
		});
		frameCounter.setDaemon(true);
		frameCounter.start();

		StackPane pane = new StackPane();
		Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setImageSmoothing(false);
		pane.getChildren().add(canvas);

		canvas.setFocusTraversable(true);
		canvas.setOnKeyPressed(e -> this.keys.put(e.getCode(), true));
		canvas.setOnKeyReleased(e -> this.keys.put(e.getCode(), false));
		canvas.setOnMousePressed(e -> this.currentScreen.handleMouseInput(e, SCALE, OFFSET_X));
		canvas.setOnMouseMoved(e -> this.currentScreen.handleMouseMovement(e, SCALE, OFFSET_X));

		Timeline loop = new Timeline(new KeyFrame(Duration.millis(1000.0/60), e -> {
			update(gc);
			MainApplication.this.frames++;
		}));
		loop.setCycleCount(Animation.INDEFINITE);
		loop.play();

		Scene scene = new Scene(pane, WINDOW_WIDTH, WINDOW_HEIGHT);
		scene.setFill(Color.BLACK);
		stage.setScene(scene);

		stage.setTitle("Milky Way Escape");
		stage.setResizable(false);
		stage.getIcons().add(AssetLoader.getInstance().getImage("icon.png"));
		stage.show();
	}

	private void update(GraphicsContext gc){
		gc.clearRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		gc.translate(OFFSET_X, 0);
		this.currentScreen.update(gc, SCALE);
		gc.translate(-OFFSET_X, 0);

		// Display FPS
		gc.setFill(Color.WHITE);
		gc.fillText("FPS: "+this.fps, 20, HEIGHT-20);
	}

	public static void main(String[] args){
		launch(args);
	}
}