
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
	
	@Override
	public void show() 
	{	
		m_SpriteBatch = ResourceManager.m_sInstance.m_SpriteBatch;
		
		m_Match.Init();
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
	public void hide() 
	{	
		dispose();
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
