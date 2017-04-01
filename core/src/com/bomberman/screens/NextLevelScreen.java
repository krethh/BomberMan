package com.bomberman.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.bomberman.BomberMan;
import com.sun.org.apache.xpath.internal.operations.Or;

/**
 * Created by Wojtek on 21.03.2017.
 */
public class NextLevelScreen implements Screen {

    /**
     * Odniesienie do głównego obiektu gry.
     */
    private BomberMan game;

    /**
     * Font do wypisywania tekstu.
     */
    private BitmapFont font;

    /**
     * Do orientowania wyświetlanego obrazu.
     */
    private OrthographicCamera camera;

    /**
     * Główny konstruktor
     * @param game Odniesienie do głównego obiektu gry.
     */
    public NextLevelScreen(BomberMan game)
    {
        this.game = game;

        //update najwyższego wyniku gracza
        game.highScoresManager.setScoreIfBetter(game.nick, game.points);
    }

    /**
     * Metoda wywoływana przy pierwszym odpaleniu okna.
     */
    @Override
    public void show() {
        camera  = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.translate(camera.viewportWidth/2, camera.viewportHeight/2);
        font = new BitmapFont();
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

        font.getData().setScale(2, 2);
        font.setColor(Color.BLACK);

        font.draw(game.batch, "Gratulacje, ukończyłeś poziom!", camera.viewportWidth * 0.4f, camera.viewportHeight*0.8f);
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
}
