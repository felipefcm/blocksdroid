package blocks.resource;

import blocks.game.R;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.swarmconnect.Swarm;
import com.swarmconnect.SwarmActiveUser;
import com.swarmconnect.SwarmLeaderboard;
import com.swarmconnect.delegates.SwarmLoginListener;

public class AndroidSwarmResources implements SwarmResources
{
	private AndroidApplication m_App;
	private int m_SwarmId;
	private String m_SwarmKey;
	private int m_LeaderboardId;
	
	//private boolean m_LoginFinished;
	
	private SwarmLoginListener m_LoginListener = new SwarmLoginListener()
	{
		@Override
		public void userLoggedOut()
		{
		}
		
		@Override
		public void userLoggedIn(SwarmActiveUser arg0)
		{
		}
		
		@Override
		public void loginStarted()
		{
		}
		
		@Override
		public void loginCanceled()
		{
		}
	};
	
	public AndroidSwarmResources(AndroidApplication app)
	{
		m_App = app;
		
		m_SwarmId = m_App.getResources().getInteger(R.integer.swarm_id);
		m_SwarmKey = m_App.getResources().getString(R.string.swarm_key);
		m_LeaderboardId = m_App.getResources().getInteger(R.integer.swarm_leaderboards_id);
	}
	
	@Override
	public void PreLoadSwarm()
	{
		Swarm.preload(m_App, m_SwarmId, m_SwarmKey);
	}
	
	@Override
	public void InitSwarm()
	{
		Swarm.init(m_App, m_SwarmId, m_SwarmKey, m_LoginListener);
	}
	
	@Override
	public void SubmitScore(int score)
	{
		if(!Swarm.isInitialized())
			InitSwarm();

		SwarmLeaderboard.submitScore(m_LeaderboardId, (float) score);
	}

	@Override
	public void ShowLeaderboard()
	{
		if(!Swarm.isInitialized())
			InitSwarm();
		
		SwarmLeaderboard.showLeaderboard(m_LeaderboardId);
	}

	@Override
	public void SubmitAndShowLeaderboard(int score)
	{
		if(!Swarm.isInitialized())
			InitSwarm();
		
		SwarmLeaderboard.submitScoreAndShowLeaderboard(m_LeaderboardId, (float) score);
	}
}
