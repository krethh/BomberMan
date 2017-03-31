package com.bomberman.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.bomberman.BomberConfig;
import com.bomberman.BomberMan;

import java.io.IOException;

/**
 * Klasa służąca do uruchomienia programu w okienku.
 */
public class DesktopLauncher {
	/**
	 * Główna klasa programu.
	 * @param arg Parametr domyślny.
	 */
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		try{
			BomberConfig bomberConfig = new BomberConfig();
			config.width = bomberConfig.pixelWidth;
			config.height = bomberConfig.pixelHeight;
			config.foregroundFPS = bomberConfig.FPS;
			config.resizable = true;
			new LwjglApplication(new BomberMan(bomberConfig), config);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}


	}
}
