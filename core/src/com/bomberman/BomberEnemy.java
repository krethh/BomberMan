package com.bomberman;

import com.badlogic.gdx.Gdx;
import com.bomberman.interfaces.Collidable;
import com.bomberman.screens.MainGameScreen;
import com.sun.istack.internal.Nullable;

import java.util.ArrayList;
import java.util.Random;

/**
 * Przeciwnik na planszy. Klasa odpowiedzialna za ruch i podkładanie bomb.
 */
public class BomberEnemy implements Collidable {

    /**
     * Odniesienie do głównej klasy gry.
     */
    private BomberMan game;

    /**
     * Generowanie liczb losowych do randomizowania kierunku poruszania się.
     */
    Random random;

    /**
     * Współrzędne przeciwnika.
     */
    private float x, y;

    /**
     * Czas, kiedy przeciwnik podłożył swoją ostatnią bombę.
     */
    private float bombPlantingTime;

    /**
     * Mówi, czy obecnie jest na ekranie bomba podłożona przez tego wroga.
     */
    private boolean hasBombPlanted;

    /**
     * Ekran, po którym porusza się przeciwnik.
     */
    public MainGameScreen screen;

    /**
     * Obsługuje kolizje ze ścianami, bombami, przeszkodami, falami uderzeniowymi.
     */
    public BomberPenetrationHandler collisionHandler;

    /**
     * Główny konstruktor.
     * @param x Wspólrzędna x tekstury
     * @param y Wspólrzędna y tekstury
     * @param collisionHandler Handler kolizji.
     */
    public BomberEnemy(float x, float y, BomberPenetrationHandler collisionHandler, BomberMan game, MainGameScreen screen)
    {
        this.x = x;
        this.y = y;
        direction = movingDirection.UP;
        this.collisionHandler = collisionHandler;
        this.game = game;
        this.screen = screen;
        random = new Random();
    }

    /**
     * Sprawdza, czy istnieje kolizja z innym obiektem.
     * @param other Inny obiekt.
     * @return True, jeżeli występuje.
     */
    @Override
    public boolean hasCollisionWith(Collidable other) {
        return (x < other.getX() + screen.TILE_WIDTH && y < other.getY() +
                screen.TILE_HEIGHT && x +
                screen.TILE_WIDTH > other.getX() && y +
                screen.TILE_HEIGHT > other.getY());
    }

    /**
     * Zwraca współrzędna X.
     * @return Współrzędna X.
     */
    @Override
    public float getX() {
        return x;
    }

    /**
     * Zwraca współrzędną Y.
     * @return Współrzędna Y.
     */
    @Override
    public float getY() {
        return y;
    }

    /**
     * W którym kierunku porusza się wróg.
     */
    public enum movingDirection {UP, DOWN, LEFT, RIGHT;}

    /**
     * Konkretny kierunek poruszania sie wroga.
     */
    public movingDirection direction;

    /**
     * Czy przeciwnik jest martwy
     */
    private boolean dead;

    /**
     * Wykonuje ruch postaci w kierunku określonym przez zmienną movingDirection.
     */
    public void act()
    {
        checkCollisions();

        if(dead)
            return;

        if(bombPlantingTime + game.bomberConfig.bombWaitingTime == screen.timeCounter)
            hasBombPlanted = false;

        if(distanceTo(screen.hero.getX(), screen.hero.getY()) < screen.TILE_WIDTH)
        {
            plantBomb();
        }

        Bomb b;
        if((b = bombNearby()) != null)
        {
            runAwayFromBomb(b);
        }

        BomberTile t = tryToMove();
        if(t != null) {
            if(t.type == 'o')
                plantBomb();
            else
                changeMovingDirection();
        }
    }

    /**
     * Sprawdza, czy występują kolizje.
     */
    private void checkCollisions() {
        ArrayList<BomberCollision> collisions = new ArrayList<>();

        screen.activeShockwaves.stream().forEach(s ->{
            s.shockwavePath.stream().forEach(t -> {
                if(t == screen.getTile(x, y))
                    collisions.add(new BomberCollision(this, t, BomberCollision.collisionType.ENEMY_SHOCKWAVE));
            });
        });

        collisions.stream().forEach(c -> {
            c.register(screen);
            c.notifyObservers();
        });
    }

    /**
     * Wykonaj ruch
     * @return Kafelek, przez który nie może przejść, jeżeli ruch się nie udał.
     */
    private BomberTile tryToMove()
    {
        float DELTA = Gdx.graphics.getDeltaTime();

        if(direction == movingDirection.UP)
        {
            if(collisionHandler.cannotPenetrate(x, y + DELTA*screen.ENEMY_SPEED) == null)
                y += DELTA*screen.ENEMY_SPEED;
            else
                return collisionHandler.cannotPenetrate(x, y + DELTA*screen.ENEMY_SPEED);
        }

        else if(direction == movingDirection.DOWN)
        {
            if(collisionHandler.cannotPenetrate(x, y - DELTA*screen.ENEMY_SPEED) == null)
                y -= DELTA*screen.ENEMY_SPEED;
            else
                return collisionHandler.cannotPenetrate(x, y - DELTA*screen.ENEMY_SPEED);
        }

        else if(direction == movingDirection.RIGHT)
        {
            if(collisionHandler.cannotPenetrate(x + DELTA*screen.ENEMY_SPEED, y) == null)
                x += DELTA*screen.ENEMY_SPEED;
            else
                return collisionHandler.cannotPenetrate(x + DELTA*screen.ENEMY_SPEED, y);
        }

        else if(direction == movingDirection.LEFT)
        {
            if(collisionHandler.cannotPenetrate(x - DELTA*screen.ENEMY_SPEED, y) == null)
                x -= DELTA*screen.ENEMY_SPEED;
            else
                return collisionHandler.cannotPenetrate(x - DELTA*screen.ENEMY_SPEED, y);
        }
        return null;
    }

