package com.bomberman;

import com.badlogic.gdx.Gdx;
import com.bomberman.screens.MainGameScreen;
import com.sun.istack.internal.Nullable;

import java.util.ArrayList;

/**
 * Przeciwnik na planszy. Klasa odpowiedzialna za ruch i podkładanie bomb.
 */
public class BomberEnemy {

    /**
     * Odniesienie do głównej klasy gry.
     */
    private BomberMan game;

    /**
     * Współrzędne przeciwnika.
     */
    public float x, y;

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
    public BomberCollisionHandler collisionHandler;

    /**
     * Główny konstruktor.
     * @param x Wspólrzędna x tekstury
     * @param y Wspólrzędna y tekstury
     * @param collisionHandler Handler kolizji.
     */
    public BomberEnemy(float x, float y, BomberCollisionHandler collisionHandler, BomberMan game, MainGameScreen screen)
    {
        this.x = x;
        this.y = y;
        direction = movingDirection.UP;
        this.collisionHandler = collisionHandler;
        this.game = game;
        this.screen = screen;
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
     * Wykonuje ruch postaci w kierunku określonym przez zmienną movingDirection.
     */
    public void act()
    {
        if(bombPlantingTime + game.bomberConfig.bombWaitingTime == screen.timeCounter)
            hasBombPlanted = false;

        if(dies())
        {
            screen.deadEnemies.add(this);
            return;
        }

        if(distanceTo(screen.heroX, screen.heroY) < screen.TILE_WIDTH)
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

    /**
     * Wykonaj ruch
     * @return false, jeżeli ruch się nie udał.
     */
    private boolean move()
    {
        float DELTA = Gdx.graphics.getDeltaTime();

        if(direction == movingDirection.UP)
        {
            if(!collisionHandler.cannotPenetrate(x, y + DELTA*game.SPEED))
                y += DELTA*game.SPEED;
            else
                return false;
        }

        else if(direction == movingDirection.DOWN)
        {
            if(!collisionHandler.cannotPenetrate(x, y - DELTA*game.SPEED))
                y -= DELTA*game.SPEED;
            else
                return false;
        }

        else if(direction == movingDirection.RIGHT)
        {
            if(!collisionHandler.cannotPenetrate(x + DELTA*game.SPEED, y))
                x += DELTA*game.SPEED;
            else
                return false;
        }

        else if(direction == movingDirection.LEFT)
        {
            if(!collisionHandler.cannotPenetrate(x - DELTA*game.SPEED, y))
                x -= DELTA*game.SPEED;
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

    private void reconsiderMovingDirection()
    {

    }

    private void reverseDirection()
    {
        if(direction == movingDirection.DOWN)
            direction = movingDirection.UP;

        else if(direction == movingDirection.UP)
            direction = movingDirection.DOWN;

        else if(direction == movingDirection.LEFT)
            direction = movingDirection.RIGHT;

        else if(direction == movingDirection.RIGHT)
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
            if(!collisionHandler.cannotPenetrate(x + DELTA*game.SPEED, y))
                direction = movingDirection.RIGHT;
            else if (!collisionHandler.cannotPenetrate(x, y - DELTA*game.SPEED))
                direction = movingDirection.DOWN;
            else
                direction = movingDirection.LEFT;
        }

        //bomba z prawej górnej strony
        else if(b.x - x >= 0 && b.y >= y)
        {
            if(!collisionHandler.cannotPenetrate(x - DELTA*game.SPEED, y))
                direction = movingDirection.LEFT;
            else if (!collisionHandler.cannotPenetrate(x, y - DELTA*game.SPEED))
                direction = movingDirection.DOWN;
            else
                direction = movingDirection.UP;

        }

        //bomba z lewej dolnej strony
        else if(b.x - x < 0 && b.y < y )
        {
            if(!collisionHandler.cannotPenetrate(x + DELTA*game.SPEED, y))
                direction = movingDirection.RIGHT;
            else if (!collisionHandler.cannotPenetrate(x, y + DELTA*game.SPEED))
                direction = movingDirection.UP;
            else
                direction = movingDirection.DOWN;
        }

        //bomba z prawej dolnej strony
        else if(b.x - x >= 0 && b.y < y)
        {
            if(!collisionHandler.cannotPenetrate(x - DELTA*game.SPEED, y))
                direction = movingDirection.LEFT;
            else if (!collisionHandler.cannotPenetrate(x, y + DELTA*game.SPEED))
                direction = movingDirection.UP;
            else
                direction = movingDirection.RIGHT;
        }
    }

    private double distanceTo(float x, float y)
    {
        return Math.sqrt(Math.pow(x - this.x, 2) + Math.pow(y - this.y, 2));
    }

    private boolean dies()
    {
        BomberTile t = screen.getTile(x, y);
        for(Shockwave s : screen.activeShockwaves)
        {
            for(BomberTile st : s.shockwavePath)
                if(t == st)
                    return true;
        }
        return false;
    }

}
