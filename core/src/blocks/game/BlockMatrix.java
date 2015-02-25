
package blocks.game;

import blocks.resource.BlockFactory;
import blocks.resource.Point;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BlockMatrix
{
	private Block[][] m_Blocks;
	
	public BlockMatrix(int rows, int cols)
	{
		m_Blocks = new Block[rows][cols];
	}
	
	public void Render(SpriteBatch batch)
	{
		batch.begin();
		
		for(int i = 0; i < m_Blocks.length; ++i)
			for(int j = 0; j < m_Blocks[i].length; ++j)
				if(m_Blocks[i][j] != null)
					m_Blocks[i][j].draw(batch);
		
		batch.end();
	}
	
	public Block GetAt(Point<Integer> pos)
	{
		return GetAt(pos.y, pos.x);
	}
	
	public Block GetAt(int row, int col)
	{
		if(row >= m_Blocks.length || col >= m_Blocks[0].length)
			return null;
		
		return m_Blocks[row][col];
	}
	
	public void SetBlockAt(Block block, Point<Integer> pos)
	{
		m_Blocks[pos.y][pos.x] = block;
	}
	
	public void ClearPosition(Point<Integer> pos)
	{
		m_Blocks[pos.y][pos.x] = null;
	}
	
	public void ClearAll()
	{
		for(int i = 0; i < m_Blocks.length; ++i)
			for(int j = 0; j < m_Blocks[i].length; ++j)
				m_Blocks[i][j] = null;
	}
	
	public void DisposeBlock(Point<Integer> pos, boolean poolBlock)
	{
		Block block = m_Blocks[pos.y][pos.x];
		
		m_Blocks[pos.y][pos.x] = null;

		if(poolBlock)
		    BlockFactory.PoolBlock(block);
	}
}
