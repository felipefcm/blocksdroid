
package blocks.resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Random;

import org.andengine.engine.Engine;
import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.bitmap.BitmapTextureFormat;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.debug.Debug;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Point;
import blocks.game.Block;
import blocks.game.Blocksdroid;

public class ResourceManager
{
	public static ResourceManager m_sInstance = new ResourceManager();
	
	public Point m_DisplaySize;
	public Point m_ViewSize;
	public float m_ViewCenterX;
	public float m_ViewCenterY;
	
	Line[] m_BorderLines;
	
	//Useful references ----------------------
	public Blocksdroid m_Game;
	public Engine m_Engine;
	public TextureManager m_TextureManager;
	public FontManager m_FontManager;
	public AssetManager m_AssetManager;
	public VertexBufferObjectManager m_VBOManager;
	public Random m_Random;
	//----------------------------------------
	
	//Blocks ---------------------------------
	public BitmapTexture m_BlockTexture;
	public ITextureRegion m_BlueBlockRegion;
	public ITextureRegion m_GreenBlockRegion;
	public ITextureRegion m_RedBlockRegion;
	public ITextureRegion m_OrangeBlockRegion;
	//----------------------------------------
	
	private Font m_TitleFont;
	public Text m_TitleText;
	
	public ResourceManager()
	{
	}

	public void SetDisplaySize(final Point size)
	{
		m_DisplaySize = size;
		
		//if displayAspect != Blocksdroid.AspectRatio there will be black bars
		float displayAspect = (float) size.x / size.y;
		
		Debug.d("Game aspect: " + Blocksdroid.AspectRatio);
		Debug.d("Display aspect: " + displayAspect);
		
		m_ViewSize = new Point();
	
		m_ViewSize.x = size.x;
		m_ViewSize.y = (int) (m_ViewSize.x / Blocksdroid.AspectRatio);
		
		Debug.d("ViewSize = " + m_ViewSize.x + " x " + m_ViewSize.y);
		
		m_ViewCenterX = m_ViewSize.x * 0.5f;
		m_ViewCenterY = m_ViewSize.y * 0.5f;
	}
	
	public void InitManager(Blocksdroid game) throws Exception
	{
		m_Game = game;
		m_Engine = game.getEngine();
		m_TextureManager = game.getTextureManager();
		m_FontManager = game.getFontManager();
		m_AssetManager = game.getAssets();
		m_VBOManager = game.getVertexBufferObjectManager();
		
		long seed = new Date().getTime();
		//m_Random = new Random(seed);
		m_Random = new Random(1399949427524L); //for testing
		Debug.d("Using seed: " + seed);
		
		InitCommonResources();
	}
	
	public void LoadBlocks()
	{
		try
		{
			m_BlockTexture = new BitmapTexture
				(
					m_sInstance.m_TextureManager,
					new IInputStreamOpener() 
					{
						@Override
						public InputStream open() throws IOException 
						{
							return m_sInstance.m_AssetManager.open("gfx/blocks.png");
						}
					},
					BitmapTextureFormat.RGBA_8888,
					TextureOptions.DEFAULT
				);
		}
		catch(IOException e)
		{
			Debug.e("Error while loading blocks: " + e.getMessage());
			return;
		}
		
		m_RedBlockRegion = TextureRegionFactory.extractFromTexture(m_BlockTexture, 0, 0, 64, 64);
		m_GreenBlockRegion = TextureRegionFactory.extractFromTexture(m_BlockTexture, 64, 0, 64, 64);
		m_BlueBlockRegion = TextureRegionFactory.extractFromTexture(m_BlockTexture, 128, 0, 64, 64);
		m_OrangeBlockRegion = TextureRegionFactory.extractFromTexture(m_BlockTexture, 192, 0, 64, 64);
		
		m_BlockTexture.load();
		
		Block.m_sBlockViewSize = 1.0f / 6.25f * m_sInstance.m_ViewSize.x;
	}
	
	public void UnloadBlocks()
	{
		m_BlockTexture.unload();
	}
	
	//Scene independent resources, will be in memory during all application lifetime
	private void InitCommonResources()
	{
		//Tile text ---------------------------------------------------
		m_TitleFont = FontFactory.createFromAsset
				(
					m_sInstance.m_FontManager,
					m_sInstance.m_TextureManager,
					128, 128,
					TextureOptions.BILINEAR_PREMULTIPLYALPHA,
					m_sInstance.m_AssetManager, 
					"fonts/blox.ttf", 
					48,
					false, 
					Color.WHITE
				);
		
		m_TitleFont.load();
		
		float textPosX = m_sInstance.m_ViewSize.x / 2.0f;
		float textPosY = m_sInstance.m_ViewSize.y * 0.93f;
		
		m_TitleText = new Text(textPosX, textPosY,  m_TitleFont, "BLOCKSDROID", m_sInstance.m_VBOManager);
		
		float textScale = m_sInstance.m_ViewSize.y * 0.1f / m_TitleFont.getLineHeight();
		m_TitleText.setScale(textScale);
		
		m_TitleText.setIgnoreUpdate(true);
		//-------------------------------------------------------------
		
		//Border lines ------------------------------------------------
		m_BorderLines = new Line[4];
		
		float borderW = m_sInstance.m_ViewSize.x * 0.82f;
		float borderH = m_sInstance.m_ViewSize.y * 0.73f;
		
		float left = (m_sInstance.m_ViewSize.x - borderW) / 2.0f;
		float right = left + borderW;
		float bottom = (m_sInstance.m_ViewSize.y - borderH) / 2.0f - m_sInstance.m_ViewSize.y * 0.11f;
		float top = bottom + borderH;
				
		m_BorderLines[0] = new Line(left, bottom, right, bottom, 1.0f, m_VBOManager);
		m_BorderLines[1] = new Line(left, bottom, left, top, 1.0f, m_VBOManager);
		m_BorderLines[2] = new Line(right, bottom, right, top, 1.0f, m_VBOManager);
		m_BorderLines[3] = new Line(left, top, right, top, 1.0f, m_VBOManager);
		
		m_BorderLines[0].setColor(Color.WHITE);
		m_BorderLines[1].setColor(Color.WHITE);
		m_BorderLines[2].setColor(Color.WHITE);
		m_BorderLines[3].setColor(Color.WHITE);
		//-------------------------------------------------------------
	}
	
	public void AttachBorderLines(Entity entity)
	{
		entity.attachChild(m_BorderLines[0]);
		entity.attachChild(m_BorderLines[1]);
		entity.attachChild(m_BorderLines[2]);
		entity.attachChild(m_BorderLines[3]);
	}
	
	public void DettachBorderLines()
	{
		m_BorderLines[0].detachSelf();
		m_BorderLines[1].detachSelf();
		m_BorderLines[2].detachSelf();
		m_BorderLines[3].detachSelf();
	}
}








