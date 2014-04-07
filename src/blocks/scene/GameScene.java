
package blocks.scene;

import org.andengine.entity.scene.Scene;

import blocks.scene.SceneManager.SceneType;

public abstract class GameScene extends Scene 
{
	public abstract void Load();
	public abstract void Unload();
	public abstract SceneType GetType();
}
