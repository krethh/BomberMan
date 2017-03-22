package com.bomberman;

import com.badlogic.gdx.Gdx;
import com.bomberman.interfaces.Collidable;
import com.bomberman.screens.MainGameScreen;
import com.sun.istack.internal.Nullable;

import java.util.ArrayList;

/**
 * Przeciwnik na planszy. Klasa odpowiedzialna za ruch i podkładanie bomb.
 */
public class BomberEnemy implements Collidable {

    /**
     * Odniesienie do głównej klasy gry.
     */
    private BomberMan game;

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
    }

    @Override
    public boolean hasCollisionWith(Collidable other) {
        return (x < other.getX() + screen.TILE_WIDTH && y < other.getY() +
                screen.TILE_HEIGHT && x +
                screen.TILE_WIDTH > other.getX() && y +
                screen.TILE_HEIGHT > other.getY());
    }

    @Override
    public float getX() {
        return x;
    }

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

        if(collisionHandler.encountersObstacle(x, y))
        {

        }

        if(!move())
            changeMovingDirection();
    }

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
     * @return false, jeżeli ruch się nie udał.
     */
    private boolean move()
    {
        float DELTA = Gdx.graphics.getDeltaTime();

        if(direction == movingDirection.UP)
        {
            if(!collisionHandler.cannotPenetrate(x, y + DELTA*screen.ENEMY_SPEED))
                y += DELTA*screen.ENEMY_SPEED;
            else
                return false;
        }

        else if(direction == movingDirection.DOWN)
        {
            if(!collisionHandler.cannotPenetrate(x, y - DELTA*screen.ENEMY_SPEED))
                y -= DELTA*screen.ENEMY_SPEED;
            else
                return false;
        }

        else if(direction == movingDirection.RIGHT)
        {
            if(!collisionHandler.cannotPenetrate(x + DELTA*screen.ENEMY_SPEED, y))
                x += DELTA*screen.ENEMY_SPEED;
            else
                return false;
        }

        else if(direction == movingDirection.LEFT)
        {
            if(!collisionHandler.cannotPenetrate(x - DELTA*screen.ENEMY_SPEED, y))
                x -= DELTA*screen.ENEMY_SPEED;
            else
                return false;
        }
        return true;
    }

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

    private void runAwayFromBomb(Bomb b)
    {
        float DELTA = Gdx.graphics.getDeltaTime();

        //bomba z lewej górnej strony
        if(b.x - x <= 0 && b.y >= y)
        {
            if(!collisionHandler.cannotPenetrate(x +DELTA*screen.ENEMY_SPEED, y))
                direction = movingDirection.RIGHT;
            else if (!collisionHandler.cannotPenetrate(x, y - DELTA*screen.ENEMY_SPEED))
                direction = movingDirection.DOWN;
            else
                direction = movingDirection.LEFT;
        }

        //bomba z prawej górnej strony
        else if(b.x - x >= 0 && b.y >= y)
        {
            if(!collisionHandler.cannotPenetrate(x - DELTA*screen.ENEMY_SPEED, y))
                direction = movingDirection.LEFT;
            else if (!collisionHandler.cannotPenetrate(x, y - DELTA*screen.ENEMY_SPEED))
                direction = movingDirection.DOWN;
            else
                direction = movingDirection.UP;

        }

        //bomba z lewej dolnej strony
        else if(b.x - x < 0 && b.y < y )
        {
            if(!collisionHandler.cannotPenetrate(x + DELTA*screen.ENEMY_SPEED, y))
                direction = movingDirection.RIGHT;
            else if (!collisionHandler.cannotPenetrate(x, y + DELTA*screen.ENEMY_SPEED))
                direction = movingDirection.UP;
            else
                direction = movingDirection.DOWN;
        }

        //bomba z prawej dolnej strony
        else if(b.x - x >= 0 && b.y < y)
        {
            if(!collisionHandler.cannotPenetrate(x - DELTA*screen.ENEMY_SPEED, y))
                direction = movingDirection.LEFT;
            else if (!collisionHandler.cannotPenetrate(x, y + DELTA*screen.ENEMY_SPEED))
                direction = movingDirection.UP;
            else
                direction = movingDirection.RIGHT;
        }
    }

    private double distanceTo(float x, float y)
    {
        return Math.sqrt(Math.pow(x - this.x, 2) + Math.pow(y - this.y, 2));
    }

    public void die()
    {
        screen.deadEnemies.add(this);
        dead = true;
    }

}
