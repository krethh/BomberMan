package com.bomberman.interfaces;

import com.bomberman.BomberCollision;

/**
 * Created by Paweł Kulig on 22.03.2017.
 */
public interface CollisionObserver {

    public void handleCollision(BomberCollision collision);
}
