package com.bomberman;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

/**
 * Zarządza najwyższymi wynikami.
 */
public class HighScoresManager {

    /**
     * Przechowuje najwyższe wyniki.
     */
    private Properties scores;

    /**
     * Główny konstruktor.
     */
    public HighScoresManager() throws IOException {

        scores = new Properties();

        InputStream input = new FileInputStream("highscores.config");
        scores.load(input);
    }

    /**
     * Zwraca najwyższy wynik danego gracza.
     * @param nick Nick gracza.
     * @return Najwyższy wynik gracza. Jeżeli nie ma gracza, zwraca -1.
     */
    public int getScore(String nick)
    {
        if(scores.containsKey(nick))
            return Integer.valueOf(scores.getProperty(nick));

        else return -1;
    }

    /**
     * Ustawia najwyższy wynik gracza, jeżeli jest on wyższy niż poprzedni najwyższy wynik
     * @param nick Nick gracza.
     * @param value Wynik do ustawienia.
     */
    public void setScoreIfBetter(String nick, int value){
        if(getScore(nick) > value)
            return;

        scores.setProperty(nick, String.valueOf(value));
        try {
            scores.store(new FileOutputStream("highscores.config"), "");
        } catch (IOException e) {
            System.exit(100);
        }
    }


}
