package com.bomberman.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.*;
import com.bomberman.*;
import com.bomberman.interfaces.CollisionObserver;

import java.io.File;
import java.util.ArrayList;
import java.util.stream.Collectors;


/**
 * Główny ekran gry.
 */
public class MainGameScreen implements Screen, InputProcessor, CollisionObserver {

    /**
     * Obecnie wyświetlana mapa.
     */
    public BomberMap currentMap;

    /**
     * Okres pomiędzy kolejnymi odświeżeniami ekranu.
     */
    public static float DELTA;

    /**
     * Szybkość przemieszczania się gracza.
     */
    public final float SPEED;

    /**
     * Szybkośc poruszania się przeciwników.
     */
    public final float ENEMY_SPEED;

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
     *
     */
    public BomberHero hero;

    /**
     * Tekstura głównego bohatera.
     */
    private Texture heroTexture;

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
    public final float HERO_SCALING_FACTOR = 0.9f;

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
    private BomberPenetrationHandler penetrationHandler;

    /**
     * Czas startu.
     */
    private final double startTime;

    /**
     * Czy gra jest spauzowana.
     */
    private boolean paused;

    /**
     * Czas wciśnięcia pauzy.
     */
    private double pauseTime;

    /**
     * Offset wywołany wciśnięciami pauzy.
     */
    private double pauseOffset;

    /**
     * Czas pozyskania multibomby.
     */
    public long multibombAcquisitionTime;

    /**
     * Czy gracz rusza się w prawo.
     */
    private boolean movesRight;

    /**
     * Czy gracz rusza się w lewo.
     */
    private boolean movesLeft;

    /**
     * Czy gracz rusza się w górę.
     */
    private boolean movesUp;

    /**
     * Czy gracz rusza się w dół
     */
    private boolean movesDown;

    /**
     * Konstuktor ekranu.
     * @param game Odniesienie do głównej aplikacji gry.
     * @param map Mapa, która będzie narysowana.
     */
    public MainGameScreen(BomberMan game, BomberMap map, short difficulty)
    {
        Gdx.input.setInputProcessor(this);

        this.game = game;
        currentMap = map;
        mapGrid = generateGrid();

        // przeskalowanie prędkości, w stosunku do rozmiaru okna
        SPEED = game.bomberConfig.speed*Gdx.graphics.getWidth()/800;

        // przeciwnicy są trochę szybsi, żeby kompensować za ich głupotę
        ENEMY_SPEED = 1.1f*SPEED;

        this.difficulty = difficulty;

        TILE_HEIGHT =  Gdx.graphics.getHeight()/currentMap.screenHeight;
        TILE_WIDTH = (Gdx.graphics.getWidth() - game.bomberConfig.panelWidth)/currentMap.screenWidth;

        hero = new BomberHero(mapGrid.get(currentMap.heroPosition).getX(), mapGrid.get(currentMap.heroPosition).getY(), this);

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

        penetrationHandler = new BomberPenetrationHandler(this);

        timeCounter = 0;
        startTime = System.currentTimeMillis();

        // wypełnij tablicę z wrogami
        String[] enemyPositions = currentMap.enemyPositions.split("\\s");
        enemies = new ArrayList<>();

        for(int i = 0; i < difficulty; i++)
        {
            int index = Integer.valueOf(enemyPositions[i]);
            enemies.add(new BomberEnemy(mapGrid.get(index).getX(), mapGrid.get(index).getY(), penetrationHandler, game, this));
        }

        deadEnemies = new ArrayList<>();
    }

