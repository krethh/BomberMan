package com.bomberman.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.bomberman.BomberMan;
import com.bomberman.BomberMap;

import java.io.IOException;

/**
 * Ekran wyświetlany po nieudanej grze.
 */
public class GameOverScreen implements Screen, InputProcessor {

    /**
     * Odniesienie do głównego obiektu gry.
     */
    private BomberMan game;

    /**
     * Kamera, służąca do obsługi wyświetlania ekranu.
     */
    private OrthographicCamera camera;

    /**
     * Czcionka do rysowania napisu.
     */
    private BitmapFont font;

    /**
     * Konstruktor domyślny.
     * @param game Gra, do której należy ekran.
     */
    public GameOverScreen(BomberMan game){
        this.game = game;

        //update najwyższego wyniku gracza
        game.highScoresManager.setScoreIfBetter(game.nick, game.points);
        game.highScoresManager.sendHighScoresToServer(game);

        Gdx.input.setInputProcessor(this);
    }

    /**
     * Metoda wywoływana przy pierwszym uruchomieniu ekranu.
     */
    @Override
    public void show() {
        camera  = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.translate(camera.viewportWidth/2, camera.viewportHeight/2);
        font = new BitmapFont();
    }

    /**
     * Metoda wywoływana w każdej klatce.
     * @param delta Odstęp czasowy między klatkami.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.begin();
        game.batch.setProjectionMatrix(camera.combined);

        font.setColor(Color.BLACK);
        font.getData().setScale(2, 2);
        font.draw(game.batch, "Gra zakonczona, przegrales!", camera.viewportWidth * 0.4f, camera.viewportHeight*0.8f);
        font.draw(game.batch, "Punkty: " + String.valueOf(game.points), camera.viewportWidth * 0.4f, camera.viewportHeight*0.6f);


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

        if(keycode == Input.Keys.ENTER)
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
