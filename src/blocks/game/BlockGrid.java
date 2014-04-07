
package blocks.game;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.debug.Debug;

import android.graphics.Point;
import android.graphics.PointF;
import blocks.resource.BlockFactory;
import blocks.resource.ResourceManager;
import blocks.scene.SceneManager;

public class BlockGrid extends Entity implements ITimerCallback
{
	//Member variables ------------------------------------------------------
	private int m_NumRows;
	private int m_NumCols;
	private Block[][] m_Blocks;
	private Block m_FallingPiece;
	
	private int m_EmptyPositions;
	
	private BlockSpriteBatch m_BlocksBatch;
	
	private boolean m_TouchMoving;
	private PointF m_TouchMoveStartedPoint;
	private PointF m_TouchMoveFinishedPoint;
	//-----------------------------------------------------------------------
	
	public BlockGrid(int numRows, int numCols)
	{
		m_NumRows = numRows;
		m_NumCols = numCols;
		
		m_Blocks = new Block[m_NumRows][m_NumCols];
		
		m_EmptyPositions = m_NumRows * m_NumCols;
		
		m_BlocksBatch = new BlockSpriteBatch
				(
					ResourceManager.m_sInstance.m_BlockTexture,
					m_NumRows * m_NumCols,
					m_Blocks
				);
		
		m_TouchMoving = false;
	}
	
	public void Init()
	{
		this.attachChild(m_BlocksBatch);
		
		SceneManager.m_sInstance.GetPlayScene().registerUpdateHandler(this);
		SceneManager.m_sInstance.GetPlayScene().registerUpdateHandler(new TimerHandler(0.8f, true, this));
		SceneManager.m_sInstance.GetPlayScene().registerTouchArea(this);
		
		CreateNewFallingPiece();
	}
	
	//Grid management ------------------------------------------------------------------------
	private boolean InsertBlockInGrid(Block block)
	{
		Point blockGridPos = block.GetGridPos();
		
		if(!IsGridPositionAvailable(blockGridPos))
			return false;
		
		m_Blocks[blockGridPos.y][blockGridPos.x] = block;
		
		PointF pos = FromGridToWorld(blockGridPos);
		
		block.setPosition(pos.x, pos.y);
		
		m_BlocksBatch.UpdateBatch();
		
		--m_EmptyPositions;
		
		return true;
	}
	
	private void RemoveBlockFromGrid(Block block)
	{
		Point blockPos = block.GetGridPos();
		
		m_Blocks[blockPos.y][blockPos.x] = null;
		
		block.dispose();
		
		m_BlocksBatch.UpdateBatch();
		
		++m_EmptyPositions;
	}
	
	private boolean IsGridPositionAvailable(Point gridPosition)
	{
		return (m_Blocks[gridPosition.y][gridPosition.x] == null);
	}
	
	//for debugging
	public void RandomizeGrid()
	{
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
	//----------------------------------------------------------------------------------------
	
	//Falling piece --------------------------------------------------------------------------
	private void CreateNewFallingPiece()
	{
		if(m_EmptyPositions == 0)
			return;
		
		m_FallingPiece = BlockFactory.GetRandomBlock();
		
		do
		{
			//pick a random column to spawn falling block
			int col = ResourceManager.m_sInstance.m_Random.nextInt(m_NumCols);
			
			m_FallingPiece.SetGridPos(col, m_NumRows - 1);
		}
		while(!InsertBlockInGrid(m_FallingPiece));
	}
	
	private void MoveDownFallingPiece(Point nextGridPos)
	{
		//the position is already checked by 'onTimePassed'
		Point currentGridPos = m_FallingPiece.GetGridPos(); 
		
		m_Blocks[currentGridPos.y][currentGridPos.x] = null;
		m_Blocks[nextGridPos.y][nextGridPos.x] = m_FallingPiece;
		
		m_FallingPiece.SetGridPos(nextGridPos);
		
		PointF pos = FromGridToWorld(nextGridPos);
		m_FallingPiece.setPosition(pos.x, pos.y);
		
		m_BlocksBatch.UpdateBatch();
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
		
		if(length < Block.m_sBlockViewSize * 0.7f)
		{
			Debug.d("Ignoring move because of length: " + length);
			return;
		}
		
		Point srcGridPos = FromWorldToGrid(m_TouchMoveStartedPoint);
		Point dstGridPos = FromWorldToGrid(m_TouchMoveFinishedPoint);
		
		Block srcBlock = m_Blocks[srcGridPos.y][srcGridPos.x];
		Block dstBlock = m_Blocks[dstGridPos.y][dstGridPos.x];
		
		//if()
		
		if(srcBlock == null)
		{
			Debug.d("Ignoring move because source block is null");
			return;
		}
		
		if(!srcBlock.m_IsPlaced)
		{
			Debug.d("Ignoring move because sorce block is not placed");
			return;
		}
		
		if(dstBlock == null)
		{
			//moving block to empty position
			srcBlock.SetGridPos(dstGridPos);

			PointF newWorldPosition = FromGridToWorld(dstGridPos);
			srcBlock.setPosition(newWorldPosition.x, newWorldPosition.y);
			
			m_Blocks[dstGridPos.y][dstGridPos.x] = srcBlock;
			m_Blocks[srcGridPos.y][srcGridPos.x] = null;
			
			m_BlocksBatch.UpdateBatch();
		}
		else
		{
			//switch blocks
			srcBlock.SetGridPos(dstGridPos);
			dstBlock.SetGridPos(srcGridPos);
			
			PointF srcNewWorldPosition = FromGridToWorld(dstGridPos);
			PointF dstNewWorldPosition = FromGridToWorld(srcGridPos);
			srcBlock.setPosition(srcNewWorldPosition.x, srcNewWorldPosition.y);
			dstBlock.setPosition(dstNewWorldPosition.x, dstNewWorldPosition.y);
			
			m_Blocks[srcGridPos.y][srcGridPos.x] = dstBlock;
			m_Blocks[dstGridPos.y][dstGridPos.x] = srcBlock;
			
			m_BlocksBatch.UpdateBatch();
		}
		
		/*
		if(deltaX > 0)
		{
			Debug.d("Moved right");
		}
		else
		{
			Debug.d("Moved left");
		}
		*/
		
		Debug.d("OnTouchMoveFinished called: " + deltaX + ", " + deltaY);
	}
	
	@Override
	protected void onManagedUpdate(float pSecondsElapsed) 
	{
		super.onManagedUpdate(pSecondsElapsed);
	}
	
	@Override
	public void onTimePassed(TimerHandler pTimerHandler)
	{
		if(m_FallingPiece == null)
			return;
		
		Point blockPos = m_FallingPiece.GetGridPos();
		Point nextPos = new Point(blockPos.x, blockPos.y - 1);
		
		if(nextPos.y < 0 || !IsGridPositionAvailable(nextPos))
		{
			m_FallingPiece.m_IsPlaced = true;
			m_FallingPiece = null;
			
			CreateNewFallingPiece();
		}
		else
		{
			MoveDownFallingPiece(nextPos);
		}
	}
	//----------------------------------------------------------------------------------------
}
