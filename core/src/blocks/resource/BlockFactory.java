
package blocks.resource;

import blocks.game.Block;
import blocks.game.Block.BlockType;

public class BlockFactory 
{
	public static final int INITIAL_BLOCKS = 3;
	public static final int ENHANCED_BLOCKS = 4;
	
	public static Block GetBlock(BlockType type)
	{	
		Block newBlock = new Block(type);
		
		newBlock.setSize(Block.m_sBlockViewSize, Block.m_sBlockViewSize);
		
		return newBlock;
	}
	
	public static Block GetRandomBlock(int maxBlockValue)
	{
		int randomValue = ResourceManager.m_sInstance.m_Random.nextInt(maxBlockValue);
		
		return GetBlock(BlockType.values()[randomValue]);
	}
}
