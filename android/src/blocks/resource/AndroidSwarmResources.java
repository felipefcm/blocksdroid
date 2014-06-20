package blocks.resource;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.SystemClock;
import android.widget.Toast;
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
	
	private boolean m_LoginInProgress;
	
	private SwarmLoginListener m_LoginListener = new SwarmLoginListener()
	{
		@Override
		public void userLoggedOut()
		{
			Log.Write("Swarm Logged Out");
		}
		
		@Override
		public void userLoggedIn(SwarmActiveUser arg0)
		{
			Log.Write("Swarm Logged In");
			
			m_LoginInProgress = false;
		}
		
		@Override
		public void loginStarted()
		{
			m_LoginInProgress = true;
			
			Log.Write("Swarm Login Started");
		}
		
		@Override
		public void loginCanceled()
		{
			m_LoginInProgress = false;
			
			Log.Write("Swarm Login Cancelled");			
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
		Log.Write("Preloading Swarm");
		Swarm.preload(m_App, m_SwarmId, m_SwarmKey);
	}
	
	@Override
	public void InitSwarm()
	{		
		Log.Write("Initializing Swarm");
		Swarm.init(m_App, m_SwarmId, m_SwarmKey, m_LoginListener);
	}
	
	@Override
	public void SubmitScore(int score)
	{
		if(!IsInternetAvailable())
		{
			Toast.makeText(m_App, "No internet connection", Toast.LENGTH_LONG).show();
			return;
		}
		
		if(!Swarm.isEnabled() || !Swarm.isInitialized() || !Swarm.isOnline() || !Swarm.isLoggedIn())
			InitSwarm();
		
		while(m_LoginInProgress)
		{
			Log.Write("Login in progress, sleeping");
			SystemClock.sleep(500);
		}
		
		SystemClock.sleep(500);
		SwarmLeaderboard.submitScore(m_LeaderboardId, (float) score);
	}

	@Override
	public void ShowLeaderboard()
	{
		if(!IsInternetAvailable())
		{
			Toast.makeText(m_App, "No internet connection", Toast.LENGTH_LONG).show();
			return;
		}
		
		if(!Swarm.isEnabled() || !Swarm.isInitialized() || !Swarm.isOnline() || !Swarm.isLoggedIn())
			InitSwarm();
		
		while(m_LoginInProgress)
		{
			Log.Write("Login in progress, sleeping");
			SystemClock.sleep(500);
		}
		
		SwarmLeaderboard.showLeaderboard(m_LeaderboardId);
	}

	@Override
	public void SubmitAndShowLeaderboard(int score)
	{
		if(!IsInternetAvailable())
		{
			Toast.makeText(m_App, "No internet connection", Toast.LENGTH_LONG).show();
			return;
		}
		
		if(!Swarm.isEnabled() || !Swarm.isInitialized() || !Swarm.isOnline() || !Swarm.isLoggedIn())
			InitSwarm();
		
		while(m_LoginInProgress)
		{
			Log.Write("Login in progress, sleeping");
			SystemClock.sleep(500);
		}
		
		SystemClock.sleep(500);
		SwarmLeaderboard.submitScoreAndShowLeaderboard(m_LeaderboardId, (float) score);
	}
	
	public boolean IsInternetAvailable()
	{
		ConnectivityManager cm = (ConnectivityManager) m_App.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		NetworkInfo ni = cm.getActiveNetworkInfo();
		
		if(ni != null && ni.isConnected())
			return true;
		
		return false;
	}
}
