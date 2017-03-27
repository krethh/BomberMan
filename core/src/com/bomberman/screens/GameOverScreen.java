package com.bomberman.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.bomberman.BomberMan;

/**
 * Ekran wyświetlany po nieudanej grze.
 */
public class GameOverScreen implements Screen {

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
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.begin();
        game.batch.setProjectionMatrix(camera.combined);


        font.draw(game.batch, "Game over", 100, 100);


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
}
