
package blocks.screen;

import blocks.game.BlocksMatch;
import blocks.screen.ScreenManager.ScreenType;

public class PlayScreen extends GameScreen 
{	
	private BlocksMatch m_Match;
	
	public PlayScreen() 
	{
		m_Match = new BlocksMatch();
	}
	
	public boolean Init()
	{
		m_Match.Init();
		
		return true;
	}

	@Override
	public void render(float delta) 
	{
		m_Match.Render();
	}

	@Override
	public void resize(int width, int height) 
	{
	}

	@Override
	public void show() 
	{	
	}

	@Override
	public void hide() 
	{	
	}

	@Override
	public void pause() 
	{	
	}

	@Override
	public void resume() 
	{	
	}

	@Override
	public void dispose() 
	{
		m_Match.Dispose();
	}
	
	@Override
	public ScreenType GetType()
	{
		return ScreenType.Play;
	}
}
