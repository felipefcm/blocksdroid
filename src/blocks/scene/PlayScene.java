
package blocks.scene;

import org.andengine.entity.Entity;
import org.andengine.util.debug.Debug;

import blocks.game.BlocksMatch;
import blocks.resource.ResourceManager;
import blocks.scene.SceneManager.SceneType;

public class PlayScene extends GameScene 
{
	public BlocksMatch m_Match;
	
	public Entity m_BackLayer;
	public Entity m_FrontLayer;
	
	@Override
	public void Load() 
	{
		Debug.d("Loading PlayScene");
		
		ResourceManager.m_sInstance.LoadBlocks();
		
		this.attachChild(m_BackLayer = new Entity());
		this.attachChild(m_FrontLayer = new Entity());
		
		m_BackLayer.setZIndex(0);
		m_FrontLayer.setZIndex(1);
		
		this.sortChildren();
		
		m_BackLayer.attachChild(ResourceManager.m_sInstance.m_BgSprite);
		
		m_Match = new BlocksMatch();
		m_Match.Init();
	}

	@Override
	public void Unload() 
	{
		Debug.d("Unloading PlayScene");
		
		ResourceManager.m_sInstance.m_BgSprite.detachSelf();
		
		ResourceManager.m_sInstance.UnloadBlocks();
	}

	@Override
	public SceneType GetType() 
	{
		return SceneType.Play;
	}
}
