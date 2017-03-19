package com.bomberman;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
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
