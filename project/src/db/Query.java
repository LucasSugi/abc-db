package db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
    Query:
        Class that allows to make queries over the 
        Oracle Database connected by Connect Class.
*/
public class Query {
    // Stores the Objects necessary to make queries
    private final Connect connect;
    private final Statement statement;
    
    // Constructor - Sets the statement parameters
    public Query(){
        this.connect = new Connect();
        this.connect.connect();
        this.statement = this.connect.getStatement();
    }
    
    // Sends queries to the database
    public ResultSet executeQuery(String query) throws SQLException {
        return this.statement.executeQuery(query);        
    }    
}
