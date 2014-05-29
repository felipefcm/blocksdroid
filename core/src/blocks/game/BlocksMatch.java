
package blocks.game;

import blocks.resource.Point;
import blocks.resource.ResourceManager;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BlocksMatch
{
	public static final int NumRows = 6;
	public static final int NumCols = 5;

	private BlockGrid m_BlockGrid;
	private SpriteBatch m_SpriteBatch;
	private Point<Integer> m_ViewSize;
	
	private int m_Score;
	
	public float m_GameSpeed;
	
	public BlocksMatch()
	{
		m_BlockGrid = new BlockGrid(NumRows, NumCols, this);
		m_Score = 0;
		m_GameSpeed = 0.8f;
	}
		
	public void Init()
	{	
		m_SpriteBatch = ResourceManager.m_sInstance.m_SpriteBatch;
		m_ViewSize = ResourceManager.m_sInstance.m_ViewSize;

		m_BlockGrid.Init();
	}
	
	public void Render()
	{
		m_SpriteBatch.begin();
		{
			ResourceManager.m_sInstance.m_ScoreText.draw(m_SpriteBatch);
			ResourceManager.m_sInstance.m_AckFont.draw(m_SpriteBatch, "" + m_Score, m_ViewSize.x * 0.4f, m_ViewSize.y * 0.74f);
		}
		m_SpriteBatch.end();
		
		m_BlockGrid.Render();
	}
	
	public void OnMatchEnded()
	{
		
	}
	
	public void RestartMatch()
	{
		
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
