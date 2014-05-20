
package blocks.game;

import blocks.resource.Log;
import blocks.resource.ResourceManager;
import blocks.screen.ScreenManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Blocksdroid extends Game
{
	public Blocksdroid()
	{
	}
	
	@Override
	public void create() 
	{
		if(!ResourceManager.m_sInstance.Init(this))
			Log.Write("Failed to init ResourceManager");
		
		if(!ScreenManager.m_sInstance.Init())
			Log.Write("Failed to init ScreenManager");
	}
	
	@Override
	public void render() 
	{
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//paints the viewport area
		ResourceManager.m_sInstance.m_ShapeRenderer.begin(ShapeType.Filled);
		{
			ResourceManager.m_sInstance.m_ShapeRenderer.setColor(0.2f, 0.2f, 0.2f, 1.0f);
			ResourceManager.m_sInstance.m_ShapeRenderer.rect
			(
				0, 0,
				ResourceManager.m_sInstance.m_Viewport.getViewportWidth(),
				ResourceManager.m_sInstance.m_Viewport.getViewportHeight()
			);
		}
		ResourceManager.m_sInstance.m_ShapeRenderer.end();
		
		super.render();
	}
	
	@Override
	public void resize(int width, int height) 
	{
		ResourceManager.m_sInstance.m_Viewport.update(width, height, true);
		ResourceManager.m_sInstance.m_ShapeRenderer.setProjectionMatrix(ResourceManager.m_sInstance.m_Camera.combined);
		
		super.resize(width, height);
	}

	@Override
	public void dispose() 
	{
		//won't call super.dispose() because ScreenManager should do it
		ScreenManager.m_sInstance.Dispose();
		ResourceManager.m_sInstance.Dispose();
	}
}
