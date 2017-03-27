package com.bomberman;

import com.bomberman.interfaces.Collidable;
import com.bomberman.screens.MainGameScreen;

import java.util.ArrayList;

/**
 * Klasa reprezentująca głównego bohatera.
 */
public class BomberHero implements Collidable {

    /**
     * Ekran, na którym żyje bohater.
     */
    private MainGameScreen screen;

    /**
     * Współrzędne bohatera.
     */
    private float x, y;

    /**
     * Mówi, czy bohater zginął.
     */
    public boolean isDead;

    /**
     * Główny konstruktor bohatera
     * @param x Współrzędna x.
     * @param y Współrzędna y
     * @param screen Ekran, na którym żyje bohater.
     */
    public BomberHero(float x, float y, MainGameScreen screen)
    {
        this.x = x;
        this.y = y;
        this.screen = screen;
    }

    /**
     * Porusza bohatera do góry.
     */
    public void moveUp()
    {
        y += screen.DELTA*screen.SPEED;
    }

    /**
     * Porusza bohatera w dół
     */
    public void moveDown()
    {
        y -= screen.DELTA*screen.SPEED;
    }

    /**
     * Porusza bohatera w lewo
     */
    public void moveLeft()
    {
        x -= screen.DELTA*screen.SPEED;
    }

    /**
     * Porusza bohatera w prawo
     */
    public void moveRight()
    {
        x += screen.DELTA*screen.SPEED;
    }

    /**
     * Sprawdza, czy występują kolizje z bohaterem
     */
    public void checkCollisions()
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

    /**
     * Sprawdza, czy występuje kolizja z innym prosotkątem.
     * @param other Inny prostokąt.
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
     * Zwraca współrzędną X.
     * @return Współrzędna X
     */
    @Override
    public float getX() {
        return x;
    }

    /**
     * Zwraca współrzędną Y
     * @return Współrzędna Y
     */
    @Override
    public float getY() {
        return y;
    }
}
