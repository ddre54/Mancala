

import java.applet.Applet;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import java.util.List;

import javax.swing.JOptionPane;

public class Kalaha extends Applet
{

    @Override
	public void init()
    {
       
        {
            //this.twohumans = true;
            
            repaint();
            setSize(600, 270);
            int a = -1;
    		while(a == -1){
    			this.addPlayer(new Player().withName(JOptionPane.showInputDialog(null, "What is Player1 name?", "Enter Player1 name", JOptionPane.QUESTION_MESSAGE)));
    			this.addPlayer(new Player().withName(JOptionPane.showInputDialog(null, "What is Player2 name?", "Enter Player2 name", JOptionPane.QUESTION_MESSAGE)));
    			a = 0;
    			//a = JOptionPane.showOptionDialog(this, "Who has first turn?", "Choose Player Dialog", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[] {"Player1", "Player2", "Exit"}, 50);
    		}
    		if(a == 2){
    			System.exit(0);
    		}
    		if(a==0){
    			whofirst = 1;
    		}
    		else if(a==1){
    			whofirst = 0;
    		}
            
            int n = 4;
            kalahaboard = new KalahaNode(n + "," + n + "," + n + "," + n + "," + n + "," + n + ",0," + n + "," + n + "," + n + "," + n + "," + n + "," + n + ",0", 0, 0, whofirst);
           
                
                if(whofirst == 1)
                {
                    playerwaiting = true;
                } else
                {
                    playerwaiting = false;
                    computer();
                }
           
            depth = 3;
            
        }
    }

    public void gameOver(String winner)
    {
        this.winner = winner;
        gamestart = false;
        Graphics g = getGraphics();
        Font newfont = new Font(g.getFont().getName(), g.getFont().getStyle(), 24);
        Font.decode(g.getFont().getName());
        g.setFont(newfont);
        g.setColor(Color.black);
        g.drawImage(intboard, 0, 0, this);
        String winby = "";
        g.drawString(winner + winby, 150, 135);
    }

    public void computer()
    {
        thinking = "";
        int move = maxValue(kalahaboard, new KalahaNode(-999), new KalahaNode(999)).lastMove();
        animateMove(kalahaboard, move);
        boolean goesagain = kalahaboard.goesAgain(move);
        kalahaboard = makeMove(move, kalahaboard);
        if(kalahaboard.isTerminal(depth))
            if(kalahaboard.getValueAt(13) > kalahaboard.getValueAt(6)){
                //gameOver("Computer Wins!");
            	gameOver(this.players.get(1).getName() + " Wins!");
            }else{
	            if(kalahaboard.getValueAt(6) > kalahaboard.getValueAt(13)){
	                //gameOver("Human Wins!");
	                gameOver(this.players.get(0).getName() + " Wins!");
	            }
	            else{
	                gameOver("Tie Game");
	            }
            }
        if(goesagain)
        {
            showGoesAgain();
            kalahaboard.resetMove();
            computer();
            
        } else
        {
            kalahaboard.incrementPlayer();
            playerwaiting = true;
            kalahaboard.resetMove();
            paintStuff(getGraphics());
        }
    }

    public void showGoesAgain()
    {
        goagain = true;
        paintStuff(getGraphics());
    }

    public void pause(int x)
    {
        Date now = new Date();
        int nowTime = (int)now.getTime();
        for(int startTime = nowTime; nowTime - startTime < x; nowTime = (int)now.getTime())
            now = new Date();

    }

