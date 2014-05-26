
package blocks.screen;

import blocks.resource.ResourceManager;
import blocks.screen.ScreenManager.ScreenType;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainMenuScreen extends GameScreen
{
	public Stage m_Stage;
	public Table m_Table;
	
	public TextButtonStyle m_TextButtonStyle;
	public TextButton m_PlayButton;
	public TextButton m_TutorialButton;
	public TextButton m_LeaderboardsButton;
	public TextButton m_QuitButton;
	
	private SpriteBatch m_Batch;
	private Viewport m_Viewport;
	
	public MainMenuScreen()
	{
	}
	
	@Override
	public void show()
	{	
		m_Batch = ResourceManager.m_sInstance.m_SpriteBatch;
		m_Viewport = ResourceManager.m_sInstance.m_Viewport;
				
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
		
		m_PlayButton = new TextButton("PLAY", m_TextButtonStyle);
		m_PlayButton.setWidth(m_Viewport.getViewportWidth() * 0.8f);
		m_TutorialButton = new TextButton("TUTORIAL", m_TextButtonStyle);
		m_LeaderboardsButton = new TextButton("LEADERBOARDS", m_TextButtonStyle);
		m_QuitButton = new TextButton("QUIT", m_TextButtonStyle);
		
		m_Table.add(m_PlayButton);
		m_Table.row();
		m_Table.add(m_TutorialButton);
		m_Table.row();
		m_Table.add(m_LeaderboardsButton);
		m_Table.row();
		m_Table.add(m_QuitButton);
	}
	
	@Override
	public void render(float delta)
	{
		m_Stage.act(delta);
		
		m_Stage.draw();
		
		//for debugging
		Table.drawDebug(m_Stage);
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
