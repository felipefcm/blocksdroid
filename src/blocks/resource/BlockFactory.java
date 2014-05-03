
package blocks.resource;

import org.andengine.util.debug.Debug;

import blocks.game.Block;
import blocks.game.Block.BlockType;

public class BlockFactory 
{
	public static Block GetBlock(BlockType type)
	{
		Block newBlock = null;
		
		switch(type)
		{
			case Red:
				newBlock = new Block(type, ResourceManager.m_sInstance.m_RedBlockRegion);
			break;
			
			case Green:
				newBlock = new Block(type, ResourceManager.m_sInstance.m_GreenBlockRegion);
			break;
			
			case Blue:
				newBlock = new Block(type, ResourceManager.m_sInstance.m_BlueBlockRegion);
			break;
			
			case Orange:
				newBlock = new Block(type, ResourceManager.m_sInstance.m_OrangeBlockRegion);
			break;
		}
		
		newBlock.setIgnoreUpdate(true);
		
		return newBlock;
	}
	
	public static Block GetRandomBlock()
	{
		int randomValue = (int)(ResourceManager.m_sInstance.m_Random.nextFloat() * 3.0f);
		
		Block newBlock = null;
		
		switch(randomValue)
		{
			case 0:
				newBlock = GetBlock(BlockType.Red);
			break;
			
			case 1:
				newBlock = GetBlock(BlockType.Green);
			break;
			
			case 2:
				newBlock = GetBlock(BlockType.Blue);
			break;
			
			case 3:
				newBlock = GetBlock(BlockType.Orange);
			break;
				
			default:
				Debug.e("GetRandomBlock received invalid block value: " + randomValue);
				return null;
		}
		
		newBlock.setIgnoreUpdate(true);
		
		return newBlock;
	}
}
