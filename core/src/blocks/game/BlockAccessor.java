
package blocks.game;

import aurelienribon.tweenengine.TweenAccessor;
import blocks.resource.Log;

public class BlockAccessor implements TweenAccessor<Block>
{
    public static final int PositionXY = 0;
    public static final int Opacity = 1;

    @Override
    public int getValues(Block target, int tweenType, float[] returnValues)
    {
        switch(tweenType)
        {
            case PositionXY:
                returnValues[0] = target.getX();
                returnValues[1] = target.getY();
            return 2;

            case Opacity:
                returnValues[0] = target.getColor().a;
            return 1;

            default:
                return 0;
        }
    }

    @Override
    public void setValues(Block target, int tweenType, float[] newValues)
    {
        switch(tweenType)
        {
            case PositionXY:
                target.setX(newValues[0]);
                target.setY(newValues[1]);
            break;

            case Opacity:
                Log.Write("Setting alpha for " + target.toString() + " -> " + newValues[0]);
                target.setAlpha(newValues[0]);
            break;
        }
    }
}
