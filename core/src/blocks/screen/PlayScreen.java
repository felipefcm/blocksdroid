
package blocks.screen;

import blocks.game.BlocksMatch;
import blocks.resource.ResourceManager;
import blocks.screen.ScreenManager.ScreenType;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PlayScreen extends GameScreen 
{	
	private BlocksMatch match;
	private SpriteBatch spriteBatch;
	
	public PlayScreen() 
	{
		match = new BlocksMatch();
	}
	
	@Override
	public void show() 
	{	
		spriteBatch = ResourceManager.instance.spriteBatch;
		
		match.Init();
	}

	@Override
	public void render(float delta) 
	{
		spriteBatch.setTransformMatrix(ResourceManager.instance.identityMatrix);
		spriteBatch.begin();
		{
			ResourceManager.instance.blocksdroidText.draw(spriteBatch);
		}
		spriteBatch.end();
		
		match.Render();
	}
	
	public BlocksMatch GetMatch()
	{
		return match;
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
		match.Dispose();
	}
	
	@Override
	public ScreenType GetType()
	{
		return ScreenType.Play;
	}
}
