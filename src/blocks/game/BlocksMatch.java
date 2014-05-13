
package blocks.game;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;

import blocks.resource.ResourceManager;
import blocks.scene.SceneManager;

public class BlocksMatch implements ITimerCallback
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
		m_BlockGrid.setWidth(NumCols * Block.m_sBlockViewSize);
		m_BlockGrid.setHeight(NumRows * Block.m_sBlockViewSize);
		
		m_BlockGrid.setPosition
			(
				ResourceManager.m_sInstance.m_ViewCenterX, 
				ResourceManager.m_sInstance.m_ViewCenterY - Block.m_sBlockViewSize * 0.92f
			);

		SceneManager.m_sInstance.GetPlayScene().m_FrontLayer.attachChild(m_BlockGrid);
		
		m_BlockGrid.Init();
	}

	@Override
	public void onTimePassed(TimerHandler pTimerHandler) 
	{
	
	}
}
