package app;

import java.sql.ResultSet;
import java.util.ArrayList;

public class Simulate {
    // Stores the application
    private final Application app;
    
    //Hold's the user's id
    private int idUser;
    
    // Initializes the Report
    public Simulate(Application app){
        this.app = app;                                
        
        //Get the id from active user
        this.idUser = app.getIdUser();
        
        //Indicating if exist or not view
        int view_produto;
        int view_subcategoria;
        
         //Verify if materialized view exist for product
        try {            
            ResultSet rs = app.executeQuery("SELECT COUNT(*) AS EXIST FROM ALL_MVIEWS\n" +
                                            "    WHERE MVIEW_NAME ='VIEW_PRODUTO_"+this.idUser+"'\n" +
                                            "    AND OWNER = 'A9293286'");
            
            //Get the result from query
            rs.next();
            view_produto = Integer.parseInt(rs.getString("EXIST"));
             
            //Verify if view does not exist
            if(view_produto == 0){
                //Create mview
                app.executeQuery("CREATE MATERIALIZED VIEW VIEW_PRODUTO_"+this.idUser+"\n" +
                                "    BUILD IMMEDIATE\n" +
                                "    REFRESH FAST ON DEMAND\n" +
                                "    FOR UPDATE AS \n" +
                                "    SELECT ID_PRODUTO,NOME,PRECO,ID_SUBCATEGORIA,PESO,QUANTIDADE FROM PRODUTO");
            }
            else{
                System.out.println("View_produto exist");
            }
            this.app.setHasProductSimulation(true);
        } 
        // Returns null if an error occurs
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        
        //Verify if materialized view exist for product
        try {            
            ResultSet rs = app.executeQuery("SELECT COUNT(*) AS EXIST FROM ALL_MVIEWS\n" +
                                        "    WHERE MVIEW_NAME ='VIEW_SUBCATEGORIA_"+this.idUser+"'\n" +
                                        "    AND OWNER = 'A9293286'");
            
            //Get the result from query
            rs.next();
            view_subcategoria = Integer.parseInt(rs.getString("EXIST"));
             
            //Verify if view does not exist
            if(view_subcategoria == 0){
                //Create mview
                app.executeQuery("CREATE MATERIALIZED VIEW VIEW_SUBCATEGORIA_"+this.idUser+"\n" +
                                    "    BUILD IMMEDIATE\n" +
                                    "    REFRESH FAST ON DEMAND\n" +
                                    "    FOR UPDATE AS \n" +
                                    "    SELECT * FROM SUBCATEGORIA");
            }
            else{
                System.out.println("View_subcategoria exist");
            }
            this.app.setHasSubcategorySimulation(true);
        } 
        // Returns null if an error occurs
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        
    }
    
    // Stores the possible simulations (move from here)
    public static final String[][] possibleSimulations = {
        {"PRODUTO", "PRECO", "QUANTIDADE"},
        {"SUBCATEGORIA", "CATEGORIA"}
    };        
    
    // Retrieves the possible products to do simulations
    public String[] getPossibleProducts(){
        ArrayList<String> products = new ArrayList<>();
        
        // Gets all product's name
        try {            
            ResultSet rs;
            if(this.app.hasProductSimulation()){
                rs = this.app.executeQuery("SELECT NOME FROM VIEW_PRODUTO_" + this.idUser);
            } else {
                rs = this.app.executeQuery("SELECT NOME FROM PRODUTO");
            }            
            while(rs.next()) products.add(rs.getString("NOME"));                
        } 
        // Returns null if an error occurs
        catch(Exception e){
            System.out.println(e.getMessage());
            return null;
        }        
        
        // Returns the possible products
        return products.toArray(new String[0]);
    }
    
    // Retrieves the possible subcategories to do simulations
    public String[] getPossibleSubcategories(){
        ArrayList<String> products = new ArrayList<>();
        
        // Gets all product's name
        try {            
            ResultSet rs;
            if(this.app.hasSubcategorySimularion()){
                rs = this.app.executeQuery("SELECT NOME FROM VIEW_SUBCATEGORIA_" + this.idUser);
            } else {
                rs = this.app.executeQuery("SELECT NOME FROM SUBCATEGORIA");
            }            
            while(rs.next()) products.add(rs.getString("NOME"));                
        } 
        // Returns null if an error occurs
        catch(Exception e){
            System.out.println(e.getMessage());
            return null;
        }        
        
        // Returns the possible products
        return products.toArray(new String[0]);
    }
    
