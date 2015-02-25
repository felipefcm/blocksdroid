
package blocks.game;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Bounce;
import aurelienribon.tweenengine.equations.Linear;
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

import java.util.LinkedList;

public class BlockGrid extends InputAdapter
{
    //Member variables -------------------------------------------------------
	private int numRows;
	private int numCols;
	private BlockMatrix matrix;
	
	private BlocksMatch match;
	private boolean matchEnded;
	
	private Matrix4 fromGridToWorld;
	private Matrix4 fromWorldToGrid;
	
	private SpriteBatch spriteBatch;
	private ShapeRenderer shapeRenderer;
	
	private Rectangle gridArea;
	
	private Block fallingPiece;
	
	private int emptyPositions;
	
	private float updateTime;
	
	private SwapLine swapLine;
	
	private Point<Integer> touchMoveStartedPoint;
	private Point<Integer> touchMoveFinishedPoint;

	private TweenManager tweenManager;

	private final float minTouchLength = Block.BlockViewSize * 0.5f;
	private final float maxTouchLength = Block.BlockViewSize * 5.0f;

	private final float swapTweenDuration = 0.1f;
    private final float destroyTweenDuration = 0.2f;
    private final float moveDownTweenDuration = 0.08f;

    //blocks that have been removed from matrix but still are needed to animate
    private LinkedList<Block> animatingBlocks;
	
	public enum MoveDirection
	{
		Up, Down,
		Right, Left
	}
    //------------------------------------------------------------------------

	public BlockGrid(int numRows, int numCols, BlocksMatch match)
	{
		this.numRows = numRows;
		this.numCols = numCols;
		
		this.match = match;
		
		matrix = new BlockMatrix(numRows, numCols);
		
		swapLine = new SwapLine();
		
		touchMoveStartedPoint = new Point<Integer>();
		touchMoveFinishedPoint = new Point<Integer>();

		animatingBlocks = new LinkedList<Block>();

        Tween.registerAccessor(Block.class, new BlockAccessor());
	}
	
	public void Init()
	{
		spriteBatch = ResourceManager.instance.spriteBatch;
		shapeRenderer = ResourceManager.instance.shapeRenderer;
		tweenManager = ResourceManager.instance.tweenManager;
		
		matchEnded = false;
		
		matrix.ClearAll();
		animatingBlocks.clear();
		
		emptyPositions = numRows * numCols;
		
		updateTime = 0;
		
		gridArea = new Rectangle
		(
            Blocksdroid.V_WIDTH * 0.048f,
            Blocksdroid.V_HEIGHT * 0.022f,
			numCols * Block.BlockViewSize,
			numRows * Block.BlockViewSize
		);

		fromGridToWorld = new Matrix4();

		fromGridToWorld.trn(gridArea.x + 1, gridArea.y, 0);
		fromWorldToGrid = fromGridToWorld.cpy().inv();

		PrePopulateGrid();
		
		Gdx.input.setInputProcessor(this);
		
		CreateNewFallingPiece();
	}
	
	private void PrePopulateGrid()
	{
		for(int i = 0; i < numCols; ++i)
		{
			int columnHeight = ResourceManager.instance.random.nextInt(4);
			
			for(int k = 0; k < columnHeight; ++k)
			{
				Block newBlock = BlockFactory.GetRandomBlock(BlockFactory.INITIAL_BLOCKS);
				newBlock.SetGridPos(new Point<Integer>(i, k));
				newBlock.isPlaced = true;
				
				InsertBlockInGrid(newBlock);
			}
		}
		
		ProcessScoringConditions();
	}
	
	public void Render()
	{
        ResourceManager.instance.tweenManager.update(Gdx.graphics.getDeltaTime());

		updateTime += Gdx.graphics.getDeltaTime();
		
		if(updateTime >= match.gameSpeed)
		{
			updateTime = 0;
			UpdateGame();
		}

		//grid bounds
		shapeRenderer.begin(ShapeType.Line);
		{
			shapeRenderer.setColor(0.75f, 0.75f, 0.75f, 1.0f);
            shapeRenderer.rect
            (
                gridArea.x, gridArea.y,
                gridArea.width + 1, gridArea.height + 1
            );
		}
		shapeRenderer.end();
		
		spriteBatch.setTransformMatrix(fromGridToWorld);
		spriteBatch.setProjectionMatrix(ResourceManager.instance.camera.combined);
		
		matrix.Render(spriteBatch);

		if(animatingBlocks.size() > 0)
        {
            spriteBatch.begin();
            {
		        for(Block b : animatingBlocks)
		            b.draw(spriteBatch);
            }
		    spriteBatch.end();
        }

		
		if(swapLine.m_IsVisible)
			swapLine.Render(shapeRenderer);
	}
	
