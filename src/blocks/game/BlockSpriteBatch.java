
package blocks.game;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.batch.DynamicSpriteBatch;
import org.andengine.opengl.texture.ITexture;

import blocks.resource.ResourceManager;

public class BlockSpriteBatch extends DynamicSpriteBatch 
{
	private boolean m_IsBatchDirty;
	private final Block[][] m_BlockMatrix;
	
	public BlockSpriteBatch(ITexture texture, int capacity, final Block[][] blockMatrix)
	{
		super(texture, capacity, ResourceManager.m_sInstance.m_VBOManager);
		
		m_BlockMatrix = blockMatrix;
		m_IsBatchDirty = true;
	}

	public void UpdateBatch()
	{
		m_IsBatchDirty = true;
	}
	
	@Override
	protected boolean onUpdateSpriteBatch() 
	{
		if(m_IsBatchDirty)
		{
			for(int i = 0; i < m_BlockMatrix.length; ++i)
			{
				for(int j = 0; j < m_BlockMatrix[i].length; ++j)
				{					
					if(m_BlockMatrix[i][j] != null)
						InsertInBatch(m_BlockMatrix[i][j]);
				}
			}
			
			//Debug.d("BlockSpriteBatch updated");
			
			m_IsBatchDirty = false;
			return true;
		}
		
		//Debug.d("BlockSpriteBatch unchanged");
		return false;
	}
	
	private void InsertInBatch(final Sprite sprite)
	{
		this.drawWithoutChecks
			(
				sprite.getTextureRegion(),
				sprite.getX() - sprite.getWidth() * 0.5f,
				sprite.getY() - sprite.getHeight() * 0.5f,
				sprite.getWidth(),
				sprite.getHeight(),
				1.0f, 1.0f, 1.0f, 1.0f
			);
	}
}
