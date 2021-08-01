package blocks.ad;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class AndroidAdManager implements AdManager
{
	private TopBanner m_TopBanner;
	private AdView m_AdView;
	private AdHandler m_AdHandler;
	
	public AndroidAdManager(AndroidApplication app)
	{
		m_AdView = new AdView(app);
		
		m_AdView.setAdSize(AdSize.SMART_BANNER);
		m_AdView.setAdUnitId("SET-AD-UNIT");
		
		m_TopBanner = new TopBanner(m_AdView);
	}
	
	public AdView GetAdView()
	{
		return m_AdView;
	}
	
	public void SetHandler(AdHandler adHandler)
	{
		m_AdHandler = adHandler;
	}
	
	public TopBanner GetTopBanner()
	{
		return m_TopBanner;
	}
	
	//Methods called by game thread ----------------------------------------
	@Override
	public void EnableAds()
	{
		m_AdHandler.sendEmptyMessage(SHOW_ADS);
	}

	@Override
	public void DisableAds()
	{
		m_AdHandler.sendEmptyMessage(HIDE_ADS);
	}
	//----------------------------------------------------------------------
}
