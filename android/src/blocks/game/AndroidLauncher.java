
package blocks.game;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import blocks.ad.AdHandler;
import blocks.ad.AndroidAdManager;
import blocks.resource.AndroidSwarmResources;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.swarmconnect.Swarm;

public class AndroidLauncher extends AndroidApplication 
{
	private AdHandler adHandler;
	private AndroidAdManager adManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		
		config.useWakelock = true;
		config.useAccelerometer = false;
		config.useCompass = false;

		RelativeLayout relativeLayout = new RelativeLayout(this);
				
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		
		adManager = new AndroidAdManager(this);
		
		adHandler = new AdHandler(adManager.GetAdView(), adManager.GetTopBanner());
		adManager.SetHandler(adHandler);
		
		View gameView = initializeForView(new Blocksdroid(adManager), config);
		
		relativeLayout.addView(gameView);

		RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams
		(
			RelativeLayout.LayoutParams.WRAP_CONTENT, 
            RelativeLayout.LayoutParams.WRAP_CONTENT
		);
		
		adParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		adParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		
		relativeLayout.addView(adManager.GetAdView(), adParams);
		
		setContentView(relativeLayout);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		
		adManager.GetAdView().resume();
	}
	
	@Override
	protected void onPause()
	{
		adManager.GetAdView().pause();
		
		super.onPause();
	}
}
