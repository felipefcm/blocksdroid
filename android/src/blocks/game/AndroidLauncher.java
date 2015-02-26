
package blocks.game;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import blocks.ad.AdHandler;
import blocks.ad.AndroidAdManager;
import blocks.resource.AndroidGoogleApi;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

public class AndroidLauncher extends AndroidApplication 
{
	private AdHandler adHandler;
	private AndroidAdManager adManager;

	private AndroidGoogleApi androidGoogleApi;
	
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

		androidGoogleApi = new AndroidGoogleApi(this);

		AndroidGoogleApi.googleApiClient = new GoogleApiClient.Builder(this)
		                                   .addOnConnectionFailedListener(androidGoogleApi)
		                                   .addConnectionCallbacks(androidGoogleApi)
		                                   .addApi(Games.API).addScope(Games.SCOPE_GAMES)
		                                   .build();

		adManager = new AndroidAdManager(this);
		
		adHandler = new AdHandler(adManager.GetAdView(), adManager.GetTopBanner());
		adManager.SetHandler(adHandler);
		
		View gameView = initializeForView(new Blocksdroid(adManager, androidGoogleApi), config);
		
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
    protected void onStart()
    {
        super.onStart();

        androidGoogleApi.Connect();
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

    @Override
    protected void onStop()
    {
        super.onStop();

        AndroidGoogleApi.googleApiClient.disconnect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch(requestCode)
        {
            case AndroidGoogleApi.RequestConnectionErrorResolution:
                androidGoogleApi.OnConnectionResolutionReturned(resultCode);
            break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
