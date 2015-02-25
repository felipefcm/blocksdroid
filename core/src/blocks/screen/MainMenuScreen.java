
package blocks.screen;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Bounce;
import aurelienribon.tweenengine.equations.Linear;
import blocks.game.ActorAccessor;
import blocks.game.Blocksdroid;
import blocks.resource.ResourceManager;
import blocks.screen.ScreenManager.ScreenType;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainMenuScreen extends GameScreen
{
	public Stage stage;
	public Table table;
	
	public Label titleLabel;
	
	public TextButton playButton;
	public TextButton tutorialButton;
	public TextButton leaderboardsButton;
	public TextButton quitButton;
	
	private SpriteBatch batch;
	private Viewport viewport;

	private TweenManager tweenManager;
	
	public MainMenuScreen()
	{
	}
	
	@Override
	public void show()
	{	
		batch = ResourceManager.instance.spriteBatch;
		viewport = ResourceManager.instance.viewport;
		tweenManager = ResourceManager.instance.tweenManager;

		if(ResourceManager.instance.adManager != null)
		    ResourceManager.instance.adManager.EnableAds();
				
		table = new Table();
		table.setFillParent(true);
		
		stage = new Stage(viewport, batch);
		stage.addActor(table);
		
		Gdx.input.setInputProcessor(stage);
		
		LabelStyle labelStyle = ResourceManager.instance.titleLabelStyle;
		TextButtonStyle textButtonStyle = ResourceManager.instance.textButtonStyle;

        Tween.registerAccessor(Actor.class, new ActorAccessor());

        titleLabel = new Label("BLOCKSDROID", labelStyle);
		playButton = new TextButton("PLAY", textButtonStyle);
		tutorialButton = new TextButton("TUTORIAL", textButtonStyle);
		leaderboardsButton = new TextButton("LEADERBOARDS", textButtonStyle);
		quitButton = new TextButton("QUIT", textButtonStyle);
		
		playButton.addListener
        (
            new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    ScreenManager.instance.SetScreen(new PlayScreen());
                }
            }
        );
		
		tutorialButton.addListener
        (
            new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    ScreenManager.instance.SetScreen(new TutorialScreen());
                }
            }
        );
		
		leaderboardsButton.addListener
        (
            new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                }
            }
        );
		
		quitButton.addListener
        (
            new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    dispose();
                    ResourceManager.instance.game.OnExit();
                }
            }
        );
		
		float buttonsWidth = Blocksdroid.V_WIDTH * 0.8f;
		float buttonsHeight = Blocksdroid.V_HEIGHT * 0.12f;
		float buttonsSpacing = Blocksdroid.V_HEIGHT * 0.0078f;
		
		table.add(titleLabel).padBottom(Blocksdroid.V_HEIGHT * 0.02f);
		table.row();
		
		table.add(playButton).width(buttonsWidth).height(buttonsHeight).padTop(Blocksdroid.V_HEIGHT * 0.08f).spaceBottom(buttonsSpacing);
		table.row();
		table.add(tutorialButton).width(buttonsWidth).height(buttonsHeight).spaceBottom(buttonsSpacing);
		table.row();
		table.add(leaderboardsButton).width(buttonsWidth).height(buttonsHeight).spaceBottom(buttonsSpacing);
		table.row();
		table.add(quitButton).width(buttonsWidth).height(buttonsHeight);

        //HACK just to force stage to calculate Actor positions
		stage.draw();

        float tweenDuration = 0.5f;

        Tween.from((Actor) playButton, ActorAccessor.PositionX, tweenDuration)
             .target(-500.0f)
             .ease(Linear.INOUT)
             .start(tweenManager);

        Tween.from((Actor) tutorialButton, ActorAccessor.PositionX, tweenDuration)
             .target(500.0f)
             .ease(Linear.INOUT)
             .start(tweenManager);

        Tween.from((Actor) leaderboardsButton, ActorAccessor.PositionX, tweenDuration)
             .target(-500.0f)
             .ease(Linear.INOUT)
             .start(tweenManager);

        Tween.from((Actor) quitButton, ActorAccessor.PositionX, tweenDuration)
             .target(500.0f)
             .ease(Linear.INOUT)
             .start(tweenManager);
	}
	
	@Override
	public void render(float delta)
	{
	    tweenManager.update(delta);
		stage.act(delta);
		
		//table.debug();
		
		stage.draw();
		
		//for debugging
		//Table.drawDebug(stage);
	}

	@Override
	public void resize(int width, int height)
	{	
	}

	@Override
	public void hide()
	{
		dispose();
	}

	@Override
	public void pause()
	{	
	}

	@Override
	public void resume()
	{	
	}

	@Override
	public void dispose()
	{	
		stage.dispose();
		table = null;
	}

	@Override
	public ScreenType GetType()
	{
		return ScreenType.MainMenu;
	}
}
