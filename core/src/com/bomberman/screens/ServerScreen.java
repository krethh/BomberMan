package com.bomberman.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.bomberman.BomberMan;
import com.bomberman.BomberMap;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;

/**
 * Created by Wojtek on 21.03.2017.
 */
public class ServerScreen implements Screen, InputProcessor {

    /**
     * Pole przechowujące odniesienie do głównego obiektu gry.
     */
    BomberMan game;

    /**
     * Używany do wyświetlania obecnego stanu serwera.
     */
    private String serverState;

    /**
     * Szerokość tekstur.
     */
    private static int BUTTON_WIDTH = 250;

    /**
     * Wysokośc tekstur.
     */
    private static int BUTTON_HEIGHT = 100;

    /**
     * Wysokość okna
     */
    private final int WINDOW_HEIGHT;

    /**
     * Szerokość okna.
     */
    private final int WINDOW_WIDTH;

    /**
     * Czcionka używana do wypisywania.
     */
    private BitmapFont font;

    /**
     * Kamera do wyświetlania ekranu.
     */
    private OrthographicCamera camera;

    /**
     * Główny konstruktor.
     * @param game Odniesienie do głównego obiektu gry.
     */

    public ServerScreen(BomberMan game)
    {
        this.game = game;

        WINDOW_HEIGHT = game.bomberConfig.pixelHeight;
        WINDOW_WIDTH = game.bomberConfig.pixelWidth;

        serverState = "Oczekiwanie...";

        Gdx.input.setInputProcessor(this);
        font = new BitmapFont();
    }

    @Override
    public void show() {
        camera  = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.translate(camera.viewportWidth/2, camera.viewportHeight/2);
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.begin();
        game.batch.setProjectionMatrix(camera.combined);

        font.setColor(Color.BLACK);
        font.getData().setScale(2, 2);

        font.draw(game.batch, serverState, Gdx.graphics.getWidth()/3 , Gdx.graphics.getHeight()/2);
        font.draw(game.batch, "ESC - powrot, ENTER - polaczenie z serwerem", Gdx.graphics.getWidth()/3 , Gdx.graphics.getHeight()/2 + 50);

        game.batch.end();
    }

    /**
     * Handler przeskalowania okna.
     * @param width Nowa szerokość
     * @param height Nowa wysokość
     */
    @Override
    public void resize(int width, int height) {
        camera  = new OrthographicCamera(width, height);
        camera.translate(camera.viewportWidth/2, camera.viewportHeight/2);
    }

    /**
     * Handler pauzy, tu nieużywany.
     */
    @Override
    public void pause() {

    }

    /**
     * Handler wznowienia, tu nieużywany.
     */
    @Override
    public void resume() {

    }

    /**
     * Handler schowania okienka.
     */
    @Override
    public void hide() {

    }

    /**
     * Handler zniszczenia okienka.
     */
    @Override
    public void dispose() {

    }

