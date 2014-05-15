
package blocks.screen;

import blocks.screen.ScreenManager.ScreenType;

import com.badlogic.gdx.Screen;

public abstract class GameScreen implements Screen
{	
	public GameScreen()
	{
	}
	
	public abstract ScreenType GetType();
}
