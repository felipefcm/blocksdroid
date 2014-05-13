
package blocks.game;

import android.graphics.Point;

public class BlockMatrix
{
	private Block[][] m_Blocks;
	
	public BlockMatrix(int rows, int cols)
	{
		m_Blocks = new Block[rows][cols];
	}
	
	public Block GetAt(Point pos)
	{
		return m_Blocks[pos.y][pos.x];
	}
	
	public Block GetAt(int row, int col)
	{
		return m_Blocks[row][col];
	}
	
	public void SetBlockAt(Block block, Point pos)
	{
		m_Blocks[pos.y][pos.x] = block;
	}
	
	public void ClearPosition(Point pos)
	{
		Block block = m_Blocks[pos.y][pos.x];
		m_Blocks[pos.y][pos.x] = null;
		
		/*
		if(block != null)
			BlockFactory.PoolBlock(block);
		*/
	}
	
	public final Block[][] GetMatrix()
	{
		return m_Blocks;
	}
}
