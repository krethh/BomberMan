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

    /**
     * Czcionka używana do wypisywania.
     */
    private BitmapFont font;

    /**
     * Kamera do wyświetlania ekranu.
     */
    private OrthographicCamera camera;

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

        Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.begin();
        game.batch.setProjectionMatrix(camera.combined);

        font.setColor(Color.BLACK);
        font.getData().setScale(3, 3);

        font.draw(game.batch, "Narazie nic tu nie ma.", Gdx.graphics.getWidth()/3 , Gdx.graphics.getHeight()/2);
        font.draw(game.batch, "Wciśnij ESC żeby powrócić", Gdx.graphics.getWidth()/3 , Gdx.graphics.getHeight()/2 + 50);

        //rysuj przyciski
        game.batch.draw(connecting, WINDOW_WIDTH/2 - BUTTON_WIDTH/2, WINDOW_HEIGHT*0.8f , BUTTON_WIDTH, BUTTON_HEIGHT);
        font.draw(game.batch, "Connecting...", 100, 100);
        game.batch.end();
    }

    /**
     * Handler przeskalowania okna.
     * @param width Nowa szerokość
     * @param height Nowa wysokość
     */
    @Override
    public void resize(int width, int height) {

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
