
package blocks.game;

import blocks.resource.Point;
import blocks.resource.PreferencesSecurity;
import blocks.resource.ResourceManager;
import blocks.ui.EndMatchWindow;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BlocksMatch
{
	public static final int NumRows = 6;
	public static final int NumCols = 5;
	
	public static final float[] GameSpeeds = { 0.6f, 0.5f, 0.4f, 0.3f, 0.2f, 0.1f };

	public float m_GameSpeed;

	private BlockGrid m_BlockGrid;
	private SpriteBatch m_SpriteBatch;
	private Point<Integer> m_ViewSize;
	private EndMatchWindow m_EndMatchWindow;
	
	private int m_Score;
	private int m_BestScore;
	
	private boolean m_ShowEndMatchWindow;
	
	public BlocksMatch()
	{
		m_BlockGrid = new BlockGrid(NumRows, NumCols, this);
		m_EndMatchWindow = new EndMatchWindow();
	}
		
	public void Init()
	{	
		m_SpriteBatch = ResourceManager.m_sInstance.m_SpriteBatch;
		m_ViewSize = ResourceManager.m_sInstance.m_ViewSize;
		
		m_ShowEndMatchWindow = false;
		
		m_GameSpeed = GameSpeeds[0];
		
		m_Score = 0;
		
		ReadBestScoreInPreferences();

		m_BlockGrid.Init();
	}
	
	public void Render()
	{
		m_SpriteBatch.begin();
		{
			ResourceManager.m_sInstance.m_ScoreText.draw(m_SpriteBatch);
			ResourceManager.m_sInstance.m_AckFont.draw(m_SpriteBatch, "" + m_Score, m_ViewSize.x * 0.45f, m_ViewSize.y * 0.74f);
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
		if(m_Score > m_BestScore)
		{
			m_BestScore = m_Score;
			WriteBestScoreInPreferences();
		}
		
		m_ShowEndMatchWindow = true;
		m_EndMatchWindow.Init(this);
	}
	
	public void RestartMatch()
	{
		m_EndMatchWindow.Dispose();
		
		Init();
	}
	
	public void IncrementScore(int inc)
	{
		m_Score += inc;
		
		if(m_Score < 20)
			m_GameSpeed = GameSpeeds[0];
		else
			if(m_Score < 40)
				m_GameSpeed = GameSpeeds[1];
			else
				if(m_Score < 60)
					m_GameSpeed = GameSpeeds[2];
				else
					if(m_Score < 80)
						m_GameSpeed = GameSpeeds[3];
					else
						if(m_Score < 100)
							m_GameSpeed = GameSpeeds[4];
						else
							m_GameSpeed = GameSpeeds[5];
	}
	
	public int GetScore()
	{
		return m_Score;
	}
	
	public int GetBestScore()
	{
		return m_BestScore;
	}
	
	public void WriteBestScoreInPreferences()
	{
		String hash = PreferencesSecurity.m_sInstance.CalculateBestScoreHash(m_BestScore);
		
		ResourceManager.m_sInstance.m_Preferences.putString("BestScoreKey", hash);
		ResourceManager.m_sInstance.m_Preferences.putInteger("BestScore", m_BestScore);
		
		ResourceManager.m_sInstance.m_Preferences.flush();
	}
	
	public void ReadBestScoreInPreferences()
	{
		m_BestScore = ResourceManager.m_sInstance.m_Preferences.getInteger("BestScore", 0);
		
		if(m_BestScore != 0)
		{
			if(!PreferencesSecurity.m_sInstance.IsBestScoreValid(m_BestScore))
				m_BestScore = 0;
		}
	}
	
	public void Dispose()
	{
		m_BlockGrid.Dispose();
	}
}
