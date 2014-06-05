
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
	
	public static final float[] GameSpeeds = 
	{ 
		0.60f, 0.50f, 0.40f, 0.30f, 
		0.20f, 0.15f, 0.14f, 0.13f,
		0.12f, 0.11f, 0.10f 
	};

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
		
		//for debugging
		//OnMatchEnded();
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
		
		int speedIndex = (int)((float) m_Score / 20.0f);
		
		if(speedIndex < GameSpeeds.length)
			m_GameSpeed = GameSpeeds[speedIndex];
		else
			m_GameSpeed = GameSpeeds[GameSpeeds.length - 1];
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
