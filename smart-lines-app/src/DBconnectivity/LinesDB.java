package DBconnectivity;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Lines.*;

public class LinesDB {

    Connection con = null;
    Statement stmt = null;
    PreparedStatement pst = null;
    public LinesDB() {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/java_metrydb", "root", "Applehead@2809");
            //stmt = con.createStatement();
            System.out.println("Connected to database Successfully!!");
            //con.close();
        }catch(Exception e){
            System.out.println(e);
        }
    }

    public void closeDB(){
        try{
            con.close();
            System.out.println("Connection closed");
        }catch(Exception e){
            System.out.println(e);
        }
    }

    public int insertDB(int x1, int y1, int x2, int y2, double distance){
        try {
            pst = con.prepareStatement("insert into java_metrydb.lines(x1,y1,x2,y2,distance) values (?,?,?,?,?);");
            pst.setInt(1, x1);
			pst.setInt(2, x2);
			pst.setInt(3, y1);
			pst.setInt(4, y2);
			pst.setDouble(5, distance);
			return pst.executeUpdate();
        } catch (Exception e) {
            return 0;
        }
    }

    public int getRowCount(){
        try {
            pst = con.prepareStatement("SELECT count(*) as total FROM java_metrydb.lines;");
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                return rs.getInt("total");
            }else
                return 0;
        } catch (SQLException e) {
            System.out.println(e);
            return 0;
        }
    }


    public List<LineSegment> fetchFirstDB(int rows){
        List<LineSegment> ls = new ArrayList<>();
        try {
            pst = con.prepareStatement("SELECT x1,x2,y1,y2,distance FROM java_metrydb.lines ORDER BY linesID ASC LIMIT ?;");
            pst.setInt(1,rows);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                int x1 = rs.getInt(1);
                int x2 = rs.getInt(2);
                int y1 = rs.getInt(3);
                int y2 = rs.getInt(4);
                double dist = rs.getDouble(5);
                ls.add(new LineSegment(x1,y1, x2, y2, dist));
            } 
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ls;
    }


    public List<LineSegment> fetchLastDB(int rows){
        List<LineSegment> ls = new ArrayList<>();
        try {
            pst = con.prepareStatement("SELECT x1,x2,y1,y2,distance FROM java_metrydb.lines ORDER BY linesID DESC LIMIT ?;");
            pst.setInt(1,rows);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                int x1 = rs.getInt(1);
                int x2 = rs.getInt(2);
                int y1 = rs.getInt(3);
                int y2 = rs.getInt(4);
                double dist = rs.getDouble(5);
                ls.add(new LineSegment(x1,y1, x2, y2, dist));
            } 
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ls;

    }
    
}
