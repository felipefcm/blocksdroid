
package blocks.resource;

import com.badlogic.gdx.Gdx;

public class Log
{
	public static void Write(String message)
	{
		Write("Blocksdroid", message);
	}
	
	public static void Write(String tag, String message)
	{
		Gdx.app.log(tag, message);
	}
}