    public void animateMove(KalahaNode board, int tomove)
    {
        Graphics g = getGraphics();
        Font newfont = new Font(g.getFont().getName(), g.getFont().getStyle(), 24);
        Font.decode(g.getFont().getName());
        g.setFont(newfont);
        g.setColor(Color.black);
        KalahaNode b = kalahaboard.copyNode();
        Image handtouse = hand;
        if(b.getPlayer() == 1)
            handtouse = hand;
        int omancala = kalahaboard.otherMancala();
        if(tomove > 6)
            g.drawImage(handtouse, 440 - 70 * (tomove - 7), 0, this);
        else
            g.drawImage(handtouse, 84 + 70 * tomove, 143, this);
        int howmany = kalahaboard.getValueAt(tomove);
        kalahaboard.setValueAt(tomove, 0);
        pause(1000);
        showBoard(g);
        int wherenext;
        wherenext = (tomove + 1) % 14;
        int p = kalahaboard.getPlayer();
        int mancala;
        if(p == 1)
            mancala = 6;
        else
            mancala = 13;
        for(int i = 0; i < howmany; i++)
            if(wherenext == omancala)
            {
                wherenext = (wherenext + 1) % 14;
                i--;
            } else
            {

                if(wherenext <= 6 && handtouse == hand)
                    g.drawImage(handtouse, 84 + 70 * wherenext, 143, this);
                else
                if(wherenext > 6 && handtouse == hand)
                    g.drawImage(hand, 426 - 70 * (wherenext - 7), 0, this);
                else
                if(wherenext > 6 && handtouse == hand)
                    g.drawImage(handtouse, 440 - 70 * (wherenext - 7), 0, this);
                else
                if(wherenext <= 6 && handtouse == hand)
                    g.drawImage(hand, 90 + 70 * wherenext, 143, this);
                pause(400);
                kalahaboard.incrementAt(wherenext);
                showBoard(g);
                wherenext = (wherenext + 1) % 14;
            }

        int lasthole;
        if(wherenext == 0)
            lasthole = 13;
        else
            lasthole = (wherenext - 1) % 14;
        if(kalahaboard.getValueAt(lasthole) == 1 && lasthole != 13 && lasthole != 6 && kalahaboard.getValueAt(12 - lasthole) > 0 && sameSide(lasthole, mancala))
        {
            if(kalahaboard.getPlayer() == 0)
            {

            } else

            kalahaboard.setValueAt(mancala, kalahaboard.getValueAt(lasthole) + kalahaboard.getValueAt(12 - lasthole) + kalahaboard.getValueAt(mancala));
            kalahaboard.setValueAt(lasthole, 0);
            kalahaboard.setValueAt(12 - lasthole, 0);
        }
        if(kalahaboard.topClear())
        {
            for(int x = 0; x < 6; x++)
            {
                kalahaboard.setValueAt(13, kalahaboard.getValueAt(13) + kalahaboard.getValueAt(x + 7));
                kalahaboard.setValueAt(x + 7, 0);
            }

        }
        if(kalahaboard.bottomClear())
        {
            for(int y = 0; y < 6; y++)
            {
                kalahaboard.setValueAt(6, kalahaboard.getValueAt(6) + kalahaboard.getValueAt(y));
                kalahaboard.setValueAt(y, 0);
            }

        }
        if(kalahaboard.isTerminal(depth))
            
                if(kalahaboard.getValueAt(13) > kalahaboard.getValueAt(6)){
                	//Player ps2 = (Player)this.players.get(1);
                    //gameOver("Computer Wins!");
                	//gameOver(ps2.getName() + " Wins!");
                	gameOver(this.getPlayer(1).getName() + " Wins!");
                }else{
	                if(kalahaboard.getValueAt(6) > kalahaboard.getValueAt(13)){
	                	//gameOver("Human Wins!");
	                	gameOver(this.getPlayer(0).getName() + " Wins!");
	                }else{
	                    gameOver("Tie Game");
	                }
                }

        paintStuff(getGraphics());
        kalahaboard = b;
    }

