package com.bomberman;

import com.bomberman.interfaces.Collidable;
import com.bomberman.screens.MainGameScreen;

import java.util.ArrayList;

import static com.bomberman.BomberMan.DELTA;
import static com.bomberman.BomberMan.SPEED;

/**
 * Created by Paweł Kulig on 22.03.2017.
 */
public class BomberHero implements Collidable {

    MainGameScreen screen;

    float x, y;

    public BomberHero(float x, float y, MainGameScreen screen)
    {
        this.x = x;
        this.y = y;
        this.screen = screen;
    }

    public void moveUp()
    {
        y += screen.DELTA*screen.SPEED;
        checkCollisions();
    }

    public void moveDown()
    {
        y -= screen.DELTA*screen.SPEED;
        checkCollisions();
    }

    public void moveLeft()
    {
        x -= screen.DELTA*screen.SPEED;
        checkCollisions();
    }

    public void moveRight()
    {
        x += screen.DELTA*screen.SPEED;
        checkCollisions();
    }

    private void checkCollisions()
    {
        ArrayList<BomberCollision> collisions = new ArrayList<>();

        screen.activeShockwaves.stream().forEach(s -> {
            s.shockwavePath.stream().forEach(t -> {
                if(screen.getTile(x,y) == t)
                {
                    collisions.add(new BomberCollision(this, t, BomberCollision.collisionType.HERO_SHOCKWAVE));
                }
            });
        });

        screen.multibombTiles.stream().forEach(t -> {
            if(hasCollisionWith(t))
            {
                collisions.add(new BomberCollision(this, t, BomberCollision.collisionType.MULTIBOMB));
            }
        });

        screen.superbombTiles.stream().forEach(t -> {
            if(hasCollisionWith(t))
            {
                collisions.add(new BomberCollision(this, t, BomberCollision.collisionType.SUPERBOMB));
            }
        });

        collisions.stream().forEach(c -> {
            c.register(screen);
            c.notifyObservers();
        });
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
}
