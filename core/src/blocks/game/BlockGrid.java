
package blocks.game;

import blocks.game.Block.BlockType;
import blocks.resource.BlockFactory;
import blocks.resource.Log;
import blocks.resource.Point;
import blocks.resource.ResourceManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class BlockGrid extends InputAdapter
{
//Member variables -------------------------------------------------------
	private int m_NumRows;
	private int m_NumCols;
	private BlockMatrix m_Matrix;
	
	private Matrix4 m_FromGridToWorld;
	private Matrix4 m_FromWorldToGrid;
	
	private SpriteBatch m_SpriteBatch;
	private ShapeRenderer m_ShapeRenderer;
	private Point<Integer> m_ViewSize;
	
	private Block m_FallingPiece;
	
	private int m_EmptyPositions;
	
	private float m_UpdateInterval;
	private float m_UpdateTime;
	
	private Point<Integer> m_TouchMoveStartedPoint;
	private Point<Integer> m_TouchMoveFinishedPoint;
	private final float m_MinTouchLength = Block.m_sBlockViewSize * 0.7f;
	private final float m_MaxTouchLength = Block.m_sBlockViewSize * 1.6f;
	
	public enum MoveDirection
	{
		Up, Down,
		Right, Left
	}
//------------------------------------------------------------------------

	public BlockGrid(int numRows, int numCols)
	{
		m_NumRows = numRows;
		m_NumCols = numCols;
		
		m_Matrix = new BlockMatrix(numRows, numCols);
		
		m_FromGridToWorld = new Matrix4();
		
		m_EmptyPositions = m_NumRows * m_NumCols;
		
		//time in seconds between updates
		m_UpdateInterval = 0.3f;

		m_UpdateTime = 0;
		
		m_TouchMoveStartedPoint = new Point<Integer>();
		m_TouchMoveFinishedPoint = new Point<Integer>();
	}
	
	public void Init()
	{
		m_SpriteBatch = ResourceManager.m_sInstance.m_SpriteBatch;
		m_ShapeRenderer = ResourceManager.m_sInstance.m_ShapeRenderer;
		m_ViewSize = ResourceManager.m_sInstance.m_ViewSize;
		
		m_FromGridToWorld.trn(m_ViewSize.x * 0.125f, m_ViewSize.y * 0.022f, 0);
		m_FromWorldToGrid = m_FromGridToWorld.cpy().inv();
		
		Gdx.input.setInputProcessor(this);
		
		CreateNewFallingPiece();
	}
	
	public void Render()
	{
		m_UpdateTime += Gdx.graphics.getDeltaTime();
		
		if(m_UpdateTime >= m_UpdateInterval)
		{
			m_UpdateTime = 0;
			UpdateGame();
		}
		
		m_ShapeRenderer.begin(ShapeType.Line);
		{
			m_ShapeRenderer.setColor(0.75f, 0.75f, 0.75f, 1.0f);
			m_ShapeRenderer.rect
			(
				m_ViewSize.x * 0.12f,
				m_ViewSize.y * 0.018f,
				m_ViewSize.x * 0.76f,
				m_ViewSize.y * 0.685f
			);
		}
		m_ShapeRenderer.end();
		
		m_SpriteBatch.setTransformMatrix(m_FromGridToWorld);
		m_SpriteBatch.setProjectionMatrix(ResourceManager.m_sInstance.m_Camera.combined);
		
		m_Matrix.Render(m_SpriteBatch);
	}
	
	public void Dispose()
	{
		m_SpriteBatch.dispose();
	}
	
//Grid management --------------------------------------------------------
	private boolean IsGridPositionAvailable(Point<Integer> gridPosition)
	{
		return (m_Matrix.GetAt(gridPosition) == null);
	}
	
	private boolean InsertBlockInGrid(Block block)
	{
		Point<Integer> blockGridPos = block.GetGridPos();
		
		if(!IsGridPositionAvailable(blockGridPos))
			return false;
				
		block.setPosition(blockGridPos.x * Block.m_sBlockViewSize, blockGridPos.y * Block.m_sBlockViewSize);
		
		m_Matrix.SetBlockAt(block, blockGridPos);
		
		--m_EmptyPositions;
		
		return true;
	}

	private void RemoveBlockFromGrid(Point<Integer> gridPos)
	{
		if(m_Matrix.GetAt(gridPos) == null)
			return;
		
		m_Matrix.ClearPosition(gridPos);
		
		++m_EmptyPositions;
	}
	
	private boolean MoveBlockInGrid(Block block, Point<Integer> dstGridPos)
	{
		if(!IsGridPositionAvailable(dstGridPos))
			return false;
		
		Point<Integer> srcGridPos = block.GetGridPos();
		
		if(m_Matrix.GetAt(srcGridPos) == null)
			return false;
		
		m_Matrix.ClearPosition(srcGridPos);
		
		block.SetGridPos(dstGridPos);
		block.setPosition(dstGridPos.x * Block.m_sBlockViewSize, dstGridPos.y * Block.m_sBlockViewSize);
		
		m_Matrix.SetBlockAt(block, dstGridPos);
		
		return true;
	}
	
	private boolean SwapBlocksInGrid(Block blockA, Block blockB)
	{
		Point<Integer> aGridPos = blockA.GetGridPos();
		Point<Integer> bGridPos = blockB.GetGridPos();
		
		m_Matrix.SetBlockAt(blockB, aGridPos);
		m_Matrix.SetBlockAt(blockA, bGridPos);
		
		blockA.SetGridPos(bGridPos);
		blockB.SetGridPos(aGridPos);
		
		blockA.setPosition(bGridPos.x * Block.m_sBlockViewSize, bGridPos.y * Block.m_sBlockViewSize);
		blockB.setPosition(aGridPos.x * Block.m_sBlockViewSize, aGridPos.y * Block.m_sBlockViewSize);
		
		return true;
	}
	
	public void RandomizeGrid()
	{
		//for testing only
		
		for(int i = 0; i < m_NumRows; ++i)
		{
			for(int j = 0; j < m_NumCols; ++j)
			{
				Block block = BlockFactory.GetRandomBlock(BlockFactory.ENHANCED_BLOCKS);
				
				block.SetGridPos(j, i);
				
				InsertBlockInGrid(block);
			}
		}
	}
	
	private Vector3 FromGridToWorld(Point<Integer> gridPosition)
	{
		Vector3 blockPosInGridCoords = new Vector3
				(
					gridPosition.x * Block.m_sBlockViewSize,
					gridPosition.y * Block.m_sBlockViewSize,
					0
				);
		
		return blockPosInGridCoords.mul(m_FromGridToWorld);
	}
	
	private Point<Integer> FromWorldToGrid(Vector3 worldPosition)
	{
		Vector3 blockPosInGridCoords = worldPosition.mul(m_FromWorldToGrid);
		
		return new Point<Integer>
				(
					(int)(blockPosInGridCoords.x / Block.m_sBlockViewSize),
					(int)(blockPosInGridCoords.y / Block.m_sBlockViewSize)
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
				
				MoveBlockInGrid(block, new Point<Integer>(j, k + 1));
			}
	}
//------------------------------------------------------------------------
	
//Falling piece ----------------------------------------------------------
	private void CreateNewFallingPiece()
	{
		if(m_EmptyPositions == 0)
			return;
		
		Point<Integer> newPos = new Point<Integer>();
		
		newPos.y = m_NumRows - 1;
		
		do
		{
			//pick a random column to spawn falling block
			newPos.x = ResourceManager.m_sInstance.m_Random.nextInt(m_NumCols);
		}
		while(!IsGridPositionAvailable(newPos));
		
		m_FallingPiece = BlockFactory.GetRandomBlock(BlockFactory.INITIAL_BLOCKS);
		m_FallingPiece.SetGridPos(newPos);
		
		InsertBlockInGrid(m_FallingPiece);
	}
	
	private void MoveDownFallingPiece(Point<Integer> nextGridPos)
	{
		//'nextGridPos' is already checked by 'onTimePassed' function
		MoveBlockInGrid(m_FallingPiece, nextGridPos);
	}	
//------------------------------------------------------------------------
	
//Piece elimination ------------------------------------------------------
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
		RemoveBlockFromGrid(new Point(col, topMost));
		RemoveBlockFromGrid(new Point(col, topMost - 1));
		RemoveBlockFromGrid(new Point(col, topMost - 2));
	}
	
	private void DestroyRowGroup(int rightMost, int row)
	{		
		RemoveBlockFromGrid(new Point(rightMost, row));
		RemoveBlockFromGrid(new Point(rightMost - 1, row));
		RemoveBlockFromGrid(new Point(rightMost - 2, row));
	}
