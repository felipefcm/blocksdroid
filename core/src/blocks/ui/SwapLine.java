
package blocks.ui;

import blocks.resource.ResourceManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class SwapLine
{
	public boolean m_IsVisible;
	public boolean m_WrongMove;
	
	private float m_Duration;
	private float m_Elapsed;
	private float m_Alpha;
	
	private Vector2 m_StartPoint;
	private Vector2 m_EndPoint;
	
	public SwapLine()
	{
		m_IsVisible = false;
		m_Duration = 0.6f;
	}
	
	public void CreateLine(Vector2 startPoint, Vector2 endPoint)
	{
		CreateLine(startPoint, endPoint, false);
	}
	
	public void CreateLine(Vector2 startPoint, Vector2 endPoint, boolean wrongMove)
	{
		if(m_IsVisible)
			return;
		
		m_WrongMove = wrongMove;
		m_StartPoint = startPoint;
		m_EndPoint = endPoint;
		
		m_Alpha = 1.0f;
		m_Elapsed = 0;
		
		m_IsVisible = true;
	}
	
	public void Render(ShapeRenderer shapeRenderer)
	{
		m_Elapsed += Gdx.graphics.getDeltaTime();
		
		if(m_Elapsed >= m_Duration)
		{
			m_IsVisible = false;
			return;
		}
		
		m_Alpha = 1.0f - m_Elapsed / m_Duration;
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glLineWidth(ResourceManager.m_sInstance.m_ViewSize.y * 0.00625f);
		
		shapeRenderer.begin(ShapeType.Line);
		{
			if(!m_WrongMove)
				shapeRenderer.setColor(0.0f, 0.0f, 1.0f, m_Alpha);
			else
				shapeRenderer.setColor(1.0f, 0.0f, 0.0f, m_Alpha);
			
			shapeRenderer.line(m_StartPoint.x, m_StartPoint.y, m_EndPoint.x, m_EndPoint.y);
		}
		shapeRenderer.end();
		
		Gdx.gl.glLineWidth(1.0f);
		Gdx.gl.glDisable(GL20.GL_BLEND);
	}
}
