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
import com.bomberman.HighScoresManager;

import java.io.IOException;
import java.util.HashMap;

/**
 * Ekran najwyższych wyników.
 */
public class HighScoreScreen implements Screen, InputProcessor {

    /**
     * Do wyświetlania ekranu.
     */
    private OrthographicCamera camera;

    /**
     * Odniesienie do głównego obiektu gry.
     */
    private BomberMan game;

    /**
     * Czcionka do wypisywania na ekranie.
     */
    private BitmapFont font;

    /**
     * Haszmapa wyświetlanych stringów.
     */
    private HashMap<String, String> stringsDisplayed;


    /**
     * Konstruktor ekranu.
     * @param game Odniesienie do głównego obiektu gry.
     */
    public HighScoreScreen(BomberMan game)
    {
        this.game = game;
        try {
            game.highScoresManager = new HighScoresManager();
        } catch (IOException e) {
            e.printStackTrace();
        }

        stringsDisplayed = getStringsToDisplay();
    }

    /**
     * Metoda wywoływana przy pierwszym odpaleniu okna.
     */
    @Override
    public void show() {

        camera  = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.translate(camera.viewportWidth/2, camera.viewportHeight/2);

        font = new BitmapFont();
        Gdx.input.setInputProcessor(this);
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
        font.setColor(Color.BLACK);
        font.getData().setScale(3, 3);

        displayHighScoresList();
        game.batch.end();

    }

    /**
     * Wyświetla listę najlepszych wyników, posortowaną od najwyższego.
     */
    private void displayHighScoresList()
    {
        font.draw(game.batch, "Najlepsze wyniki:", camera.viewportWidth/3, camera.viewportHeight*0.9f);

        font.getData().setScale(2, 2);
        float offset = 10f;

        for(String key : stringsDisplayed.keySet())
        {
            font.draw(game.batch, key, camera.viewportWidth/4, camera.viewportHeight*0.1f + camera.viewportHeight*0.07f*offset  );
            font.draw(game.batch, stringsDisplayed.get(key), camera.viewportWidth*0.75f,camera.viewportHeight*0.1f + camera.viewportHeight*0.07f*offset );
            offset--;
        }
    }

    /**
     * Zwraca listę stringów, które mają być wyświetlone.
     * @return Haszmapa, klucz - nick, wartość - wynik.
     */
    private HashMap<String, String> getStringsToDisplay()
    {
        HashMap<Integer, String> highScores = game.highScoresManager.getHighScores();
        HashMap<String, String> strings = new HashMap<>();

        for(int i = 0; i < 10; i++)
        {
            if(!highScores.containsKey(i))
                break;

            strings.put(String.valueOf(highScores.get(i)), String.valueOf(game.highScoresManager.getScore(highScores.get(i))));
        }

        return strings;
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
