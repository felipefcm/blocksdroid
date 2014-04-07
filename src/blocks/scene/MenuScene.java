package blocks.scene;

import org.andengine.util.debug.Debug;

import blocks.resource.ResourceManager;
import blocks.scene.SceneManager.SceneType;

public class MenuScene extends GameScene 
{
	
	@Override
	public void Load() 
	{
		Debug.d("Loading MenuScene");
		
		this.attachChild(ResourceManager.m_sInstance.m_TitleText);
	}

	@Override
	public void Unload() 
	{
		Debug.d("Unloading MenuScene");
		
		ResourceManager.m_sInstance.m_TitleText.detachSelf();
	}

	@Override
	public SceneType GetType() 
	{
		return SceneType.Menu;
	}
}