    @Override
	public void paint(Graphics g)
    {
        if(introb)
        {
            boolean allLoaded = false;
            MediaTracker tracker = new MediaTracker(this);
            int trackNum = 0;
            
            tracker.addImage(intboard, trackNum++);
            try
            {
                tracker.waitForAll();
                allLoaded = tracker.isErrorAny() ^ true;
            }
            catch(InterruptedException _ex) { }
            if(!allLoaded)
            {
                
                System.exit(0);
                return;
            }
          
            tracker = new MediaTracker(this);
            trackNum = 0;
            boardimage = getImage(getCodeBase(), "mancala.gif");
            ball = getImage(getCodeBase(), "U.gif");
            ball1 = getImage(getCodeBase(), "U1.gif");
            ball2 = getImage(getCodeBase(), "U2.gif");
            ball3 = getImage(getCodeBase(), "U3.gif");
            ball4 = getImage(getCodeBase(), "U4.gif");
            ball5 = getImage(getCodeBase(), "U5.gif");
            ball6 = getImage(getCodeBase(), "U6.gif");
            ball7 = getImage(getCodeBase(), "U7.gif");
            ball8 = getImage(getCodeBase(), "U8.gif");
            ball9 = getImage(getCodeBase(), "U9.gif");
            hand = getImage(getCodeBase(), "bTop.gif");
            tracker.addImage(boardimage, trackNum++);
            tracker.addImage(ball, trackNum++);
            tracker.addImage(ball1, trackNum++);
            tracker.addImage(ball2, trackNum++);
            tracker.addImage(ball3, trackNum++);
            tracker.addImage(ball4, trackNum++);
            tracker.addImage(ball5, trackNum++);
            tracker.addImage(ball6, trackNum++);
            tracker.addImage(ball7, trackNum++);
            tracker.addImage(ball8, trackNum++);
            tracker.addImage(ball9, trackNum++);
            tracker.addImage(hand, trackNum++);
            tracker.addImage(hand, trackNum++);
            tracker.addImage(hand, trackNum++);
            tracker.addImage(hand, trackNum++);
            try
            {
                tracker.waitForAll();
                allLoaded = tracker.isErrorAny() ^ true;
            }
            catch(InterruptedException _ex) { }
            if(!allLoaded)
            {
                
                System.exit(0);
                return;
            }
            introb = false;
            gamestart = true;
        }
        paintStuff(g);
    }

    public void paintStuff(Graphics g)
    {
        if(!gamestart)
        {
            g.drawImage(intboard, 0, 0, this);
            if(!winner.equals(""))
            {
                Font newfont = new Font(g.getFont().getName(), g.getFont().getStyle(), 24);
                Font.decode(g.getFont().getName());
                g.setFont(newfont);
                g.setColor(Color.black);
                String winby = "";
                //if(winner.equals("Player1 Wins!") || winner.equals("Player One Wins!"))
                if(winner.equals(this.getPlayer(0).getName() + " Wins!"))
                    winby = " " + kalahaboard.getValueAt(6) + " to " + kalahaboard.getValueAt(13);
                else
                    winby = " " + kalahaboard.getValueAt(13) + " to " + kalahaboard.getValueAt(6);
                g.drawString(winner + winby, 150, 135);
            }
            if(play != null)
                play.reshape(300, 112, 150, 20);
            if(cporhu != null)
                cporhu.reshape(145, 112, 150, 20);
            if(level != null)
                level.reshape(115, 112, 130, 20);
            if(who != null)
                who.reshape(250, 112, 130, 20);
            if(afterl != null)
                afterl.reshape(385, 112, 100, 20);
            
        } else
        {
            showBoard(g);
        }
    }

