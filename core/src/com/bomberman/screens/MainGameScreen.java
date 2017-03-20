package com.bomberman.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.*;
import com.bomberman.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Główny ekran gry.
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
    public float TILE_WIDTH;

    /**
     * Wysokość kafelka.
     */
    public float TILE_HEIGHT;

    /**
     * Siatka współrzędnych, w których są wyświetlane kolejne kafelki.
     */
    public ArrayList<BomberTile> mapGrid;

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
    public final float HERO_SCALING_FACTOR = 0.95f;

    /**
     * Tekstura bomby.
     */
    private Texture bomb;

    /**
     * Tekstura superbomby (litera).
     */
    private Texture superbombLetter;

    /**
     * Tekstura multibomby (litera).
     */
    private Texture multibombLetter;

    /**
     * Tekstura nieaktywnej superbomby, do wyswietlania w panelu.
     */
    private Texture superbombInactive;

    /**
     * Tekstura nieaktywnej multibomby, do wyświetlania w panelu.
     */
    private Texture multibombInactive;

    /**
     * Tekstura fali uderzeniowej.
     */
    private Texture shockwave;

    /**
     * Licznik sekund. Przydatny przy odliczaniu czasu i wybuchu bomb.
     */
    private long timeCounter;

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
    public ArrayList<BomberTile> superbombTiles;

    /**
     * Kamera, która służy do wyświetlania i skalowania ekranu gry.
     */
    private OrthographicCamera camera;

    /**
     * Mówi, czy gracz może użyć superbomby.
     */
    private boolean playerHasSuperbomb;

    /**
     * Mówi, czy gracz może użyć multibomby.
     */
    private boolean playerHasMultibomb;

    /**
     * Czcionka do pisania w panelu użytkownika.
     */
    private BitmapFont font;

    /**
     * Obiekt obsługujący kolizje ze ścianami, ulepszeniami, bombami i falami uderzeniowymi.
     */
    private BomberCollisionHandler collisionHandler;

    /**
     * Czas startu.
     */
    private final double startTime;

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

        TILE_HEIGHT =  Gdx.graphics.getHeight()/currentMap.screenHeight;
        TILE_WIDTH = (Gdx.graphics.getWidth() - game.bomberConfig.panelWidth)/currentMap.screenWidth;

        heroX = mapGrid.get(currentMap.heroPosition).x;
        heroY = mapGrid.get(currentMap.heroPosition).y;

        bombsPlanted = new ArrayList<>();
        activeShockwaves = new ArrayList<>();

        String[] superbombChars = currentMap.superbombPositions.split("\\s");
        superbombTiles = new ArrayList<>();
        for(String c : superbombChars)
            superbombTiles.add(mapGrid.get(Integer.valueOf(c)));

        collisionHandler = new BomberCollisionHandler(this);

        timeCounter = 0;
        startTime = System.currentTimeMillis();
    }

    /**
     * Metoda służąca do inicjalizacji komponentów graficznych.
     */
    @Override
    public void show() {
        hero = new Texture("img" + File.separator + "hero.png");
        wall = new Texture("img" + File.separator + "sciana.png");
        passage = new Texture("img" + File.separator + "przejscie.png");
        obstacle = new Texture("img" + File.separator + "przeszkoda.png");
        bomb = new Texture("img" + File.separator + "bomba.png");
        shockwave = new Texture("img" + File.separator + "shockwave.png");
        superbombLetter = new Texture("img" + File.separator + "superbomba.png");
        multibombLetter = new Texture("img" + File.separator + "multibomba.png");
        multibombInactive = new Texture("img" + File.separator + "multibomba_szara.png");
        superbombInactive = new Texture("img" + File.separator + "superbomba_szara.png");

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        font = new BitmapFont();
        camera.translate(camera.viewportWidth/2, camera.viewportHeight/2);
    }

    /**
     * Główna pętla programu, odpala się ilość razy na sekundę zdefiniowaną w pliku konfiguracyjnym (FPS).
     * @param delta Czas między kolejnymi odpaleniami pętli.
     */
    @Override
    public void render(float delta) {

        DELTA = Gdx.graphics.getDeltaTime();

        timeCounter = (long) (System.currentTimeMillis() - startTime)/1000;

        // ustaw białe tło
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // ustaw kamerę
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
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
            if(!collisionHandler.canPenetrate(heroX + DELTA*SPEED, heroY))
                heroX += DELTA*SPEED;
        }

        // ruch bohatera w lewo
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
        {
            if(!collisionHandler.canPenetrate(heroX - DELTA*SPEED, heroY))
                heroX -= DELTA*SPEED;
        }

        //ruch bohatera w górę
        if(Gdx.input.isKeyPressed(Input.Keys.UP))
        {
            if(!collisionHandler.canPenetrate(heroX, heroY + DELTA*SPEED))
                heroY += DELTA*SPEED;
        }

        // ruch bohatera w dół
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN))
        {
            if(!collisionHandler.canPenetrate(heroX, heroY - DELTA*SPEED))
                heroY -= DELTA*SPEED;
        }

        //podłożenie bomby
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && bombsPlanted.stream().filter(b -> b.isPlantedByUser).count() == 0)
        {
            bombsPlanted.add(new Bomb(
                    false, heroX, heroY, timeCounter, true
            ));
        }

        //podłożenie superbomby
        if(Gdx.input.isKeyJustPressed(Input.Keys.S) && bombsPlanted.stream().filter(b -> b.isPlantedByUser).count() == 0 && playerHasSuperbomb)
        {
            bombsPlanted.add(new Bomb(
                    true, heroX, heroY, timeCounter, true
            ));
            playerHasSuperbomb = false;
        }

        //jeżeli jakaś bomba ma wybuchnąć, znajdź tę bombę
        Optional<Bomb> explodingBomb = bombsPlanted.stream().filter(b -> b.plantingTime + game.bomberConfig.bombWaitingTime == timeCounter).findFirst();

        // usuń wybuchające bomby z podłożonych bomb
        bombsPlanted = bombsPlanted.stream().filter(b-> b.plantingTime + game.bomberConfig.bombWaitingTime != timeCounter).collect(Collectors.toCollection(ArrayList::new));

        // narysuj bohatera
        game.batch.draw(hero, heroX, heroY, HERO_SCALING_FACTOR*TILE_WIDTH, HERO_SCALING_FACTOR*TILE_HEIGHT);

        // narysuj podłożone bomby
        bombsPlanted.stream().forEach(b -> game.batch.draw(bomb, b.x, b.y, TILE_WIDTH, TILE_HEIGHT));

        // jeżeli występuje wybuchająca bomba
        if(explodingBomb.isPresent())
        {
            Bomb b = explodingBomb.get();
            activeShockwaves.add(new Shockwave(b, timeCounter, getTile(b.x, b.y)));
        }

        // dla każdej fali uderzeniowej narysuj tę falę
        activeShockwaves.stream().forEach(s -> {
            getShockwavePath(s.tileOfExplosion, s.bomb.isSuperbomb).stream().forEach(t -> {
                game.batch.draw(shockwave, t.x, t.y, TILE_WIDTH, TILE_HEIGHT);
            });
        });

        //zniszcz przeszkody
        activeShockwaves.stream().filter(s -> s.explosionTime + game.bomberConfig.shockwaveTime == timeCounter).forEach(s -> {
            getShockwavePath(s.tileOfExplosion, s.bomb.isSuperbomb).stream().forEach(t -> {
                if(t.type == 'o')
                    t.type = 'p';
            });
        });

        // usuń kończące się fale uderzeniowe
        activeShockwaves = activeShockwaves.stream().
                filter(s -> s.explosionTime + game.bomberConfig.shockwaveTime  != timeCounter).
                collect(Collectors.toCollection(ArrayList::new));

        //narysuj superbomby
        superbombTiles.stream().forEach(s -> {
            if(s.type == 'p')
                game.batch.draw(superbombLetter, s.x, s.y, TILE_WIDTH, TILE_HEIGHT);
        });

        // zbieranie achievementów
        if(collisionHandler.picksUpSuperbomb(heroX, heroY))
            playerHasSuperbomb = true;

        renderGamePanel();

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

    /**
     * Handler przeskalowania okna.
     * @param width Nowa szerokość okna.
     * @param height Nowa wysokość okna.
     */
    @Override
    public void resize(int width, int height) {
    }

    /**
     * Handler pauzy.
     */
    @Override
    public void pause() {

    }

    /**
     * Handler wznowienia gry.
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
     * Metoda zwalniająca zasoby po zakończeniu działania ekranu.
     */
    @Override
    public void dispose() {
        hero.dispose();
        wall.dispose();
        passage.dispose();
        obstacle.dispose();
        bomb.dispose();
        shockwave.dispose();
        superbombLetter.dispose();
    }

    /**
     * Generuje listę współrzędnych, w któych są wyświetlane kolejne elementy mapy. Rezerwuje miejsce na panel z lewej strony.
     * @return Lista współrzędnych punktów.
     */
    private ArrayList<BomberTile> generateGrid()
    {
        ArrayList<BomberTile> points = new ArrayList<>();
        int panelWidth = game.bomberConfig.panelWidth;

        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

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
        for (int i = 0, j = index; i < factor*game.bomberConfig.shockwaveLength && (j) % currentMap.screenWidth != 0; i++, j--)
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

    /**
     * Rysuje panel gry.
     */
    private void renderGamePanel()
    {
        font.setColor(Color.BLACK);
        font.getData().setScale(2, 2);

        font.draw(game.batch, "Czas:", 10, camera.viewportHeight*0.9f);
        font.draw(game.batch, String.valueOf(timeCounter), 10, camera.viewportHeight*0.85f);

        game.batch.draw(playerHasSuperbomb ? superbombLetter  : superbombInactive, 0, camera.viewportHeight*0.65f, TILE_WIDTH, TILE_HEIGHT);
        game.batch.draw(playerHasMultibomb ? multibombLetter  :  multibombInactive, game.bomberConfig.panelWidth/2, camera.viewportHeight*0.65f, TILE_WIDTH, TILE_HEIGHT);


    }
}
