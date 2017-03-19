package com.bomberman.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.bomberman.BomberMan;
import javafx.geometry.Point2D;

import java.awt.*;
import java.io.File;

/**
 * Klasa reprezentująca ekran głównego menu.
 * @author Paweł Kulig, Wojciech Sobczak
 */
public class MainMenuScreen implements Screen{

    /**
     * Pole przechowujące odniesienie do głównego obiektu gry.
     */
    BomberMan game;

    /**
     * Tekstura aktywnego przycisku "Graj".
     */
    Texture playButtonActive;

    /**
     * Tekstura nieaktywnego przycisku "Graj".
     */
    Texture playButtonInactive;

    /**
     * Tekstura aktywnego przycisku "Najlepze wyniki".
     */
    Texture highScoreButtonActive;

    /**
     * Tekstura nieaktywnego przycisku "Najlepze wyniki".
     */
    Texture highScoreButtonInactive;

    /**
     * Tekstura aktywnego przycisku "Instrukcja".
     */
    Texture manualButtonActive;

    /**
     * Tekstura nieaktywnego przycisku "Instrukcja".
     */
    Texture manualButtonInactive;

    /**
     * Tekstura aktywnego przycisku "Serwer".
     */
    Texture serverButtonActive;

    /**
     * Tekstura nieaktywnego przycisku "Serwer".
     */
    Texture serverButtonInactive;

    /**
     * Tekstura aktywnego przycisku "Koniec".
     */
    Texture exitButtonActive;

    /**
     * Tekstura nieaktywnego przycisku "Koniec".
     */
    Texture exitButtonInactive;

    /**
     * Szerokość przycisków.
     */
    private static final int BUTTON_WIDTH = 250;

    /**
     * Wysokośc przycisków.
     */
    private static final int BUTTON_HEIGHT = 100;

    /**
     * Wysokość okna
     */
    private final int WINDOW_HEIGHT;

    /**
     * Szerokość okna.
     */
    private final int WINDOW_WIDTH;

    /**
     * Mówi, która opcja jest obecnie wybrana.
     */
    public short optionSelected;

    /**
     * Główny konstruktor.
     * @param game Odniesienie do głównego obiektu gry.
     */
    public MainMenuScreen(BomberMan game)
    {
        this.game = game;

        playButtonActive = new Texture("img" + File.separator + "graj_active.png");
        playButtonInactive = new Texture( "img" + File.separator + "graj.png" );
        serverButtonActive = new Texture("img" + File.separator + "serwer_active.png");
        serverButtonInactive = new Texture("img" + File.separator + "serwer.png");
        exitButtonActive = new Texture("img" + File.separator + "koniec_active.png");
        exitButtonInactive = new Texture("img" + File.separator + "koniec.png");
        highScoreButtonActive = new Texture("img" + File.separator + "najlepszewyniki_active.png");
        highScoreButtonInactive = new Texture("img" + File.separator + "najlepszewyniki.png");
        manualButtonActive = new Texture("img" + File.separator + "instrukcja_active.png");
        manualButtonInactive = new Texture( "img" + File.separator + "instrukcja.png" );

        WINDOW_HEIGHT = game.bomberConfig.pixelHeight;
        WINDOW_WIDTH = game.bomberConfig.pixelWidth;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN))
        {
            optionSelected = (short) ((optionSelected + 1) % 5);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP))
        {
            if(optionSelected == 0)
                optionSelected = 4;
            else
                optionSelected = (short) ((optionSelected - 1) % 5);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER))
        {
            if(optionSelected == 0)
            {
                game.setScreen(new MainGameScreen(game, game.bomberConfig.maps.get(0)));
            }

            if(optionSelected == 4)
                System.exit(1);
        }

        game.batch.begin();

        //rysuj przyciski
        if(optionSelected == 0)
            game.batch.draw(playButtonActive, WINDOW_WIDTH/2 - BUTTON_WIDTH/2, WINDOW_HEIGHT*0.8f , BUTTON_WIDTH, BUTTON_HEIGHT);
        else
            game.batch.draw(playButtonInactive, WINDOW_WIDTH/2 - BUTTON_WIDTH/2, WINDOW_HEIGHT*0.8f , BUTTON_WIDTH, BUTTON_HEIGHT);

        if(optionSelected == 1)
            game.batch.draw(manualButtonActive, WINDOW_WIDTH/2 - BUTTON_WIDTH/2, WINDOW_HEIGHT*0.65f , BUTTON_WIDTH, BUTTON_HEIGHT);
        else
            game.batch.draw(manualButtonInactive, WINDOW_WIDTH/2 - BUTTON_WIDTH/2, WINDOW_HEIGHT*0.65f , BUTTON_WIDTH, BUTTON_HEIGHT);

        if(optionSelected == 2)
            game.batch.draw(highScoreButtonActive, WINDOW_WIDTH/2 - BUTTON_WIDTH/2, WINDOW_HEIGHT*0.5f, BUTTON_WIDTH, BUTTON_HEIGHT);
        else
            game.batch.draw(highScoreButtonInactive, WINDOW_WIDTH/2 - BUTTON_WIDTH/2, WINDOW_HEIGHT*0.5f, BUTTON_WIDTH, BUTTON_HEIGHT);

        if(optionSelected == 3)
            game.batch.draw(manualButtonActive, WINDOW_WIDTH/2 - BUTTON_WIDTH/2, WINDOW_HEIGHT*0.35f , BUTTON_WIDTH, BUTTON_HEIGHT);
        else
            game.batch.draw(manualButtonInactive, WINDOW_WIDTH/2 - BUTTON_WIDTH/2, WINDOW_HEIGHT*0.35f , BUTTON_WIDTH, BUTTON_HEIGHT);

        if(optionSelected == 4)
            game.batch.draw(exitButtonActive, WINDOW_WIDTH/2 - BUTTON_WIDTH/2, WINDOW_HEIGHT*0.2f , BUTTON_WIDTH, BUTTON_HEIGHT);
        else
            game.batch.draw(exitButtonInactive, WINDOW_WIDTH/2 - BUTTON_WIDTH/2, WINDOW_HEIGHT*0.2f , BUTTON_WIDTH, BUTTON_HEIGHT);

        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {

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

}
