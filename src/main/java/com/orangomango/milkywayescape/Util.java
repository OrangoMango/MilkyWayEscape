package com.orangomango.milkywayescape;

public class Util{
	public static final double MIN_SPEED = 3;
	public static final double MAX_SPEED = 20;

	public static void playSound(String name){
		AssetLoader.getInstance().getAudio(name).play();
	}

	public static double mapSpeed(double speed){
		final double kmmin = 40;
		final double kmmax = 130;

		return (speed-MIN_SPEED) / (MAX_SPEED-MIN_SPEED) * (kmmax-kmmin) + kmmin;
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