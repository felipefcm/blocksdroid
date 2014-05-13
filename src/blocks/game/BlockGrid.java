
package blocks.game;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.debug.Debug;

import android.graphics.Point;
import android.graphics.PointF;
import blocks.game.Block.BlockType;
import blocks.resource.BlockFactory;
import blocks.resource.ResourceManager;
import blocks.scene.SceneManager;

public class BlockGrid extends Entity implements ITimerCallback
{
//Member variables ------------------------------------------------------
	private int m_NumRows;
	private int m_NumCols;
	private BlockMatrix m_Matrix;
	
	private Block m_FallingPiece;
	
	private int m_EmptyPositions;
	
	private BlockSpriteBatch m_BlocksBatch;
	
	private boolean m_TouchMoving;
	private PointF m_TouchMoveStartedPoint;
	private PointF m_TouchMoveFinishedPoint;
	
	private final float m_MinTouchLength = Block.m_sBlockViewSize * 0.7f;
	private final float m_MaxTouchLength = Block.m_sBlockViewSize * 1.6f;
	
	public enum MoveDirection
	{
		Up, Down,
		Right, Left
	}
//-----------------------------------------------------------------------

	public BlockGrid(int numRows, int numCols)
	{
		m_NumRows = numRows;
		m_NumCols = numCols;
		
		m_Matrix = new BlockMatrix(numRows, numCols);
		
		m_EmptyPositions = m_NumRows * m_NumCols;
		
		m_BlocksBatch = new BlockSpriteBatch
				(
					ResourceManager.m_sInstance.m_BlockTexture,
					m_NumRows * m_NumCols,
					m_Matrix.GetMatrix()
				);
		
		m_TouchMoving = false;
	}
	
	public void Init()
	{
		this.attachChild(m_BlocksBatch);
		
		SceneManager.m_sInstance.GetPlayScene().registerUpdateHandler(new TimerHandler(0.25f, true, this));
		SceneManager.m_sInstance.GetPlayScene().registerTouchArea(this);
		
		CreateNewFallingPiece();
	}
	
//Grid management ------------------------------------------------------------------------
	private boolean IsGridPositionAvailable(Point gridPosition)
	{
		return (m_Matrix.GetAt(gridPosition) == null);
	}
	
	private boolean InsertBlockInGrid(Block block)
	{
		return InsertBlockInGrid(block, true);
	}
	
	private boolean InsertBlockInGrid(Block block, boolean updateBatch)
	{
		Point blockGridPos = block.GetGridPos();
		
		if(!IsGridPositionAvailable(blockGridPos))
			return false;
		
		PointF pos = FromGridToWorld(blockGridPos);
		
		block.setPosition(pos.x, pos.y);
		
		m_Matrix.SetBlockAt(block, blockGridPos);
		
		if(updateBatch)
			m_BlocksBatch.UpdateBatch();
		
		--m_EmptyPositions;
		
		return true;
	}

	private void RemoveBlockFromGrid(Point gridPos, boolean updateBatch)
	{
		if(m_Matrix.GetAt(gridPos) == null)
			return;
		
		m_Matrix.ClearPosition(gridPos);
		
		if(updateBatch)
			m_BlocksBatch.UpdateBatch();
		
		++m_EmptyPositions;
	}
	
	private boolean MoveBlockInGrid(Block block, Point dstGridPos, boolean updateBatch)
	{
		if(!IsGridPositionAvailable(dstGridPos))
			return false;
		
		Point srcGridPos = block.GetGridPos();
		
		if(m_Matrix.GetAt(srcGridPos) == null)
			return false;
		
		PointF worldPos = FromGridToWorld(dstGridPos);
		
		m_Matrix.ClearPosition(srcGridPos);
		
		block.SetGridPos(dstGridPos);
		block.setPosition(worldPos.x, worldPos.y);
		
		m_Matrix.SetBlockAt(block, dstGridPos);
		
		if(updateBatch)
			m_BlocksBatch.UpdateBatch();
		
		return true;
	}
	
	private boolean SwapBlocksInGrid(Block blockA, Block blockB, boolean updateBatch)
	{
		Point aGridPos = blockA.GetGridPos();
		Point bGridPos = blockB.GetGridPos();
		PointF aWorldPos = FromGridToWorld(aGridPos);
		PointF bWorldPos = FromGridToWorld(bGridPos);
		
		m_Matrix.SetBlockAt(blockB, aGridPos);
		m_Matrix.SetBlockAt(blockA, bGridPos);
		
		blockA.SetGridPos(bGridPos);
		blockB.SetGridPos(aGridPos);
		
		blockA.setPosition(bWorldPos.x, bWorldPos.y);
		blockB.setPosition(aWorldPos.x, aWorldPos.y);
		
		if(updateBatch)
			m_BlocksBatch.UpdateBatch();
		
		return true;
	}
	
	public void RandomizeGrid()
	{
		//for testing only
		
		for(int i = 0; i < m_NumRows; ++i)
		{
			for(int j = 0; j < m_NumCols; ++j)
			{
				Block block = BlockFactory.GetRandomBlock();
				
				block.SetGridPos(j, i);
				
				InsertBlockInGrid(block);
			}
		}
		
		m_BlocksBatch.UpdateBatch();
	}
	
