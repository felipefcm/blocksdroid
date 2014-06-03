package blocks.ad;

import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.google.android.gms.ads.AdView;

public class AdHandler extends Handler
{
	private AdView m_AdView;
	private TopBanner m_TopBanner;
	
	public AdHandler(AdView adView, TopBanner topBanner)
	{
		m_AdView = adView;
		m_TopBanner = topBanner;
	}
	
	@Override
	public void handleMessage(Message msg)
	{
		switch(msg.what)
		{
			case AdManager.SHOW_ADS:
			{
				if(!m_TopBanner.m_IsInit)
					m_TopBanner.Init();
				
				m_AdView.setVisibility(View.VISIBLE);
			}
			break;
			
			case AdManager.HIDE_ADS:
			{
				m_AdView.setVisibility(View.GONE);
			}
			break;
		}
	}
}
