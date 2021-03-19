import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.DoubleBuffer;
import java.util.ArrayList;

public class BlockusGame extends JFrame implements MouseMotionListener,MouseListener, MouseWheelListener, Runnable{

    JPanel panel = new JPanel();
    Player p1;
    Player p2;

    int turn=0;
    int BLUE=0;
    int GREEN=1;

    int curColour=0;
    int blockWidth = 500 / 14;
    int blockHeight = 500 / 14;
    double mouseX;
    double mouseY;
    boolean blueDragging=false;
    boolean greenDragging=false;

    int currentBShape=1;
    int currentGShape=1;
    BufferedImage buffer = new BufferedImage(1000,750,BufferedImage.TYPE_4BYTE_ABGR);
    BufferedImage pass;
    JDialog dialog = new JDialog(this,"Results",true);

    public ArrayList<Point>BBPieces=new ArrayList<>();
    public ArrayList<Point>GBPieces=new ArrayList<>();

    ArrayList<Point>holderForDroppedPiece=new ArrayList<>();

    public final int BLUESQUARE=3;
    public final int GREENSQUARE=1;
    public final int EMPTYSQUARE=2;
    public final int PREVIEWSQUARE=4;

    int[][]board=new int[14][14];

    int gamestate=0;
    public final int FIRSTURN=0;
    public final int SECONDTURN=1;
    public final int PLAYING=2;
    public final int WINNER=3;

    int passCount=0;
    WinnerPanel wp=new WinnerPanel(1,0);

    public BlockusGame() {
        super("Blokus Duo");
        setSize(1000, 750);
        p1 = new Player();
        p2=new Player();
        add(panel);
        setUndecorated(true);
        addMouseListener(this);
        addMouseWheelListener(this);
        addMouseMotionListener(this);
        Thread t=new Thread(this);
        dialog.setSize(new Dimension(400, 400));
        dialog.setLocationRelativeTo(this);
        try{
            pass= ImageIO.read(new File("C:\\Users\\othscs121\\Dropbox\\compsci2\\Blokus\\src\\pass.png"));
        }
        catch(IOException e){}
        t.start();
        setVisible(true);

    }