    /**
     * Metoda służąca do inicjalizacji komponentów graficznych.
     */
    @Override
    public void show() {
        heroTexture = new Texture("img" + File.separator + "hero.png");
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

        if(!paused)
            update();

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
                game.batch.draw(wall, p.getX(), p.getY(), TILE_WIDTH, TILE_HEIGHT);
            }
            if(p.type == 'o')
            {
                game.batch.draw(obstacle, p.getX(), p.getY(), TILE_WIDTH, TILE_HEIGHT);
            }
            if(p.type == 'p')
            {
                game.batch.draw(passage, p.getX(), p.getY(), TILE_WIDTH, TILE_HEIGHT);
            }
        });

        // narysuj bohatera
        game.batch.draw(heroTexture, hero.getX(), hero.getY(), HERO_SCALING_FACTOR*TILE_WIDTH, HERO_SCALING_FACTOR*TILE_HEIGHT);

        // narysuj podłożone bomby
        bombsPlanted.stream().forEach(b -> game.batch.draw(bomb, b.x, b.y, TILE_WIDTH, TILE_HEIGHT));

        // dla każdej fali uderzeniowej narysuj tę falę i sprawdź, czy nie ginie bohater
        activeShockwaves.stream().forEach(s -> {
            s.shockwavePath.stream().forEach(t -> {
                game.batch.draw(shockwave, t.getX(), t.getY(), TILE_WIDTH, TILE_HEIGHT);
            });
        });

        //narysuj superbomby
        superbombTiles.stream().forEach(s -> {
            if(s.type == 'p')
                game.batch.draw(superbombLetter, s.getX(), s.getY(), TILE_WIDTH, TILE_HEIGHT);
        });

        // narysuj multibomby
        multibombTiles.stream().forEach(m ->{
            if(m.type == 'p')
                game.batch.draw(multibombLetter, m.getX(), m.getY(), TILE_WIDTH, TILE_HEIGHT );
        });

        // rysuj przeciwników
        enemies.stream().forEach(e -> {
            game.batch.draw(enemy, e.getX(), e.getY(), HERO_SCALING_FACTOR*TILE_WIDTH, HERO_SCALING_FACTOR*TILE_HEIGHT);
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
            if( Math.pow(passages.get(i).getX() - x, 2) + Math.pow(passages.get(i).getY() - y, 2) < Math.pow(smallest.getX() - x, 2) + Math.pow(smallest.getY() - y, 2) )
                smallest = passages.get(i);
        }
        return smallest;
    }

    /**
     * Aktualizuje świat gry w każdym wywołaniu pętli.
     */
    private void update()
    {
        timeCounter = (long) (System.currentTimeMillis() - pauseOffset - startTime)/1000;

        hero.checkCollisions();
        checkExplosions();
        if(movesRight)
            if(!penetrationHandler.cannotPenetrate(hero.getX() + DELTA*SPEED, hero.getY()))
                hero.moveRight();

        if(movesLeft)
            if(!penetrationHandler.cannotPenetrate(hero.getX() - DELTA*SPEED, hero.getY()))
                hero.moveLeft();

        if(movesUp)
            if(!penetrationHandler.cannotPenetrate(hero.getX(), hero.getY() + DELTA*SPEED))
                hero.moveUp();

        if(movesDown)
            if(!penetrationHandler.cannotPenetrate(hero.getX(), hero.getY() - DELTA*SPEED))
                hero.moveDown();

        //zniszcz martwych przeciwników
        deadEnemies.stream().forEach(d -> {
            if(enemies.contains(d))
                enemies.remove(d);
        });

        // sprawdź, czy multibomba się nie przeterminowuje
        if(multibombAcquisitionTime + game.bomberConfig.multibombDuration == timeCounter)
            playerHasMultibomb = false;

        // wykonaj ruchy przeciwników
        enemies.stream().forEach(e -> {
            e.act();
        });

        // usuń kończące się fale uderzeniowe
        activeShockwaves = activeShockwaves.stream().
                filter(s -> s.explosionTime + game.bomberConfig.shockwaveTime  != timeCounter).
                collect(Collectors.toCollection(ArrayList::new));
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
        heroTexture.dispose();
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
                        currentMap.mapObjects.charAt(index),
                        this
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

    /**
     * Handler przyciśnięcia przycisku.
     * @param keycode Kod wciśniętego przycisku.
     * @return true, jeżeli sukces.
     */
    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.RIGHT)
            movesRight = true;

        if(keycode == Input.Keys.LEFT)
            movesLeft = true;

        if(keycode == Input.Keys.UP)
            movesUp = true;

        if(keycode == Input.Keys.DOWN)
            movesDown = true;

        if(keycode == Input.Keys.P)
        {
            if(!paused)
            {
                pauseTime = System.currentTimeMillis();
                paused = true;
            }
            else
            {
                pauseOffset += System.currentTimeMillis() - pauseTime;
                paused = false;
            }
        }

        //podłożenie bomby
        // jeżeli jest zero bomb lub player ma multibombę i jest mniej niż 3
        if(keycode == Input.Keys.SPACE)
        {
            if(bombsPlanted.stream().filter(b -> b.isPlantedByUser).count() == 0 || (playerHasMultibomb && bombsPlanted.stream().filter(b -> b.isPlantedByUser).count() < 3))
                bombsPlanted.add(new Bomb(
                        false, hero.getX(), hero.getY(), timeCounter, true
                ));
        }

        //podłożenie superbomby
        if(keycode == Input.Keys.S && bombsPlanted.stream().filter(b -> b.isPlantedByUser).count() == 0 && playerHasSuperbomb)
        {
            bombsPlanted.add(new Bomb(
                    true, hero.getX(), hero.getY(), timeCounter, true
            ));
            playerHasSuperbomb = false;
        }

        return true;
    }

    /**
     * Handler podniesienia przycisku.
     * @param keycode Podniesiony przycisk
     * @return True, jeżeli sukces.
     */
    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.RIGHT)
            movesRight = false;

        if(keycode == Input.Keys.LEFT)
            movesLeft = false;

        if(keycode == Input.Keys.UP)
            movesUp = false;

        if(keycode == Input.Keys.DOWN)
            movesDown = false;

        return true;
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

    /**
     * Obsługuje zdarzenie kolizji.
     * @param collision Kolizja.
     */
    @Override
    public void handleCollision(BomberCollision collision) {

        if(collision.type == BomberCollision.collisionType.HERO_SHOCKWAVE)
        {
            game.setScreen(new GameOverScreen(game));
        }

        if(collision.type == BomberCollision.collisionType.MULTIBOMB)
        {
            multibombTiles.remove(collision.secondObject);
            playerHasMultibomb = true;
            multibombAcquisitionTime = timeCounter;
        }

        if(collision.type == BomberCollision.collisionType.SUPERBOMB)
        {
            superbombTiles.remove(collision.secondObject);
            playerHasSuperbomb = true;
        }

        if(collision.type == BomberCollision.collisionType.ENEMY_SHOCKWAVE)
        {
            ((BomberEnemy) collision.firstObject).die();
        }
    }

    /**
     * Sprawdza, czy w danym momencie występują nowe eksplozje.
     */
    private void checkExplosions()
    {
        ArrayList<Bomb> explodingBombs = bombsPlanted.stream().filter(b -> b.plantingTime + game.bomberConfig.bombWaitingTime == timeCounter).collect(Collectors.toCollection(ArrayList::new));
        bombsPlanted = bombsPlanted.stream().filter(b -> b.plantingTime + game.bomberConfig.bombWaitingTime != timeCounter).collect(Collectors.toCollection(ArrayList::new));

        explodingBombs.stream().forEach(b -> {
            if(b.plantingTime + game.bomberConfig.bombWaitingTime == timeCounter)
            {
                Shockwave s = new Shockwave(b, timeCounter, getTile(b.x, b.y), mapGrid, game, currentMap);
                activeShockwaves.add(s);

                BomberExplosion explosion = new BomberExplosion(s);
                mapGrid.stream().filter(t -> t.type == 'o').forEach(t -> explosion.register(t));
                explosion.notifyObservers();
            }
        });
    }
}
