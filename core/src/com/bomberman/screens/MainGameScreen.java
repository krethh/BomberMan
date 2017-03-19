package com.bomberman.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bomberman.*;
import javafx.geometry.Point2D;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;


/**
 * Created by Paweł Kulig on 19.03.2017.
 */
public class MainGameScreen implements Screen {

    /**
     * Obecnie wyświetlana mapa.
     */
    public BomberMap currentMap;

    /**
     * Okres pomiędzy kolejnymi odświeżeniami ekranu.
     */
    public static float DELTA;

    /**
     * Szybkość przemieszczania się graczy i przeciwników.
     */
    public static float SPEED;

    /**
     * Szerokośc kafelka.
     */
    private final int TILE_WIDTH;

    /**
     * Wysokość kafelka.
     */
    private final int TILE_HEIGHT;

    /**
     * Siatka współrzędnych, w których są wyświetlane kolejne kafelki.
     */
    private ArrayList<BomberTile> mapGrid;

    /**
     * Odniesienie do głównej klasy gry.
     */
    private BomberMan game;

    /**
     * Obecna pozycja gracza.
     */
    private Point2D heroPosition;

    /**
     * Tekstura głównego bohatera.
     */
    private Texture hero;

    /**
     * Tekstura ściany.
     */
    private Texture wall;

    /**
     * Tekstura przejścia.
     */
    private Texture passage;

    /**
     * Tekstura przeszkody.
     */
    private Texture obstacle;

    /**
     * Konstuktor ekranu.
     * @param game Odniesienie do głównej aplikacji gry.
     * @param map Mapa, która będzie narysowana.
     */
    public MainGameScreen(BomberMan game, BomberMap map)
    {
        this.game = game;
        currentMap = map;
        mapGrid = generateGrid();

        TILE_HEIGHT =  game.bomberConfig.pixelHeight/currentMap.screenHeight;
        TILE_WIDTH = (game.bomberConfig.pixelWidth - game.bomberConfig.panelWidth)/currentMap.screenWidth;

        heroPosition = new Point2D(mapGrid.get(currentMap.heroPosition).x, mapGrid.get(currentMap.heroPosition).y);
    }

    @Override
    public void show() {
        hero = new Texture("img" + File.separator + "hero.png");
        wall = new Texture("img" + File.separator + "sciana.png");
        passage = new Texture("img" + File.separator + "przejscie.png");
        obstacle = new Texture("img" + File.separator + "przeszkoda.png");

        SPEED = 120;
    }

    @Override
    public void render(float delta) {
        DELTA = Gdx.graphics.getDeltaTime();

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        //rysuj kafelki
        mapGrid.stream().forEach(p -> {
            if(p.type == 'w')
            {
                game.batch.draw(wall, p.x, p.y, TILE_WIDTH, TILE_HEIGHT);
            }
            if(p.type == 'o')
            {
                game.batch.draw(obstacle, p.x, p.y, TILE_WIDTH, TILE_HEIGHT);
            }
            if(p.type == 'p')
            {
                game.batch.draw(passage, p.x, p.y, TILE_WIDTH, TILE_HEIGHT);
            }
        });

        game.batch.draw(hero, (float) heroPosition.getX(), (float) heroPosition.getY(), TILE_WIDTH, TILE_HEIGHT);
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

    /**
     * Generuje listę współrzędnych, w któych są wyświetlane kolejne elementy mapy. Rezerwuje miejsce na panel z lewej strony.
     * @return Lista współrzędnych punktów.
     */
    private ArrayList<BomberTile> generateGrid()
    {
        ArrayList<BomberTile> points = new ArrayList<>();
        int height = game.bomberConfig.pixelHeight;
        int width = game.bomberConfig.pixelWidth;
        int panelWidth = game.bomberConfig.panelWidth;

        int index = 0;
        for(int i = 0; i < currentMap.screenHeight; i++)
            for(int j = 0; j < currentMap.screenWidth; j++)
            {
                points.add(new BomberTile(
                        (1 - (float) 100/width )*j*width/currentMap.screenWidth + 100,
                        (float) i*height/currentMap.screenHeight,
                        currentMap.mapObjects.charAt(index)
                ));
                index++;
            }
        return points;
    }
}
