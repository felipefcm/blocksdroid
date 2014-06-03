
package blocks.screen;

import blocks.resource.Point;
import blocks.resource.ResourceManager;
import blocks.screen.ScreenManager.ScreenType;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainMenuScreen extends GameScreen
{
	public Stage m_Stage;
	public Table m_Table;
	
	public Label m_TitleLabel;
	
	public TextButton m_PlayButton;
	public TextButton m_TutorialButton;
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
		
		LabelStyle labelStyle = ResourceManager.m_sInstance.m_TitleLabelStyle;
		TextButtonStyle textButtonStyle = ResourceManager.m_sInstance.m_TextButtonStyle;
		
		m_TitleLabel = new Label("BLOCKSDROID", labelStyle);
		m_PlayButton = new TextButton("PLAY", textButtonStyle);
		m_TutorialButton = new TextButton("TUTORIAL", textButtonStyle);
		m_QuitButton = new TextButton("QUIT", textButtonStyle);
		
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
		m_Table = null;
	}

	@Override
	public ScreenType GetType()
	{
		return ScreenType.MainMenu;
	}
}