	public void Dispose()
	{
		matrix.ClearAll();
		animatingBlocks.clear();
	}
	
    //Grid management --------------------------------------------------------
	private boolean IsGridPositionAvailable(Point<Integer> gridPosition)
	{
		return (matrix.GetAt(gridPosition) == null);
	}
	
	private boolean InsertBlockInGrid(Block block)
	{
		Point<Integer> blockGridPos = block.GetGridPos();
		
		if(!IsGridPositionAvailable(blockGridPos))
			return false;
				
		block.setPosition(blockGridPos.x * Block.BlockViewSize, blockGridPos.y * Block.BlockViewSize);
		
		matrix.SetBlockAt(block, blockGridPos);
		
		--emptyPositions;
		
		return true;
	}

	private void RemoveBlockFromGrid(Point<Integer> gridPos)
	{
	    final Block block = matrix.GetAt(gridPos);

		if(block == null)
			return;

        animatingBlocks.add(block);

        matrix.DisposeBlock(gridPos, false);
        ++emptyPositions;

        Tween.to(block, BlockAccessor.Opacity, destroyTweenDuration)
             .target(0.0f)
             .ease(Linear.INOUT)
             .setCallbackTriggers(TweenCallback.COMPLETE)
             .setCallback(new TweenCallback()
                          {
                              @Override
                              public void onEvent(int type, BaseTween<?> source)
                              {
                                  animatingBlocks.remove(block);
                                  BlockFactory.PoolBlock(block);
                              }
                          })
             .start(tweenManager);
	}
	
	private boolean MoveBlockInGrid(Block block, Point<Integer> dstGridPos)
	{
		if(!IsGridPositionAvailable(dstGridPos))
			return false;
		
		Point<Integer> srcGridPos = block.GetGridPos();
		
		if(matrix.GetAt(srcGridPos) == null)
			return false;
		
		matrix.ClearPosition(srcGridPos);
		
		block.SetGridPos(dstGridPos);
		matrix.SetBlockAt(block, dstGridPos);

		//block.setPosition(dstGridPos.x * Block.BlockViewSize, dstGridPos.y * Block.BlockViewSize);

		Tween.to(block, BlockAccessor.PositionXY, block == fallingPiece ? 0.03f : moveDownTweenDuration)
		     .target(dstGridPos.x * Block.BlockViewSize, dstGridPos.y * Block.BlockViewSize)
		     .ease(Linear.INOUT)
		     .start(tweenManager);

		return true;
	}
	
	private boolean SwapBlocksInGrid(Block blockA, Block blockB)
	{
		Point<Integer> aGridPos = blockA.GetGridPos();
		Point<Integer> bGridPos = blockB.GetGridPos();
		
		matrix.SetBlockAt(blockB, aGridPos);
		matrix.SetBlockAt(blockA, bGridPos);
		
		blockA.SetGridPos(bGridPos);
		blockB.SetGridPos(aGridPos);
		
		//blockA.setPosition(bGridPos.x * Block.BlockViewSize, bGridPos.y * Block.BlockViewSize);
		//blockB.setPosition(aGridPos.x * Block.BlockViewSize, aGridPos.y * Block.BlockViewSize);

        Tween.to(blockA, BlockAccessor.PositionXY, swapTweenDuration)
             .target(bGridPos.x * Block.BlockViewSize, bGridPos.y * Block.BlockViewSize)
             .ease(Linear.INOUT)
             .start(tweenManager);

        Tween.to(blockB, BlockAccessor.PositionXY, swapTweenDuration)
             .target(aGridPos.x * Block.BlockViewSize, aGridPos.y * Block.BlockViewSize)
             .ease(Linear.INOUT)
             .start(tweenManager);
		
		return true;
	}
	
	public void RandomizeGrid()
	{
		//for testing only
		
		for(int i = 0; i < numRows; ++i)
		{
			for(int j = 0; j < numCols; ++j)
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
            gridPosition.x * Block.BlockViewSize,
            gridPosition.y * Block.BlockViewSize,
            0
        );
		
		return blockPosInGridCoords.cpy().mul(fromGridToWorld);
	}
	
