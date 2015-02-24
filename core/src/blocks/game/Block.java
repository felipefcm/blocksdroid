
package blocks.game;

import blocks.resource.Point;
import blocks.resource.ResourceManager;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class Block extends Sprite
{
	public static final int m_sBlockRealSize = 64;
	public static float m_sBlockViewSize;
	
	public enum BlockType
	{
		Blue,
		Green,
		Red,
		Orange
	}
	
	public boolean m_IsPlaced;
	
	private boolean m_IsFixed;
	
	private BlockType m_Type;
	
	private int m_GridPosX;
	private int m_GridPosY;
	
	public Block(BlockType type)
	{
		m_GridPosX = m_GridPosY = 0;
		
		m_IsPlaced = false;
		m_IsFixed = false;
		
		m_Type = type;
		ApplyTexture();
	}
	
	private void ApplyTexture()
	{
		switch(m_Type)
		{
			case Red:
			{
				if(!m_IsFixed)
					this.setRegion(ResourceManager.m_sInstance.redBlockRegion);
				else
					this.setRegion(ResourceManager.m_sInstance.redBlockFixedRegion);
			}
			break;
			
			case Green:
			{
				if(!m_IsFixed)
					this.setRegion(ResourceManager.m_sInstance.greenBlockRegion);
				else
					this.setRegion(ResourceManager.m_sInstance.greenBlockFixedRegion);
			}
			break;
			
			case Blue:
			{
				if(!m_IsFixed)
					this.setRegion(ResourceManager.m_sInstance.blueBlockRegion);
				else
					this.setRegion(ResourceManager.m_sInstance.blueBlockFixedRegion);
			}
			break;
			
			case Orange:
			{
				if(!m_IsFixed)
					this.setRegion(ResourceManager.m_sInstance.orangeBlockRegion);
				else
					this.setRegion(ResourceManager.m_sInstance.orangeBlockFixedRegion);
			}
			break;
		}
	}
	
	public void SetGridPos(Point<Integer> p)
	{
		SetGridPos(p.x, p.y);
	}
	
	public void SetGridPos(int x, int y)
	{
		m_GridPosX = x;
		m_GridPosY = y;
	}
	
	public Point<Integer> GetGridPos()
	{
		return new Point<Integer>(m_GridPosX, m_GridPosY);
	}
	
	public BlockType GetType()
	{
		return m_Type;
	}
	
	public void SetFixed(boolean fixed)
	{
		m_IsFixed = fixed;
		
		ApplyTexture();
	}
	
	public boolean IsFixed()
	{
		return m_IsFixed;
	}
	
	public void SetType(BlockType type)
	{
		m_Type = type;
		ApplyTexture();
	}
}
