
package blocks.resource;

import java.util.Date;
import java.util.Random;

import blocks.ad.AdManager;
import blocks.game.Block;
import blocks.game.Blocksdroid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class ResourceManager 
{
	public static ResourceManager m_sInstance = new ResourceManager();
	
	public static final float m_sGameAspectRatio = 9.0f / 16.0f;
	
    //Useful references --------------------------------------------------
	public Blocksdroid game;
	public Random random;
	public Point<Integer> screenSize;
	
	public FitViewport viewport;
	public OrthographicCamera camera;
	public ShapeRenderer shapeRenderer;
	public SpriteBatch spriteBatch;
	
	public final Matrix4 identityMatrix = new Matrix4();
	
	public Preferences preferences;
	
	public AdManager adManager;
    //--------------------------------------------------------------------
	
    //Textures -----------------------------------------------------------
	public Texture blockTexture;
	public TextureRegion redBlockRegion;
	public TextureRegion greenBlockRegion;
	public TextureRegion blueBlockRegion;
	public TextureRegion orangeBlockRegion;
	public TextureRegion redBlockFixedRegion;
	public TextureRegion greenBlockFixedRegion;
	public TextureRegion blueBlockFixedRegion;
	public TextureRegion orangeBlockFixedRegion;
	
	public Texture mainSkinTexture;
	public TextureRegion buttonUpRegion;
	public TextureRegion buttonDownRegion;
	public TextureRegion retryButtonRegion;
	public TextureRegion cancelButtonRegion;
	public TextureRegion pauseButtonRegion;
    //--------------------------------------------------------------------
	
    //Fonts --------------------------------------------------------------
	public BitmapFont bloxFont;
	public BitmapFontCache blocksdroidText;
	public BitmapFont ackFont;
	public BitmapFontCache scoreText;
	public BitmapFontCache pausedText;
	public BitmapFontCache touchToQuitText;
    //--------------------------------------------------------------------
	
    //Skin styles --------------------------------------------------------
	public TextButtonStyle textButtonStyle;
	public ButtonStyle retryButtonStyle;
	public ButtonStyle cancelButtonStyle;
	public LabelStyle titleLabelStyle;
	public LabelStyle ackLabelStyle;
    //--------------------------------------------------------------------
	
	public boolean Init(final Blocksdroid game)
	{
		this.game = game;
		
		long seed = new Date().getTime();
		random = new Random(seed);
		Log.Write("Using random seed: " + seed);
		
		screenSize = new Point<Integer>(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Log.Write("Screen size: W=" + screenSize.x + " H=" + screenSize.y);
		
		camera = new OrthographicCamera();
		viewport = new FitViewport(Blocksdroid.V_WIDTH, Blocksdroid.V_HEIGHT, camera);
		viewport.update(screenSize.x, screenSize.y, true);
		
		spriteBatch = new SpriteBatch(80);
		spriteBatch.setProjectionMatrix(camera.combined);
		
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(camera.combined);
		
		preferences = Gdx.app.getPreferences("Blocksdroid_Prefs");
		
		if(!InitCommonResources())
			return false;
		
		return true;
	}
	
	private boolean InitCommonResources()
	{		
	    //font -----------------------------------------------------------
		bloxFont = new BitmapFont(Gdx.files.internal("fonts/blox.fnt"));
		bloxFont.setScale(Blocksdroid.V_WIDTH * 0.0035f);
		bloxFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		ackFont = new BitmapFont(Gdx.files.internal("fonts/ack.fnt"));
		ackFont.setScale(Blocksdroid.V_WIDTH * 0.0011f);
		
		TextBounds bounds = bloxFont.getBounds("BLOCKSDROID");

		blocksdroidText = new BitmapFontCache(bloxFont);
		blocksdroidText.setColor(0.93f, 0.95f, 0.95f, 1.0f);
		blocksdroidText.addText("BLOCKSDROID", (Blocksdroid.V_WIDTH - bounds.width) / 2.0f, Blocksdroid.V_HEIGHT * 0.9f);
		
		scoreText = new BitmapFontCache(ackFont);
		scoreText.setColor(0.93f, 0.95f, 0.95f, 1.0f);
		scoreText.addText("SCORE:", Blocksdroid.V_WIDTH * 0.125f, Blocksdroid.V_HEIGHT * 0.74f);
		
		bounds = ackFont.getBounds("PAUSED");
		
		pausedText = new BitmapFontCache(ackFont);
		pausedText.setColor(0.93f, 0.95f, 0.95f, 1.0f);
		pausedText.addText("PAUSED", (Blocksdroid.V_WIDTH - bounds.width) / 2.0f, (Blocksdroid.V_HEIGHT - bounds.height) / 2.0f + Blocksdroid.V_HEIGHT * 0.03f);
		
		bounds = ackFont.getBounds("TOUCH TO RESUME");
		
		touchToQuitText = new BitmapFontCache(ackFont);
		touchToQuitText.setColor(0.93f, 0.95f, 0.95f, 1.0f);
		touchToQuitText.addText("TOUCH TO RESUME", (Blocksdroid.V_WIDTH - bounds.width) / 2.0f, (Blocksdroid.V_HEIGHT - bounds.height) / 2.0f - Blocksdroid.V_HEIGHT * 0.03f);
	    //----------------------------------------------------------------
		
	    //textures -------------------------------------------------------
		blockTexture = new Texture(Gdx.files.internal("gfx/blocks.png"));
		
		redBlockRegion = new TextureRegion(blockTexture, 0, 0, 64, 64);
		greenBlockRegion = new TextureRegion(blockTexture, 64, 0, 64, 64);
		blueBlockRegion = new TextureRegion(blockTexture, 128, 0, 64, 64);
		orangeBlockRegion = new TextureRegion(blockTexture, 192, 0, 64, 64);
		
		redBlockFixedRegion = new TextureRegion(blockTexture, 0, 64, 64, 64);
		greenBlockFixedRegion = new TextureRegion(blockTexture, 64, 64, 64, 64);
		blueBlockFixedRegion = new TextureRegion(blockTexture, 128, 64, 64, 64);
		orangeBlockFixedRegion = new TextureRegion(blockTexture, 192, 64, 64, 64);
		
		Block.m_sBlockViewSize = Blocksdroid.V_WIDTH * 0.15f;
		
		mainSkinTexture = new Texture(Gdx.files.internal("gfx/UI/main_skin.png"));
		mainSkinTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		buttonUpRegion = new TextureRegion(mainSkinTexture, 0, 0, 128, 32);
		buttonDownRegion = new TextureRegion(mainSkinTexture, 0, 32, 128, 32);
		retryButtonRegion = new TextureRegion(mainSkinTexture, 0, 64, 32, 32);
		cancelButtonRegion = new TextureRegion(mainSkinTexture, 32, 64, 32, 32);
		pauseButtonRegion = new TextureRegion(mainSkinTexture, 64, 64, 32, 32);
	    //----------------------------------------------------------------
		
	    //skin -----------------------------------------------------------
		textButtonStyle = new TextButtonStyle
		(
			new TextureRegionDrawable(buttonUpRegion),
			new TextureRegionDrawable(buttonDownRegion),
			new TextureRegionDrawable(buttonUpRegion),
                ackFont
		);
		
		retryButtonStyle = new ButtonStyle
		(
			new TextureRegionDrawable(retryButtonRegion),
			new TextureRegionDrawable(retryButtonRegion),
			new TextureRegionDrawable(retryButtonRegion)
		);
		
		cancelButtonStyle = new ButtonStyle
		(
			new TextureRegionDrawable(cancelButtonRegion),
			new TextureRegionDrawable(cancelButtonRegion),
			new TextureRegionDrawable(cancelButtonRegion)
		);
				
		titleLabelStyle = new LabelStyle
		(
                bloxFont,
			new Color(Color.rgba8888(0.93f, 0.95f, 0.95f, 1.0f))
		);
		
		ackLabelStyle = new LabelStyle
		(
                ackFont,
			new Color(Color.rgba8888(0.93f, 0.95f, 0.95f, 1.0f))
		);
	    //----------------------------------------------------------------
		
		return true;
	}
	
	public void DisposeCommonResources()
	{
		mainSkinTexture.dispose();
		blockTexture.dispose();
		ackFont.dispose();
		bloxFont.dispose();
	}
	
	public void Dispose()
	{
		DisposeCommonResources();
		
		shapeRenderer.dispose();
		spriteBatch.dispose();
	}
}


















