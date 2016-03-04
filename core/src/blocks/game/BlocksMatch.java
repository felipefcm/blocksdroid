
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

    public static final float InitialGameSpeed = 0.5f;

    public enum MatchState
    {
        NotStarted,
        Running,
        Paused,
        Ended
    }

    private MatchState matchState;

    public float gameSpeed;

    private BlockGrid blockGrid;
    private SpriteBatch spriteBatch;
    private ShapeRenderer shapeRenderer;
    private EndMatchWindow endMatchWindow;

    private int score;
    private int bestScore;

    public Sprite pauseButton;

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
            pauseButton.setBounds(Blocksdroid.V_WIDTH * 0.8f, Blocksdroid.V_HEIGHT * 0.71f, Blocksdroid.V_WIDTH * 0.09f, Blocksdroid.V_WIDTH * 0.09f);
        }

        gameSpeed = InitialGameSpeed;

        matchState = MatchState.NotStarted;

        blockGrid.Init();
        score = 0;

        //for debugging
        //OnMatchEnded();
    }

    public void Start()
    {
        matchState = MatchState.Running;
    }

    public void Render()
    {
        switch(matchState)
        {
            case NotStarted:
            break;

            case Running:
            {
                spriteBatch.begin();
                {
                    ResourceManager.instance.scoreText.draw(spriteBatch);
                    ResourceManager.instance.ackFont.draw(spriteBatch, "" + score, Blocksdroid.V_WIDTH * 0.45f, Blocksdroid.V_HEIGHT * 0.74f);

                    pauseButton.draw(spriteBatch);
                }
                spriteBatch.end();

                blockGrid.Render();
            }
            break;

            case Paused:
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
            break;

            case Ended:
            {
                endMatchWindow.Render();
            }
            break;
        }
    }

    public void SetPause(boolean paused)
    {
        matchState = paused ? MatchState.Paused : MatchState.Running;
    }

    public boolean IsPaused()
    {
        return (matchState == MatchState.Paused);
    }

    public void OnMatchEnded()
    {
        matchState = MatchState.Ended;

        ReadBestScoreInPreferences();

        if(score > bestScore)
        {
            bestScore = score;
            WriteBestScoreInPreferences();
        }

        endMatchWindow.Init(this);
    }

    public void RestartMatch()
    {
        endMatchWindow.Dispose();

        Init();
        Start();
    }

    public void IncrementScore(int inc)
    {
        score += inc;

        gameSpeed = Math.max(0.08f, gameSpeed * 0.99f);
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
