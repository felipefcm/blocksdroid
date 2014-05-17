
package blocks.game;

import blocks.resource.Log;
import blocks.resource.ResourceManager;
import blocks.screen.ScreenManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

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
		Gdx.gl.glClearColor(0.25f, 0.25f, 0.25f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		super.render();
	}
	
	@Override
	public void resize(int width, int height) 
	{
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
