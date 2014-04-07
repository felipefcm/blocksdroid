
package blocks.scene;

import blocks.resource.ResourceManager;

public class SceneManager 
{
	public static SceneManager m_sInstance = new SceneManager();
	
	public enum SceneType
	{
		Menu,
		Play
	}
	
	private GameScene m_CurrentScene;

	private MenuScene m_MenuScene;
	private PlayScene m_PlayScene;
	
	public SceneManager()
	{
		m_CurrentScene = null;
	}
	
	public void LoadMenuScene()
	{
		if(m_CurrentScene != null)
			m_CurrentScene.Unload();
		
		m_MenuScene = new MenuScene();
		m_CurrentScene = m_MenuScene;
		
		m_MenuScene.Load();
		
		ResourceManager.m_sInstance.m_Engine.setScene(m_CurrentScene);
	}
	
	public void LoadPlayScene()
	{
		if(m_CurrentScene != null)
			m_CurrentScene.Unload();
		
		m_PlayScene = new PlayScene();
		m_CurrentScene = m_PlayScene;
		
		m_PlayScene.Load();
		
		ResourceManager.m_sInstance.m_Engine.setScene(m_CurrentScene);
	}
	
	public GameScene GetCurrentScene()
	{
		return m_CurrentScene;
	}
	
	public PlayScene GetPlayScene()
	{
		if(m_CurrentScene != m_PlayScene)
			return null;
		
		return m_PlayScene;
	}
}





















