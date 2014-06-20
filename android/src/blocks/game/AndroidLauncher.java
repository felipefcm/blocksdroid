
package blocks.game;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import blocks.ad.AdHandler;
import blocks.ad.AndroidAdManager;
import blocks.resource.AndroidSwarmResources;
import blocks.resource.Log;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.swarmconnect.Swarm;

public class AndroidLauncher extends AndroidApplication 
{
	private AdHandler m_AdHandler;
	private AndroidAdManager m_AdManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		
		config.useAccelerometer = false;
		config.useCompass = false;
		config.useWakelock = true;
		
		RelativeLayout relativeLayout = new RelativeLayout(this);
				
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		
		m_AdManager = new AndroidAdManager(this);
		
		m_AdHandler = new AdHandler(m_AdManager.GetAdView(), m_AdManager.GetTopBanner());
		m_AdManager.SetHandler(m_AdHandler);
		
		View gameView = initializeForView(new Blocksdroid(m_AdManager, new AndroidSwarmResources(this)), config);
		
		relativeLayout.addView(gameView);

		RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams
		(
			RelativeLayout.LayoutParams.WRAP_CONTENT, 
            RelativeLayout.LayoutParams.WRAP_CONTENT
		);
		
		adParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		adParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		
		relativeLayout.addView(m_AdManager.GetAdView(), adParams);
		
		setContentView(relativeLayout);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		
		m_AdManager.GetAdView().resume();
		
		Log.Write("Swarm set active");
		Swarm.setActive(this);
	}
	
	@Override
	protected void onPause()
	{
		m_AdManager.GetAdView().pause();
		
		Log.Write("Swarm set inactive");
		Swarm.setInactive(this);
		
		super.onPause();
	}
}