    public void showBoard(Graphics b)
    {
        current = createImage(600, 250);
        Graphics g = current.getGraphics();
        Font newfont = new Font(g.getFont().getName(), g.getFont().getStyle(), 24);
        Font.decode(g.getFont().getName());
        g.setFont(newfont);
        g.setColor(Color.black);
        g.drawImage(boardimage, 0, 0, this);
        
            if(kalahaboard.getPlayer() == 0)
            	g.drawString(this.getPlayer(1).getName(), 100, 130);
                //g.drawString("Player2", 100, 130);
            else
            	g.drawString(this.getPlayer(0).getName(), 100, 130);
                //g.drawString("Player1", 100, 130);
        
        if(goagain )
        {
            g.drawString("Go again", 350, 130);
            
           
            goagain = false;
        }
        for(int i = 0; i < 6; i++)
        {
            int num = kalahaboard.getValueAt(i);
            int over = 87;
            if(num == 1)
                g.drawImage(ball1, over + 70 * i, 150, this);
            else
            if(num == 2)
                g.drawImage(ball2, over + 70 * i, 150, this);
            else
            if(num == 3)
                g.drawImage(ball3, over + 70 * i, 150, this);
            else
            if(num == 4)
                g.drawImage(ball4, over + 70 * i, 150, this);
            else
            if(num == 5)
                g.drawImage(ball5, over + 70 * i, 150, this);
            else
            if(num == 6)
                g.drawImage(ball6, over + 70 * i, 150, this);
            else
            if(num == 7)
                g.drawImage(ball7, over + 70 * i, 150, this);
            else
            if(num == 8)
                g.drawImage(ball8, over + 70 * i, 150, this);
            else
            if(num >= 9)
                g.drawImage(ball9, over + 70 * i, 150, this);
            String shownum = String.valueOf(num);
            if(num < 10)
                shownum = " " + num;
            g.drawString(shownum, 108 + 70 * i, 200);
        }

        for(int j = 7; j < 13; j++)
        {
            int num = kalahaboard.getValueAt(j);
            int over = 162;
            if(num == 1)
                g.drawImage(ball1, 600 - over - 70 * (j - 7), 8, this);
            else
            if(num == 2)
                g.drawImage(ball2, 600 - over - 70 * (j - 7), 8, this);
            else
            if(num == 3)
                g.drawImage(ball3, 600 - over - 70 * (j - 7), 8, this);
            else
            if(num == 4)
                g.drawImage(ball4, 600 - over - 70 * (j - 7), 8, this);
            else
            if(num == 5)
                g.drawImage(ball5, 600 - over - 70 * (j - 7), 8, this);
            else
            if(num == 6)
                g.drawImage(ball6, 600 - over - 70 * (j - 7), 8, this);
            else
            if(num == 7)
                g.drawImage(ball7, 600 - over - 70 * (j - 7), 8, this);
            else
            if(num == 8)
                g.drawImage(ball8, 600 - over - 70 * (j - 7), 8, this);
            else
            if(num >= 9)
                g.drawImage(ball9, 600 - over - 70 * (j - 7), 8, this);
            String shownum = String.valueOf(num);
            if(num < 10)
                shownum = " " + num;
            g.drawString(shownum, 456 - 70 * (j - 7), 55);
        }

        int n = 0;
        for(int m = 0; m < kalahaboard.getValueAt(6); m++)
        {
            g.drawImage(ball, 530 + n, 202 - (20 + 5 * (m % 30)), this);
            if(m % 30 == 29)
                n += 8;
        }

        n = 0;
        g.drawString(String.valueOf(kalahaboard.getValueAt(6)), 540, 135);
        for(int l = 0; l < kalahaboard.getValueAt(13); l++)
        {
            g.drawImage(ball, 29 + n, 40 + 5 * (l % 30), this);
            if(l % 30 == 29)
                n += 8;
        }

        g.drawString(String.valueOf(kalahaboard.getValueAt(13)), 39, 135);
        b.drawImage(current, 0, 0, this);
    }

    public KalahaNode minValue(KalahaNode a_node, KalahaNode alpha, KalahaNode beta)
    {
        if(a_node.isTerminal(depth))
        {
            a_node.evaluate();
            return a_node;
        }
        Vector successors = possibleMoves(a_node);
        if(successors.size() < 2)
        {
            a_node.evaluate();
            return a_node;
        }
        KalahaNode toreturn = beta;
        for(int i = 0; i < successors.size(); i++)
        {
            beta = getMin(beta, maxValue((KalahaNode)successors.elementAt(i), alpha, beta));
            if(beta.getValue() <= alpha.getValue())
            {
                i = successors.size();
                toreturn = alpha;
            }
            if(i == successors.size() - 1)
                toreturn = beta;
        }

        return toreturn;
    }

    public KalahaNode maxValue(KalahaNode a_node, KalahaNode alpha, KalahaNode beta)
    {
        if(a_node.isTerminal(depth))
        {
            a_node.evaluate();
            return a_node;
        }
        Vector successors = possibleMoves(a_node);
        if(successors.size() < 1)
        {
            a_node.evaluate();
            return a_node;
        }
        KalahaNode toreturn = alpha;
        for(int i = 0; i < successors.size(); i++)
        {
            alpha = getMax(alpha, minValue((KalahaNode)successors.elementAt(i), alpha, beta));
            if(alpha.getValue() >= beta.getValue())
            {
                i = successors.size();
                toreturn = beta;
            }
            if(i == successors.size() - 1)
                toreturn = alpha;
        }

        return toreturn;
    }

