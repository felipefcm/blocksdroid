
package blocks.resource;

import blocks.game.Block;
import blocks.game.Block.BlockType;

import com.badlogic.gdx.utils.Array;

public class BlockFactory 
{
	public static final int INITIAL_BLOCKS = 3;
	public static final int ENHANCED_BLOCKS = 4;
	
	private static final int MAX_POOL_SIZE = 12;
	private static Array<Block> m_sBlockPool = new Array<Block>(false, MAX_POOL_SIZE);
	
	public static Block GetBlock(BlockType type)
	{	
		Block newBlock;
		
		if(m_sBlockPool.size > 0)
		{
			newBlock = m_sBlockPool.pop();
			newBlock.SetType(type);
		}
		else
		{
			newBlock = new Block(type);			
			newBlock.setSize(Block.m_sBlockViewSize, Block.m_sBlockViewSize);
		}		
		
		return newBlock;
	}
	
	public static Block GetRandomBlock(int maxBlockValue)
	{
		int randomValue = ResourceManager.m_sInstance.m_Random.nextInt(maxBlockValue);
		
		return GetBlock(BlockType.values()[randomValue]);
	}
	
	public static void PoolBlock(Block block)
	{
		if(m_sBlockPool.size < MAX_POOL_SIZE)
			m_sBlockPool.add(block);
	}
}
