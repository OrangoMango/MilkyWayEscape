package com.orangomango.milkywayescape;

import javafx.geometry.Point2D;

public class Util{
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