/**
 * @(#) Player.java
 */


public class Player
{
	private String name;
	
	private Kalaha kalaha;
	
	private KalahaNode kalahaNode;
	
	public boolean setKalaha(Kalaha value)
	{
		if(value != null){
			this.kalaha = value;
			return true;
		}
		return false;
	}
	
	public Player withKalaha(Kalaha value)
	{
		setKalaha(value);
		return this;
	}
	
	public Kalaha getKalaha( )
	{
		return this.kalaha;
	}
	
	public void setName(String value)
	{
		this.name = value;
	}
	
	public Player withName(String value)
	{
		setName(value);
		return this;
	}
	
	public String getName( )
	{
		return this.name;
	}
	
	public boolean addToKalahaNode( )
	{
		return false;
	}
	
	public Player withKalahaNode( )
	{
		return null;
	}
	
	public Player withoutKalahaNode( )
	{
		return null;
	}
	
	public boolean removeFromKalahaNodes( )
	{
		return false;
	}
	
	public void removeAllFromKalahaNodes( )
	{
		
	}
	
	public boolean hasInKalahaNodes( )
	{
		return false;
	}
	
	public int sizeOfKalahaNodes( )
	{
		return 0;
	}
	
	public void remove( )
	{
		this.kalaha = null;
		this.kalahaNode = null;
	}
	
	
}
