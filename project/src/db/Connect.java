package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/*
    Connect:
        Class that allows a connection to an oracle database.
*/
public class Connect {        
    // Objects that enables database operations
    private Connection connection;
    private Statement statement;
    
    // Flag that keeps the connection state
    private boolean isConnected;
    
    // Constructor - Sets the connection parameters
    public Connect(){
        this.isConnected = false;
    }
    
    // Tries to connect to database, using connection parameters
    public boolean connect(){
        // Verifies if is already connected
        if(this.isConnected) return true;        
    
        // Tries to connect
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
            this.connection = DriverManager.getConnection("jdbc:oracle:thin:"
                    + "@" + Parameters.host + ":15215:orcl", Parameters.user, Parameters.password);
            this.isConnected = true; // Sets the connection status
        } 
        // Reports a message if the connection excepted
        catch(ClassNotFoundException | SQLException e){
            System.out.println(e.getMessage() + " " + e.initCause(e));
            this.isConnected = false;
        }
        
        // Tries to create a statement
        try{
            this.statement = this.connection.createStatement();
        } 
        // Reports a message and close de connection if the statement to fail
        catch(SQLException e){
            System.out.println(e.getMessage() + " " + e.initCause(e));
            // Tries to disconnect
            try{
                this.connection.close();
                this.isConnected = false; // Sets the connection status
            }
            // Reports a message if the disconnection excepted
            catch(SQLException ev){
                System.out.println(ev.getMessage());
                this.isConnected = true;
            }            
        }
        
        // Returns the connection status
        return this.isConnected;
    }
    
    // Tries to disconnect to database 
    public boolean disconnect() {
        // Verifies if is already disconnected
        if(!this.isConnected) return true;       
        
        // Tries to disconnect
        try {
            this.statement.close();
            this.connection.close();
            this.isConnected = false; // Sets the connection status
        } 
        // Reports a message if the disconnection excepted
        catch(SQLException e){
            System.out.println(e.getMessage());
            this.isConnected = true;
        }
        
        // Returns true if disconnection occurred successful
        return !(this.isConnected);
    }
    
    // Allows the access to the Statement
    protected Statement getStatement(){
        return this.statement;
    }
}