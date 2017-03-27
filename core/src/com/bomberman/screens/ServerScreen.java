package com.bomberman.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.bomberman.BomberMan;

import java.io.File;

/**
 * Created by Wojtek on 21.03.2017.
 */
public class ServerScreen implements Screen, InputProcessor {

    /**
     * Pole przechowujące odniesienie do głównego obiektu gry.
     */
    BomberMan game;

    /**
     * Tekstura "Laczenie".
     */
    Texture connecting;

    /**
     * Tekstura "Pobieranie".
     */
    Texture downloading;

    /**
     * Tekstura "Pobieranie powiodlo sie.".
     */
    Texture downloadSuccess;

    /**
     * Tekstura "Pobieranie nie powiodlo sie.".
     */
    Texture downloadError;



    /**
     * Szerokość tekstur.
     */
    private static int BUTTON_WIDTH = 250;

    /**
     * Wysokośc tekstur.
     */
    private static int BUTTON_HEIGHT = 100;

    /**
     * Wysokość okna
     */
    private final int WINDOW_HEIGHT;

    /**
     * Szerokość okna.
     */
    private final int WINDOW_WIDTH;


    private BitmapFont font;


    OrthographicCamera camera;

    /**
     * Główny konstruktor.
     * @param game Odniesienie do głównego obiektu gry.
     */

    public ServerScreen(BomberMan game)
    {
        this.game = game;

        connecting = new Texture("img" + File.separator + "laczenie.png");
        //downloading = new Texture("img" + File.separator + "downloading.png");
        // downloadSuccess= new Texture("img" + File.separator + "DownloadSuccess.png");
        // downloadError = new Texture("img" + File.separator + "downloadError.png");

        WINDOW_HEIGHT = game.bomberConfig.pixelHeight;
        WINDOW_WIDTH = game.bomberConfig.pixelWidth;

        Gdx.input.setInputProcessor(this);
        font = new BitmapFont();
    }

    @Override
    public void show() {
        camera  = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.translate(camera.viewportWidth/2, camera.viewportHeight/2);
    }

    @Override
    public void render(float delta) {
        update();

        Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.begin();
        game.batch.setProjectionMatrix(camera.combined);

        //rysuj przyciski
        game.batch.draw(connecting, WINDOW_WIDTH/2 - BUTTON_WIDTH/2, WINDOW_HEIGHT*0.8f , BUTTON_WIDTH, BUTTON_HEIGHT);
        font.draw(game.batch, "Connecting...", 100, 100);
        game.batch.end();

    }

    public void update()
    {

    }


    @Override
    public void resize(int width, int height) {
        game.camera = new OrthographicCamera(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
