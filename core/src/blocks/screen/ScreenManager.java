
package blocks.screen;

import blocks.game.Blocksdroid;
import blocks.resource.ResourceManager;

public class ScreenManager 
{
	public static ScreenManager m_sInstance = new ScreenManager();
	
	public enum ScreenType
	{
		MainMenu,
		Play,
		Tutorial
	}
	
	private Blocksdroid m_Game;
	private GameScreen m_CurrentScreen;
	
	public ScreenManager()
	{
		m_CurrentScreen = null;		
	}
	
	public boolean Init()
	{
		m_Game = ResourceManager.m_sInstance.m_Game;
		
		//initial scene
		SetScreen(new MainMenuScreen());
		
		return true;
	}
	
	public void SetScreen(GameScreen screen)
	{
		m_CurrentScreen = screen;
		
		//setScreen already calls hide() in the old screen
		m_Game.setScreen(screen);
	}
	
	public void Dispose()
	{
		if(m_CurrentScreen != null)
			m_CurrentScreen.hide();
	}
}
