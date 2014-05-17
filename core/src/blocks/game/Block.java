
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
	public boolean m_IsFixed;
	
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
					this.setRegion(ResourceManager.m_sInstance.m_RedBlockRegion);
				else
					this.setRegion(ResourceManager.m_sInstance.m_RedBlockFixedRegion);
			}
			break;
			
			case Green:
			{
				if(!m_IsFixed)
					this.setRegion(ResourceManager.m_sInstance.m_GreenBlockRegion);
				else
					this.setRegion(ResourceManager.m_sInstance.m_GreenBlockFixedRegion);
			}
			break;
			
			case Blue:
			{
				if(!m_IsFixed)
					this.setRegion(ResourceManager.m_sInstance.m_BlueBlockRegion);
				else
					this.setRegion(ResourceManager.m_sInstance.m_BlueBlockFixedRegion);
			}
			break;
			
			case Orange:
			{
				if(!m_IsFixed)
					this.setRegion(ResourceManager.m_sInstance.m_OrangeBlockRegion);
				else
					this.setRegion(ResourceManager.m_sInstance.m_OrangeBlockFixedRegion);
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
	
	public void SetType(BlockType type)
	{
		m_Type = type;
		ApplyTexture();
	}
}