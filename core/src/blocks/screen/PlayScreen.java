
package blocks.screen;

import blocks.game.BlocksMatch;
import blocks.resource.Point;
import blocks.resource.ResourceManager;
import blocks.screen.ScreenManager.ScreenType;

import com.badlogic.gdx.graphics.OrthographicCamera;

public class PlayScreen extends GameScreen 
{
	public OrthographicCamera m_Camera;
	
	private BlocksMatch m_Match;
	
	public PlayScreen() 
	{
		Point<Integer> viewSize = ResourceManager.m_sInstance.m_ViewSize;
		
		m_Camera = new OrthographicCamera(viewSize.x, viewSize.y);
		m_Camera.update();
		
		m_Match = new BlocksMatch();
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