	private PointF FromGridToWorld(Point gridPosition)
	{
		return new PointF
				(
					((float) gridPosition.x + 0.5f) * Block.m_sBlockViewSize,
					((float) gridPosition.y + 0.5f) * Block.m_sBlockViewSize
				);
	}
	
	private Point FromWorldToGrid(PointF worldPosition)
	{
		return new Point
				(
					(int)(worldPosition.x / Block.m_sBlockViewSize),
					(int)(worldPosition.y / Block.m_sBlockViewSize)
				);
	}
	
	private void MoveDownPlacedBlocks()
	{
		for(int i = 1; i < m_NumRows; ++i) //we can skip first row
			for(int j = 0; j < m_NumCols; ++j)
			{
				Block block = m_Matrix.GetAt(i, j); 
				
				if(block == null || block == m_FallingPiece)
					continue;
				
				//last occupied position
				int k;
				
				for(k = i - 1; k >= 0; --k)
				{
					if(m_Matrix.GetAt(k, j) == null)
						continue;
					else
						break;
				}
				
				MoveBlockInGrid(block, new Point(j, k + 1), false);
			}
		
		m_BlocksBatch.UpdateBatch();
	}
//----------------------------------------------------------------------------------------
	
//Falling piece --------------------------------------------------------------------------
	private void CreateNewFallingPiece()
	{
		Debug.d("Creating new falling piece");
		
		if(m_EmptyPositions == 0)
			return;
		
		Point newPos = new Point();
		
		newPos.y = m_NumRows - 1;
		
		do
		{
			//pick a random column to spawn falling block
			newPos.x = ResourceManager.m_sInstance.m_Random.nextInt(m_NumCols);
		}
		while(!IsGridPositionAvailable(newPos));
		
		m_FallingPiece = BlockFactory.GetRandomBlock();
		m_FallingPiece.SetGridPos(newPos);
		
		InsertBlockInGrid(m_FallingPiece);
	}
	
	private void MoveDownFallingPiece(Point nextGridPos, boolean updateBatch)
	{
		Debug.d("Moving down falling piece");
		
		//'nextGridPos' is already checked by 'onTimePassed' function
		MoveBlockInGrid(m_FallingPiece, nextGridPos, updateBatch);
	}	
//----------------------------------------------------------------------------------------
	
//Piece elimination ----------------------------------------------------------------------
	private void ProcessScoringConditions()
	{
		boolean destructionFinished = false;
		
		while(!destructionFinished)
		{
			boolean hadColumnDestruction = false;
			boolean hadRowDestruction = false;
			
			for(int col = 0; col < m_NumCols; ++col)
			{
				int colEliminationPos = CheckColumnElimination(col);
				
				if(colEliminationPos != -1)
				{
					DestroyColumnGroup(colEliminationPos, col);
					MoveDownPlacedBlocks(); //TODO check if can move down only that column
					
					hadColumnDestruction = true;
					break;
				}
			}
			
			for(int row = 0; row < m_NumRows; ++row)
			{
				int rowEliminationPos = CheckRowElimination(row);
				
				if(rowEliminationPos != -1)
				{
					DestroyRowGroup(rowEliminationPos, row);
					MoveDownPlacedBlocks(); //TODO check if can move down only three columns
					
					hadRowDestruction = true;
					break;
				}
			}
			
			destructionFinished = !hadColumnDestruction && !hadRowDestruction;
		}
	}

	private int CheckColumnElimination(int col)
	{
		BlockType currentType = null;
		int currentTypeCount = 0;
		
		for(int i = 0; i < m_NumRows; ++i)
		{
			Block block = m_Matrix.GetAt(i, col); 
			
			if(block == null || block == m_FallingPiece)
			{
				return -1;
			}
			
			if(currentType == null)
				currentType = block.GetType();
			
			if(currentType == block.GetType())
			{
				++currentTypeCount;
			}
			else
			{
				currentType = block.GetType();
				currentTypeCount = 1;
			}
			
			if(currentTypeCount == 3)
			{
				//return the last block (the top most one)
				return i;
			}
		}
		
		return -1;
	}
	
	private int CheckRowElimination(int row)
	{
		BlockType currentType = null;
		int currentTypeCount = 0;
		
		for(int i = 0; i < m_NumCols; ++i)
		{
			Block block = m_Matrix.GetAt(row, i); 
			
			if(block == null || block == m_FallingPiece)
			{
				currentType = null;
				currentTypeCount = 0;
				continue;
			}
			
			if(currentType == null)
				currentType = block.GetType();
			
			if(currentType == block.GetType())
			{
				++currentTypeCount;
			}
			else
			{
				currentType = block.GetType();
				currentTypeCount = 1;
			}
			
			if(currentTypeCount == 3)
			{
				//return the last block (the right most one)
				return i;
			}
		}
		
		return -1;
	}
	
