package blocks.resource;

public interface SwarmResources
{
	public void PreLoadSwarm();
	public void InitSwarm();
	
	public void SubmitScore(int score);
	public void SubmitAndShowLeaderboard(int score);
	
	public void ShowLeaderboard();
}
