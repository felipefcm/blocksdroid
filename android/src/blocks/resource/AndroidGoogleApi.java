
package blocks.resource;

import android.app.Activity;
import android.app.Dialog;
import android.content.IntentSender;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.leaderboard.Leaderboards;

import blocks.game.AndroidLauncher;
import blocks.game.R;

public class AndroidGoogleApi implements GoogleApiInterface, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks
{
    public static GoogleApiClient googleApiClient;

    public static final int RequestLeaderboard = 624;
    public static final int RequestConnectionErrorResolution = 6245;

    private AndroidLauncher launcher;

    private boolean isConnected;
    private boolean isConnecting;
    private boolean isResolvingConnectionError;

    public AndroidGoogleApi(AndroidLauncher launcher)
    {
        this.launcher = launcher;
        googleApiClient = null;

        isConnected = false;
        isResolvingConnectionError = false;
        isConnecting = false;
    }

    @Override
    public void Connect()
    {
        if(isConnected || isResolvingConnectionError)
            return;

        isConnecting = true;

        if(googleApiClient != null)
            googleApiClient.connect();
    }

    @Override
    public boolean IsConnected()
    {
        return isConnected;
    }

    @Override
    public boolean IsConnecting()
    {
        return isConnecting;
    }

    @Override
    public boolean ShowLeaderboard()
    {
        if(!isConnected)
            return false;

        launcher.startActivityForResult
        (
            Games.Leaderboards.getLeaderboardIntent(googleApiClient, launcher.getString(R.string.highscores_id)),
            RequestLeaderboard
        );

        return true;
    }

    @Override
    public void SendScore(int score)
    {
        Games.Leaderboards.submitScore(googleApiClient, launcher.getString(R.string.highscores_id), (long) score);
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        isConnected = true;
        isConnecting = false;
        isResolvingConnectionError = false;

        Log.Write("GoogleApiClient connection successful");
    }

    @Override
    public void onConnectionSuspended(int i)
    {
        isConnected = false;
        isConnecting = false;

        Log.Write("GoogleApiClient connection suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {
        if(isResolvingConnectionError)
            return;

        isConnected = false;
        isResolvingConnectionError = true;

        Log.Write("GoogleApiClient connection failed, will try to resolve. Error code: " + connectionResult.getErrorCode());

        if(connectionResult.hasResolution())
        {
            Log.Write("Connection fail has resolution");

            try
            {
                connectionResult.startResolutionForResult(launcher, RequestConnectionErrorResolution);
            }
            catch(IntentSender.SendIntentException e)
            {
                Log.Write("Error in startResolutionForResult, intent cancelled before sending, starting again");

                isResolvingConnectionError = false;
                googleApiClient.connect();
            }
        }
        else
        {
            Log.Write("Connection fail has no resolution");

            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), launcher, 0);

            if(dialog != null)
                dialog.show();
        }

        isResolvingConnectionError = false;
        isConnecting = false;
    }

    public void OnConnectionResolutionReturned(int resultCode)
    {
        if(resultCode != Activity.RESULT_OK)
        {
            Log.Write("Error while trying to resolve connection error, resultCode: " + resultCode);
            isConnecting = false;
            return;
        }

        Log.Write("Connection resolution returned OK");
        googleApiClient.connect();

        isResolvingConnectionError = false;
    }
}
