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

    public static final float SPEED = 120;
    public static float DELTA;
    public OrthographicCamera camera;

	/**
	 * Obiekt zawierający całą konfigurację gry.
	 */
	public BomberConfig bomberConfig;

	/**
	 * Kontener służący do przechowywania tekstur.
	 */
	public SpriteBatch batch;

	/**
	 * Mapa, która obecnie jest grana.
	 */
	public BomberMap currentMap;

	/**
	 * Główny konstruktor gry.
	 * @param bomberConfig Obiekt konfiguracyjny, tworzony w klasie DesktopLauncher.
	 */
	public BomberMan(BomberConfig bomberConfig)
	{
		this.bomberConfig = bomberConfig;
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
