
package blocks.screen;

import blocks.game.Blocksdroid;
import blocks.resource.ResourceManager;

public class ScreenManager 
{
	public static ScreenManager instance = new ScreenManager();
	
	public enum ScreenType
	{
		MainMenu,
		Play,
		Tutorial
	}
	
	private Blocksdroid game;
	private GameScreen currentScreen;
	
	public ScreenManager()
	{
		currentScreen = null;
	}
	
	public boolean Init()
	{
		game = ResourceManager.instance.game;
		
		//initial scene
		SetScreen(new MainMenuScreen());
		
		return true;
	}
	
	public void SetScreen(GameScreen screen)
	{
		currentScreen = screen;
		
		//setScreen already calls hide() in the old screen
		game.setScreen(screen);
	}
	
	public void ActivityPaused()
	{
		if(currentScreen instanceof PlayScreen)
		{
			((PlayScreen) currentScreen).GetMatch().SetPause(true);
		}
	}

	public void ActivityResumed()
	{
	}
	
	public void Dispose()
	{
		if(currentScreen != null)
			currentScreen.hide();
	}
}
