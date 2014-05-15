
package blocks.resource;

import blocks.game.Blocksdroid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType;

public class ResourceManager 
{
	public static ResourceManager m_sInstance = new ResourceManager();
	
	public Blocksdroid m_Game;
	
	public TextureAtlas m_BlockTexture;
	
	public FreeType m_BlockFont;
	
	public boolean Init(final Blocksdroid game)
	{
		m_Game = game;
		
		if(!InitCommonResources())
			return false;
		
		return true;
	}
	
	private boolean InitCommonResources()
	{
		m_BlockTexture = new TextureAtlas(Gdx.files.internal("gfx/blocks.png"));
		m_BlockFont = new FreeType();
		
		return true;
	}
}
