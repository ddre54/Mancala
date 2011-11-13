
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class KalahaNode
{

    public KalahaNode(Vector config, int value, int depth, Vector move, int player)
    {
        this.config = config;
        this.move = move;
        this.value = value;
        this.depth = depth;
        this.player = player;
    }

    public KalahaNode(String config, int value, int depth, int player)
    {
        this.config = getVals(config);
        move = new Vector();
        this.value = value;
        this.depth = depth;
        this.player = player;
    }

    public KalahaNode(int value)
    {
        this(new Vector(), value, 0, new Vector(), 0);
    }
    
    public KalahaNode(){
    	this.players = new ArrayList<Player>();
    	this.stones = new ArrayList<Stone>();
    }

    public Vector getConfig()
    {
        return config;
    }

    public Vector getMove()
    {
        return move;
    }

    public int moveLength()
    {
        return move.size();
    }

    public int getValue()
    {
        return value;
    }

    public int getDepth()
    {
        return depth;
    }

    public void incrementDepth()
    {
        depth++;
    }

    public void incrementPlayer()
    {
        if(player == 1)
            player = 0;
        else
            player = 1;
    }

    public int getPlayer()
    {
        return player;
    }

    public void setConfig(Vector config)
    {
        this.config = config;
    }

    public void addMove(int nextmove)
    {
        move.addElement(String.valueOf(nextmove));
    }

    public void resetMove()
    {
        move = new Vector();
    }

    public void setConfig(String config)
    {
        this.config = getVals(config);
    }

    public void setValue(int value)
    {
        this.value = value;
    }

    public void setDepth(int depth)
    {
        this.depth = depth;
    }

    public void setPlayer(int player)
    {
        this.player = player;
    }

    public void setValueAt(int where, int value)
    {
        config.setElementAt(String.valueOf(value), where);
    }

    public int getValueAt(int where)
    {
        return Integer.parseInt((String)config.elementAt(where));
    }

    public boolean emptyAt(int where)
    {
        return getValueAt(where) == 0;
    }

    public void incrementAt(int where)
    {
        config.setElementAt(String.valueOf(getValueAt(where) + 1), where);
    }

    public int otherMancala()
    {
        return player != 1 ? 6 : 13;
    }

    public int lastMove()
    {
        if(move.size() < 1)
        {
            int pstart = Math.abs(7 * (1 - getPlayer()));
            for(int i = 0; i < 6; i++)
                if(!emptyAt(pstart + i))
                    return pstart + i;

        } else
        {
            return Integer.parseInt((String)move.elementAt(0));
        }
        return 12;
    }

    public boolean isTerminal(int maxdepth)
    {
        return depth - 1 >= maxdepth || topClear() || bottomClear();
    }

    public void evaluate()
    {
        value = Integer.parseInt((String)config.elementAt(13)) - Integer.parseInt((String)config.elementAt(6));
    }

    public KalahaNode copyNode()
    {
        Vector newconfig = new Vector();
        for(int i = 0; i < config.size(); i++)
            newconfig.addElement(config.elementAt(i));

        Vector newmove = new Vector();
        for(int j = 0; j < move.size(); j++)
            newmove.addElement(move.elementAt(j));

        return new KalahaNode(newconfig, value, depth, newmove, player);
    }

    public int across(int hole)
    {
        return Integer.parseInt((String)config.elementAt(12 - hole));
    }

    public boolean goesAgain(int hole)
    {
        int mancala = 13;
        if(player == 1)
            mancala = 6;
        int wherenext = (hole + 1) % 14;
        int omancala = otherMancala();
        for(int i = 0; i < getValueAt(hole); i++)
            if(wherenext == omancala)
            {
                wherenext = (wherenext + 1) % 14;
                i--;
            } else
            {
                wherenext = (wherenext + 1) % 14;
            }

        int lasthole;
        if(wherenext == 0)
            lasthole = 13;
        else
            lasthole = (wherenext - 1) % 14;
        return lasthole == mancala;
    }

    private Vector getVals(String s)
    {
        int p = s.indexOf(',');
        int q = s.indexOf(',', p + 1);
        Vector i_v = new Vector();
        boolean done = false;
        i_v.addElement(s.substring(0, p));
        while(!done) 
        {
            i_v.addElement(s.substring(p + 1, q));
            p = q;
            q = s.indexOf(',', p + 1);
            if(q < 0)
                done = true;
        }
        i_v.addElement(s.substring(p + 1));
        return i_v;
    }

    public boolean topClear()
    {
        return Integer.parseInt((String)config.elementAt(0)) == 0 && Integer.parseInt((String)config.elementAt(1)) == 0 && Integer.parseInt((String)config.elementAt(2)) == 0 && Integer.parseInt((String)config.elementAt(3)) == 0 && Integer.parseInt((String)config.elementAt(4)) == 0 && Integer.parseInt((String)config.elementAt(5)) == 0;
    }

    public boolean bottomClear()
    {
        return Integer.parseInt((String)config.elementAt(7)) == 0 && Integer.parseInt((String)config.elementAt(8)) == 0 && Integer.parseInt((String)config.elementAt(9)) == 0 && Integer.parseInt((String)config.elementAt(10)) == 0 && Integer.parseInt((String)config.elementAt(11)) == 0 && Integer.parseInt((String)config.elementAt(12)) == 0;
    }

    private Vector config;
    private Vector move;
    private int value;
    private int depth;
    private int player;
	
	private List<Player> players;
	private List<Stone> stones;
	
	
	public boolean removeFromStones (Stone value)
	   {
	      boolean changed = false;
	      if ((this.stones != null) && (value != null))
	      {
	         changed = this.stones.remove (value);
	         if (changed)
	         {
	            value.setKalahaNode(null);
	         }
	      }
	      return changed;
	   }
	
	public boolean addToStones (Stone value)
	   {
	      boolean changed = false;
	      if (value != null)
	      {
	         if (this.stones == null)
	         {
	            this.stones = new ArrayList<Stone> ();
	         }
	      
	         changed = this.stones.add (value);
	         if (changed)
	         {
	            value.setKalahaNode(this);
	         }
	      }
	      return changed;
	   }
}