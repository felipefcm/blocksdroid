
package blocks.resource;

import java.util.Date;
import java.util.Random;

import blocks.game.Block;
import blocks.game.Blocksdroid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class ResourceManager 
{
	public static ResourceManager m_sInstance = new ResourceManager();
	
	public static final float m_sGameAspectRatio = 3.0f / 4.0f;
	
//Useful references --------------------------------------------------
	public Blocksdroid m_Game;
	public Random m_Random;
	public Point<Integer> m_ScreenSize;
	public Point<Integer> m_ViewSize;
	
	public Viewport m_Viewport;
	public OrthographicCamera m_Camera;
	public ShapeRenderer m_ShapeRenderer;
	public SpriteBatch m_SpriteBatch;
	
	public final Matrix4 m_IdentityMatrix = new Matrix4();
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
	public BitmapFont m_BloxFont;
	public BitmapFontCache m_BlocksdroidText;
	public BitmapFont m_AckFont;
	public BitmapFontCache m_ScoreText;
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
		
		m_Camera = new OrthographicCamera();
		m_Viewport = new FitViewport(m_ViewSize.x, m_ViewSize.y, m_Camera);
		m_Viewport.update(m_ScreenSize.x, m_ScreenSize.y, true);
		
		m_SpriteBatch = new SpriteBatch(80);
		m_SpriteBatch.setProjectionMatrix(m_Camera.combined);
		
		m_ShapeRenderer = new ShapeRenderer();
		m_ShapeRenderer.setProjectionMatrix(m_Camera.combined);
		
		if(!InitCommonResources())
			return false;
		
		return true;
	}
	
	private boolean InitCommonResources()
	{		
	//font -----------------------------------------------------------
		m_BloxFont = new BitmapFont(Gdx.files.internal("fonts/blox.fnt"));
		m_BloxFont.setScale(m_ViewSize.x * 0.003f);
		
		m_AckFont = new BitmapFont(Gdx.files.internal("fonts/ack.fnt"));
		m_AckFont.setScale(m_ViewSize.x * 0.0009f);
		
		TextBounds bounds = m_BloxFont.getBounds("BLOCKSDROID");

		m_BlocksdroidText = new BitmapFontCache(m_BloxFont);
		m_BlocksdroidText.setColor(0.95f, 0.95f, 0.95f, 1.0f);
		m_BlocksdroidText.addText("BLOCKSDROID", (m_ViewSize.x - bounds.width) / 2.0f, m_ViewSize.y * 0.98f);
		
		m_ScoreText = new BitmapFontCache(m_AckFont);
		m_ScoreText.setColor(0.95f, 0.95f, 0.95f, 1.0f);
		m_ScoreText.addText("SCORE:",  m_ViewSize.x * 0.125f, m_ViewSize.y * 0.74f);
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
		
		Block.m_sBlockViewSize = m_ViewSize.x * 0.15f;
	//----------------------------------------------------------------
		
		return true;
	}
	
	public void Dispose()
	{
		m_BlockTexture.dispose();
		m_BloxFont.dispose();
		m_ShapeRenderer.dispose();
		m_SpriteBatch.dispose();
	}
}


