    /**
     * Handler wciśnięcia przycisku.
     * @param keycode Kod przycisku.
     * @return True, jeżeli sukces.
     */
    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.ESCAPE)
        {
            game.setScreen(new MainMenuScreen(game));
        }
        if(keycode == Input.Keys.ENTER)
        {
            new Thread(() -> connectAndDownload()).start();
        }
        return true;
    }

    /**
     * Handler podniesienia przycisku
     * @param keycode Kod przycisku
     * @return True, jeżeli sukces.
     */
    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    /**
     * Do obsługi znaków z klawiatury.
     * @param character Wprowadzony znak.
     * @return True jeżeli sukces.
     */
    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    /**
     * Do obsługi ekranów dotykowych, nieużywany.
     * @param screenX
     * @param screenY
     * @param pointer
     * @param button
     * @return
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    /**
     * Do obsługi ekranów dotykowych, nieużywany.
     * @param screenX
     * @param screenY
     * @param pointer
     * @param button
     * @return
     */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    /**
     * Do obsługi ekranów dotykowych, nieużywany.
     * @param screenX
     * @param screenY
     * @param pointer
     * @return
     */
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    /**
     * Handler poruszenia myszką, nieużywany.
     * @param screenX
     * @param screenY
     * @return
     */
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    /**
     * Handler scrollowania, nieużywany.
     * @param amount
     * @return
     */
    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    /**
     * Nawiązuje połączenie z serwerem i pobiera konfigurację.
     * @return false jeżeli połączenie się nie powiodło, true jeżeli pobrano pliki i zgodnie z planem zakończono połączenie.
     */
    private boolean connectAndDownload()
    {
        Socket socket;
        try
        {
            String response;

            serverState = "Inicjalizacja polaczenia...";
            socket = new Socket(game.bomberConfig.serverIpAddress, game.bomberConfig.serverPort);

            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());

            serverState = "Pytanie o INFO...";
            outToServer.writeBytes("GET_INFO\n");

            response = inFromServer.readLine();
            serverState = "Otrzymano INFO....";

            String[] parts = response.split(",");

            // sprawdzenie, czy odpowiedź jest przewidywana
            if(!isResponseCorrect("INFO", parts[0]))
            {
                serverState = "Niepoprawna odpowiedz serwera!";
                return false;
            }

            int mapsNumber = Integer.valueOf(parts[1]);

            // pobieranie listy najlepszych wyników
            serverState = "Pobieranie listy najlepszych wynikow...";

            outToServer.writeBytes("GET_HIGHSCORES\n");
            response = inFromServer.readLine();
            serverState = "Otrzymano liste najlepszych wynikow...";

            parts = response.split(",");
            if(!isResponseCorrect("HIGHSCORES", parts[0]))
            {
                serverState = "Niepoprawna odpowiedz serwera!";
                return false;
            }

            Properties scores = new Properties();

            // od int = 1, bo pierwsze słowo to HIGHSCORES
            for(int i = 1; i < parts.length - 2; i+=2)
            {
                scores.put(parts[i], parts[i+1]);
            }
            game.highScoresManager.setHighScores(scores);

            // pobieranie ogólnej konfiguracji gry
            serverState = "Pobieranie konfiguracji gry...";

            outToServer.writeBytes("GET_GAMECONFIG\n");
            response = inFromServer.readLine();
            serverState = "Otrzymano konfiguracje gry...";

            parts = response.split(",");
            if(!isResponseCorrect("GAMECONFIG", parts[0]))
                return false;

            // przypisanie w konfiguracji pobranych parametrów
            game.bomberConfig.pixelHeight = Short.valueOf(parts[1]);
            game.bomberConfig.panelWidth = Short.valueOf(parts[2]);
            game.bomberConfig.shockwaveLength = Short.valueOf(parts[3]);
            game.bomberConfig.bombWaitingTime = Short.valueOf(parts[4]);
            game.bomberConfig.shockwaveTime = Short.valueOf(parts[5]);
            game.bomberConfig.multibombDuration = Short.valueOf(parts[6]);
            game.bomberConfig.panelWidth = Short.valueOf(parts[7]);
            game.bomberConfig.FPS = Short.valueOf(parts[8]);
            game.bomberConfig.speed = Short.valueOf(parts[9]);
            game.bomberConfig.shockwaveRecoveryTime = Short.valueOf(parts[10]);

            for(int i = 1; i <= mapsNumber; i++)
            {
                outToServer.writeBytes("GET_MAP," + i + "\n");
                serverState = "Pobieranie mapy nr " + i + "...";
                response = inFromServer.readLine();

                parts = response.split(",");
                BomberMap map = new BomberMap();

                map.screenWidth = Short.valueOf(parts[1]);
                map.screenHeight = Short.valueOf(parts[2]);
                map.mapObjects = parts[3];
                map.multibombPositions = parts[4];
                map.superbombPositions = parts[5];
                map.cherryPosition = Short.valueOf(parts[6]);
                map.elimnationPosition = Short.valueOf(parts[7]);
                map.enemyPositions = parts[8];
                map.heroPosition = Short.valueOf(parts[9]);

                game.bomberConfig.serverMaps.add(map);
            }

            serverState = "Polaczenie zakonczone sukcesem!";
            socket.close();
        }
        catch(UnknownHostException e)
        {
            e.printStackTrace();
        }
        catch(EOFException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            serverState = "Polaczenie odrzucone.";
        }
        return  true;
    }

    /**
     * Sprawdza, czy odpowiedź od servera jest poprawna.
     * @param expectedResponse Oczekiwana odpowiedź
     * @param actualResponse Odpowiedź otrzymana.
     * @param actualResponse Odpowiedź otrzymana.
     * @return True, jeżeli odpowiedź jest poprawna. False, jeżeli interakcja z serwerem przebiegła w sposób nieoczekiwany lub wystąpił błąd.
     */
    private boolean isResponseCorrect(String expectedResponse, String actualResponse)
    {
        if(actualResponse == "SERVER_PANIC 600")
        {
            serverState = "Błąd wewnętrzny serwera.";
            return false;
        }

        else if (!actualResponse.equals(expectedResponse))
        {
            serverState = "Nieoczekiwana odpowiedź serwera.";
            return false;
        }
        else return true;
    }
}
