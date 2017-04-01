package com.bomberman;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bomberman.screens.MainGameScreen;
import com.bomberman.screens.MainMenuScreen;

import java.io.IOException;

/**
 * Główna klasa gry.
 * @author Paweł Kulig, Wojciech Sobczak
 */
public class BomberMan extends Game {

    public OrthographicCamera camera;

	/**
	 * Obiekt zawierający całą konfigurację gry.
	 */
	public BomberConfig bomberConfig;

	/**
	 * Obiekt zarządzający najwyższymi wynikami.
	 */
	public HighScoresManager highScoresManager;

	/**
	 * Kontener służący do przechowywania tekstur.
	 */
	public SpriteBatch batch;

	/**
	 * Nick gracza
	 */
	public String nick;

	/**
	 * Mapa, która obecnie jest grana.
	 */
	public BomberMap currentMap;

	/**
	 * Obecna ilość punktów gracza.
	 */
	public int points;

	/**
	 * Główny konstruktor gry.
	 * @param bomberConfig Obiekt konfiguracyjny, tworzony w klasie DesktopLauncher.
	 */
	public BomberMan(BomberConfig bomberConfig, HighScoresManager highScoresManager)
	{
		this.bomberConfig = bomberConfig;
		this.highScoresManager = highScoresManager;
		currentMap = null;
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.translate(camera.viewportWidth/2, camera.viewportHeight/2);

        this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
	    super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}

}
