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
     * Lista przechowująca przeciwników bombera.
     */
    public ArrayList<BomberEnemy> enemies;

    /**
     * Martwi przeciwnicy.
     */
    public ArrayList<BomberEnemy> deadEnemies;

    /**
     * Poziom trudności (1,2,3)
     */
    public short difficulty;

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
    public float heroX;

    /**
     * Wspołrzędna Y gracza.
     */
    public float heroY;

    /**
     * Tekstura głównego bohatera.
     */
    private Texture hero;

    /**
     * Tekstura wroga.
     */
    private Texture enemy;

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
    public long timeCounter;

    /**
     * Lista bomb, które w tej chwili są podłożone.
     */
    public ArrayList<Bomb> bombsPlanted;

    /**
     * Lista fal uderzeniowych, które aktualnie mają miejsce.
     */
    public ArrayList<Shockwave> activeShockwaves;

    /**
     * Kafelek, gdzie znajduje się ulepszenie superbomby.
     */
    public ArrayList<BomberTile> superbombTiles;

    /**
     * Kafelki, gdzie znajdują się multibomby.
     */
    public ArrayList<BomberTile> multibombTiles;

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
     * Czas pozyskania multibomby.
     */
    public long multibombAcquisitionTime;

    /**
     * Konstuktor ekranu.
     * @param game Odniesienie do głównej aplikacji gry.
     * @param map Mapa, która będzie narysowana.
     */
    public MainGameScreen(BomberMan game, BomberMap map, short difficulty)
    {
        this.game = game;
        currentMap = map;
        mapGrid = generateGrid();
        SPEED = game.bomberConfig.speed;
        this.difficulty = difficulty;

        TILE_HEIGHT =  Gdx.graphics.getHeight()/currentMap.screenHeight;
        TILE_WIDTH = (Gdx.graphics.getWidth() - game.bomberConfig.panelWidth)/currentMap.screenWidth;

        heroX = mapGrid.get(currentMap.heroPosition).x;
        heroY = mapGrid.get(currentMap.heroPosition).y;

        bombsPlanted = new ArrayList<>();
        activeShockwaves = new ArrayList<>();

        // załaduj superbomby
        String[] superbombChars = currentMap.superbombPositions.split("\\s");
        superbombTiles = new ArrayList<>();
        for(String c : superbombChars)
            superbombTiles.add(mapGrid.get(Integer.valueOf(c)));

        // załaduj multibomby
        String[] multibombChars = currentMap.multibombPositions.split("\\s");
        multibombTiles = new ArrayList<>();
        for (String c : multibombChars)
            multibombTiles.add(mapGrid.get(Integer.valueOf(c)));

        collisionHandler = new BomberCollisionHandler(this);

        timeCounter = 0;
        startTime = System.currentTimeMillis();

        // wypełnij tablicę z wrogami
        String[] enemyPositions = currentMap.enemyPositions.split("\\s");
        enemies = new ArrayList<>();

        for(int i = 0; i < difficulty; i++)
        {
            int index = Integer.valueOf(enemyPositions[i]);
            enemies.add(new BomberEnemy(mapGrid.get(index).x, mapGrid.get(index).y, collisionHandler, game, this));
        }

        deadEnemies = new ArrayList<>();
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
        enemy = new Texture("img" + File.separator + "enemy.png");

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
            if(!collisionHandler.cannotPenetrate(heroX + DELTA*SPEED, heroY))
                heroX += DELTA*SPEED;
        }

        // ruch bohatera w lewo
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
        {
            if(!collisionHandler.cannotPenetrate(heroX - DELTA*SPEED, heroY))
                heroX -= DELTA*SPEED;
        }

        //ruch bohatera w górę
        if(Gdx.input.isKeyPressed(Input.Keys.UP))
        {
            if(!collisionHandler.cannotPenetrate(heroX, heroY + DELTA*SPEED))
                heroY += DELTA*SPEED;
        }

        // ruch bohatera w dół
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN))
        {
            if(!collisionHandler.cannotPenetrate(heroX, heroY - DELTA*SPEED))
                heroY -= DELTA*SPEED;
        }

        //podłożenie bomby
        // jeżeli jest zero bomb lub player ma multibombę i jest mniej niż 3
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE) &&
                (bombsPlanted.stream().filter(b -> b.isPlantedByUser).count() == 0 || (playerHasMultibomb && bombsPlanted.stream().filter(b -> b.isPlantedByUser).count() < 3)))
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
            activeShockwaves.add(new Shockwave(b, timeCounter, getTile(b.x, b.y), mapGrid, game, currentMap));
        }

        // dla każdej fali uderzeniowej narysuj tę falę i sprawdź, czy nie ginie bohater
        activeShockwaves.stream().forEach(s -> {
            s.shockwavePath.stream().forEach(t -> {
                game.batch.draw(shockwave, t.x, t.y, TILE_WIDTH, TILE_HEIGHT);

                if(t == getTile(heroX, heroY))
                    game.setScreen(new GameOverScreen());
            });
        });

        //zniszcz przeszkody
        activeShockwaves.stream().filter(s -> s.explosionTime + game.bomberConfig.shockwaveTime == timeCounter).forEach(s -> {
            s.shockwavePath.stream().forEach(t -> {
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

        // narysuj multibomby
        multibombTiles.stream().forEach(m ->{
            if(m.type == 'p')
                game.batch.draw(multibombLetter, m.x, m.y, TILE_WIDTH, TILE_HEIGHT );
        });

        // zbieranie achievementów
        if(collisionHandler.picksUpSuperbomb(heroX, heroY))
            playerHasSuperbomb = true;

        if(collisionHandler.picksUpMultibomb(heroX, heroY))
        {
            playerHasMultibomb = true;
            multibombAcquisitionTime = timeCounter;
        }

        // sprawdź, czy multibomba się nie przeterminowuje
        if(multibombAcquisitionTime + game.bomberConfig.multibombDuration == timeCounter)
            playerHasMultibomb = false;

        //zniszcz martwych przeciwników
        deadEnemies.stream().forEach(d -> {
            if(enemies.contains(d))
                enemies.remove(d);
        });

        // wykonaj ruchy przeciwników
        enemies.stream().forEach(e -> {
            e.act();
        });

        // rysuj przeciwników
        enemies.stream().forEach(e -> {
            game.batch.draw(enemy, e.x, e.y, 0.95f*TILE_WIDTH, 0.95f*TILE_HEIGHT);
        });

        renderGamePanel();

        game.batch.end();
    }

    /**
     * Zwraca kafelek, do którego należą podane współrzędne.
     * @param x Współrzędna x punktu.
     * @param y Współrzędna y punktu.
     * @return Obiekt kafelka, do którego należy ten punkt.
     */
    public BomberTile getTile(float x, float y)
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