    // Returns a desired value to fill the fields of UI
    public String getValue(int simIndex, int fieldIndex, String item){
        String value = "", query;
        
        if(simIndex == 0){
            if(this.app.hasProductSimulation()){
                query = "SELECT " + Simulate.possibleSimulations[0][fieldIndex] + " "
                    + "FROM VIEW_PRODUTO_" + this.idUser + " WHERE NOME = '" + item + "'";
            } else {
                query = "SELECT " + Simulate.possibleSimulations[0][fieldIndex] + " "
                    + "FROM PRODUTO WHERE NOME = '" + item + "'";
            }            
            try {
                ResultSet rs = this.app.executeQuery(query);
                while(rs.next()){
                    value = rs.getString(Simulate.possibleSimulations[0][fieldIndex]);
                }                
            } 
            // Returns null if an error occurs
            catch(Exception e){
                System.out.println(e.getMessage());
                return null;
            }        
        } else if(simIndex == 1){            
            if(this.app.hasSubcategorySimularion()){
                query = "SELECT C.NOME AS CATEGORIA FROM CATEGORIA C JOIN VIEW_SUBCATEGORIA_" + this.idUser + " S ON C.ID_CATEGORIA = S.ID_CATEGORIA "
                    + "WHERE S.NOME = '" + item + "'"; 
            } else {
                query = "SELECT C.NOME AS CATEGORIA FROM CATEGORIA C JOIN SUBCATEGORIA S ON C.ID_CATEGORIA = S.ID_CATEGORIA "
                    + "WHERE S.NOME = '" + item + "'"; 
            }            
            try {
                ResultSet rs = this.app.executeQuery(query);
                while(rs.next()){
                    value = rs.getString(Simulate.possibleSimulations[1][fieldIndex]);
                }                
            } 
            // Returns null if an error occurs
            catch(Exception e){
                System.out.println(e.getMessage());
                return null;
            }
        }
                        
        // Returns the value
        return value;
    }
    
    // Tries to do a desired simulation into a table
    public boolean simulate(String table, String register, String value1, String value2){

        //Verify what table is
        if(table.equals("PRODUTO")){
            //Update product
            try {            
                this.app.executeQuery("UPDATE VIEW_PRODUTO_"+this.idUser+"\n" +
"	SET PRECO = "+value1+", QUANTIDADE = "+value2+"\n" +
"	WHERE NOME = '"+register+"'");
            } 
            // Returns null if an error occurs
            catch(Exception e){
                System.out.println(e.getMessage());
                return false;
            }
        }
        else{
             //Update subcategoria
            try {            
                ResultSet rs = this.app.executeQuery("SELECT ID_CATEGORIA FROM CATEGORIA \n" +
"    WHERE NOME = '"+value1+"'");
                
                rs.next();
                int idCategoria = Integer.parseInt(rs.getString("ID_CATEGORIA"));
                
                this.app.executeQuery("UPDATE VIEW_SUBCATEGORIA_"+this.idUser+"\n" +
"    SET ID_CATEGORIA = "+idCategoria+"\n" +
"    WHERE NOME = '"+register+"'");
                
            } 
            // Returns null if an error occurs
            catch(Exception e){
                System.out.println(e.getMessage());
                return false;
            }
        }
        
        // DEVE RETORNAR TRUE se deu certo, FALSE se nao deu certo (para aparecer uma mensagem pro user na UI)
        return true;        
    }
    
    // Tries to reset the simulations done over a table
    public void reset(String table){
           
        //Verify what table is
        if(table.equals("PRODUTO")){
             //Refresh produto
            try {          
                this.app.executeQuery("DROP MATERIALIZED VIEW VIEW_PRODUTO_"+this.idUser);
                this.app.setHasProductSimulation(false);
            } 
            // Returns null if an error occurs
            catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
        else{
             //Refresh subcategoria
            try {                            
                this.app.executeQuery("DROP MATERIALIZED VIEW VIEW_SUBCATEGORIA_"+this.idUser);
                this.app.setHasSubcategorySimulation(false);
            } 
            // Returns null if an error occurs
            catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
}