
package blocks.screen;

import blocks.game.BlocksMatch;
import blocks.game.Blocksdroid;
import blocks.resource.ResourceManager;
import blocks.screen.ScreenManager.ScreenType;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class PlayScreen extends GameScreen 
{	
	private BlocksMatch match;
	private SpriteBatch spriteBatch;
	private ShapeRenderer shapeRenderer;
	
	public PlayScreen() 
	{
		match = new BlocksMatch();
	}
	
	@Override
	public void show() 
	{	
		spriteBatch = ResourceManager.instance.spriteBatch;
		shapeRenderer = ResourceManager.instance.shapeRenderer;
		
		match.Init();
		match.Start();
	}

	@Override
	public void render(float delta) 
	{
        shapeRenderer.setTransformMatrix(ResourceManager.instance.identityMatrix);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        {
            shapeRenderer.setColor(0.12f, 0.2f, 0.28f, 1.0f);
            shapeRenderer.rect(0, 0, Blocksdroid.V_WIDTH, Blocksdroid.V_HEIGHT);
        }
        shapeRenderer.end();

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
