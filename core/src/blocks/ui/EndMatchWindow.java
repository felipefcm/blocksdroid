
package blocks.ui;

import blocks.resource.Point;
import blocks.resource.ResourceManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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
	private Button m_RetryButton;
	private Button m_CancelButton;
	
	public EndMatchWindow()
	{
	}
	
	public void Init()
	{
		m_Batch = ResourceManager.m_sInstance.m_SpriteBatch;
		m_Viewport = ResourceManager.m_sInstance.m_Viewport;
		m_ViewSize = ResourceManager.m_sInstance.m_ViewSize;
		m_ShapeRenderer = ResourceManager.m_sInstance.m_ShapeRenderer;
		
		m_BgWidth = m_ViewSize.x * 0.7f;
		m_BgHeight = m_ViewSize.y * 0.35f;
		
		m_Table = new Table();
		m_Table.setFillParent(true);

		m_Stage = new Stage(m_Viewport, m_Batch);
		m_Stage.addActor(m_Table);
		
		m_GameOverLabel = new Label("GAME OVER", ResourceManager.m_sInstance.m_AckLabelStyle);
		m_RetryButton = new Button(ResourceManager.m_sInstance.m_RetryButtonStyle);
		m_CancelButton = new Button(ResourceManager.m_sInstance.m_CancelButtonStyle);
		
		m_Table.add(m_GameOverLabel);
		m_Table.row();
		m_Table.add(m_RetryButton).spaceRight(m_BgWidth * 0.1f);
		m_Table.add(m_CancelButton);
		
	}
	
	public void Render()
	{		
		Gdx.gl.glEnable(GL20.GL_BLEND);
		m_ShapeRenderer.begin(ShapeType.Filled);
		{
			m_ShapeRenderer.setColor(0, 0, 0, 0.2f);
			m_ShapeRenderer.rect(0, 0, m_ViewSize.x, m_ViewSize.y);
			
			m_ShapeRenderer.setColor(0.32f, 0.6f, 0.78f, 1.0f);
			m_ShapeRenderer.rect((m_ViewSize.x - m_BgWidth) / 2.0f, (m_ViewSize.y - m_BgHeight) / 2.0f, m_BgWidth, m_BgHeight);
		}
		m_ShapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		
		m_Stage.draw();
	}
	
	public void Dispose()
	{
		m_Stage.dispose();
		m_Table = null;
	}
}
