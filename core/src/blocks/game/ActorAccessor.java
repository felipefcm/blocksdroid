package blocks.game;

import com.badlogic.gdx.scenes.scene2d.Actor;

import aurelienribon.tweenengine.TweenAccessor;

public class ActorAccessor implements TweenAccessor<Actor>
{
    public static final int PositionX = 0;
    public static final int Scale = 1;

    @Override
    public int getValues(Actor target, int tweenType, float[] returnValues)
    {
        switch(tweenType)
        {
            case PositionX:
                returnValues[0] = target.getX();
            return 1;

            case Scale:
                returnValues[0] = target.getScaleX();
            return 1;
        }

        return 0;
    }

    @Override
    public void setValues(Actor target, int tweenType, float[] newValues)
    {
        switch(tweenType)
        {
            case PositionX:
                target.setX(newValues[0]);
            break;

            case Scale:
                target.setScale(newValues[0]);
            break;
        }
    }
}
