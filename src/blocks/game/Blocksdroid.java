
package blocks.game;

import java.io.IOException;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.debug.Debug;

import android.graphics.Point;
import android.view.Display;
import blocks.resource.ResourceManager;
import blocks.scene.SceneManager;

public class Blocksdroid extends BaseGameActivity
{
	//public static final String DebugTag = "Blocksdroid_Debug";
	public static final float AspectRatio = 3.0f / 4.0f;
	
	public Camera m_Camera;
	
	@Override
	@SuppressWarnings("deprecation")
	public EngineOptions onCreateEngineOptions() 
	{
		Display display = getWindowManager().getDefaultDisplay();
		
		Point screenSize = new Point(display.getWidth(), display.getHeight());
		ResourceManager.m_sInstance.SetDisplaySize(screenSize);
		
		m_Camera = new Camera(0, 0, ResourceManager.m_sInstance.m_ViewSize.x, ResourceManager.m_sInstance.m_ViewSize.y);
		
		EngineOptions options = new EngineOptions
				(
					true,
					ScreenOrientation.PORTRAIT_FIXED,
					new RatioResolutionPolicy(AspectRatio),
					m_Camera
				);
		
		return options;
	}
	
	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) 
	{
		return new LimitedFPSEngine(pEngineOptions, 30);
	}

	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback)	throws IOException 
	{
		try
		{
			ResourceManager.m_sInstance.InitManager(this);
		}
		catch(Exception e)
		{
			Debug.e("Exception thrown while initializing ResourceManager: " + e.getMessage());
			System.exit(1);
		}
		
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws IOException 
	{
		//Load initial scene here
		//SceneManager.m_sInstance.LoadMenuScene();
		SceneManager.m_sInstance.LoadPlayScene();
		
		pOnCreateSceneCallback.onCreateSceneFinished(SceneManager.m_sInstance.GetCurrentScene());
	}

	@Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException 
	{
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
	
	@Override
	public synchronized void onPauseGame() 
	{
		super.onPauseGame();
	}
	
	@Override
	public synchronized void onResumeGame() 
	{
		super.onResumeGame();
	}
}
