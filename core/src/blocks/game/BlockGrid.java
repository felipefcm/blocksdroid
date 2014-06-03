
package blocks.game;

import blocks.game.Block.BlockType;
import blocks.resource.BlockFactory;
import blocks.resource.Log;
import blocks.resource.Point;
import blocks.resource.ResourceManager;
import blocks.ui.SwapLine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class BlockGrid extends InputAdapter
{
//Member variables -------------------------------------------------------
	private int m_NumRows;
	private int m_NumCols;
	private BlockMatrix m_Matrix;
	
	private BlocksMatch m_Match;
	private boolean m_MatchEnded;
	
	private Matrix4 m_FromGridToWorld;
	private Matrix4 m_FromWorldToGrid;
	
	private SpriteBatch m_SpriteBatch;
	private ShapeRenderer m_ShapeRenderer;
	private Point<Integer> m_ViewSize;
	
	private Rectangle m_GridArea;
	
	private Block m_FallingPiece;
	
	private int m_EmptyPositions;
	
	private float m_UpdateTime;
	
	private SwapLine m_SwapLine;
	
	private Point<Integer> m_TouchMoveStartedPoint;
	private Point<Integer> m_TouchMoveFinishedPoint;
	private final float m_MinTouchLength = Block.m_sBlockViewSize * 0.5f;
	private final float m_MaxTouchLength = Block.m_sBlockViewSize * 1.7f;
	
	public enum MoveDirection
	{
		Up, Down,
		Right, Left
	}
//------------------------------------------------------------------------

	public BlockGrid(int numRows, int numCols, BlocksMatch match)
	{
		m_NumRows = numRows;
		m_NumCols = numCols;
		
		m_Match = match;
		
		m_Matrix = new BlockMatrix(numRows, numCols);
		
		m_SwapLine = new SwapLine();
		
		m_TouchMoveStartedPoint = new Point<Integer>();
		m_TouchMoveFinishedPoint = new Point<Integer>();
	}
	
	public void Init()
	{
		m_SpriteBatch = ResourceManager.m_sInstance.m_SpriteBatch;
		m_ShapeRenderer = ResourceManager.m_sInstance.m_ShapeRenderer;
		m_ViewSize = ResourceManager.m_sInstance.m_ViewSize;
		
		m_MatchEnded = false;
		
		m_Matrix.ClearAll();
		
		m_EmptyPositions = m_NumRows * m_NumCols;
		
		m_UpdateTime = 0;
		
		m_FromGridToWorld = new Matrix4();
		
		m_FromGridToWorld.trn(m_ViewSize.x * 0.125f, m_ViewSize.y * 0.022f, 0);
		m_FromWorldToGrid = m_FromGridToWorld.cpy().inv();
		
		m_GridArea = new Rectangle
		(
			m_ViewSize.x * 0.125f, 
			m_ViewSize.y * 0.022f, 
			m_NumCols * Block.m_sBlockViewSize, 
			m_NumRows * Block.m_sBlockViewSize
		);
		
		Gdx.input.setInputProcessor(this);
		
		CreateNewFallingPiece();
	}
	
	public void Render()
	{
		m_UpdateTime += Gdx.graphics.getDeltaTime();
		
		if(m_UpdateTime >= m_Match.m_GameSpeed)
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
		
		if(m_SwapLine.m_IsVisible)
			m_SwapLine.Render(m_ShapeRenderer);
	}
	
	public void Dispose()
	{
		m_Matrix.ClearAll();
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
		
		m_Matrix.DisposeBlock(gridPos);
		
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
				Block block = BlockFactory.GetRandomBlock(BlockFactory.INITIAL_BLOCKS);
				
				block.SetGridPos(j, i);
				
				InsertBlockInGrid(block);
			}
		}
	}
	
	private Vector3 FromGridToWorld(final Point<Integer> gridPosition)
	{
		Vector3 blockPosInGridCoords = new Vector3
				(
					gridPosition.x * Block.m_sBlockViewSize,
					gridPosition.y * Block.m_sBlockViewSize,
					0
				);
		
		return blockPosInGridCoords.cpy().mul(m_FromGridToWorld);
	}
	
	private Point<Integer> FromWorldToGrid(final Vector3 worldPosition)
	{
		Vector3 blockPosInGridCoords = worldPosition.cpy().mul(m_FromWorldToGrid);
		
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
	private class EliminationData
	{
		public EliminationData(int pos, int count)
		{
			this.pos = pos;
			this.count = count;
		}
		
		public int pos;
		public int count;
	}
	
	private void ProcessScoringConditions()
	{
		boolean destructionFinished = false;
		
		while(!destructionFinished)
		{
			boolean hadColumnDestruction = false;
			boolean hadRowDestruction = false;
			
			for(int col = 0; col < m_NumCols; ++col)
			{
				EliminationData colElimination = CheckColumnElimination(col);
				
				if(colElimination != null)
				{
					DestroyColumnGroup(colElimination.pos, col, colElimination.count);
					m_Match.IncrementScore(1);
					MoveDownPlacedBlocks(); //TODO check if can move down only that column
					
					hadColumnDestruction = true;
					break;
				}
			}
			
			for(int row = 0; row < m_NumRows; ++row)
			{
				EliminationData rowElimination = CheckRowElimination(row);
				
				if(rowElimination != null)
				{
					DestroyRowGroup(rowElimination.pos, row, rowElimination.count);
					m_Match.IncrementScore(1);
					MoveDownPlacedBlocks(); //TODO check if can move down only three columns
					
					hadRowDestruction = true;
					break;
				}
			}
			
			destructionFinished = !hadColumnDestruction && !hadRowDestruction;
		}
	}

	private EliminationData CheckColumnElimination(int col)
	{
		BlockType currentType = null;
		int currentTypeCount = 0;
		
		for(int i = 0; i < m_NumRows; ++i)
		{
			Block block = m_Matrix.GetAt(i, col); 
						
			if(block != null && block != m_FallingPiece && currentType == block.GetType())
			{
				++currentTypeCount;
			}
			else
			{
				if(currentTypeCount >= 3)
				{
					//return the last block (the top most one)
					return new EliminationData(i - 1, currentTypeCount);
				}
				
				if(block == null || block == m_FallingPiece)
					return null;
				
				currentType = block.GetType();
				currentTypeCount = 1;
			}
		}
		
		if(currentTypeCount >= 3)
		{
			//return the last block (the top most one)
			return new EliminationData(m_NumRows - 1, currentTypeCount);
		}
		
		return null;
	}
	
	private EliminationData CheckRowElimination(int row)
	{
		BlockType currentType = null;
		int currentTypeCount = 0;
		
		for(int i = 0; i < m_NumCols; ++i)
		{
			Block block = m_Matrix.GetAt(row, i); 
			
			if(block != null && block != m_FallingPiece && currentType == block.GetType())
			{
				++currentTypeCount;
			}
			else
			{
				if(currentTypeCount >= 3)
				{
					//return the last block (the right most one)
					return new EliminationData(i - 1, currentTypeCount);
				}
				
				if(block == null || block == m_FallingPiece)
					currentType = null;
				else
					currentType = block.GetType();
				
				currentTypeCount = 1;
			}
		}
		
		if(currentTypeCount >= 3)
		{
			//return the last block (the right most one)
			return new EliminationData(m_NumCols - 1, currentTypeCount);
		}
		
		return null;
	}
	
	private void DestroyColumnGroup(int topMost, int col, int count)
	{
		for(int i = 0; i < count; ++i)
			RemoveBlockFromGrid(new Point<Integer>(col, topMost - i));
	}
	
	private void DestroyRowGroup(int rightMost, int row, int count)
	{		
		for(int i = 0; i < count; ++i)
			RemoveBlockFromGrid(new Point<Integer>(rightMost - i, row));
	}
//------------------------------------------------------------------------
	
//Events -----------------------------------------------------------------
	public void UpdateGame() 
	{	
		if(m_MatchEnded)
			return;
		
		ProcessScoringConditions();
		
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
			
			if(m_FallingPiece == null)
			{
				//match ended
				m_MatchEnded = true;
				m_Match.OnMatchEnded();
			}
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
		Vector3 srcWorldPos = ResourceManager.m_sInstance.m_Viewport.unproject(new Vector3(m_TouchMoveStartedPoint.x, m_TouchMoveStartedPoint.y, 0));
		Vector3 dstWorldPos = ResourceManager.m_sInstance.m_Viewport.unproject(new Vector3(m_TouchMoveFinishedPoint.x, m_TouchMoveFinishedPoint.y, 0));
	
		if(!m_GridArea.contains(new Vector2(srcWorldPos.x, srcWorldPos.y)))
		{
			Log.Write("Ignoring move because src point not in the grid");
			return;
		}
		
		if(!m_GridArea.contains(new Vector2(dstWorldPos.x, dstWorldPos.y)))
		{
			Log.Write("Ignoring move because dst point not in the grid");
			return;
		}
		
		Point<Integer> srcGridPos = FromWorldToGrid(srcWorldPos);
		//Point<Integer> dstGridPos = FromWorldToGrid(dstWorldPos);
		
		float deltaX = dstWorldPos.x - srcWorldPos.x;
		float deltaY = dstWorldPos.y - srcWorldPos.y;
		
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
				//Log.Write("Moved right");
				
				if(srcGridPos.x < m_NumCols)
					dstBlock = m_Matrix.GetAt(srcGridPos.y, srcGridPos.x + 1);
			}
			else
			{
				//Log.Write("Moved left");
				
				if(srcGridPos.x > 0)
					dstBlock = m_Matrix.GetAt(srcGridPos.y, srcGridPos.x - 1);
			}
		}
		else
		{
			if(deltaY > 0)
			{
				//Log.Write("Moved up");
				
				if(srcGridPos.y < m_NumRows)
					dstBlock = m_Matrix.GetAt(srcGridPos.y + 1, srcGridPos.x);
			}
			else
			{
				//Log.Write("Moved down");
				
				if(srcGridPos.y > 0)
					dstBlock = m_Matrix.GetAt(srcGridPos.y - 1, srcGridPos.x);
			}
		}
		
		if(dstBlock == null)
		{
			Log.Write("Ignoring move because there is no destination piece");
			return;
		}
		
		if(dstBlock == m_FallingPiece)
		{
			Log.Write("Ignoring block switch because destination piece is the falling piece");
			return;
		}
		
		boolean wrongMove = false;
		
		if(wrongMove = srcBlock.IsFixed() || dstBlock.IsFixed())
		{
			Log.Write("Ignoring move because src and/or dst block is fixed");
		}
		
		Vector3 srcCornerWorld = FromGridToWorld(srcGridPos);
		Vector3 dstCornerWorld = FromGridToWorld(dstBlock.GetGridPos());
		
		m_SwapLine.CreateLine
		(
			new Vector2(srcCornerWorld.x + Block.m_sBlockViewSize * 0.5f, srcCornerWorld.y + Block.m_sBlockViewSize * 0.5f), 
			new Vector2(dstCornerWorld.x + Block.m_sBlockViewSize * 0.5f, dstCornerWorld.y + Block.m_sBlockViewSize * 0.5f), 
			wrongMove
		);
		
		if(wrongMove)
			return;
		
		//we can finally switch the blocks
		SwapBlocksInGrid(srcBlock, dstBlock);
		
		srcBlock.SetFixed(true);
	}
//------------------------------------------------------------------------
}
