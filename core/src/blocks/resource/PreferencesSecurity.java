
package blocks.resource;

import java.security.MessageDigest;

public class PreferencesSecurity
{
	public static PreferencesSecurity m_sInstance = new PreferencesSecurity();
	
	public boolean IsBestScoreValid(final int score)
	{	
		if(!ResourceManager.instance.preferences.contains("BestScoreKey"))
			return false;
		
		String prefHash = ResourceManager.instance.preferences.getString("BestScoreKey");
		String calcHash = CalculateBestScoreHash(score); 
		
		if(prefHash.contentEquals(calcHash))
			return true;
		
		return false;
	}
	
	public String CalculateBestScoreHash(final int score)
	{
		String hash = "";
		
		MessageDigest digest = null;
		
		try
		{
			digest = MessageDigest.getInstance("SHA-1");
		}
		catch(Exception e)
		{
			Log.Write("Could not create SHA-1 digest object");
			return hash;
		}
		
		try
		{
			digest.update(("bgt23jusdvgby512hyxy3457" + score).getBytes("UTF-8"));
		}
		catch(Exception e)
		{
			Log.Write("Failed to obtain UTF-8 charset");
			return hash;
		}
		
		byte[] digestBytes = digest.digest();
		
		for(int i = 0; i < digestBytes.length; ++i)
		{
			hash += Integer.toString((digestBytes[i] & 0xff) + 0x100, 16).substring(1);
		}
		
		return hash;
	}
}
