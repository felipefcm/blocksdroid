
package blocks.screen;

public class ScreenManager 
{
	public static ScreenManager m_sInstance = new ScreenManager();
	
	public enum ScreenType
	{
		Play
	}
	
	public PlayScreen m_PlayScreen;
	
	public void SetScreen(GameScreen screen)
	{
	}
}
