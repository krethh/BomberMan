package com.bomberman.interfaces;

/**
 * Created by Paweł Kulig on 22.03.2017.
 */
public interface Collidable {

    public boolean hasCollisionWith(Collidable other);

    public float getX();
    public float getY();
}
