
package blocks.screen;

import blocks.game.Blocksdroid;
import blocks.resource.ResourceManager;
import blocks.screen.ScreenManager.ScreenType;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TutorialScreen extends GameScreen
{
	private Texture tutorialTexture;
	private Sprite tutorialSprite;
	
	private SpriteBatch batch;
	
	@Override
	public void show()
	{
		batch = ResourceManager.instance.spriteBatch;
		
		tutorialTexture = new Texture(Gdx.files.internal("gfx/tutorial.png"));
		tutorialSprite = new Sprite(tutorialTexture);
		
		tutorialSprite.setBounds(0, 0, Blocksdroid.V_WIDTH, Blocksdroid.V_HEIGHT);

		if(ResourceManager.instance.adManager != null)
		    ResourceManager.instance.adManager.DisableAds();
		
		Gdx.input.setInputProcessor
		(
			new InputAdapter()
			{
				@Override
				public boolean touchUp(int screenX, int screenY, int pointer, int button)
				{
					ScreenManager.instance.SetScreen(new MainMenuScreen());
					return true;
				}
				
				@Override
				public boolean keyDown(int keycode)
				{
					if(keycode == Keys.BACK)
					{
						ScreenManager.instance.SetScreen(new MainMenuScreen());
						return true;
					}
						
					return false;
				}
			}
		);
	}
	
	@Override
	public void render(float delta)
	{
		batch.setTransformMatrix(ResourceManager.instance.identityMatrix);
		batch.begin();
		{
			tutorialSprite.draw(batch);
		}
		batch.end();
	}

	@Override
	public void resize(int width, int height)
	{	
	}

	@Override
	public void hide()
	{
		dispose();

        if(ResourceManager.instance.adManager != null)
		    ResourceManager.instance.adManager.EnableAds();
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
		tutorialTexture.dispose();
	}

	@Override
	public ScreenType GetType()
	{
		return ScreenType.Tutorial;
	}
}