    public boolean sameSide(int lasthole, int mancala)
    {
        int start;
        int end;
        if(mancala == 6)
        {
            start = 0;
            end = 5;
        } else
        {
            start = 7;
            end = 12;
        }
        return lasthole <= end && lasthole >= start;
    }

    public KalahaNode getMax(KalahaNode alpha, KalahaNode another)
    {
        if(alpha.getValue() > another.getValue())
            return alpha;
        else
            return another;
    }

    public KalahaNode getMin(KalahaNode beta, KalahaNode another)
    {
        if(beta.getValue() < another.getValue())
            return beta;
        else
            return another;
    }

    public KalahaNode makeMove(int holenum, KalahaNode board)
    {
        KalahaNode b = board.copyNode();
        int p = b.getPlayer();
        int mancala;
        if(p == 1)
            mancala = 6;
        else
            mancala = 13;
        int wherenext = (holenum + 1) % 14;
        int omancala = b.otherMancala();
        int stones = b.getValueAt(holenum);
        b.setValueAt(holenum, 0);
        for(int i = 0; i < stones; i++)
            if(wherenext == omancala)
            {
                wherenext = (wherenext + 1) % 14;
                i--;
            } else
            {
                b.incrementAt(wherenext);
                wherenext = (wherenext + 1) % 14;
            }

        int lasthole;
        if(wherenext == 0)
            lasthole = 13;
        else
            lasthole = (wherenext - 1) % 14;
        boolean lastempty = b.emptyAt(lasthole);
        if(b.getValueAt(lasthole) == 1 && lasthole != 13 && lasthole != 6 && b.getValueAt(12 - lasthole) > 0 && sameSide(lasthole, mancala))
        {
            b.setValueAt(mancala, b.getValueAt(lasthole) + b.getValueAt(12 - lasthole) + b.getValueAt(mancala));
            b.setValueAt(lasthole, 0);
            b.setValueAt(12 - lasthole, 0);
        }
        if(b.topClear())
        {
            for(int x = 0; x < 6; x++)
            {
                b.setValueAt(13, b.getValueAt(13) + b.getValueAt(x + 7));
                b.setValueAt(x + 7, 0);
            }

        }
        if(b.bottomClear())
        {
            for(int y = 0; y < 6; y++)
            {
                b.setValueAt(6, b.getValueAt(6) + b.getValueAt(y));
                b.setValueAt(y, 0);
            }

        }
        b.addMove(holenum);
        return b;
    }

    public Vector mergeVectors(Vector a, Vector b)
    {
        for(int va = 0; va < a.size(); va++)
            b.addElement(a.elementAt(va));

        return b;
    }

    public Vector assembleNodes(int holenum, KalahaNode a_node)
    {
        thinkCounter++;
        if(thinkCounter % 50 == 0)
        {
            thinking = thinking + ".";
            Graphics g = getGraphics();
            Font newfont = new Font(g.getFont().getName(), g.getFont().getStyle(), 24);
            Font.decode(g.getFont().getName());
            g.setFont(newfont);
            g.setColor(Color.black);
            g.drawString(thinking, 100, 145);
            thinkCounter = 0;
        }
        Vector blist = new Vector();
        if(a_node.moveLength() >= depth)
            return blist;
        if(a_node.goesAgain(holenum))
            blist = mergeVectors(blist, possibleMoves(makeMove(holenum, a_node.copyNode())));
        else
            blist.addElement(makeMove(holenum, a_node.copyNode()));
        for(int i = 0; i < blist.size(); i++)
        {
            ((KalahaNode)blist.elementAt(i)).incrementDepth();
            ((KalahaNode)blist.elementAt(i)).incrementPlayer();
        }

        return blist;
    }

