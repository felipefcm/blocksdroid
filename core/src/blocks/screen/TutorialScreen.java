
package blocks.screen;

import blocks.game.Blocksdroid;
import blocks.resource.Point;
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
	private Texture m_TutorialTexture;
	private Sprite m_TutorialSprite;
	
	private SpriteBatch m_Batch;
	
	@Override
	public void show()
	{
		m_Batch = ResourceManager.m_sInstance.spriteBatch;
		
		m_TutorialTexture = new Texture(Gdx.files.internal("gfx/tutorial.png"));
		m_TutorialSprite = new Sprite(m_TutorialTexture);
		
		m_TutorialSprite.setBounds(0, 0, Blocksdroid.V_WIDTH, Blocksdroid.V_HEIGHT);
		
		ResourceManager.m_sInstance.adManager.DisableAds();
		
		Gdx.input.setInputProcessor
		(
			new InputAdapter()
			{
				@Override
				public boolean touchUp(int screenX, int screenY, int pointer, int button)
				{
					ScreenManager.m_sInstance.SetScreen(new MainMenuScreen());
					return true;
				}
				
				@Override
				public boolean keyDown(int keycode)
				{
					if(keycode == Keys.BACK)
					{
						ScreenManager.m_sInstance.SetScreen(new MainMenuScreen());
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
		m_Batch.setTransformMatrix(ResourceManager.m_sInstance.identityMatrix);
		m_Batch.begin();
		{
			m_TutorialSprite.draw(m_Batch);
		}
		m_Batch.end();
	}

	@Override
	public void resize(int width, int height)
	{	
	}

	@Override
	public void hide()
	{
		dispose();
		
		ResourceManager.m_sInstance.adManager.EnableAds();
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
		m_TutorialTexture.dispose();
	}

	@Override
	public ScreenType GetType()
	{
		return ScreenType.Tutorial;
	}
}
