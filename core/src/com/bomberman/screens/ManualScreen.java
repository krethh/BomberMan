package com.bomberman.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.bomberman.BomberMan;

import java.io.File;

/**
 * Ekran instrukcji dla gracza.
 */
public class ManualScreen implements Screen, InputProcessor {

    /**
     * Pole przechowujące odniesienie do głównego obiektu gry.
     */
    BomberMan game;

    /**
     * Kamera, służąca do obsługi wyświetlania.
     */
    private OrthographicCamera camera;

    /**
     * Tekstura instrukcji.
     */
    private Texture manual;

    /**
     * Konstruktor ekranu
     * @param game Główny obiekt gry
     */
    public ManualScreen(BomberMan game)
    {
        this.game = game;

        Gdx.input.setInputProcessor(this);
    }

    /**
     * Metoda wywoływana przy pierwszym odpaleniu okna.
     */
    @Override
    public void show() {
        manual = new Texture("img" + File.separator + "manual.png");
        camera  = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.translate(camera.viewportWidth/2, camera.viewportHeight/2);
    }

    /**
     * Metoda wywoływana w każdej klatce.
     * @param delta odstęp czasowy między klatkami.
     */
    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        game.batch.begin();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.draw(manual, camera.viewportWidth/2 - 400, camera.viewportHeight/2 - 300, 800, 600);

        game.batch.end();
    }

    /**
     * Handler przeskalowania okna.
     * @param width Nowa szerokość
     * @param height Nowa wysokość
     */
    @Override
    public void resize(int width, int height) {
        camera  = new OrthographicCamera(width, height);
        camera.translate(camera.viewportWidth/2, camera.viewportHeight/2);
    }

    /**
     * Handler pauzy, tu nieużywany.
     */
    @Override
    public void pause() {

    }

    /**
     * Handler wznowienia, tu nieużywany.
     */
    @Override
    public void resume() {

    }

    /**
     * Handler schowania okienka.
     */
    @Override
    public void hide() {

    }

    /**
     * Handler zniszczenia okienka.
     */
    @Override
    public void dispose() {

    }

    /**
     * Handler wciśnięcia przycisku.
     * @param keycode Kod przycisku.
     * @return True, jeżeli sukces.
     */
    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.ESCAPE)
        {
            game.setScreen(new MainMenuScreen(game));
        }
        return true;
    }

    /**
     * Handler podniesienia przycisku
     * @param keycode Kod przycisku
     * @return True, jeżeli sukces.
     */
    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    /**
     * Do obsługi znaków z klawiatury.
     * @param character Wprowadzony znak.
     * @return True jeżeli sukces.
     */
    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    /**
     * Do obsługi ekranów dotykowych, nieużywany.
     * @param screenX
     * @param screenY
     * @param pointer
     * @param button
     * @return
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    /**
     * Do obsługi ekranów dotykowych, nieużywany.
     * @param screenX
     * @param screenY
     * @param pointer
     * @param button
     * @return
     */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    /**
     * Do obsługi ekranów dotykowych, nieużywany.
     * @param screenX
     * @param screenY
     * @param pointer
     * @return
     */
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    /**
     * Handler poruszenia myszką, nieużywany.
     * @param screenX
     * @param screenY
     * @return
     */
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    /**
     * Handler scrollowania, nieużywany.
     * @param amount
     * @return
     */
    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
