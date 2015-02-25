
package blocks.game;

import blocks.resource.PreferencesSecurity;
import blocks.resource.ResourceManager;
import blocks.ui.EndMatchWindow;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class BlocksMatch
{
	public static final int NumRows = 8;
	public static final int NumCols = 6;
	
	public static final float[] GameSpeeds = 
	{ 
		0.60f, 0.50f, 0.40f, 0.30f, 
		0.20f, 0.15f, 0.14f, 0.13f,
		0.12f, 0.11f, 0.10f 
	};

	public float gameSpeed;

	private BlockGrid blockGrid;
	private SpriteBatch spriteBatch;
	private ShapeRenderer shapeRenderer;
	private EndMatchWindow endMatchWindow;
	
	private int score;
	private int bestScore;
	
	private boolean showEndMatchWindow;
	
	public Sprite pauseButton;
	private boolean paused;
	
	public BlocksMatch()
	{
		blockGrid = new BlockGrid(NumRows, NumCols, this);
		endMatchWindow = new EndMatchWindow();
	}
		
	public void Init()
	{	
		spriteBatch = ResourceManager.instance.spriteBatch;
		shapeRenderer = ResourceManager.instance.shapeRenderer;
		
		if(pauseButton == null)
		{
			pauseButton = new Sprite(ResourceManager.instance.pauseButtonRegion);
			pauseButton.setBounds(Blocksdroid.V_WIDTH * 0.8f, Blocksdroid.V_HEIGHT * 0.71f, Blocksdroid.V_WIDTH * 0.08f, Blocksdroid.V_WIDTH * 0.08f);
		}
		
		showEndMatchWindow = false;
		paused = false;
		
		gameSpeed = GameSpeeds[0];
		
		ReadBestScoreInPreferences();

		blockGrid.Init();
		score = 0;
		
		//for debugging
		//OnMatchEnded();
	}
	
	public void Render()
	{
		spriteBatch.begin();
		{
			ResourceManager.instance.scoreText.draw(spriteBatch);
			ResourceManager.instance.ackFont.draw(spriteBatch, "" + score, Blocksdroid.V_WIDTH * 0.45f, Blocksdroid.V_HEIGHT * 0.74f);
			
			pauseButton.draw(spriteBatch);
		}
		spriteBatch.end();
		
		if(paused)
		{
			Gdx.gl.glEnable(GL20.GL_BLEND);
			shapeRenderer.begin(ShapeType.Filled);
			{
				shapeRenderer.setColor(0, 0, 0, 0.9f);
				shapeRenderer.rect(0, 0, Blocksdroid.V_WIDTH, Blocksdroid.V_HEIGHT);
			}
			shapeRenderer.end();
			Gdx.gl.glDisable(GL20.GL_BLEND);
			
			spriteBatch.begin();
			{
				ResourceManager.instance.pausedText.draw(spriteBatch);
				ResourceManager.instance.touchToQuitText.draw(spriteBatch);
			}
			spriteBatch.end();
		}
		else		
			blockGrid.Render();
		
		if(showEndMatchWindow)
		{
			endMatchWindow.Render();
		}
	}
	
	public void PauseMatch()
	{
		if(showEndMatchWindow)
			return;
		
		paused = true;
	}
	
	public void UnpauseMatch()
	{
		paused = false;
	}
	
	public boolean IsPaused()
	{
		return paused;
	}
	
	public void OnMatchEnded()
	{
		if(score > bestScore)
		{
			bestScore = score;
			WriteBestScoreInPreferences();
		}
		
		showEndMatchWindow = true;
		endMatchWindow.Init(this);
	}
	
	public void RestartMatch()
	{
		endMatchWindow.Dispose();
		
		Init();
	}
	
	public void IncrementScore(int inc)
	{
		score += inc;
		
		int speedIndex = (int)((float) score / 20.0f);
		
		if(speedIndex < GameSpeeds.length)
			gameSpeed = GameSpeeds[speedIndex];
		else
			gameSpeed = GameSpeeds[GameSpeeds.length - 1];
	}
	
	public int GetScore()
	{
		return score;
	}
	
	public int GetBestScore()
	{
		return bestScore;
	}
	
	public void WriteBestScoreInPreferences()
	{
		String hash = PreferencesSecurity.m_sInstance.CalculateBestScoreHash(bestScore);
		
		ResourceManager.instance.preferences.putString("BestScoreKey", hash);
		ResourceManager.instance.preferences.putInteger("BestScore", bestScore);
		
		ResourceManager.instance.preferences.flush();
	}
	
	public void ReadBestScoreInPreferences()
	{
		bestScore = ResourceManager.instance.preferences.getInteger("BestScore", 0);
		
		if(bestScore != 0)
		{
			if(!PreferencesSecurity.m_sInstance.IsBestScoreValid(bestScore))
				bestScore = 0;
		}
	}
	
	public void Dispose()
	{
		blockGrid.Dispose();
	}
}
