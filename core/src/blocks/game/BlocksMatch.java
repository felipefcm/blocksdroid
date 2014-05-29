
package blocks.game;

import blocks.resource.Point;
import blocks.resource.ResourceManager;
import blocks.ui.EndMatchWindow;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class BlocksMatch
{
	public static final int NumRows = 6;
	public static final int NumCols = 5;

	public float m_GameSpeed;

	private BlockGrid m_BlockGrid;
	private SpriteBatch m_SpriteBatch;
	private ShapeRenderer m_ShapeRenderer;
	private Point<Integer> m_ViewSize;
	private EndMatchWindow m_EndMatchWindow;
	
	private int m_Score;
	
	private boolean m_ShowEndMatchWindow;
	
	public BlocksMatch()
	{
		m_BlockGrid = new BlockGrid(NumRows, NumCols, this);
		m_Score = 0;
		m_GameSpeed = 0.8f;
		m_EndMatchWindow = new EndMatchWindow();
		m_ShowEndMatchWindow = false;
	}
		
	public void Init()
	{	
		m_SpriteBatch = ResourceManager.m_sInstance.m_SpriteBatch;
		m_ShapeRenderer = ResourceManager.m_sInstance.m_ShapeRenderer;
		m_ViewSize = ResourceManager.m_sInstance.m_ViewSize;

		m_BlockGrid.Init();
		
		//for debug
		OnMatchEnded();
	}
	
	public void Render()
	{
		m_SpriteBatch.begin();
		{
			ResourceManager.m_sInstance.m_ScoreText.draw(m_SpriteBatch);
			ResourceManager.m_sInstance.m_AckFont.draw(m_SpriteBatch, "" + m_Score, m_ViewSize.x * 0.4f, m_ViewSize.y * 0.75f);
		}
		m_SpriteBatch.end();
		
		m_BlockGrid.Render();
		
		if(m_ShowEndMatchWindow)
		{
			m_EndMatchWindow.Render();
		}
	}
	
	public void OnMatchEnded()
	{
		m_ShowEndMatchWindow = true;
		m_EndMatchWindow.Init();
	}
	
	public void RestartMatch()
	{
		m_ShowEndMatchWindow = false;
		m_EndMatchWindow.Dispose();
		
		m_BlockGrid.Init();
	}
	
	public void IncrementScore(int inc)
	{
		m_Score += inc;
		
		if(m_Score < 20)
			m_GameSpeed = 0.7f;
		else
			if(m_Score < 40)
				m_GameSpeed = 0.6f;
			else
				if(m_Score < 60)
					m_GameSpeed = 0.4f;
				else
					if(m_Score < 80)
						m_GameSpeed = 0.3f;
					else
						if(m_Score < 100)
							m_GameSpeed = 0.2f;
						else
							m_GameSpeed = 0.1f;
	}
	
	public int GetScore()
	{
		return m_Score;
	}
	
	public void Dispose()
	{
		m_BlockGrid.Dispose();
	}
}
