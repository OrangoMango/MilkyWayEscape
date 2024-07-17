package com.orangomango.milkywayescape;

import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.AudioClip;

import java.util.HashMap;
import java.io.*;

public class AssetLoader{
	private static AssetLoader instance;

	static {
		AssetLoader loader = new AssetLoader();
		loader.loadAssets("/files/assets.data");
	}

	private HashMap<String, Image> images = new HashMap<>();
	private HashMap<String, AudioClip> audios = new HashMap<>();
	private HashMap<String, Media> musics = new HashMap<>();

	public AssetLoader(){
		AssetLoader.instance = this;
	}

	public void loadAssets(String fileName){
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(fileName)));
			int loadingMode = 0;
			String line;
			while ((line = reader.readLine()) != null){
				if (line.isBlank()){
					loadingMode++;
					continue;
				}

				switch (loadingMode){
					case 0:
						loadImage(line);
						break;
					case 1:
						loadAudio(line);
						break;
					case 2:
						loadMusic(line);
						break;
				}
			}
			reader.close();
		} catch (IOException ex){
			ex.printStackTrace();
		}
	}

	public Image getImage(String name){
		return this.images.getOrDefault(name, null);
	}

	public AudioClip getAudio(String name){
		return this.audios.getOrDefault(name, null);
	}

	public Media getMusic(String name){
		return this.musics.getOrDefault(name, null);
	}

	private void loadImage(String name){
		String[] data = name.split("/");
		this.images.put(data[data.length-1], new Image(getClass().getResourceAsStream(name)));
	}

	private void loadAudio(String name){
		String[] data = name.split("/");
		this.audios.put(data[data.length-1], new AudioClip(getClass().getResource(name).toExternalForm()));
	}

	private void loadMusic(String name){
		String[] data = name.split("/");
		this.musics.put(data[data.length-1], new Media(getClass().getResource(name).toExternalForm()));
	}

	public static AssetLoader getInstance(){
		return AssetLoader.instance;
	}
}