    public Vector possibleMoves(KalahaNode a_node)
    {
        Vector movelist = new Vector();
        int pstart = Math.abs(7 * (1 - a_node.getPlayer()));
        for(int i = 0; i < 6; i++)
            if(!a_node.emptyAt(pstart + i))
                movelist = mergeVectors(assembleNodes(pstart + i, a_node.copyNode()), movelist);

        return movelist;
    }

    @Override
	public boolean action(Event e, Object o)
    {
        if(e.target == cporhu)
            type = o.toString();
        else
        if(e.target == play)
        {
           
                removeAll();
                level = new Choice();
                level.addItem("--");
                add(level);
                who = new Choice();
                who.addItem("You Go First");
                //who.addItem("Player2 Goes First");
                who.addItem(this.getPlayer(0).getName() + " Goes First");
                add(who);
                afterl = new Button("Play");
                add(afterl);

        } else

                depth = 1;

        if(e.target == who)
        {
            //if(o.equals("Player2 Goes First"))
        	if(o.equals(this.getPlayer(0).getName() + " Goes First"))
                whofirst = 0;
        } else
        if(e.target == afterl)
        {
            removeAll();
            gamestart = true;
            if(whofirst == 1)
            {
                kalahaboard = new KalahaNode("6,6,6,6,6,6,0,6,6,6,6,6,6,0", 0, 0, 1);
                //twohumans = false;
                playerwaiting = true;
            } else
            {
                kalahaboard = new KalahaNode("6,6,6,6,6,6,0,6,6,6,6,6,6,0", 0, 0, 0);
                //twohumans = false;
                playerwaiting = false;
                paintStuff(getGraphics());
                computer();
            }
        } else
        
        paintStuff(getGraphics());
        return true;
    }

    @Override
	public boolean mouseDown(Event e, int x, int y)
    {
        if(!gamestart)
            return true;
        if(playagain)
            try
            {
                getAppletContext().showDocument(new URL(getDocumentBase(), "mancala.html"));
            }
            catch(Exception _ex) { }
        if(!twohumans)
        {
            boolean yesorno = false;
            int hole = 0;
            if(y > 150 && y < 250 && x > 90 && x < 160)
            {
                hole = 0;
                yesorno = true;
            } else
            if(y > 150 && y < 250 && x > 161 && x < 230)
            {
                hole = 1;
                yesorno = true;
            } else
            if(y > 150 && y < 250 && x > 231 && x < 300)
            {
                hole = 2;
                yesorno = true;
            } else
            if(y > 150 && y < 250 && x > 301 && x < 370)
            {
                hole = 3;
                yesorno = true;
            } else
            if(y > 150 && y < 250 && x > 371 && x < 420)
            {
                hole = 4;
                yesorno = true;
            } else
            if(y > 150 && y < 250 && x > 421 && x < 510)
            {
                hole = 5;
                yesorno = true;
            }
            if(yesorno && !kalahaboard.emptyAt(hole))
            {
                animateMove(kalahaboard, hole);
                boolean goesagain = kalahaboard.goesAgain(hole);
                kalahaboard = makeMove(hole, kalahaboard);
                if(goesagain)
                {
                    paintStuff(getGraphics());
                    showGoesAgain();
                    kalahaboard.resetMove();
                    playerwaiting = true;
                } else
                {
                    kalahaboard.incrementPlayer();
                    playerwaiting = false;
                    kalahaboard.resetMove();
                    paintStuff(getGraphics());
                    computer();
                }
            } else
            {
                Toolkit.getDefaultToolkit().beep();
            }
        } else
        if(playerwaiting)
        {
            boolean oneyesorno = false;
            int hole = 0;
            if(y > 150 && y < 250 && x > 90 && x < 160)
            {
                hole = 0;
                oneyesorno = true;
            } else
            if(y > 150 && y < 250 && x > 161 && x < 230)
            {
                hole = 1;
                oneyesorno = true;
            } else
            if(y > 150 && y < 250 && x > 231 && x < 300)
            {
                hole = 2;
                oneyesorno = true;
            } else
            if(y > 150 && y < 250 && x > 301 && x < 370)
            {
                hole = 3;
                oneyesorno = true;
            } else
            if(y > 150 && y < 250 && x > 371 && x < 420)
            {
                hole = 4;
                oneyesorno = true;
            } else
            if(y > 150 && y < 250 && x > 421 && x < 510)
            {
                hole = 5;
                oneyesorno = true;
            }
            if(oneyesorno && !kalahaboard.emptyAt(hole))
            {
                animateMove(kalahaboard, hole);
                boolean goesagain = kalahaboard.goesAgain(hole);
                kalahaboard = makeMove(hole, kalahaboard);
                if(goesagain)
                {
                    showGoesAgain();
                    kalahaboard.resetMove();
                    playerwaiting = true;
                    computerwaiting = false;
                } else
                {
                    kalahaboard.incrementPlayer();
                    playerwaiting = false;
                    computerwaiting = true;
                    kalahaboard.resetMove();
                    paintStuff(getGraphics());
                }
            } else
            {
                Toolkit.getDefaultToolkit().beep();
            }
        } else
        if(computerwaiting)
        {
            boolean twoyesorno = false;
            int hole = 0;
            if(y > 0 && y < 100 && x > 90 && x < 160)
            {
                hole = 12;
                twoyesorno = true;
            } else
            if(y > 0 && y < 100 && x > 161 && x < 230)
            {
                hole = 11;
                twoyesorno = true;
            } else
            if(y > 0 && y < 100 && x > 231 && x < 300)
            {
                hole = 10;
                twoyesorno = true;
            } else
            if(y > 0 && y < 100 && x > 301 && x < 370)
            {
                hole = 9;
                twoyesorno = true;
            } else
            if(y > 0 && y < 100 && x > 371 && x < 420)
            {
                hole = 8;
                twoyesorno = true;
            } else
            if(y > 0 && y < 100 && x > 421 && x < 510)
            {
                hole = 7;
                twoyesorno = true;
            }
            if(twoyesorno && !kalahaboard.emptyAt(hole))
            {
                animateMove(kalahaboard, hole);
                boolean goesagain = kalahaboard.goesAgain(hole);
                kalahaboard = makeMove(hole, kalahaboard);
                if(goesagain)
                {
                    showGoesAgain();
                    kalahaboard.resetMove();
                    computerwaiting = true;
                    playerwaiting = false;
                } else
                {
                    kalahaboard.incrementPlayer();
                    computerwaiting = false;
                    playerwaiting = true;
                    kalahaboard.resetMove();
                    paintStuff(getGraphics());
                }
            } else
            {
                Toolkit.getDefaultToolkit().beep();
            }
        }
        return true;
    }

