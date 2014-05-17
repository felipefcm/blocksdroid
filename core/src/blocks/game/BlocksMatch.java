
package blocks.game;

public class BlocksMatch
{
	public static final int NumRows = 6;
	public static final int NumCols = 5;

	private BlockGrid m_BlockGrid;
	
	public BlocksMatch()
	{
		m_BlockGrid = new BlockGrid(NumRows, NumCols);
	}
	
	public void Render()
	{
		m_BlockGrid.Render();
	}
	
	public void Init()
	{	
		//m_BlockGrid.setWidth(NumCols * Block.m_sBlockViewSize);
		//m_BlockGrid.setHeight(NumRows * Block.m_sBlockViewSize);
		
		/*
		m_BlockGrid.setPosition
			(
				ResourceManager.m_sInstance.m_ViewCenterX, 
				ResourceManager.m_sInstance.m_ViewCenterY - Block.m_sBlockViewSize * 0.92f
			);
		*/
		
		m_BlockGrid.Init();
	}
	
	public void Dispose()
	{
		m_BlockGrid.Dispose();
	}
}
