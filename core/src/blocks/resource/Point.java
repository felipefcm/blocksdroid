
package blocks.resource;

public class Point<T>
{
	public Point()
	{	
	}
	
	public Point(T x, T y)
	{
		this.x = x;
		this.y = y;
	}	
	
	public T x;
	public T y;
}
