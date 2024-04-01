/* 
 * This Program Lets's The user draw lines on a graph by entering coordinates
 * 
 */

 /*
    Added Features:
    - Added a clear button to clear the entire graph and start fresh.
    - Added different colors to different lines drawn.
    - Added a seperate panel to display the distance values based on the colors of the lines and coordinates.
    - THE PROJECT NAME IS "LINE ART" USER WILL PROVIDE NUMBER OF LINES, WANTS TO DRAW.

    Bugs to Fix: (Fixed!!)
    - Application window scalability issue.
  */
  import Lines.*;
  import DBconnectivity.*;
  import javax.swing.*;
  import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
  import java.awt.event.*;
  import java.util.ArrayList;
  import java.util.List;
  
  public class SmartLines extends JFrame {
  

    // This method will help run our code
    public static void main(String[] args) {
        SmartLines graph = new SmartLines(); // Displays the window
        graph.setVisible(true); // Makes the window visible
    }

      
  
      



      /*
       * These are all the global variables of the class
       * 
       * 
       */
      
    private int linesUpdated=0;
    private JTextField x1Field, y1Field, x2Field, y2Field;// These are variables that stores the coordinates of the two points (x1,y1) & (x2,y2)
    private JButton drawButton; // This is a variable that stores a button object which when clicked will draw a line on the graph
    private JButton clearButton;
    private JButton lineDataButton;
    private JButton saveButton;
    private JButton showHistoryButton;
    private JPanel linesPanel;
   // private JPanel historyPanel;
    private List<LineSegment> historyLines = new ArrayList<>();
    private JPanel drawingPanel; // This defines the section of our window that will be used for drawing lines and for the display of the Cartesian Graph.
    private List<LineSegment> lines = new ArrayList<>();
    private JFrame linesFrame;
    private JFrame historyFrame;
    static LinesDB L;
    private int width=515, height=605;






      public SmartLines() {
          setTitle("Cartesian Graph v2"); // Gives a name to the window of our program
          setSize(width, height); // sets the height and width of our window
          setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // This command specifies that if we close the window, the program will terminate automatically
          L = new LinesDB();
          // Create input fields
          JPanel inputPanel = new JPanel(new GridLayout(2, 4)); // Creates an invisible grid format on the window which has 2 rows and 4 coloumns
          inputPanel.add(new JLabel("x1:")); // This adds a label that says "x1: "
          x1Field = new JTextField(5); // This creates and input space where the user can enter the x1 value
          inputPanel.add(x1Field); // The above x1Field variable is added on to the window panel
          inputPanel.add(new JLabel("y1:"));// This adds a label that says "y1: "
          y1Field = new JTextField(5);// This creates and input space where the user can enter the y1 value
          inputPanel.add(y1Field);// The above y1Field variable is added on to the window panel
          inputPanel.add(new JLabel("x2:"));// This adds a label that says "x2: "
          x2Field = new JTextField(5);// This creates and input space where the user can enter the x2 value
          inputPanel.add(x2Field);// The above x2Field variable is added on to the window panel
          inputPanel.add(new JLabel("y2:"));// This adds a label that says "y2: "
          y2Field = new JTextField(5);// This creates and input space where the user can enter the y2 value
          inputPanel.add(y2Field);// The above y2Field variable is added on to the window panel
          // Sets the default values for x1, y1, x2, y2 as 0
         /*  x1Field.setText("0");
          y1Field.setText("0");
          x2Field.setText("0");
          y2Field.setText("0"); */
          // Create drawing panel which consists of the Cartesian Graph and the line that is drawon on to it 
          drawingPanel = new JPanel() {
              @Override
              protected void paintComponent(Graphics g) {
                  super.paintComponent(g);
                  drawCartesianGraph(g); // First draws the cartesian graph
                  int i=0;
                  for (LineSegment line : lines) {
                      line.draw(g, i, drawingPanel.getWidth(), drawingPanel.getHeight()); // Draws lines every time  value is enterd by the user
                      i++;
                  }
              }
          };
          // This button will display the data of the lines that are drawin on the screen
          lineDataButton= new JButton("show data");
          lineDataButton.addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e){
                  if (!lines.isEmpty()) {
                      showLinesFrame();
                  } else {
                      JOptionPane.showMessageDialog(SmartLines.this, "No lines to show.");
                  }
              }
          });
          // This button will insert the line data inside the database
          saveButton = new JButton("Save Data");
          saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                saveData();
            }
          });
          // This button will clear all the existing drawings from the screen
          clearButton = new JButton("clear");
          clearButton.addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e){
                // Show a Yes/No message box
                if(!lines.isEmpty() && linesUpdated==0){
                    int option = JOptionPane.showConfirmDialog(null, "Do you want to save your data?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    // Check the user's choice
                    if (option == JOptionPane.YES_OPTION){
                        saveData();
                    }
                }
                lines.clear();
                drawingPanel.repaint();
                if(!lines.isEmpty())
                    showLinesFrame();
                else{
                    linesUpdated = 0;
                    if(linesFrame.isVisible())
                        linesFrame.dispose();
                }   
              }
          });
          // Create draw button that when clicked will draw a line on to the graph
          drawButton = new JButton("Draw Line"); // The label of the button says "Draw Line:"
          drawButton.addActionListener(new ActionListener() { // This method makes the button function the way it should
              @Override
              public void actionPerformed(ActionEvent e) {
                  drawLine();
                  drawingPanel.repaint();// This means that everytime the button is clicked, a new line is drawn on the graph
                  if(!lines.isEmpty())
                    showLinesFrame();
              }
          });
          showHistoryButton = new JButton("Show History");
          showHistoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                historyFrame = new JFrame("Show History");
                getHistory(historyFrame);
            }
          });
          // This will check if the window is being resized, if so it will change the droawing panel accordingly
          addComponentListener(new ComponentAdapter() {
              @Override
              public void componentResized(ComponentEvent e) {
                  drawingPanel.repaint();
              }
          });
          JPanel lowerPanel = new JPanel(new GridLayout(2, 2, 10, 10));
          lowerPanel.add(drawButton);
          lowerPanel.add(clearButton);
          lowerPanel.add(lineDataButton);
          lowerPanel.add(saveButton);
          lowerPanel.add(showHistoryButton);
          // Add the different components to the program window such as the coordinate inputs, The Graph & the button
          Container contentPane = getContentPane();
          contentPane.add(inputPanel, BorderLayout.NORTH); // input components at the top
          contentPane.add(drawingPanel, BorderLayout.CENTER); // cartesian graph at the center
          contentPane.add(lowerPanel, BorderLayout.SOUTH); // 'Draw Line' button at the bottom
          // Add a window listener to the frame
            addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Print a statement when the window is being closed
                L.closeDB();
            }
        });
    }


    private void getHistory(JFrame parent){
         // Create an array of options for the first dropdown
         String[] options1 = {"top", "bottom"};

         // Create an array of options for the second dropdown
         
         String[] options2 = new String[L.getRowCount()];
        for(int i=0; i<L.getRowCount(); i++)
            options2[i] = ""+(i+1);
         // Create dropdowns
         JComboBox<String> dropdown1 = new JComboBox<>(options1);
         JComboBox<String> dropdown2 = new JComboBox<>(options2);
 
         // Create panel with components
         JPanel panel = new JPanel();
         panel.add(new JLabel("Show the data of"));
         panel.add(dropdown2);
         panel.add(new JLabel("row(s) from"));
         panel.add(dropdown1);
 
         // Show dialog box
         int result = JOptionPane.showConfirmDialog(parent, panel, "History", JOptionPane.OK_CANCEL_OPTION);
 
         // Check the result
         if (result == JOptionPane.OK_OPTION) {
             String selectedOption1 = (String) dropdown1.getSelectedItem();
             String selectedOption2 = (String) dropdown2.getSelectedItem();
            if(selectedOption1.compareTo("top")==0 && L.getRowCount() != 0){
                historyLines = L.fetchFirstDB(Integer.parseInt(selectedOption2));
                showHistoryFrame();
            }else if(selectedOption1.compareTo("bottom")==0 && L.getRowCount() != 0){
                historyLines = L.fetchLastDB(Integer.parseInt(selectedOption2));
                showHistoryFrame();
            }else{
                // Show error message dialog
                JOptionPane.showMessageDialog(parent, "No data is available!", "Error", JOptionPane.ERROR_MESSAGE);
                parent.dispose();
            }
         } else {
             System.out.println("Operation canceled");
         }
    }

    private void showHistoryFrame(){
        historyFrame = new JFrame("Lines History");
        historyFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        // Create a JTable with default table model
        DefaultTableModel model = new DefaultTableModel();
        
        // Add columns to the table
        model.addColumn("Sr. no");
        model.addColumn("1st Point");
        model.addColumn("2nd Point");
        model.addColumn("Distance");
        model.addColumn("");

        int i = 1;
        // Add rows to the table using a loop
        for (LineSegment line : historyLines) {
            String p1 = "("+line.x1+","+line.y1+")";
            String p2 = "("+line.x2+","+line.y2+")";
            String d = String.format("%.2f",line.dist)+" units";
            JButton drawHistory = new JButton("draw");
            drawHistory.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e){
                    
                }
            });
            model.addRow(new Object[]{i, p1, p2, d, drawHistory});
            i++;
        }
        JTable table = new JTable(model);
         // Center align text in cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);
        // Add padding around the text data inside each cell
        table.setIntercellSpacing(new Dimension(10, 10));
         // Increase the height of each row
         table.setRowHeight(30); // Adjust the value as needed
        // Add the JTable to a JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);
        // Set the preferred size of the table
        table.setPreferredScrollableViewportSize(new Dimension(500, 500));
        // Add the scroll pane to the history frame
        historyFrame.add(scrollPane);
        // Pack the frame to adjust its size
        historyFrame.pack();
        // Set the frame visible
        historyFrame.setVisible(true);
    }

    /*
     * This method will update the lines data frame with old and new values
     * 
     * 
     */
  
      private JScrollPane updateLinesPanel(){
          linesPanel = new JPanel(new GridLayout(lines.size(), 1));
              // Iterate through each line and add label with coordinates and distance
              int i =0;
              for (LineSegment line: lines) {
                  i++;
                  JLabel label = new JLabel("L" + i + "--> (" + line.x1 + "," + line.y1 + ")   (" + line.x2 + "," + line.y2 + ")  ||  Distance: " + String.format("%.2f",line.dist)+" units");
                  label.setForeground(line.colors[(i-1)%8]); // Set the color of the label
                  linesPanel.add(label);
              }

               // Wrap the linesPanel inside a JScrollPane
            JScrollPane scrollPane = new JScrollPane(linesPanel);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // Always show vertical scrollbar
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Never show horizontal scrollbar
            return scrollPane;
        }
  

      private void saveData(){
        int i = -1;
        for(LineSegment line: lines){
            i = L.insertDB(line.x1, line.y1, line.x2, line.y2, line.dist);
            if(i==0)
                break;
        }
        if(i!=0){
            JOptionPane.showMessageDialog(null, "Your data has been saved!", "Success", JOptionPane.INFORMATION_MESSAGE); 
            linesUpdated = i;
        }else
            JOptionPane.showMessageDialog(null, "your data was not saved", "Failed", JOptionPane.ERROR_MESSAGE);

      }


      /*
       * This method will show the Lines Data on a seperate window frame
       * 
       * 
       */
  
      private void showLinesFrame() {
          if (linesFrame != null) {
              linesFrame.dispose();
          }
              linesFrame = new JFrame("Lines Data");
              linesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
              linesFrame.setSize(300, 200);
              // Create a panel to display line coordinates and distances
              linesFrame.add(updateLinesPanel());
              linesFrame.add(linesPanel);
          linesFrame.setVisible(true);
      }
  
  


      /*
       * This method will draw and design the outline of the graph (or the drawing area)
       * 
       * 
       */
  
      private void drawCartesianGraph(Graphics g) {
          int width = drawingPanel.getWidth(); // It stores the width of the drawing area
          int height = drawingPanel.getHeight(); // It stores the height of the drawing area
          g.setColor(Color.WHITE); // It sets the color WHITE
          g.fillRect(0, 0, width, height); // Fills the background with the white color
          g.setColor(Color.BLACK); // It sets the color BLACK for the x & y axis lines drawn
          g.drawLine(width / 2, 0, width / 2, height); // y-axis is drawn
          g.drawLine(0, height / 2, width, height / 2); // x-axis is drawn
          g.setFont(new Font("Arial", Font.PLAIN, 14)); // Set font size to 12
          g.drawString("X",width -10, (height/2) -10); // prints X label in the positive side of X-axis
          g.drawString("-X",10, (height/2) -10); // prints -X label on the negetive side " " "
          g.drawString("-Y",(width/2) +10, height-10); // prints -Y label on the negative side of Y -axis
          g.drawString("Y",(width/2) +10, 10); // prints Y label on the positive side " " "
          g.setFont(new Font("Arial", Font.PLAIN, 10)); // Set font size to 12
          g.drawString("O",(width/2) +5, (height/2) +15); // labels the Origin point of graph (0,0)
          // Adds number lines on to the X-axis and Y-axis
          printAxisPoints(g);
  
      }
      



      /*
       * 
       * A method to mark all the points from X & Y axis
       */
 
      private void printAxisPoints(Graphics g){
          int width = drawingPanel.getWidth();
          int height = drawingPanel.getHeight();
          int originX = (int) width/2;
          int originY = (int) height/2;
          int i= originX,j= originX;
          while(true){
              i = i+25; j = j-25;
              if(i >= width || j <= 0){
                  break;
              }
              g.drawLine(i, originY-5 ,i, originY+5); // marks the X-axis points from top-down
              g.drawLine(j, originY-5 ,j, originY+5); // " " " " from bottom-up
          }
          i = originY; j = originY;
          while(true){
              i = i+25; j = j-25;
              if(i >= height || j <= 0){
                  break;
              }
              g.drawLine(originX-5, i, originX+5, i); // marks the Y-axis points from top-down
              g.drawLine(originX-5, j, originX+5, j); //  ' ' ' ' from bottom-up
              j++;
          }
      }
  
  
  
      /* 
       
      This method is used to draw the line as per the coordinates given by the user
       
       */
      private void drawLine() {
          try {
              // Converts String inputs into integer values
              int x1 = Integer.parseInt(x1Field.getText());
              int y1 = Integer.parseInt(y1Field.getText());
              int x2 = Integer.parseInt(x2Field.getText());
              int y2 = Integer.parseInt(y2Field.getText());
              // Addes the values of x & y coordinates of a line to a list of Line Segments
              lines.add(new LineSegment(x1, y1, x2, y2, CalDistance())); 
          } catch (NumberFormatException e) {
              JOptionPane.showMessageDialog(this, "Please enter valid integers for coordinates."); // This will show a pop-up error message if the inputs of the coordinates aren't Integer values
          }
      }
     



      /* 
      
        This meathod caluclates the distance measure of the line
      
      */
      private double CalDistance(){
          try{
              // Converts String inputs into integer values
              int x1 = Integer.parseInt(x1Field.getText());
              int y1 = Integer.parseInt(y1Field.getText());
              int x2 = Integer.parseInt(x2Field.getText());
              int y2 = Integer.parseInt(y2Field.getText());
              return Math.sqrt(Math.pow((x2-x1),2) + Math.pow((y2-y1),2)); // applies the distance formula : √((x2 – x1)² + (y2 – y1)²)
          }catch (NumberFormatException e) {
              return 0.0; // if the user input is anything but a number, it will keep a default value value of 0.0
          }
          
      }
  




  }
  