    /**
     * Zmienia kierunek ruchu na w drugą stronę.
     */
    private void changeMovingDirection()
    {
        if (direction == movingDirection.LEFT)
            direction = movingDirection.DOWN;

        else if(direction == movingDirection.DOWN)
            direction = movingDirection.RIGHT;

        else if(direction == movingDirection.RIGHT)
            direction = movingDirection.UP;

        else if(direction == movingDirection.UP)
            direction = movingDirection.LEFT;
    }

    /**
     * Podłożenie bomby.
     */
    private void plantBomb()
    {
        if(!hasBombPlanted)
        {
            screen.bombsPlanted.add(new Bomb(
                    false, x, y, screen.timeCounter, false
            ));
            hasBombPlanted = true;
            bombPlantingTime = screen.timeCounter;
        }

    }

    /**
     * Sprawdza, czy istnieje w okolicy bomba.
     * @return Bomba, jeżeli istnieje w okolicy.
     */
    @Nullable
    private Bomb bombNearby()
    {
        for(Bomb b : screen.bombsPlanted)
        {
            Shockwave s = new Shockwave(b, 0, screen.getTile(b.x, b.y), screen.mapGrid, game, screen.currentMap);

            for( BomberTile t : s.shockwavePath)
                if(collisionHandler.haveCollision(x, y, t))
                    return b;
        }
        for(Shockwave s : screen.activeShockwaves)
        {
            for(BomberTile t : s.shockwavePath)
                if(collisionHandler.haveCollision(x, y, t))
                    return s.bomb;
        }
        return null;
    }

    /**
     * Powoduje, że przeciwnik ucieka od bomby.
     * @param b Bomba, od której ma uciekać.
     */
    private void runAwayFromBomb(Bomb b)
    {
        float DELTA = Gdx.graphics.getDeltaTime();

        //bomba z lewej górnej strony
        if(b.x - x <= 0 && b.y >= y)
        {
            if(collisionHandler.cannotPenetrate(x +DELTA*screen.ENEMY_SPEED, y) == null)
                direction = movingDirection.RIGHT;
            else if (collisionHandler.cannotPenetrate(x, y - DELTA*screen.ENEMY_SPEED) == null)
                direction = movingDirection.DOWN;
            else if(random.nextInt(2) == 0)
                direction = movingDirection.LEFT;
            else
                direction = movingDirection.UP;
        }

        //bomba z prawej górnej strony
        else if(b.x - x >= 0 && b.y >= y)
        {
            if(collisionHandler.cannotPenetrate(x - DELTA*screen.ENEMY_SPEED, y) == null)
                direction = movingDirection.LEFT;
            else if (collisionHandler.cannotPenetrate(x, y - DELTA*screen.ENEMY_SPEED) == null)
                direction = movingDirection.DOWN;
            else if(random.nextInt(2) == 0)
                direction = movingDirection.RIGHT;
            else
                direction = movingDirection.UP;
        }

        //bomba z lewej dolnej strony
        else if(b.x - x < 0 && b.y < y )
        {
            if(collisionHandler.cannotPenetrate(x + DELTA*screen.ENEMY_SPEED, y) == null)
                direction = movingDirection.RIGHT;
            else if (collisionHandler.cannotPenetrate(x, y + DELTA*screen.ENEMY_SPEED) == null)
                direction = movingDirection.UP;
            else if(random.nextInt(2) == 0)
                direction = movingDirection.LEFT;
            else
                direction = movingDirection.DOWN;
        }

        //bomba z prawej dolnej strony
        else if(b.x - x >= 0 && b.y < y)
        {
            if(collisionHandler.cannotPenetrate(x - DELTA*screen.ENEMY_SPEED, y) == null)
                direction = movingDirection.LEFT;
            else if (collisionHandler.cannotPenetrate(x, y + DELTA*screen.ENEMY_SPEED) == null)
                direction = movingDirection.UP;
            else if(random.nextInt(2) == 0)
                direction = movingDirection.RIGHT;
            else
                direction = movingDirection.DOWN;
        }
    }

    /**
     * Dystans do jakiegoś punktu.
     * @param x Współrzędna x tego punktu.
     * @param y Współrzędna y tego punktu.
     * @return Dystans do tego punktu.
     */
    private double distanceTo(float x, float y)
    {
        return Math.sqrt(Math.pow(x - this.x, 2) + Math.pow(y - this.y, 2));
    }

    /**
     * Sprawia, że przeciwnik umiera.
     */
    public void die()
    {
        screen.deadEnemies.add(this);
        dead = true;
    }

}
