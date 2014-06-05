
package blocks.game;

import blocks.ad.AdManager;
import blocks.resource.Log;
import blocks.resource.ResourceManager;
import blocks.resource.SwarmResources;
import blocks.screen.ScreenManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Blocksdroid extends Game
{
	private ShapeRenderer m_ShapeRenderer;
	
	public Blocksdroid(AdManager adManager, SwarmResources swarmResources)
	{
		ResourceManager.m_sInstance.m_AdManager = adManager;
		ResourceManager.m_sInstance.m_SwarmResources = swarmResources;
	}
	
	@Override
	public void create() 
	{
		Gdx.input.setCatchBackKey(true);
		
		if(!ResourceManager.m_sInstance.Init(this))
			Log.Write("Failed to init ResourceManager");
		
		if(!ScreenManager.m_sInstance.Init())
			Log.Write("Failed to init ScreenManager");
		
		ResourceManager.m_sInstance.m_SwarmResources.PreLoadSwarm();
		
		m_ShapeRenderer = ResourceManager.m_sInstance.m_ShapeRenderer;
		
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	@Override
	public void render() 
	{
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//paints the viewport area
		m_ShapeRenderer.begin(ShapeType.Filled);
		{
			m_ShapeRenderer.setColor(0.17f, 0.25f, 0.33f, 1.0f);
			m_ShapeRenderer.rect
			(
				0, 0,
				ResourceManager.m_sInstance.m_Viewport.getViewportWidth(),
				ResourceManager.m_sInstance.m_Viewport.getViewportHeight()
			);
		}
		m_ShapeRenderer.end();
		
		super.render();
	}
	
	@Override
	public void resize(int width, int height) 
	{
		ResourceManager.m_sInstance.m_Viewport.update(width, height, true);
		
		m_ShapeRenderer.setProjectionMatrix(ResourceManager.m_sInstance.m_Camera.combined);
		
		super.resize(width, height);
	}

	@Override
	public void dispose() 
	{
		ScreenManager.m_sInstance.Dispose();
		ResourceManager.m_sInstance.Dispose();
	}
	
	public void OnExit()
	{
		Gdx.app.exit();
	}
}
