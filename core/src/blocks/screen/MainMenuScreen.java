
package blocks.screen;

import blocks.resource.Point;
import blocks.resource.ResourceManager;
import blocks.screen.ScreenManager.ScreenType;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainMenuScreen extends GameScreen
{
	public Stage m_Stage;
	public Table m_Table;
	
	public TextButtonStyle m_TextButtonStyle;
	public LabelStyle m_LabelStyle;
	
	public Label m_TitleLabel;
	
	public TextButton m_PlayButton;
	public TextButton m_TutorialButton;
	public TextButton m_LeaderboardsButton;
	public TextButton m_QuitButton;
	
	private SpriteBatch m_Batch;
	private Viewport m_Viewport;
	private Point<Integer> m_ViewSize;
	
	public MainMenuScreen()
	{
	}
	
	@Override
	public void show()
	{	
		m_Batch = ResourceManager.m_sInstance.m_SpriteBatch;
		m_Viewport = ResourceManager.m_sInstance.m_Viewport;
		m_ViewSize = ResourceManager.m_sInstance.m_ViewSize;
				
		m_Table = new Table();
		m_Table.setFillParent(true);
		
		m_Stage = new Stage(m_Viewport, m_Batch);
		m_Stage.addActor(m_Table);
		
		Gdx.input.setInputProcessor(m_Stage);
		
		m_TextButtonStyle = new TextButtonStyle
		(
			new TextureRegionDrawable(ResourceManager.m_sInstance.m_ButtonUpRegion),
			new TextureRegionDrawable(ResourceManager.m_sInstance.m_ButtonDownRegion),
			new TextureRegionDrawable(ResourceManager.m_sInstance.m_ButtonUpRegion),
			ResourceManager.m_sInstance.m_AckFont
		);
		
		m_LabelStyle = new LabelStyle
		(
			ResourceManager.m_sInstance.m_BloxFont, 
			new Color(Color.rgba8888(0.93f, 0.95f, 0.95f, 1.0f))
		);
		
		m_TitleLabel = new Label("BLOCKSDROID", m_LabelStyle);
		m_PlayButton = new TextButton("PLAY", m_TextButtonStyle);
		m_TutorialButton = new TextButton("TUTORIAL", m_TextButtonStyle);
		m_LeaderboardsButton = new TextButton("LEADERBOARDS", m_TextButtonStyle);
		m_QuitButton = new TextButton("QUIT", m_TextButtonStyle);
		
		m_PlayButton.addListener
		(
			new ClickListener()
			{
				@Override
				public void clicked(InputEvent event, float x, float y)
				{
					ScreenManager.m_sInstance.SetScreen(new PlayScreen());
				}
			}
		);
		
		m_TutorialButton.addListener
		(
			new ClickListener()
			{
				@Override
				public void clicked(InputEvent event, float x, float y)
				{
					ScreenManager.m_sInstance.SetScreen(new TutorialScreen());
				}
			}
		);
		
		m_LeaderboardsButton.addListener
		(
			new ClickListener()
			{
				@Override
				public void clicked(InputEvent event, float x, float y)
				{
					
				}
			}
		);
		
		m_QuitButton.addListener
		(
			new ClickListener()
			{
				@Override
				public void clicked(InputEvent event, float x, float y)
				{
					dispose();
					ResourceManager.m_sInstance.m_Game.OnExit();
				}
			}
		);
		
		float buttonsWidth = m_ViewSize.x * 0.8f;
		float buttonsHeight = m_ViewSize.y * 0.12f;
		float buttonsSpacing = m_ViewSize.y * 0.0078f;
		
		m_Table.add(m_TitleLabel).padBottom(m_ViewSize.y * 0.02f);
		m_Table.row();
		
		m_Table.add(m_PlayButton).width(buttonsWidth).height(buttonsHeight).padTop(m_ViewSize.y * 0.08f).spaceBottom(buttonsSpacing);
		m_Table.row();
		m_Table.add(m_TutorialButton).width(buttonsWidth).height(buttonsHeight).spaceBottom(buttonsSpacing);
		m_Table.row();
		m_Table.add(m_LeaderboardsButton).width(buttonsWidth).height(buttonsHeight).spaceBottom(buttonsSpacing);
		m_Table.row();
		m_Table.add(m_QuitButton).width(buttonsWidth).height(buttonsHeight);
	}
	
	@Override
	public void render(float delta)
	{
		m_Stage.act(delta);
		
		//m_Table.debug();
		
		m_Stage.draw();
		
		//for debugging
		//Table.drawDebug(m_Stage);
	}

	@Override
	public void resize(int width, int height)
	{	
	}

	@Override
	public void hide()
	{
		dispose();
	}

	@Override
	public void pause()
	{	
	}

	@Override
	public void resume()
	{	
	}

	@Override
	public void dispose()
	{	
		m_Stage.dispose();
	}

	@Override
	public ScreenType GetType()
	{
		return ScreenType.MainMenu;
	}
}
