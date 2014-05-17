
package blocks.resource;

import blocks.game.Block;
import blocks.game.Block.BlockType;

public class BlockFactory 
{
	public static Block GetBlock(BlockType type)
	{	
		return new Block(type);
	}
	
	public static Block GetRandomBlock()
	{
		int randomValue = ResourceManager.m_sInstance.m_Random.nextInt(BlockType.values().length);
		
		return new Block(BlockType.values()[randomValue]);
	}
}
