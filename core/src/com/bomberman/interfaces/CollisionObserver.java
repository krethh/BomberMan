package com.bomberman.interfaces;

import com.bomberman.BomberCollision;

/**
 * Obserwator kolizji.
 */
public interface CollisionObserver {

    /**
     * Obsługuje zdarzenie kolizji
     * @param collision Kolizja.
     */
    public void handleCollision(BomberCollision collision);
}