//------------------------------------------------------------------------
	
//Events -----------------------------------------------------------------
	public void UpdateGame() 
	{	
		if(m_FallingPiece == null)
		{
			return;
		}
		
		Point<Integer> blockPos = m_FallingPiece.GetGridPos();
		Point<Integer> nextPos = new Point<Integer>(blockPos.x, blockPos.y - 1);
		
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
			MoveDownFallingPiece(nextPos);
		}
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{
		m_TouchMoveStartedPoint.x = screenX;
		m_TouchMoveStartedPoint.y = screenY;
		
		return true;
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		m_TouchMoveFinishedPoint.x = screenX;
		m_TouchMoveFinishedPoint.y = screenY;
		
		OnTouchMoveFinished();

		return true;
	}
		
	private void OnTouchMoveFinished()
	{
		float deltaX = m_TouchMoveFinishedPoint.x - m_TouchMoveStartedPoint.x;
		float deltaY = -(m_TouchMoveFinishedPoint.y - m_TouchMoveStartedPoint.y);
		
		float length = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
		
		if(length >= m_MaxTouchLength)
		{
			Log.Write("Ignoring move because it has passed maximum length");
			return;
		}
		
		if(Math.abs(deltaX) >= m_MinTouchLength && Math.abs(deltaY) >= m_MinTouchLength)
		{
			Log.Write("Ignoring move because it is ambiguous");
			return;
		}
		
		Vector3 srcWorldPos = ResourceManager.m_sInstance.m_Viewport.unproject(new Vector3(m_TouchMoveStartedPoint.x, m_TouchMoveStartedPoint.y, 0));
		
		Point<Integer> srcGridPos = FromWorldToGrid(srcWorldPos);
		
		Block srcBlock = m_Matrix.GetAt(srcGridPos);
		Block dstBlock = null;
		
		if(srcBlock == null)
		{
			Log.Write("Ignoring move because source block is null");
			return;
		}
		
		if(!srcBlock.m_IsPlaced)
		{
			Log.Write("Ignoring move because source block is not placed (falling piece)");
			return;
		}
		
		if(Math.abs(deltaX) >= m_MinTouchLength)
		{
			if(deltaX > 0)
			{
				Log.Write("Moved right");
				
				if(srcGridPos.x < m_NumCols)
					dstBlock = m_Matrix.GetAt(srcGridPos.y, srcGridPos.x + 1);
			}
			else
			{
				Log.Write("Moved left");
				
				if(srcGridPos.x > 0)
					dstBlock = m_Matrix.GetAt(srcGridPos.y, srcGridPos.x - 1);
			}
		}
		else
		{
			if(deltaY > 0)
			{
				Log.Write("Moved up");
				
				if(srcGridPos.y < m_NumRows)
					dstBlock = m_Matrix.GetAt(srcGridPos.y + 1, srcGridPos.x);
			}
			else
			{
				Log.Write("Moved down");
				
				if(srcGridPos.y > 0)
					dstBlock = m_Matrix.GetAt(srcGridPos.y - 1, srcGridPos.x);
			}
		}
		
		if(dstBlock == m_FallingPiece)
		{
			Log.Write("Ignoring block switch because destination piece is the falling piece");
			return;
		}
		
		if(dstBlock != null)
		{			
			SwapBlocksInGrid(srcBlock, dstBlock);
			
			ProcessScoringConditions();
		}
	}
//------------------------------------------------------------------------
}