    public void paint(Graphics g) {
        if(gamestate!=WINNER) {
            Graphics bg = buffer.getGraphics();
            bg.setFont(new Font("Times New Roman", Font.BOLD, 16));
            bg.setColor(Color.GRAY);
            bg.fillRect(0, 0, getWidth(), getHeight());
            bg.setColor(Color.BLACK);
            bg.fillRect(245, 0, 500, 495);
            //bluebox
            bg.drawRect(0, 495, 500, 500);
            //greenbox
            bg.drawRect(505, 495, 500, 500);

            bg.fillRect(500, 495, 5, 250);
            bg.setColor(new Color(139, 69, 19));
            bg.fillRect(250, 0, 490, 490);
            bg.setColor(Color.BLACK);
            for (int i = 0; i <= 12; i++) {
                Point p = calCoord(new Point(i + 1, 0));
                bg.fillRect((int) p.getX(), (int) p.getY(), 2, 14 * (int) blockHeight);
                Point pp = calCoord(new Point(0, i + 1));
                bg.fillRect((int) pp.getX(), (int) pp.getY(), 14 * (int) blockHeight, 2);

            }
            bg.setColor(Color.GRAY);
            bg.drawOval(255 + ((int) blockWidth * 4), 5 + (int) blockHeight * 4, (int) blockWidth - 10, (int) blockHeight - 10);
            bg.drawOval(255 + ((int) blockWidth * 9), 5 + (int) blockHeight * 9, (int) blockWidth - 10, (int) blockHeight - 10);
            bg.setColor(Color.BLUE);
            bg.drawString("Blue Side", 50, 25);
            for (int i = 0; i < p1.shapes.shapes.get(currentBShape).size(); i++) {
                //int h=p1.shapes.getHeight(currentBShape);
                //int w=p1.shapes.getWidth(currentBShape);
                Point p = calCoordForBlueBox(p1.shapes.shapes.get(currentBShape).get(i));
                bg.fillRect((int) p.getX(), (int) p.getY(), 28, 28);

            }

            bg.setColor(Color.GREEN);
            bg.drawString("Green Side", 505 + 150 + 10 + 150, 25);
            for (int i = 0; i < p2.shapes.shapes.get(currentGShape).size(); i++) {

                Point p = calCoordForGreenBox(p2.shapes.shapes.get(currentGShape).get(i));
                bg.fillRect((int) p.getX(), (int) p.getY(), 28, 28);
            }
            //System.out.println(mouseX+" "+mouseY);
            if (blueDragging && turn%2==0) {
                if (curColour != 3)
                    passCount = 0;

                curColour = 3;
                holderForDroppedPiece = new ArrayList<>();
                bg.setColor(Color.BLUE);
                ArrayList<Point> curShape = p1.shapes.shapes.get(currentBShape);
                double x = mouseX + curShape.get(0).getX() * blockWidth;
                double y = mouseY - curShape.get(0).getY() * blockHeight;
                Point p = calPointForBoard();
                try {
                    holderForDroppedPiece.add(new Point((int) p.getX() + (int) curShape.get(0).getX(), (int) p.getY() - (int) curShape.get(0).getY()));
                    if (board[(int) p.getX() + (int) curShape.get(0).getX()][(int) p.getY() - (int) curShape.get(0).getY()] == 0 || board[(int) p.getX() + (int) curShape.get(0).getX()][(int) p.getY() - (int) curShape.get(0).getY()] == 2)
                        board[(int) p.getX() + (int) curShape.get(0).getX()][(int) p.getY() - (int) curShape.get(0).getY()] = 4;
                } catch (Exception e) {
                }
                for (int i = 0; i < curShape.size(); i++) {


                    bg.fillRect((int) x, (int) y, (int) blockWidth, (int) blockHeight);
                    try {
                        x = mouseX + ((curShape.get(i + 1).getX()) * blockWidth);
                        y = mouseY - ((curShape.get(i + 1).getY()) * blockHeight);
                        holderForDroppedPiece.add(new Point((int) p.getX() + (int) (curShape.get(i + 1).getX()), (int) p.getY() - (int) (curShape.get(i + 1).getY())));
                        if (board[(int) p.getX() + (int) (curShape.get(i + 1).getX())][(int) p.getY() - (int) (curShape.get(i + 1).getY())] == 2 || board[(int) p.getX() + (int) (curShape.get(i + 1).getX())][(int) p.getY() - (int) (curShape.get(i + 1).getY())] == 0)
                            board[(int) p.getX() + (int) (curShape.get(i + 1).getX())][(int) p.getY() - (int) (curShape.get(i + 1).getY())] = 4;
                    } catch (Exception e) {
                    }
                }


            }
            if (greenDragging && turn%2!=0) {
                if (curColour != 1)
                    passCount = 0;
                curColour = 1;
                holderForDroppedPiece = new ArrayList<>();
                bg.setColor(Color.GREEN);
                ArrayList<Point> curShape = p2.shapes.shapes.get(currentGShape);
                double x = mouseX + curShape.get(0).getX() * blockWidth;
                double y = mouseY - curShape.get(0).getY() * blockHeight;
                Point p = calPointForBoard();
                try {
                    holderForDroppedPiece.add(new Point((int) p.getX() + (int) curShape.get(0).getX(), (int) p.getY() - (int) curShape.get(0).getY()));
                    if (board[(int) p.getX() + (int) curShape.get(0).getX()][(int) p.getY() - (int) curShape.get(0).getY()] == 0 || board[(int) p.getX() + (int) curShape.get(0).getX()][(int) p.getY() - (int) curShape.get(0).getY()] == 2)
                        board[(int) p.getX() + (int) curShape.get(0).getX()][(int) p.getY() - (int) curShape.get(0).getY()] = 4;
                } catch (Exception e) {
                }
                for (int i = 0; i < curShape.size(); i++) {


                    bg.fillRect((int) x, (int) y, (int) blockWidth, (int) blockHeight);

                    try {
                        x = mouseX + ((curShape.get(i + 1).getX()) * blockWidth);
                        y = mouseY - ((curShape.get(i + 1).getY()) * blockHeight);
                        holderForDroppedPiece.add(new Point((int) p.getX() + (int) (curShape.get(i + 1).getX()), (int) p.getY() - (int) (curShape.get(i + 1).getY())));
                        if (board[(int) p.getX() + (int) (curShape.get(i + 1).getX())][(int) p.getY() - (int) (curShape.get(i + 1).getY())] == 2 || board[(int) p.getX() + (int) (curShape.get(i + 1).getX())][(int) p.getY() - (int) (curShape.get(i + 1).getY())] == 0)
                            board[(int) p.getX() + (int) (curShape.get(i + 1).getX())][(int) p.getY() - (int) (curShape.get(i + 1).getY())] = 4;
                    } catch (Exception e) {
                    }

                }

            }

            bg.drawImage(pass, 50, 300, null);
            bg.drawImage(pass, 800, 300, null);

            for (int r = 0; r < board.length; r++) {
                for (int c = 0; c < board[0].length; c++) {
                    if (board[r][c] == BLUESQUARE) {
                        bg.setColor(Color.BLUE);
                        bg.fillRect((int) (r * blockWidth) + 252, (int) (c * blockHeight) + 2, (int) blockWidth - 2, (int) blockHeight - 2);
                    }
                    if (board[r][c] == GREENSQUARE) {
                        bg.setColor(Color.GREEN);
                        bg.fillRect((int) (r * blockWidth) + 252, (int) (c * blockHeight) + 2, (int) blockWidth - 2, (int) blockHeight - 2);
                    }
                    if (board[r][c] == PREVIEWSQUARE) {
                        bg.setColor(Color.RED);
                        bg.fillRect((int) (r * blockWidth) + 252, (int) (c * blockHeight) + 2, (int) blockWidth - 2, (int) blockHeight - 2);
                        board[r][c] = 0;
                    }
                }
            }


            g.drawImage(buffer, 0, 0, null);
        }

    }

