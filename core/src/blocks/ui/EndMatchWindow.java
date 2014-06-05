
package blocks.ui;

import blocks.game.BlocksMatch;
import blocks.resource.Point;
import blocks.resource.ResourceManager;
import blocks.screen.MainMenuScreen;
import blocks.screen.ScreenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;

public class EndMatchWindow
{
	private Stage m_Stage;
	private Table m_Table;
	
	private Point<Integer> m_ViewSize;
	private Viewport m_Viewport;
	private SpriteBatch m_Batch;
	private ShapeRenderer m_ShapeRenderer;
	
	private float m_BgWidth;
	private float m_BgHeight;
	
	private Label m_GameOverLabel;
	private Label m_ScoreLabel;
	private Label m_BestLabel;
	private Button m_RetryButton;
	private Button m_CancelButton;
	private TextButton m_SubmitScoreButton;
	
	public EndMatchWindow()
	{
	}
	
	public void Init(final BlocksMatch match)
	{
		m_Batch = ResourceManager.m_sInstance.m_SpriteBatch;
		m_Viewport = ResourceManager.m_sInstance.m_Viewport;
		m_ViewSize = ResourceManager.m_sInstance.m_ViewSize;
		m_ShapeRenderer = ResourceManager.m_sInstance.m_ShapeRenderer;
		
		m_BgWidth = m_ViewSize.x * 0.7f;
		m_BgHeight = m_ViewSize.y * 0.4f;
		
		m_Table = new Table();
		m_Table.setFillParent(true);

		m_Stage = new Stage(m_Viewport, m_Batch);
		m_Stage.addActor(m_Table);
		
		m_GameOverLabel = new Label("GAME OVER", ResourceManager.m_sInstance.m_AckLabelStyle);
		m_GameOverLabel.setAlignment(Align.center);
		m_GameOverLabel.setColor(0.2f, 0.28f, 0.36f, 1.0f);
		
		m_ScoreLabel = new Label("SCORE: " + match.GetScore(), ResourceManager.m_sInstance.m_AckLabelStyle);
		m_ScoreLabel.setAlignment(Align.center);
		
		m_BestLabel = new Label("BEST: " + match.GetBestScore(), ResourceManager.m_sInstance.m_AckLabelStyle);
		m_BestLabel.setAlignment(Align.center);
		
		m_RetryButton = new Button(ResourceManager.m_sInstance.m_RetryButtonStyle);
		
		m_CancelButton = new Button(ResourceManager.m_sInstance.m_CancelButtonStyle);
		
		m_SubmitScoreButton = new TextButton("SUBMIT SCORE", ResourceManager.m_sInstance.m_TextButtonStyle);
		
		m_Table.add(m_GameOverLabel).width(m_BgWidth * 0.8f).padBottom(8.0f).colspan(2);
		m_Table.row();
		m_Table.add(m_ScoreLabel).colspan(2);
		m_Table.row();
		m_Table.add(m_BestLabel).colspan(2);
		m_Table.row();
		
		m_Table.add(m_RetryButton).width(m_BgWidth * 0.2f).height(m_BgWidth * 0.2f).padTop(m_BgHeight * 0.1f);
		m_Table.add(m_CancelButton).width(m_BgWidth * 0.2f).height(m_BgWidth * 0.2f).padTop(m_BgHeight * 0.1f);
		m_Table.row();
		
		m_Table.add(m_SubmitScoreButton).padTop(m_BgWidth * 0.07f).colspan(2);
		
		Gdx.input.setInputProcessor(m_Stage);
		
		m_RetryButton.addListener
		(
			new ClickListener()
			{
				@Override
				public void clicked(InputEvent event, float x, float y)
				{
					match.RestartMatch();
				}
			}
		);
		
		m_CancelButton.addListener
		(
			new ClickListener()
			{
				@Override
				public void clicked(InputEvent event, float x, float y)
				{
					ScreenManager.m_sInstance.SetScreen(new MainMenuScreen());
				}
			}
		);
		
		m_SubmitScoreButton.addListener
		(
			new ClickListener()
			{
				@Override
				public void clicked(InputEvent event, float x, float y)
				{
					ResourceManager.m_sInstance.m_SwarmResources.SubmitAndShowLeaderboard(match.GetBestScore());
				}
			}
		);
	}
	
	public void Render()
	{		
		Gdx.gl.glEnable(GL20.GL_BLEND);
		m_ShapeRenderer.begin(ShapeType.Filled);
		{
			m_ShapeRenderer.setColor(0, 0, 0, 0.75f);
			m_ShapeRenderer.rect(0, 0, m_ViewSize.x, m_ViewSize.y);
			
			m_ShapeRenderer.setColor(0.32f, 0.6f, 0.78f, 1.0f);
			m_ShapeRenderer.rect((m_ViewSize.x - m_BgWidth) / 2.0f, (m_ViewSize.y - m_BgHeight) / 2.0f, m_BgWidth, m_BgHeight);
		}
		m_ShapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		
		//m_Table.debug();
		//Table.drawDebug(m_Stage);
		
		m_Stage.draw();
	}
	
	public void Dispose()
	{
		m_Stage.dispose();
		m_Table = null;
	}
}
