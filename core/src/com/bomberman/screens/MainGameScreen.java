package com.bomberman.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.bomberman.*;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;


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
    public final float SPEED;

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
     * Współrzędna X gracza.
     */
    private float heroX;

    /**
     * Wspołrzędna Y gracza.
     */
    private float heroY;

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
     * Definiuje jaką część kafelka wypełnia postać. Przydatne dla płynniejszego poruszania się.
     */
    private final float HERO_SCALING_FACTOR = 0.95f;

    /**
     * Kafelek, w którym obecnie znajduje się bohater.
     */
    private short currentHeroTile;

    /**
     * Tekstura bomby.
     */
    private Texture bomb;

    /**
     * Tekstura superbomby.
     */
    private Texture superbomb;

    /**
     * Tekstura fali uderzeniowej.
     */
    private Texture shockwave;

    /**
     * Licznik klatek. Przydatny przy odliczaniu czasu i wybuchu bomb.
     */
    private long frameCounter;

    /**
     * Lista bomb, które w tej chwili są podłożone.
     */
    private ArrayList<Bomb> bombsPlanted;

    /**
     * Lista fal uderzeniowych, które aktualnie mają miejsce.
     */
    private ArrayList<Shockwave> activeShockwaves;

    /**
     * Kafelek, gdzie znajduje się ulepszenie superbomby.
     */
    private ArrayList<BomberTile> superbombTiles;

    /**
     * Mówi, czy gracz może użyć superbomby.
     */
    private boolean playerHasSuperbomb;

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
        SPEED = game.bomberConfig.speed;

        TILE_HEIGHT =  game.bomberConfig.pixelHeight/currentMap.screenHeight;
        TILE_WIDTH = (game.bomberConfig.pixelWidth - game.bomberConfig.panelWidth)/currentMap.screenWidth;

        heroX = mapGrid.get(currentMap.heroPosition).x;
        heroY = mapGrid.get(currentMap.heroPosition).y;
        currentHeroTile = currentMap.heroPosition;

        bombsPlanted = new ArrayList<>();
        activeShockwaves = new ArrayList<>();

        String[] superbombChars = currentMap.superbombPositions.split("\\s");
        superbombTiles = new ArrayList<>();
        for(String c : superbombChars)
            superbombTiles.add(mapGrid.get(Integer.valueOf(c)));
    }

    @Override
    public void show() {
        hero = new Texture("img" + File.separator + "hero.png");
        wall = new Texture("img" + File.separator + "sciana.png");
        passage = new Texture("img" + File.separator + "przejscie.png");
        obstacle = new Texture("img" + File.separator + "przeszkoda.png");
        bomb = new Texture("img" + File.separator + "bomba.png");
        shockwave = new Texture("img" + File.separator + "shockwave.png");
        superbomb = new Texture("img" + File.separator + "superbomba.png");
    }

    @Override
    public void render(float delta) {
        DELTA = Gdx.graphics.getDeltaTime();
        frameCounter++;


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

        /// ruch bohatera w prawo
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
        {
            if(!hasCollisionWith(heroX + DELTA*SPEED, heroY))
                heroX += DELTA*SPEED;
        }

        // ruch bohatera w lewo
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
        {
            if(!hasCollisionWith(heroX - DELTA*SPEED, heroY))
                heroX -= DELTA*SPEED;
        }

        //ruch bohatera w górę
        if(Gdx.input.isKeyPressed(Input.Keys.UP))
        {
            if(!hasCollisionWith(heroX, heroY + DELTA*SPEED))
                heroY += DELTA*SPEED;
        }

        // ruch bohatera w dół
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN))
        {
            if(!hasCollisionWith(heroX, heroY - DELTA*SPEED))
                heroY -= DELTA*SPEED;
        }

        //podłożenie bomby
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && bombsPlanted.stream().filter(b -> b.isPlantedByUser).count() == 0)
        {
            bombsPlanted.add(new Bomb(
                    false, heroX, heroY, frameCounter, true
            ));
        }

        //podłożenie superbomby
        if(Gdx.input.isKeyJustPressed(Input.Keys.S) && bombsPlanted.stream().filter(b -> b.isPlantedByUser).count() == 0 && playerHasSuperbomb)
        {
            bombsPlanted.add(new Bomb(
                    true, heroX, heroY, frameCounter, true
            ));
            playerHasSuperbomb = false;
        }

        //jeżeli jakaś bomba ma wybuchnąć, weź tę bombę
        Optional<Bomb> explodingBomb = bombsPlanted.stream().filter(b -> b.plantingTime + 180 == frameCounter).findFirst();

        bombsPlanted = bombsPlanted.stream().filter(b-> b.plantingTime + game.bomberConfig.bombWaitingTime != frameCounter).collect(Collectors.toCollection(ArrayList::new));

        game.batch.draw(hero, heroX, heroY, HERO_SCALING_FACTOR*TILE_WIDTH, HERO_SCALING_FACTOR*TILE_HEIGHT);
        bombsPlanted.stream().forEach(b -> game.batch.draw(bomb, b.x, b.y, TILE_WIDTH, TILE_HEIGHT));

        if(explodingBomb.isPresent())
        {
            Bomb b = explodingBomb.get();
            activeShockwaves.add(new Shockwave(b, frameCounter, getTile(b.x, b.y)));
        }

        activeShockwaves.stream().forEach(s -> {
            getShockwavePath(s.tileOfExplosion, s.bomb.isSuperbomb).stream().forEach(t -> {
                game.batch.draw(shockwave, t.x, t.y, TILE_WIDTH, TILE_HEIGHT);
            });
        });

        //zniszcz przeszkody
        activeShockwaves.stream().filter(s -> s.explosionTime + game.bomberConfig.shockwaveTime == frameCounter).forEach(s -> {
            getShockwavePath(s.tileOfExplosion, s.bomb.isSuperbomb).stream().forEach(t -> {
                if(t.type == 'o')
                    t.type = 'p';
            });
        });

        // usuń kończące się fale uderzeniowe
        activeShockwaves = activeShockwaves.stream().
                filter(s -> s.explosionTime + game.bomberConfig.shockwaveTime != frameCounter).
                collect(Collectors.toCollection(ArrayList::new));

        //narysuj superbomby
        superbombTiles.stream().forEach(s -> {
            if(s.type == 'p')
                game.batch.draw(superbomb, s.x, s.y, TILE_WIDTH, TILE_HEIGHT);
        });

        game.batch.end();
    }

    /**
     * Zwraca kafelek, do którego należą podane współrzędne.
     * @param x Współrzędna x punktu.
     * @param y Współrzędna y punktu.
     * @return Obiekt kafelka, do którego należy ten punkt.
     */
    private BomberTile getTile(float x, float y)
    {
        int i = 0;
        BomberTile smallest = mapGrid.get(i);
        ArrayList<BomberTile> passages = mapGrid.stream().filter(t -> t.type == 'p').collect(Collectors.toCollection(ArrayList::new));
        for (; i < passages.size(); i++)
        {
            if( Math.pow(passages.get(i).x - x, 2) + Math.pow(passages.get(i).y - y, 2) < Math.pow(smallest.x - x, 2) + Math.pow(smallest.y - y, 2) )
                smallest = passages.get(i);
        }
        return smallest;
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

    /// TODO: zoptymalizuj to jakoś normalnie bo szkoda gadać
    private boolean hasCollisionWith(float x1, float y1)
    {
        ArrayList<BomberTile> walls = mapGrid.stream().filter(t -> t.type == 'w' || t.type == 'o').collect(Collectors.toCollection(ArrayList::new));
        walls.addAll(superbombTiles);

        for(BomberTile t : walls)
        {
            if(x1 < t.x + TILE_WIDTH && y1 < t.y + TILE_HEIGHT && x1 + HERO_SCALING_FACTOR*TILE_WIDTH > t.x && y1 + HERO_SCALING_FACTOR*TILE_HEIGHT > t.y)
            {
                // zbieranie achievementów
                if(t.type == 'p' && superbombTiles.contains(t))
                {
                    superbombTiles.remove(t);
                    playerHasSuperbomb = true;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Zwraca listę kafelków, do których powinna dotrzeć fala uderzeniowa.
     * @param start Kafelek, w którym wybuchła bomba.
     * @return Lista kafelków, do których dotrze fala uderzeniowa.
     */
    private ArrayList<BomberTile> getShockwavePath(BomberTile start, boolean isSuperbomb)
    {
        int factor = isSuperbomb ? 2 : 1;
        ArrayList<BomberTile> shockwaveTiles = new ArrayList<>();
        shockwaveTiles.add(start);

        //znajdz pozycje w liscie kafelków
        int index = 0;
        for (; index < mapGrid.size(); index++)
        {
            if(mapGrid.get(index) == start)
                break;
        }

        // w prawo
        for (int i = 0, j = index; i < factor*game.bomberConfig.shockwaveLength && (j + 1) % currentMap.screenWidth != 0; i++, j++)
        {
            if(mapGrid.get(j).type == 'w')
                break;
            if(mapGrid.get(j).type == 'o')
            {
                shockwaveTiles.add(mapGrid.get(j));
                if(factor == 1)
                    break;
                else
                    factor = 1;
            }
            if(mapGrid.get(j).type == 'p')
                shockwaveTiles.add(mapGrid.get(j));
        }

        // w lewo
        for (int i = 0, j = index; i < factor*game.bomberConfig.shockwaveLength && (j - 1) % currentMap.screenWidth != 0; i++, j--)
        {
            if(mapGrid.get(j).type == 'w')
                break;
            if(mapGrid.get(j).type == 'o')
            {
                shockwaveTiles.add(mapGrid.get(j));
                if(factor == 1)
                    break;
                else
                    factor = 1;
            }
            if(mapGrid.get(j).type == 'p')
                shockwaveTiles.add(mapGrid.get(j));
        }

        // w górę
        for (int i = 0, j = index; i < factor*game.bomberConfig.shockwaveLength && (j + 16) < currentMap.screenWidth * currentMap.screenHeight; i++, j+=16)
        {
            if(mapGrid.get(j).type == 'w')
                break;
            if(mapGrid.get(j).type == 'o')
            {
                shockwaveTiles.add(mapGrid.get(j));
                if(factor == 1)
                    break;
                else
                    factor = 1;
            }
            if(mapGrid.get(j).type == 'p')
                shockwaveTiles.add(mapGrid.get(j));
        }

        // w dół
        for (int i = 0, j = index; i < factor*game.bomberConfig.shockwaveLength && (j - 16) > 0; i++, j-=16) {
            if (mapGrid.get(j).type == 'w')
                break;
            if (mapGrid.get(j).type == 'o') {
                shockwaveTiles.add(mapGrid.get(j));
                if(factor == 1)
                    break;
                else
                    factor = 1;
            }
            if (mapGrid.get(j).type == 'p')
                shockwaveTiles.add(mapGrid.get(j));
        }
        return shockwaveTiles;
    }
}
