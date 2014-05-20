
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
		
	public void Init()
	{	
		m_BlockGrid.Init();
	}
	
	public void Render()
	{
		m_BlockGrid.Render();
	}
	
	public void Dispose()
	{
		m_BlockGrid.Dispose();
	}
}
