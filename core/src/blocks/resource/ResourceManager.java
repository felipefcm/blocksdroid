
package blocks.resource;

import java.util.Date;
import java.util.Random;

import blocks.game.Blocksdroid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class ResourceManager 
{
	public static ResourceManager m_sInstance = new ResourceManager();
	
	public static final float m_sGameAspectRatio = 3.0f / 4.0f;
	
	//Useful references --------------------------------------------------
	public Blocksdroid m_Game;
	public Random m_Random;
	public Point<Integer> m_ScreenSize;
	public Point<Integer> m_ViewSize;
	//--------------------------------------------------------------------
	
	//Textures -----------------------------------------------------------
	public Texture m_BlockTexture;
	public TextureRegion m_RedBlockRegion;
	public TextureRegion m_GreenBlockRegion;
	public TextureRegion m_BlueBlockRegion;
	public TextureRegion m_OrangeBlockRegion;
	public TextureRegion m_RedBlockFixedRegion;
	public TextureRegion m_GreenBlockFixedRegion;
	public TextureRegion m_BlueBlockFixedRegion;
	public TextureRegion m_OrangeBlockFixedRegion;
	//--------------------------------------------------------------------
	
	//Fonts --------------------------------------------------------------
	public BitmapFont m_BlockFont;
	//--------------------------------------------------------------------
	
	public boolean Init(final Blocksdroid game)
	{
		m_Game = game;
		
		long seed = new Date().getTime();
		m_Random = new Random(seed);
		Log.Write("Using random seed: " + seed);
		
		m_ScreenSize = new Point<Integer>(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Log.Write("Screen size: W=" + m_ScreenSize.x + " H=" + m_ScreenSize.y);
		
		m_ViewSize = new Point<Integer>(m_ScreenSize.x, (int)(m_ScreenSize.x / m_sGameAspectRatio));
		Log.Write("View size: W=" + m_ViewSize.x + " H=" + m_ViewSize.y);
		
		if(!InitCommonResources())
			return false;
		
		return true;
	}
	
	private boolean InitCommonResources()
	{		
		//font -----------------------------------------------------------
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/blox.ttf"));
		FreeTypeFontParameter fontParam = new FreeTypeFontParameter();
		fontParam.size = 12;
		fontParam.characters = "BLCKSDRI";
		
		m_BlockFont = generator.generateFont(fontParam);
		generator.dispose();
		//----------------------------------------------------------------
		
		//textures -------------------------------------------------------
		m_BlockTexture = new Texture(Gdx.files.internal("gfx/blocks.png"));
		
		m_RedBlockRegion = new TextureRegion(m_BlockTexture, 0, 0, 64, 64);
		m_GreenBlockRegion = new TextureRegion(m_BlockTexture, 64, 0, 64, 64);
		m_BlueBlockRegion = new TextureRegion(m_BlockTexture, 128, 0, 64, 64);
		m_OrangeBlockRegion = new TextureRegion(m_BlockTexture, 192, 0, 64, 64);
		
		m_RedBlockFixedRegion = new TextureRegion(m_BlockTexture, 0, 64, 64, 64);
		m_GreenBlockFixedRegion = new TextureRegion(m_BlockTexture, 64, 64, 64, 64);
		m_BlueBlockFixedRegion = new TextureRegion(m_BlockTexture, 128, 64, 64, 64);
		m_OrangeBlockFixedRegion = new TextureRegion(m_BlockTexture, 192, 64, 64, 64);
		//----------------------------------------------------------------
		
		return true;
	}
	
	public void Dispose()
	{
		m_BlockTexture.dispose();
		m_BlockFont.dispose();
	}
}


