	private void DestroyColumnGroup(int topMost, int col)
	{
		Debug.d("DestroyCol: " + col + " topMost: " + topMost);
		
		RemoveBlockFromGrid(new Point(col, topMost), false);
		RemoveBlockFromGrid(new Point(col, topMost - 1), false);
		RemoveBlockFromGrid(new Point(col, topMost - 2), false);
	}
	
	private void DestroyRowGroup(int rightMost, int row)
	{
		Debug.d("DestroyRow: " + row + " rightMost: " + rightMost);
		
		RemoveBlockFromGrid(new Point(rightMost, row), false);
		RemoveBlockFromGrid(new Point(rightMost - 1, row), false);
		RemoveBlockFromGrid(new Point(rightMost - 2, row), false);
	}
//----------------------------------------------------------------------------------------
	
//Events ---------------------------------------------------------------------------------
	@Override
	public boolean onAreaTouched(TouchEvent touchEvent, float localX, float localY) 
	{		
		switch(touchEvent.getAction())
		{
			case TouchEvent.ACTION_DOWN:
			{		
				m_TouchMoveStartedPoint = new PointF(localX, localY);
				
				//this condition occurs when the touch move went out of grid
				if(m_TouchMoving)
					m_TouchMoving = false;
			}
			break;
			
			case TouchEvent.ACTION_UP:
			{
				if(m_TouchMoving)
				{
					m_TouchMoveFinishedPoint = new PointF(localX, localY);
					OnTouchMoveFinished();
				}
				
				m_TouchMoving = false;
			}
			break;
				
			case TouchEvent.ACTION_MOVE:
			{
				m_TouchMoving = true;
			}
			break;
		}
		
		return true;
	}
	
	private void OnTouchMoveFinished()
	{
		float deltaX = m_TouchMoveFinishedPoint.x - m_TouchMoveStartedPoint.x;
		float deltaY = m_TouchMoveFinishedPoint.y - m_TouchMoveStartedPoint.y;
		
		float length = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
		
		if(length >= m_MaxTouchLength)
		{
			Debug.d("Ignoring move because it has passed maximum length");
			return;
		}
		
		if(Math.abs(deltaX) >= m_MinTouchLength && Math.abs(deltaY) >= m_MinTouchLength)
		{
			Debug.d("Ignoring move because it is ambiguous");
			return;
		}
		
		Point srcGridPos = FromWorldToGrid(m_TouchMoveStartedPoint);
		
		Block srcBlock = m_Matrix.GetAt(srcGridPos);
		Block dstBlock = null;
		
		if(srcBlock == null)
		{
			Debug.d("Ignoring move because source block is null");
			return;
		}
		
		if(!srcBlock.m_IsPlaced)
		{
			Debug.d("Ignoring move because source block is not placed (falling piece)");
			return;
		}
		
		if(Math.abs(deltaX) >= m_MinTouchLength)
		{
			if(deltaX > 0)
			{
				Debug.d("Moved right");
				
				if(srcGridPos.x < m_NumCols)
					dstBlock = m_Matrix.GetAt(srcGridPos.y, srcGridPos.x + 1);
			}
			else
			{
				Debug.d("Moved left");
				
				if(srcGridPos.x > 0)
					dstBlock = m_Matrix.GetAt(srcGridPos.y, srcGridPos.x - 1);
			}
		}
		else
		{
			if(deltaY > 0)
			{
				Debug.d("Moved up");
				
				if(srcGridPos.y < m_NumRows)
					dstBlock = m_Matrix.GetAt(srcGridPos.y + 1, srcGridPos.x);
			}
			else
			{
				Debug.d("Moved down");
				
				if(srcGridPos.y > 0)
					dstBlock = m_Matrix.GetAt(srcGridPos.y - 1, srcGridPos.x);
			}
		}
		
		if(dstBlock == m_FallingPiece)
		{
			Debug.d("Ignoring block switch because destination piece is the falling piece");
			return;
		}
		
		if(dstBlock != null)
		{			
			SwapBlocksInGrid(srcBlock, dstBlock, true);
			
			ProcessScoringConditions();
			
			m_BlocksBatch.UpdateBatch();
		}
	}
	
	@Override
	protected final void onManagedUpdate(float pSecondsElapsed) 
	{
		super.onManagedUpdate(pSecondsElapsed);
	}
	
	@Override
	public void onTimePassed(TimerHandler pTimerHandler)
	{
		if(m_FallingPiece == null)
		{
			Debug.d("Falling piece null in 'onTimePassed', unregistering timer");
			SceneManager.m_sInstance.GetCurrentScene().unregisterUpdateHandler(pTimerHandler);
			return;
		}
		
		Point blockPos = m_FallingPiece.GetGridPos();
		Point nextPos = new Point(blockPos.x, blockPos.y - 1);
		
		//Check if falling piece can fall another position
		if(nextPos.y < 0 || !IsGridPositionAvailable(nextPos))
		{
			//Falling piece cannot fall anymore, place it
			m_FallingPiece.m_IsPlaced = true;
			m_FallingPiece = null;
			
			ProcessScoringConditions();
			
			CreateNewFallingPiece();
		}
		else
		{
			//it can
			MoveDownFallingPiece(nextPos, true);
		}
	}
//----------------------------------------------------------------------------------------
}
