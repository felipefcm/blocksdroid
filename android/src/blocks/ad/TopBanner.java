
package blocks.ad;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class TopBanner
{
	private AdView m_AdView;
	
	public boolean m_IsInit;
	
	public TopBanner(AdView adView)
	{
		m_AdView = adView;
		m_IsInit = false;
	}
	
	protected void Init()
	{		
		AdRequest adRequest = new AdRequest.Builder()
							.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
							.build();
		
		m_AdView.loadAd(adRequest);
		
		m_IsInit = true;
	}
}
