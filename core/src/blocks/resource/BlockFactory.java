
package blocks.resource;

import blocks.game.Block;
import blocks.game.Block.BlockType;

public class BlockFactory 
{
	public static Block GetBlock(BlockType type)
	{	
		Block newBlock = new Block(type);
		
		newBlock.setSize(Block.m_sBlockViewSize, Block.m_sBlockViewSize);
		
		return newBlock;
	}
	
	public static Block GetRandomBlock()
	{
		int randomValue = ResourceManager.m_sInstance.m_Random.nextInt(BlockType.values().length);
		
		return GetBlock(BlockType.values()[randomValue]);
	}
}
