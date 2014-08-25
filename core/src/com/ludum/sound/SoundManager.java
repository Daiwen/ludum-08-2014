package com.ludum.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {
	private static SoundManager singleton;
	
	
	private Music music;
	private Sound jump;
	private float volume = 0.5f;
	
	
	public static SoundManager getInstance(){
		if (singleton == null)
			singleton = new SoundManager();
		return singleton;
	}
	
	private SoundManager() {
		music = Gdx.audio.newMusic(Gdx.files.internal("sound/music.wav"));
		music.setLooping(true);
		music.setVolume(volume);
		jump = Gdx.audio.newSound(Gdx.files.internal("sound/jump.ogg"));
		jump.setVolume(0, volume);
	}
	
	public void startBackGroundMusic(){
		music.play();
	}
	
	public void jump(){
		jump.play();
	}
	
}