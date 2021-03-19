import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Shape {

    File shapeStorage;
    Scanner scanner=null;
   public ArrayList<ArrayList<Point>>shapes;

    public Shape(){
        shapes=new ArrayList<>();
        try{
                                           // "C:\\Users\\othscs121\\Dropbox\\compsci2\\DodgeAttemptTwo\\src\\LevelOne.txt");
            shapeStorage=new File("C:\\Users\\Reilly\\Dropbox\\compsci2\\Blokus\\src\\Shapes.txt");
             scanner=new Scanner(shapeStorage);;
        } catch(Exception e){
            e.printStackTrace();
        }
        load();

    }
    public void load(){

        for(int i=0; i<=21; i++) {
            ArrayList<Point>newShape=new ArrayList<>();

            while (scanner.hasNextLine()) {
                String[] data = scanner.nextLine().split(",");
                if(data[0].equals("E"))
                    break;
                else
                newShape.add(new Point(Integer.parseInt(data[0]),Integer.parseInt(data[1])));

            }

            shapes.add(newShape);
        }
    }
    public void rotate(int num){
        //roatate counterclockwise
        int lowestY=0;
        int lowestX=0;
        ArrayList<Point>newRotation=shapes.get(num);

        for(int i=0; i<newRotation.size();i++)
        {
            int x=(int)newRotation.get(i).getX();
            int y=(int)newRotation.get(i).getY();
            if(-y<lowestY)
                lowestY=y+1;
            if(x<lowestX)
                lowestX=x;


        }
        for(int i=0; i<newRotation.size();i++)
        {
            int x=(int)newRotation.get(i).getX();
            int y=(int)newRotation.get(i).getY();
            newRotation.get(i).setLocation(-y+lowestY,x);

        }
    }
    public void flip(int num){
        ArrayList<Point>newRotation=shapes.get(num);
        int lowestX=0;
        for(int i=0; i<newRotation.size();i++){

            int x=(int)newRotation.get(i).getX();
            if(-x<lowestX)
                lowestX=-x;

            newRotation.get(i).setLocation(-x,newRotation.get(i).getY());
        }
        for(int i=0; i<newRotation.size();i++)
            newRotation.get(i).setLocation(newRotation.get(i).getX()-lowestX,newRotation.get(i).getY());

    }
    public int getHeight(int shape){
        int biggestY=-10;
        int smallestY=10;
        for(int i=0; i<shapes.size();i++){
            if(shapes.get(i).get(shape).getY()<smallestY)
                smallestY=(int)shapes.get(i).get(shape).getY();
            else if(shapes.get(i).get(shape).getY()>biggestY)
                biggestY=(int)shapes.get(i).get(shape).getY();
        }
        return biggestY-smallestY;
    }
    public int getWidth(int shape){
        int biggestY=-10;
        int smallestY=10;
        for(int i=0; i<shapes.size();i++){
            if(shapes.get(i).get(shape).getX()<smallestY)
                smallestY=(int)shapes.get(i).get(shape).getX();
            else if(shapes.get(i).get(shape).getX()>biggestY)
                biggestY=(int)shapes.get(i).get(shape).getX();
        }
        return biggestY-smallestY;
    }

}
