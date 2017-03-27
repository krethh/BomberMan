package com.bomberman.interfaces;

/**
 * Interfejs obiektów, które mogą się zderzać.
 */
public interface Collidable {

    /**
     * Sprawdza, czy występuje kolizja z innym Collidable.
     * @param other Inny obiekt.
     * @return True, jeżeli występuje
     */
    public boolean hasCollisionWith(Collidable other);

    /**
     * Zwróć współrzędna X lewego dolnego rogu obiektu.
     * @return Wspólrzędna X.
     */
    public float getX();

    /**
     * Zwróć współrzędną Y lewego dolnego rogu obiektu.
     * @return Współrzędna Y.
     */
    public float getY();
}
