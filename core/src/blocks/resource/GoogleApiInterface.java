
package blocks.resource;

public interface GoogleApiInterface
{
    public void Connect();
    public boolean IsConnected();
    public boolean IsConnecting();
    public boolean ShowLeaderboard();
    public void SendScore(int score);
}