    public Kalaha()
    {
        depth = 1;
        whofirst = 1;
        type = "Player 1 Vs. Player2";
        thinkCounter = 0;
        thinking = "";
        winner = "";
        //playagain = false;
        introb = true;
        
        this.twohumans = true;
        this.playagain = false;
        
    }

    public static final int black = 1;
    public static final int BLACK = 0;
    private int depth;
    private int whofirst;
    private boolean twohumans;
    private boolean playerwaiting;
    private boolean computerwaiting;
    private boolean gamestart;
    private boolean goagain;
    private Image boardimage;
    private Image ball;
    private Image ball1;
    private Image ball2;
    private Image ball3;
    private Image ball4;
    private Image ball5;
    private Image ball6;
    private Image ball7;
    private Image ball8;
    private Image ball9;
    private Image intboard;
    private Image hand;
    private Image current;
    private Choice cporhu;
    private Choice level;
    private Choice who;
    private Button play;
    private Button afterl;
    private String type;
    private int thinkCounter;
    private String thinking;
    private String winner;
    private boolean playagain;
    private boolean introb;
    private KalahaNode kalahaboard;
	
	private List<Player> players;
	
	public boolean addPlayer(Player value){
		boolean changed = false;
		if(value != null){
			if(this.players == null){
				this.players = new ArrayList<Player>();
			}
			changed = this.players.add(value);
			
			if(changed){
				value.setKalaha(this);
			}
		}
		return changed;
	}
	
	public Player getPlayer(int index){
		Player value = this.players.get(index);
		if(value == null ){
			value = new Player().withName("Player" + index);
		}
		return value;
	}
	
}