	private Point<Integer> FromWorldToGrid(final Vector3 worldPosition)
	{
		Vector3 blockPosInGridCoords = worldPosition.cpy().mul(fromWorldToGrid);
		
		return new Point<Integer>
        (
            (int)(blockPosInGridCoords.x / Block.BlockViewSize),
            (int)(blockPosInGridCoords.y / Block.BlockViewSize)
        );
	}
	
	private void MoveDownPlacedBlocks()
	{
		for(int i = 1; i < numRows; ++i) //we can skip first row
			for(int j = 0; j < numCols; ++j)
			{
				Block block = matrix.GetAt(i, j);
				
				if(block == null || block == fallingPiece)
					continue;
				
				//last occupied position
				int k;
				
				for(k = i - 1; k >= 0; --k)
				{
					if(matrix.GetAt(k, j) == null)
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
		if(emptyPositions == 0)
			return;
		
		Point<Integer> newPos = new Point<Integer>();
		
		newPos.y = numRows - 1;
		
		do
		{
			//pick a random column to spawn falling block
			newPos.x = ResourceManager.instance.random.nextInt(numCols);
		}
		while(!IsGridPositionAvailable(newPos));
		
		fallingPiece = BlockFactory.GetRandomBlock(BlockFactory.INITIAL_BLOCKS);
		
		fallingPiece.SetGridPos(newPos);
		
		InsertBlockInGrid(fallingPiece);
	}
	
	private void MoveDownFallingPiece(Point<Integer> nextGridPos)
	{
		//'nextGridPos' is already checked by 'onTimePassed' function
		MoveBlockInGrid(fallingPiece, nextGridPos);
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
			
			for(int col = 0; col < numCols; ++col)
			{
				EliminationData colElimination = CheckColumnElimination(col);
				
				if(colElimination != null)
				{
					DestroyColumnGroup(colElimination.pos, col, colElimination.count);
					match.IncrementScore(1);
					MoveDownPlacedBlocks(); //TODO check if can move down only that column
					
					hadColumnDestruction = true;
					break;
				}
			}
			
			for(int row = 0; row < numRows; ++row)
			{
				EliminationData rowElimination = CheckRowElimination(row);
				
				if(rowElimination != null)
				{
					DestroyRowGroup(rowElimination.pos, row, rowElimination.count);
					match.IncrementScore(1);
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
		
		for(int i = 0; i < numRows; ++i)
		{
			Block block = matrix.GetAt(i, col);
						
			if(block != null && block != fallingPiece && currentType == block.GetType())
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
				
				if(block == null || block == fallingPiece)
					return null;
				
				currentType = block.GetType();
				currentTypeCount = 1;
			}
		}
		
		if(currentTypeCount >= 3)
		{
			//return the last block (the top most one)
			return new EliminationData(numRows - 1, currentTypeCount);
		}
		
		return null;
	}
	
	private EliminationData CheckRowElimination(int row)
	{
		BlockType currentType = null;
		int currentTypeCount = 0;
		
		for(int i = 0; i < numCols; ++i)
		{
			Block block = matrix.GetAt(row, i);
			
			if(block != null && block != fallingPiece && currentType == block.GetType())
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
				
				if(block == null || block == fallingPiece)
					currentType = null;
				else
					currentType = block.GetType();
				
				currentTypeCount = 1;
			}
		}
		
		if(currentTypeCount >= 3)
		{
			//return the last block (the right most one)
			return new EliminationData(numCols - 1, currentTypeCount);
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
		if(matchEnded)
			return;
		
		ProcessScoringConditions();
		
		Point<Integer> blockPos = fallingPiece.GetGridPos();
		Point<Integer> nextPos = new Point<Integer>(blockPos.x, blockPos.y - 1);
		
		//Check if falling piece can fall another position
		if(nextPos.y < 0 || !IsGridPositionAvailable(nextPos))
		{
			//Falling piece cannot fall anymore, place it
			fallingPiece.isPlaced = true;
			fallingPiece = null;
			
			ProcessScoringConditions();
			
			CreateNewFallingPiece();
			
			if(fallingPiece == null)
			{
				//match ended
				matchEnded = true;
				match.OnMatchEnded();
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
		touchMoveStartedPoint.x = screenX;
		touchMoveStartedPoint.y = screenY;
		
		return true;
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		touchMoveFinishedPoint.x = screenX;
		touchMoveFinishedPoint.y = screenY;
		
		if(match.IsPaused())
		{
			match.UnpauseMatch();
			return true;
		}
		
		OnTouchMoveFinished();

		return true;
	}
		
	private void OnTouchMoveFinished()
	{		
		Vector3 srcWorldPos = ResourceManager.instance.viewport.unproject(new Vector3(touchMoveStartedPoint.x, touchMoveStartedPoint.y, 0));
		Vector3 dstWorldPos = ResourceManager.instance.viewport.unproject(new Vector3(touchMoveFinishedPoint.x, touchMoveFinishedPoint.y, 0));
	
		//FIX
		if(match.pauseButton.getBoundingRectangle().contains(dstWorldPos.x, dstWorldPos.y))
		{
			match.PauseMatch();
			return;
		}
		
		if(!gridArea.contains(new Vector2(srcWorldPos.x, srcWorldPos.y)))
		{
			Log.Write("Ignoring move because src point not in the grid");
			return;
		}
		
		if(!gridArea.contains(new Vector2(dstWorldPos.x, dstWorldPos.y)))
		{
			if(dstWorldPos.x < gridArea.x)
				dstWorldPos.x = gridArea.x;
			else
				if(dstWorldPos.x > gridArea.x + gridArea.width)
					dstWorldPos.x = gridArea.x + gridArea.width;
			
			if(dstWorldPos.y < gridArea.y)
				dstWorldPos.y = gridArea.y;
			else
				if(dstWorldPos.y > gridArea.y + gridArea.height)
					dstWorldPos.y = gridArea.y + gridArea.height;
		}
		
		Point<Integer> srcGridPos = FromWorldToGrid(srcWorldPos);
		//Point<Integer> dstGridPos = FromWorldToGrid(dstWorldPos);
		
		float deltaX = dstWorldPos.x - srcWorldPos.x;
		float deltaY = dstWorldPos.y - srcWorldPos.y;
		
		float length = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
		
		if(length >= maxTouchLength)
		{
			Log.Write("Ignoring move because it has passed maximum length");
			return;
		}
		
		if(Math.abs(deltaX) >= minTouchLength * 1.3f && Math.abs(deltaY) >= minTouchLength * 1.3f)
		{
			Log.Write("Ignoring move because it is ambiguous");
			return;
		}
		
		Block srcBlock = matrix.GetAt(srcGridPos);
		Block dstBlock = null;
		
		if(srcBlock == null)
		{
			Log.Write("Ignoring move because source block is null");
			return;
		}
		
		if(!srcBlock.isPlaced)
		{
			Log.Write("Ignoring move because source block is not placed (falling piece)");
			return;
		}
		
		if(Math.abs(deltaX) >= minTouchLength)
		{
			if(deltaX > 0)
			{
				//Log.Write("Moved right");
				
				if(srcGridPos.x < numCols)
					dstBlock = matrix.GetAt(srcGridPos.y, srcGridPos.x + 1);
			}
			else
			{
				//Log.Write("Moved left");
				
				if(srcGridPos.x > 0)
					dstBlock = matrix.GetAt(srcGridPos.y, srcGridPos.x - 1);
			}
		}
		else
		{
			if(deltaY > 0)
			{
				//Log.Write("Moved up");
				
				if(srcGridPos.y < numRows)
					dstBlock = matrix.GetAt(srcGridPos.y + 1, srcGridPos.x);
			}
			else
			{
				//Log.Write("Moved down");
				
				if(srcGridPos.y > 0)
					dstBlock = matrix.GetAt(srcGridPos.y - 1, srcGridPos.x);
			}
		}
		
		if(dstBlock == null)
		{
			Log.Write("Ignoring move because there is no destination piece");
			return;
		}
		
		if(dstBlock == fallingPiece)
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

		if(wrongMove)
        {
            swapLine.CreateLine
            (
                new Vector2(srcCornerWorld.x + Block.BlockViewSize * 0.5f, srcCornerWorld.y + Block.BlockViewSize * 0.5f),
                new Vector2(dstCornerWorld.x + Block.BlockViewSize * 0.5f, dstCornerWorld.y + Block.BlockViewSize * 0.5f),
                true
            );

            return;
        }
		
		//we can finally switch the blocks
		SwapBlocksInGrid(srcBlock, dstBlock);
		
		srcBlock.SetFixed(true);
	}
    //------------------------------------------------------------------------
}
