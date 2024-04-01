package Lines;

import java.awt.Color;
import java.awt.Graphics;

public class LineSegment {
    public Color[] colors = {Color.RED, Color.BLUE, Color.CYAN, Color.GREEN, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.YELLOW};
    public int x1, y1, x2, y2; // These variable stores the values of x & y coordinates in units
    public int X1, Y1, X2, Y2; // These stores the values pf x & y coordinates in pixels
    public double dist;
    int dpWidth, dpHeight; 
    
    public LineSegment(int x1, int y1, int x2, int y2, double dist) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.dist = dist;
        X1 = 0;
        Y1 = 0;
        X2 = 0;
        Y2 = 0;
        dpWidth = 0;
        dpHeight = 0;
    }


    public void draw(Graphics g, int color_index, int dpWidth, int dpHeight) {
        this.dpWidth=dpWidth;
        this.dpHeight=dpHeight;
        color_index = color_index%colors.length;
        X1 = convertX(x1);
        Y1 = convertY(y1);
        X2 = convertX(x2);
        Y2 = convertY(y2);
        g.setColor(colors[color_index]);
        g.drawLine(X1, Y1, X2, Y2);
        g.setColor(Color.BLACK);
        // Draw filled circles at each end of the line
        int circleRadius = 5; // Set the radius of the circle
        g.fillOval(X1 - circleRadius / 2, Y1 - circleRadius / 2, circleRadius, circleRadius); // Draw circle at (x1, y1)
        g.fillOval(X2 - circleRadius / 2, Y2 - circleRadius / 2, circleRadius, circleRadius); // Draw circle at (x2, y2)
        
    }

     // This will make sure that the coordinates of x entered by the user will be calculated based on the origin (0,0) of the Cartesian Graph
     public int convertX(int x) {
        return dpWidth / 2 + (x*25);
    }
    // This will make sure that the coordinates of y entered by the user will be calculated based on the origin (0,0) of the Cartesian Graph
    public int convertY(int y) {
        return dpHeight / 2 - (y*25);
    }

}

