package com.ludum.game;

import com.ludum.map.Map;
import com.ludum.map.MapLoader;
import com.ludum.map.WorldState;
import com.ludum.map.WorldType;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.ludum.configuration.ConfigManager;
import com.ludum.controls.PlayerControls;
import com.ludum.entity.player.Player;
import com.ludum.entity.player.PlayerFactory;
import com.ludum.physics.PhysicsManager;
import com.ludum.rendering.Background;
import com.ludum.rendering.CharacterCenteredCamera;
import com.ludum.sound.SoundManager;

public class ClassicMode extends ScreenAdapter {
	private LudumGame game;

	private WorldState state;
	private MapLoader mapLoader;

	private SpriteBatch worldBatch;
	private SpriteBatch uiBatch;

	private int currentCharacterIndex = 0;
	private List<Player> characters = new ArrayList<Player>();
	private List<InputProcessor> characterControllers = new ArrayList<InputProcessor>();

	private Map map;
	private CharacterCenteredCamera cam;

	public ClassicMode(LudumGame g) {
		// Gdx.audio.newMusic();
		game = g;
		currentCharacterIndex = -1;
		worldBatch = new SpriteBatch();
		uiBatch = new SpriteBatch();		

		mapLoader = MapLoader.getLoader();
		state = new WorldState();
		loadLevel();
		
		SoundManager.getInstance().startBackGroundMusic();
	}

	private void update(float dt) {
		for (Player p : characters)
			p.updatePhysics(dt);
		PhysicsManager.getInstance().update(dt);
		
		if (!state.isSwapped())
			state.hasSwapped();
		
		for (Player p : characters)
			p.update(dt);
		cam.follow();
	}

	private void draw(float dt) {
		/* Render part */
		if (state.getState() == WorldType.DARK)
			Gdx.gl.glClearColor(0.16f, 0.16f, 0.16f, 1);
		else
			Gdx.gl.glClearColor(0.18f, 0.19f, 0.25f, 1);

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		worldBatch.setProjectionMatrix(cam.combined);
		worldBatch.begin();

		Background.getInstance().render(worldBatch, cam.position.x,
				cam.position.y, state);
		worldBatch.end();
		map.render(cam);
		worldBatch.begin();
		for (Player p : characters) {
			p.draw(worldBatch);
		}
		worldBatch.end();

		drawUI();
	}

	private void drawUI() {
		int i = 0;
		for (Player p : characters) {
			p.drawUI(uiBatch, new Vector2(i * ConfigManager.portraitSizeX, 0),
					p == characters.get(currentCharacterIndex));
			i++;
		}
	}

	@Override
	public void render(float dt) {
		update(dt);
		draw(dt);

		/* Check end condition */
		int end = 0;
		for (Player p : characters) {
			if (p.isAtEnd1()) {
				end++;
				break;
			}
		}

		for (Player p : characters) {
			if (p.isAtEnd2()) {
				end++;
				break;
			}
		}

		for (Player p : characters) {
			if (p.isAtEnd3()) {
				end++;
				break;
			}
		}

		if (end == map.getEndNumber()) {
			if (mapLoader.isLastMap())
				game.startCreditMode();
			else {
				loadLevel();
			}
		}
	}

	public void nextCharacter() {
		characters.get(currentCharacterIndex).stop();

		game.removeInputProcessor(characterControllers
				.get(currentCharacterIndex));

		currentCharacterIndex = (currentCharacterIndex + 1) % characters.size();

		game.addInputProcessor(characterControllers.get(currentCharacterIndex));
		cam.changeCharacter(characters.get(currentCharacterIndex));
	}

	public void swapWorld() {
		state.swapWorld();
	}

	public void loadLevel() {
		/* Clear last map */
		if (currentCharacterIndex >= 0)
			game.removeInputProcessor(characterControllers
					.get(currentCharacterIndex));
		PhysicsManager.getInstance().clear();
		characterControllers.clear();
		characters.clear();

		/* Load new map */
		map = mapLoader.getNextMap(state);

		PlayerFactory playerFactory = PlayerFactory.getFactory();
		Vector2 spawn;

		/* Spawn Player */
		spawn = map.getSpawnSwan();
		if (spawn != null) {
			Player p = playerFactory.getSwan(spawn, map.getSize(), state);
			characters.add(p);
			characterControllers.add(new PlayerControls(p));
		}

		spawn = map.getSpawnJupiter();
		if (spawn != null) {
			Player p = playerFactory.getJupiter(spawn, map.getSize(), state);
			characters.add(p);
			characterControllers.add(new PlayerControls(p));
		}

		spawn = map.getSpawnSeal();
		if (spawn != null) {
			Player p = playerFactory.getSeal(spawn, map.getSize(), state);
			characters.add(p);
			characterControllers.add(new PlayerControls(p));
		}

		currentCharacterIndex = 0;

		/* Settup input and camera */
		game.addInputProcessor(characterControllers.get(currentCharacterIndex));
		cam = new CharacterCenteredCamera(map, characters.get(currentCharacterIndex));
	}
}
