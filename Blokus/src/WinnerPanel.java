import javax.swing.*;
import java.awt.*;

public class WinnerPanel extends JPanel {

    public int gScore;
    public int bScore;

    public WinnerPanel(int gScore,int bScore){
        this.gScore=gScore;
        this.bScore=bScore;
        setSize(500,500);
        setVisible(true);
    }
    public void paint(Graphics g){
        g.setFont(new Font("Times New Roman", Font.BOLD, 16));
        g.setColor(Color.BLACK);
        g.fillRect(0,0,this.getWidth(),this.getHeight());
        g.setColor(Color.BLUE);
        g.drawString("BLUE",115,20);
        g.drawString("Score: "+bScore,120,40);

        g.setColor(Color.GREEN);
        g.drawString("GREEN",365,20);
        g.drawString("Score"+gScore,370,40);

        if(gScore>bScore){
            g.setColor(Color.GREEN);
            g.drawString("Green Wins!",245,300);
        }
        else if(bScore>gScore){
            g.setColor(Color.BLUE);
            g.drawString("Blue Wins!",245,300);
        }
    }

    public int getgScore() {
        return gScore;
    }

    public void setgScore(int gScore) {
        this.gScore = gScore;
    }

    public int getbScore() {
        return bScore;
    }

    public void setbScore(int bScore) {
        this.bScore = bScore;
    }
}
