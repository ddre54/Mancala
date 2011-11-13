/**
 * @(#) Stone.java
 */

public class Stone
{
	private KalahaNode kalahaNode;
	
	public boolean setKalahaNode(KalahaNode value){
		boolean changed = false;

	      if (this.kalahaNode != value)
	      {
	      
	    	  KalahaNode oldValue = this.kalahaNode;
	         Stone source = this;
	         if (this.kalahaNode != null)
	         {
	            this.kalahaNode = null;
	            oldValue.removeFromStones (this);
	         }
	         this.kalahaNode = value;

	         if (value != null)
	         {
	            value.addToStones (this);
	         }
	         changed = true;
	      
	      }
	      return changed;
	}
	
	public Stone withKalahaNode(KalahaNode value)
	{
		setKalahaNode(value);
		return this;
	}
	
	public KalahaNode getKalahaNode( )
	{
		return this.kalahaNode;
	}
	
	public void remove( )
	{
		this.setKalahaNode(null);
	}
}
