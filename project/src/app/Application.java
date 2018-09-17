package app;

import db.Query;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/*
    Application:
        Class that handles the operations of the application, as database 
        conecction, database queries, etc., enabling a better interaction
        between Database and GUI.
*/
public class Application {
    // Object that allows operations over the database
    private Query query;
    private boolean hasProductSimulation;
    private boolean hasSubcategorySimulation;
    private int idUser = -1;
    private int userType = -1;
    
    // Constructor - Starts the dependencies to application executes
    public Application(){
        this.connectToDatabase();
        this.hasProductSimulation = false;
        this.hasSubcategorySimulation = false;
    }
    
    // Connects the application to Database
    private void connectToDatabase(){
        this.query = new Query();
    }
    
    // Allows a query Execution over the Database
    public ResultSet executeQuery(String string) throws SQLException {
        return this.query.executeQuery(string);
    }                
    
    // Returns if has product simulation
    public boolean hasProductSimulation(){
        return this.hasProductSimulation;
    }
    
    // Returns if has subcategory simularion
    public boolean hasSubcategorySimularion(){
        return this.hasSubcategorySimulation;
    }
    
    // Sets the attribute value
    public void setHasProductSimulation(boolean value){
        this.hasProductSimulation = value;
    }
    
    // Sets the attribute value
    public void setHasSubcategorySimulation(boolean value){
        this.hasSubcategorySimulation = value;
    }
    
     // Sets the attribute value
    public void setIdUser(int value){
        this.idUser = value;
    }
    
     // Gets the attribute value
    public int getIdUser(){
        return this.idUser;
    }
    
    // Sets the attribute value
    public void setUserType(){        
        try {
            ResultSet rs = this.executeQuery("SELECT FUNCAO FROM EMPREGADO WHERE ID_PESSOA = " + this.idUser);
            rs.next();
            String function = rs.getString("FUNCAO");                        
            if(function.equals("Production Technician - WC20")) this.userType = 1;
            else if(function.equals("Shipping and Receiving Clerk")) this.userType = 1;
            else if(function.equals("Stocker")) this.userType = 2;
            else this.userType = 3;
        } catch (Exception e){
            this.userType = 4;
        }                
    }
    
    // Gets the attribute value
    public int getUserType(){
        return this.userType;
    }
    
    // Updates the Log
    public void updateLog(){        
        Random r = new Random();
        int n = r.nextInt() + 1;
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
        LocalDateTime now = LocalDateTime.now();  
        System.out.println(dtf.format(now));  
        
        try {
            this.executeQuery("INSERT INTO SYS_LOG VALUES(" + Math.abs(n) + ", " + this.idUser + ", '" + dtf.format(now) + "')");
        } catch(Exception e){
            try {
                this.executeQuery("INSERT INTO SYS_LOG VALUES(" + (n+13) + ", " + this.idUser + ", '" + dtf.format(now) + "')");
            } catch(Exception e2){}
        }        
    }
}
