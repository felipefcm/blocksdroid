
package blocks.ui;

import blocks.game.BlocksMatch;
import blocks.game.Blocksdroid;
import blocks.resource.GoogleApiInterface;
import blocks.resource.PreferencesSecurity;
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

import java.time.Instant;
import java.util.Date;

public class EndMatchWindow
{
	private Stage m_Stage;
	private Table m_Table;

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
		m_Batch = ResourceManager.instance.spriteBatch;
		m_Viewport = ResourceManager.instance.viewport;
		m_ShapeRenderer = ResourceManager.instance.shapeRenderer;
		
		m_BgWidth = Blocksdroid.V_WIDTH * 0.7f;
		m_BgHeight = Blocksdroid.V_HEIGHT * 0.45f;
		
		m_Table = new Table();
		m_Table.setFillParent(true);

		m_Stage = new Stage(m_Viewport, m_Batch);
		m_Stage.addActor(m_Table);
		
		m_GameOverLabel = new Label("GAME OVER", ResourceManager.instance.ackLabelStyle);
		m_GameOverLabel.setAlignment(Align.center);
		m_GameOverLabel.setColor(0.2f, 0.28f, 0.36f, 1.0f);
		
		m_ScoreLabel = new Label("SCORE: " + match.GetScore(), ResourceManager.instance.ackLabelStyle);
		m_ScoreLabel.setAlignment(Align.center);
		
		m_BestLabel = new Label("BEST: " + match.GetBestScore(), ResourceManager.instance.ackLabelStyle);
		m_BestLabel.setAlignment(Align.center);
		
		m_RetryButton = new Button(ResourceManager.instance.retryButtonStyle);
		
		m_CancelButton = new Button(ResourceManager.instance.cancelButtonStyle);
		
		m_SubmitScoreButton = new TextButton("SUBMIT SCORE", ResourceManager.instance.textButtonStyle);
		
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
					ScreenManager.instance.SetScreen(new MainMenuScreen());
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
                    GoogleApiInterface googleApiInterface = ResourceManager.instance.googleApiInterface;

                    if(googleApiInterface == null)
                        return;

                    match.ReadBestScoreInPreferences();

                    if(!googleApiInterface.IsConnected())
                    {
                        googleApiInterface.Connect();

                        int timeout = Date.from(Instant.EPOCH).getSeconds() + 60;

                        while(googleApiInterface.IsConnecting() && Date.from(Instant.EPOCH).getSeconds() < timeout)
                            ;
                    }

                    if(googleApiInterface.IsConnected())
                        googleApiInterface.SendScore(match.GetBestScore());
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
			m_ShapeRenderer.rect(0, 0, Blocksdroid.V_WIDTH, Blocksdroid.V_HEIGHT);
			
			m_ShapeRenderer.setColor(0.32f, 0.6f, 0.78f, 1.0f);
			m_ShapeRenderer.rect((Blocksdroid.V_WIDTH - m_BgWidth) / 2.0f, (Blocksdroid.V_HEIGHT - m_BgHeight) / 2.0f, m_BgWidth, m_BgHeight);
		}
		m_ShapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		
		//table.debug();
		//Table.drawDebug(stage);
		
		m_Stage.draw();
	}
	
	public void Dispose()
	{
		m_Stage.dispose();
		m_Table = null;
	}
}
