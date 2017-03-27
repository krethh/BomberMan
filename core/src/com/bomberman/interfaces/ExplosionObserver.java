package com.bomberman.interfaces;

import com.bomberman.BomberExplosion;

/**
 * Obserwator eksplozji.
 */
public interface ExplosionObserver {

    /**
     * Obsługiwanie zdarzenia eksplozji.
     * @param e Eksplozja
     */
    public void handleExplosion(BomberExplosion e);
}
