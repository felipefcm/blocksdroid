
package blocks.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import blocks.game.Blocksdroid;

public class DesktopLauncher
{
    public static void main(String[] args)
    {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.title = "Blocksdroid";
        config.width = (int)(Blocksdroid.V_WIDTH * Blocksdroid.DESKTOP_SCALE);
        config.height = (int)(Blocksdroid.V_HEIGHT * Blocksdroid.DESKTOP_SCALE);

        new LwjglApplication(new Blocksdroid(null, null), config);
    }
}
