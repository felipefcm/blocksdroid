
package blocks.game;

import blocks.ad.AdManager;
import blocks.resource.Log;
import blocks.resource.ResourceManager;
import blocks.screen.ScreenManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Blocksdroid extends Game
{
    public static final int V_WIDTH = 720;
    public static final int V_HEIGHT = 1280;
    public static final float DESKTOP_SCALE = 0.7f;

	private ShapeRenderer m_ShapeRenderer;
	
	public Blocksdroid(AdManager adManager)
	{
		ResourceManager.instance.adManager = adManager;
	}
	
	@Override
	public void create() 
	{
		Gdx.input.setCatchBackKey(true);
		
		if(!ResourceManager.instance.Init(this))
			Log.Write("Failed to init ResourceManager");
		
		if(!ScreenManager.instance.Init())
			Log.Write("Failed to init ScreenManager");
		
		m_ShapeRenderer = ResourceManager.instance.shapeRenderer;
		
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
                Blocksdroid.V_WIDTH,
                Blocksdroid.V_HEIGHT
			);
		}
		m_ShapeRenderer.end();
		
		super.render();
	}
	
	@Override
	public void resize(int width, int height) 
	{
		ResourceManager.instance.viewport.update(width, height, true);
		
		m_ShapeRenderer.setProjectionMatrix(ResourceManager.instance.camera.combined);
		
		super.resize(width, height);
	}
	
	@Override
	public void pause()
	{
		ScreenManager.instance.ActivityPaused();
		super.pause();
	}
	
	@Override
	public void resume()
	{
		super.resume();
		ScreenManager.instance.ActivityResumed();
	}

	@Override
	public void dispose() 
	{
		ScreenManager.instance.Dispose();
		ResourceManager.instance.Dispose();
	}
	
	public void OnExit()
	{
		Gdx.app.exit();
	}
}
