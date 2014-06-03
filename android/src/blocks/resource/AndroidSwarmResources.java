package blocks.resource;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.swarmconnect.Swarm;
import com.swarmconnect.SwarmLeaderboard;

public class AndroidSwarmResources implements SwarmResources
{
	private AndroidApplication m_App;
	
	public AndroidSwarmResources(AndroidApplication app)
	{
		m_App = app;
	}
	
	@Override
	public void InitSwarm()
	{
		Swarm.init(m_App, 11734, "ca50c72c1b3e7d4be979c7da11cfc35b");
	}
	
	@Override
	public void SubmitScore(int score)
	{
		if(!Swarm.isInitialized())
			InitSwarm();
		
		SwarmLeaderboard.submitScore(16486, (float) score);
	}

	@Override
	public void ShowLeaderboard()
	{
		if(!Swarm.isInitialized())
			InitSwarm();
		
		Swarm.showLeaderboards();
	}
}
