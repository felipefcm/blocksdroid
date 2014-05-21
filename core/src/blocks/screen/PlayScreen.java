
package blocks.screen;

import blocks.game.BlocksMatch;
import blocks.resource.ResourceManager;
import blocks.screen.ScreenManager.ScreenType;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PlayScreen extends GameScreen 
{	
	private BlocksMatch m_Match;
	private SpriteBatch m_SpriteBatch;
	
	public PlayScreen() 
	{
		m_Match = new BlocksMatch();
	}
	
	public boolean Init()
	{
		m_SpriteBatch = ResourceManager.m_sInstance.m_SpriteBatch;
		
		m_Match.Init();		
		
		return true;
	}

	@Override
	public void render(float delta) 
	{
		m_SpriteBatch.setTransformMatrix(ResourceManager.m_sInstance.m_IdentityMatrix);
		m_SpriteBatch.begin();
		{
			ResourceManager.m_sInstance.m_BlocksdroidText.draw(m_SpriteBatch);
		}
		m_SpriteBatch.end();
		
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
