
package blocks.game;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;

import android.graphics.Point;
import blocks.resource.ResourceManager;

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
	
	private BlockType m_Type;
	
	private int m_GridPosX;
	private int m_GridPosY;
	
	public Block(BlockType type, ITextureRegion region)
	{
		super(0, 0, m_sBlockViewSize, m_sBlockViewSize, region, ResourceManager.m_sInstance.m_VBOManager);
		
		m_Type = type;
		
		m_GridPosX = m_GridPosY = 0;
		m_IsPlaced = false;
	}
	
	public void SetGridPos(Point p)
	{
		SetGridPos(p.x, p.y);
	}
	
	public void SetGridPos(int x, int y)
	{
		m_GridPosX = x;
		m_GridPosY = y;
	}
	
	public Point GetGridPos()
	{
		return new Point(m_GridPosX, m_GridPosY);
	}
	
	public BlockType GetType()
	{
		return m_Type;
	}
}
