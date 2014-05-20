
package blocks.screen;

import blocks.resource.ResourceManager;

public class ScreenManager 
{
	public static ScreenManager m_sInstance = new ScreenManager();
	
	public enum ScreenType
	{
		Play
	}
	
//Screens ----------------------------------------------------------------
	public PlayScreen m_PlayScreen;
//------------------------------------------------------------------------
	
	public boolean Init()
	{
		//initial scene
		m_PlayScreen = new PlayScreen();
		m_PlayScreen.Init();
		
		SetScreen(m_PlayScreen);
		
		return true;
	}
	
	public void SetScreen(GameScreen screen)
	{
		ResourceManager.m_sInstance.m_Game.setScreen(screen);
	}
	
	public void Dispose()
	{
	}
}