    public Point calCoord(Point p) {
        return new Point( ((int)(p.getX() * blockWidth)) + 250, ((int)( p.getY() * blockHeight)));
    }
    public Point calCoordForBlueBox(Point p){

        return new Point((int)p.getX()*28+165,-(int)p.getY()*28+635);
    }
    public Point calCoordForGreenBox(Point p){
        return new Point((int)p.getX()*28+825,-(int)p.getY()*28+635);
    }
    public Point calPointForBoard(){
        return new Point(((int)mouseX-245)/blockWidth,(int)mouseY/blockHeight);
    }
    public boolean isValidMove(ArrayList<Point>shape){
        boolean isValid=true;
        boolean diagonalCheck=false;
        for(int i=0; i<shape.size();i++){

            int c=(int)shape.get(i).getY();
            int r=(int)shape.get(i).getX();

            boolean firstTurnCheck=false;
            if(gamestate==FIRSTURN && curColour==BLUESQUARE){
                for(int ii=0; ii<shape.size();ii++)
                    if(shape.get(ii).getX()==4 && shape.get(ii).getY()==4 )
                        firstTurnCheck=true;

                    return firstTurnCheck;
            }
            /*boolean secondTurnCheck=false;
            if(gamestate==SECONDTURN && curColour==GREENSQUARE){
                for(int ii=0; ii<shape.size();ii++)
                    if(shape.get(ii).getX()==9 && shape.get(ii).getY()==9 )
                        secondTurnCheck=true;

                return secondTurnCheck;
            }*/

            if(board[r][c]!=0 )
                isValid=false;


            if(r!=13 && board[r+1][c]==curColour)
                isValid=false;
            if(r!=0 && board[r-1][c]==curColour)
                isValid=false;
            if(c!=13 && board[r][c+1]==curColour)
                isValid=false;
            if(c!=0 && board[r][c-1]==curColour)
                isValid=false;


            for(int t=0;t<shape.size();t++) {
                 if(r!=13 && c!=13 && board[r + 1][c + 1] == curColour )
                     diagonalCheck = true;
                 if(r!=0 && c!=0 &&board[r - 1][c - 1] == curColour)
                     diagonalCheck = true;
                 if(r!=0 && c!=13 && board[r - 1][c + 1] == curColour)
                     diagonalCheck = true;
                 if(r!=13 && c!=0 && board[r + 1][c - 1] == curColour )
                     diagonalCheck = true;
                 if(gamestate == FIRSTURN || gamestate == SECONDTURN)
                    diagonalCheck = true;

            }

        }
        if(diagonalCheck && isValid){
            if(gamestate==SECONDTURN)
                gamestate=PLAYING;
            if(gamestate==FIRSTURN)
                gamestate=SECONDTURN;


        return true;
        }
        else
            return false;
    }
    public void tryNewAdd(){
        boolean hasDropped=true;
        ArrayList<Point>boardPlace=new ArrayList<>();

        for(int i=0; i<holderForDroppedPiece.size(); i++){
            if(  board[(int)holderForDroppedPiece.get(i).getX()][(int)holderForDroppedPiece.get(i).getY()]==2 ||   board[(int)holderForDroppedPiece.get(i).getX()][(int)holderForDroppedPiece.get(i).getY()]==0)
                boardPlace.add(new Point((int)holderForDroppedPiece.get(i).getX(),(int)holderForDroppedPiece.get(i).getY()));
        }
        boolean isValid=isValidMove(boardPlace);

        if(isValid) {
            for (int i = 0; i < holderForDroppedPiece.size(); i++) {
               //System.out.println( board[(int) holderForDroppedPiece.get(i).getX()][(int) holderForDroppedPiece.get(i).getY()]);
                if (board[(int) holderForDroppedPiece.get(i).getX()][(int) holderForDroppedPiece.get(i).getY()] == 2 || board[(int) holderForDroppedPiece.get(i).getX()][(int) holderForDroppedPiece.get(i).getY()] == 0)
                    board[(int) holderForDroppedPiece.get(i).getX()][(int) holderForDroppedPiece.get(i).getY()] = curColour;
                else
                    hasDropped = false;
                //System.out.println( board[(int) holderForDroppedPiece.get(i).getX()][(int) holderForDroppedPiece.get(i).getY()]);
            }
        }
        if(hasDropped && isValid)
        {turn++;
            if(curColour==3){
                p1.shapes.shapes.remove(currentBShape);
                if(currentBShape!=0)
                currentBShape--;
                else
                    currentBShape=p1.shapes.shapes.size()-2;
            }
            if(curColour==1) {
                p2.shapes.shapes.remove(currentGShape);
                if(currentGShape!=0)
                currentGShape--;
                else
                    currentGShape=p2.shapes.shapes.size()-2;
            }
        }

    }
    public void mouseExited(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) { blueDragging=false;greenDragging=false;tryNewAdd();
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }
    public void mouseDragged(MouseEvent e){
        if(e.getX()<500 && e.getX()>0 && e.getY()<750 && e.getY()>495)
            blueDragging=true;
        if(e.getX()>500 && e.getX()<1000 && e.getY()<750 && e.getY()>495)
            greenDragging=true;





        mouseX=e.getX();
        mouseY=e.getY();


    }
    public void mouseMoved(MouseEvent e){

                mouseX=e.getX();
                mouseY=e.getY();
    }
    public void mouseClicked(MouseEvent e) {
        if(e.getX()<500 && e.getX()>0 && e.getY()<750 && e.getY()>495){
            if(e.getButton()==e.BUTTON3)
                p1.shapes.rotate(currentBShape);
            if(e.getButton()==e.BUTTON2)
                p1.shapes.flip(currentBShape);}
        if(e.getX()>500 && e.getX()<1000 && e.getY()<750 && e.getY()>495) {
            if (e.getButton() == e.BUTTON3)
                p2.shapes.rotate(currentGShape);
            if(e.getButton()==e.BUTTON2)
                p2.shapes.flip(currentGShape);
        }
        if(e.getX()<150 && e.getX()>50 && e.getY()>300 && e.getY()<350){

        curColour=GREENSQUARE;
            passCount++;
        turn++;}
        if(e.getX()<900 && e.getX()>800 && e.getY()>300 && e.getY()<350) {
            passCount++;
            curColour=BLUESQUARE;turn++;
        }
        if(gamestate==WINNER)
            gamestate=FIRSTURN;

    }
    public void mouseWheelMoved(MouseWheelEvent e){
        int t=e.getWheelRotation();
        if(e.getX()<500&& e.getX()>0 && e.getY()<750 && e.getY()>500)
            currentBShape+=t;
            if(currentBShape>=p1.shapes.shapes.size()-1)
                currentBShape-=p1.shapes.shapes.size()-1;
                if(currentBShape<0)
                    currentBShape+=p1.shapes.shapes.size()-1;
        if(e.getX()<1000 && e.getX()>500 && e.getY()<750 && e.getY()>500)
            currentGShape+=t;
        if(currentGShape>=p2.shapes.shapes.size()-1)
            currentGShape-=p2.shapes.shapes.size()-1;
        if(currentGShape<0)
            currentGShape+=p2.shapes.shapes.size()-1;
    }
    public void run() {

        while(true){
            repaint();
            update();
            if(passCount>=2){
                gamestate=WINNER;

            }

            try{
                Thread.sleep(1000/60);
            }catch(Exception e){}
        }
    }
    public void reset(){
        board=new int[14][14];
        passCount=0;
        p1=new Player();
        p2=new Player();
        currentGShape=1;
        currentBShape=1;
    }
    public int countGScore(){
        int score=0;
        for(int r=0;r<board.length;r++)
            for(int c=0;c<board[0].length;c++)
                if(board[r][c]==GREENSQUARE)
                    score++;
        if(p2.shapes.shapes.size()==0)
            score+=15;

        score=score-p2.shapes.shapes.size();

        return score;

    }
    public int countBScore(){
        int score=0;
        for(int r=0;r<board.length;r++)
            for(int c=0;c<board[0].length;c++)
                if(board[r][c]==BLUESQUARE)
                    score++;
        if(p1.shapes.shapes.size()==0)
            score+=15;

        score=score-p1.shapes.shapes.size();

        return score;
    }
    public void update(){

        if(gamestate==WINNER){
            int bs=countBScore();
            //int gs=countGScore();

            if(bs>countGScore())
            dialog.add(new JLabel("Blue Wins with "+bs+" points!"));
            else if(bs==countGScore())
                dialog.add(new JLabel("Game tied at "+bs+" points"));
            else
                dialog.add(new JLabel("Green Wins with "+countGScore()+" points!"));
            dialog.setVisible(true);
            this.add(dialog);
            reset();
        }
        if(gamestate!=WINNER) {
            this.remove(dialog);
        }
    }

    public static void main(String[] args) {
        BlockusGame g = new BlockusGame();
    }
}