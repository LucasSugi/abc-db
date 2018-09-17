package app;

import java.sql.ResultSet;
import java.util.ArrayList;

/*
    Overview:
        Class that interacts with the data and UI related to the Overview Reports.
*/
public class Overview {          
    
    // Stores the application
    private final Application app;
    
    // Initializes the Report
    public Overview(Application app){
        this.app = app;
    }
    
    // Stores the possible overview reports (move from here)
    public static final String[][] possibleReports = {
        {"TOTAL DE VENDAS", "DIA", "MÊS", "ANO"},
        {"3 FUNCIONÁRIOS QUE MAIS VENDERAM", "MÊS", "ANO"},
        {"15 PRODUTOS MAIS VENDIDOS"},
        {"CLIENTES QUE MAIS COMPRARAM NO ANO", "ANO"},
        {"CLIENTES QUE MAIS COMPRARAM"},
        {"PRODUTOS COM ESTOQUE MENOR QUE 10"}            
    };
    
    // Generates the reports
    public ArrayList<String[]> generateReport(int reportId, String filter1, String filter2, String filter3){                
        // Generates and returns the correct report
        if(reportId == 0){
            return this.generateReport0(filter1, filter2, filter3);
        } else if(reportId == 1){
            return this.generateReport1(filter1, filter2);
        } else if(reportId == 2){
            return this.generateReport2();
        } else if(reportId == 3){
            return this.generateReport3(filter1);
        } else if(reportId == 4){
            return this.generateReport4();
        } else if(reportId == 5){
            return this.generateReport5();
        }
        // Returns null if the report doesnt exists
        return null;
    }
    
    // Generates the report 0
    private ArrayList<String[]> generateReport0(String filter1, String filter2, String filter3){
        ArrayList<String[]> report = new ArrayList<>();
        String query, queryWhere;
        String[] cols;
        
        // Mounts the suitable where clause
        if("".equals(filter1) && "".equals(filter2) && "".equals(filter3)){ // Without any filter
            query = "SELECT EXTRACT(YEAR FROM DATA_VENDA) AS ANO, SUM(TOTAL) AS \"VALOR TOTAL\"\n" +
                    "   FROM VENDA\n"+
                    "       WHERE (EXTRACT(YEAR FROM DATA_VENDA)>= '2014' AND EXTRACT(YEAR FROM DATA_VENDA)<= '2018')\n"+
                    "       GROUP BY EXTRACT(YEAR FROM DATA_VENDA)\n"+
                    "       ORDER BY EXTRACT(YEAR FROM DATA_VENDA)";
            
            cols = new String[2];
            cols[0] = "ANO";
            cols[1] = "VALOR TOTAL";
            
        } else if(!("".equals(filter1)) && !("".equals(filter2)) && !("".equals(filter3))){ // Fully Filtered
            query = "SELECT DIA, CASE MES\n" +
                    "   WHEN 1 THEN 'JANEIRO'\n" +
                    "   WHEN 2 THEN 'FEVEREIRO'\n" +
                    "   WHEN 3 THEN 'MARCO'\n" +
                    "   WHEN 4 THEN 'ABRIL'\n" +
                    "   WHEN 5 THEN 'MAIO'\n" +
                    "   WHEN 6 THEN 'JUNHO'\n" +
                    "   WHEN 7 THEN 'JULHO'\n" +
                    "   WHEN 8 THEN 'AGOSTO'\n" +
                    "   WHEN 9 THEN 'SETEMBRO'\n" +
                    "   WHEN 10 THEN 'OUTUBRO'\n" +
                    "   WHEN 11 THEN 'NOVEMBRO'\n" +
                    "   WHEN 12 THEN 'DEZEMBRO' END AS MES, ANO, VALOR_TOTAL AS \"VALOR TOTAL\" FROM\n" +
                    "   (SELECT EXTRACT(DAY FROM DATA_VENDA) AS DIA, EXTRACT(MONTH FROM DATA_VENDA) AS MES, EXTRACT(YEAR FROM DATA_VENDA) AS ANO, SUM(TOTAL) AS VALOR_TOTAL\n" +
                    "       FROM VENDA\n" +
                    "           WHERE EXTRACT(DAY FROM DATA_VENDA) = '"+filter1+"' AND EXTRACT(MONTH FROM DATA_VENDA) = '"+filter2+"' AND EXTRACT(YEAR FROM DATA_VENDA) = '"+filter3+"'\n" +
                    "           GROUP BY EXTRACT(DAY FROM DATA_VENDA), EXTRACT(MONTH FROM DATA_VENDA), EXTRACT(YEAR FROM DATA_VENDA))";
            
            cols = new String[4];
            cols[0] = "DIA";
            cols[1] = "MES";
            cols[2] = "ANO";
            cols[3] = "VALOR TOTAL";
            
        } else if(!("".equals(filter2)) && !("".equals(filter3))){ // With month and year filters
            query = "SELECT CASE MES\n" +
                    "   WHEN 1 THEN 'JANEIRO'\n" +
                    "   WHEN 2 THEN 'FEVEREIRO'\n" +
                    "   WHEN 3 THEN 'MARCO'\n" +
                    "   WHEN 4 THEN 'ABRIL'\n" +
                    "   WHEN 5 THEN 'MAIO'\n" +
                    "   WHEN 6 THEN 'JUNHO'\n" +
                    "   WHEN 7 THEN 'JULHO'\n" +
                    "   WHEN 8 THEN 'AGOSTO'\n" +
                    "   WHEN 9 THEN 'SETEMBRO'\n" +
                    "   WHEN 10 THEN 'OUTUBRO'\n" +
                    "   WHEN 11 THEN 'NOVEMBRO'\n" +
                    "   WHEN 12 THEN 'DEZEMBRO' END AS MES, ANO, VALOR_TOTAL AS \"VALOR TOTAL\" FROM\n" +
                    "   (SELECT EXTRACT(MONTH FROM DATA_VENDA) AS MES, EXTRACT(YEAR FROM DATA_VENDA) AS ANO, SUM(TOTAL) AS VALOR_TOTAL\n" +
                    "       FROM VENDA\n" +
                    "           WHERE EXTRACT(MONTH FROM DATA_VENDA) = '"+filter2+"' AND EXTRACT(YEAR FROM DATA_VENDA) = '"+filter3+"'\n" +
                    "           GROUP BY EXTRACT(MONTH FROM DATA_VENDA), EXTRACT(YEAR FROM DATA_VENDA))";
            
            cols = new String[3];
            cols[0] = "MES";
            cols[1] = "ANO";
            cols[2] = "VALOR TOTAL";

        } else if(!("".equals(filter3))){ // With year filter only
            query = "SELECT EXTRACT(YEAR FROM DATA_VENDA) AS ANO, SUM(TOTAL) AS \"VALOR TOTAL\"\n" +
                    "    FROM VENDA\n" +
                    "    WHERE EXTRACT(YEAR FROM DATA_VENDA) = '"+filter3+"'\n" +
                    "    GROUP BY EXTRACT(YEAR FROM DATA_VENDA)";
            
            cols = new String[2];
            cols[0] = "ANO";
            cols[1] = "VALOR TOTAL";
        }
        else{
            query = "";
            cols = null;
        }
        
        // Executes the Query
       try{
            // Executes the Query
            ResultSet rs = this.app.executeQuery(query);
                  
            // Adds the Cols Description into ArrayList<String[]>
            report.add(cols);
                        
            // Adds the Data into ArrayList<String[]>
            String[] str;
            while(rs.next()){
                str = new String[cols.length];
                for(int i = 0; i < cols.length; i++){
                    str[i] = rs.getString(cols[i]);
                }
                report.add(str);
            }            
        } 
        // Prints a message if an error occurrs
        catch(Exception e){
            System.out.println(e.getMessage());            
            System.out.println(query);
            return null;
        }
                                
        // Returns the Report Data
        return report;
    }
        
    // Generates the report 1
    private ArrayList<String[]> generateReport1(String filter1, String filter2){
        ArrayList<String[]> report = new ArrayList<>();
        String query, queryWhere;
        
        // Mounts the suitable where clause
        if(!("".equals(filter1)) && !("".equals(filter2))){ // Fully Filtered
            queryWhere = "WHERE EXTRACT(MONTH FROM DATA_VENDA) = '"+filter1+"' AND EXTRACT(YEAR FROM DATA_VENDA) = '"+filter2+"'"; 
        } else if("".equals(filter1) && !("".equals(filter2))){ // Filtered with year 
            queryWhere = "WHERE EXTRACT(YEAR FROM DATA_VENDA) = '"+filter2+"'"; 
        } else{ // With no filters
            queryWhere = "";
        }
                
        // Mounts the Query
        query = "SELECT * FROM\n" +
                "    (SELECT P.NOME AS NOME, EXTRACT(MONTH FROM DATA_VENDA) AS MES, EXTRACT(YEAR FROM DATA_VENDA) AS ANO, COUNT(*) AS \"NUMERO DE VENDAS\"\n" +
                "        FROM VENDEDOR V\n" +
                "        JOIN PESSOA P ON V.ID_PESSOA = P.ID_PESSOA\n" +
                "        JOIN VENDA VD ON V.ID_PESSOA = VD.ID_VENDEDOR\n" +
                         queryWhere +
                "        GROUP BY V.ID_PESSOA, P.NOME, P.ID_PESSOA, EXTRACT(MONTH FROM DATA_VENDA), EXTRACT(YEAR FROM DATA_VENDA)\n" +
                "        ORDER BY COUNT(*) DESC)\n" +
                "    WHERE ROWNUM <= 3"; 
        
        try{
            // Executes the Query
            ResultSet rs = this.app.executeQuery(query);
            
            // Creates a String array with the columns description
            String[] cols = {
                "NOME",
                "MES",
                "ANO",
                "NUMERO DE VENDAS"
            };
            
            // Adds the Cols Description into ArrayList<String[]>
            report.add(cols);
                        
            // Adds the Data into ArrayList<String[]>
            String[] str;
            while(rs.next()){
                str = new String[cols.length];
                for(int i = 0; i < cols.length; i++){
                    str[i] = rs.getString(cols[i]);
                }
                report.add(str);
            }            
        } 
        // Prints a message if an error occurrs
        catch(Exception e){
            System.out.println(e.getMessage());            
            System.out.println(query);
            return null;
        }
                
        // Returns the Report Data
        return report;
    }
    
    // Generates the report 2
    private ArrayList<String[]> generateReport2(){
        ArrayList<String[]> report = new ArrayList<>();
        String query;

        // Mounts the Query
        
        query = "SELECT * FROM\n" +
                "    (SELECT P.NOME AS NOME, COUNT(*) AS \"NUMERO DE VENDAS\"\n" +
                "        FROM ITEMVENDA I\n" +
                "        JOIN VENDA V ON I.ID_VENDA = V.ID_VENDA\n" +
                "        JOIN PRODUTO P ON I.ID_PRODUTO = P.ID_PRODUTO\n" +
                "        GROUP BY P.NOME\n" +
                "        ORDER BY COUNT(*) DESC)\n" +
                "    WHERE ROWNUM <= 15"; 
        
        try{
            // Executes the Query
            ResultSet rs = this.app.executeQuery(query);
            
            // Creates a String array with the columns description
            String[] cols = {
                "NOME",
                "NUMERO DE VENDAS"
            };
            
            // Adds the Cols Description into ArrayList<String[]>
            report.add(cols);
                        
            // Adds the Data into ArrayList<String[]>
            String[] str;
            while(rs.next()){
                str = new String[cols.length];
                for(int i = 0; i < cols.length; i++){
                    str[i] = rs.getString(cols[i]);
                }
                report.add(str);
            }            
        } 
        // Prints a message if an error occurrs
        catch(Exception e){
            System.out.println(e.getMessage());            
            System.out.println(query);
            return null;
        }
                
        // Returns the Report Data
        return report;
    }
    
    // Generates the report 3
    private ArrayList<String[]> generateReport3(String filter1){
        ArrayList<String[]> report = new ArrayList<>();
        String query, queryWhere;
        
         // Mounts the suitable where clause
        if(!("".equals(filter1))){ // Filtered
            query = "SELECT P.NOME AS NOME, EXTRACT(YEAR FROM V.DATA_VENDA) AS ANO, COUNT(*) AS \"NUMERO DE COMPRAS\"\n" +
                    "    FROM CLIENTE C\n" +
                    "    JOIN PESSOA P ON C.ID_PESSOA = P.ID_PESSOA\n" +
                    "    JOIN VENDA V ON C.ID_CLIENTE = V.ID_CLIENTE\n" +
                    "    WHERE EXTRACT(YEAR FROM V.DATA_VENDA) = '"+filter1+"'\n"+
                    "    GROUP BY P.NOME, EXTRACT(YEAR FROM V.DATA_VENDA)\n" +
                    "    ORDER BY COUNT(*) DESC";
        } else if("".equals(filter1)){ // Without filters
            query = "SELECT P.NOME AS NOME, EXTRACT(YEAR FROM V.DATA_VENDA) AS ANO, COUNT(*) AS \"NUMERO DE COMPRAS\"\n" +
                    "    FROM CLIENTE C\n" +
                    "    JOIN PESSOA P ON C.ID_PESSOA = P.ID_PESSOA\n" +
                    "    JOIN VENDA V ON C.ID_CLIENTE = V.ID_CLIENTE\n" +
                    "    WHERE (EXTRACT(YEAR FROM DATA_VENDA)>= 2014 AND EXTRACT(YEAR FROM DATA_VENDA)<= 2018)\n" +
                    "    GROUP BY P.NOME, EXTRACT(YEAR FROM V.DATA_VENDA)\n" +
                    "    ORDER BY COUNT(*) DESC";
        } else{
            query = "";
        }
                
        
        try{
            // Executes the Query
            ResultSet rs = this.app.executeQuery(query);
            
            // Creates a String array with the columns description
            String[] cols = {
                "NOME",
                "ANO",
                "NUMERO DE COMPRAS"
            };
            
            // Adds the Cols Description into ArrayList<String[]>
            report.add(cols);
                        
            // Adds the Data into ArrayList<String[]>
            String[] str;
            while(rs.next()){
                str = new String[cols.length];
                for(int i = 0; i < cols.length; i++){
                    str[i] = rs.getString(cols[i]);
                }
                report.add(str);
            }            
        } 
        // Prints a message if an error occurrs
        catch(Exception e){
            System.out.println(e.getMessage());            
            System.out.println(query);
            return null;
        }
                
        // Returns the Report Data
        return report;
    }
    
    // Generates the report 4
    private ArrayList<String[]> generateReport4(){
        ArrayList<String[]> report = new ArrayList<>();
        String query, queryWhere;
                       
       query = "SELECT P.NOME AS NOME, COUNT(*) AS \"NUMERO DE COMPRAS\"\n" +
                "    FROM CLIENTE C\n" +
                "    JOIN PESSOA P ON C.ID_PESSOA = P.ID_PESSOA\n" +
                "    JOIN VENDA V ON C.ID_CLIENTE = V.ID_CLIENTE\n" +
                "    GROUP BY P.NOME\n" +
                "    ORDER BY COUNT(*) DESC";              
        
        try{
            // Executes the Query
            ResultSet rs = this.app.executeQuery(query);
            
            // Creates a String array with the columns description
            String[] cols = {
                "NOME",
                "NUMERO DE COMPRAS"
            };
            
            // Adds the Cols Description into ArrayList<String[]>
            report.add(cols);
                        
            // Adds the Data into ArrayList<String[]>
            String[] str;
            while(rs.next()){
                str = new String[cols.length];
                for(int i = 0; i < cols.length; i++){
                    str[i] = rs.getString(cols[i]);
                }
                report.add(str);
            }            
        } 
        // Prints a message if an error occurrs
        catch(Exception e){
            System.out.println(e.getMessage());            
            System.out.println(query);
            return null;
        }
                
        // Returns the Report Data
        return report;
    }
    
    // Generates the report 5
    private ArrayList<String[]> generateReport5(){
        ArrayList<String[]> report = new ArrayList<>();
        String query, queryAux;
                
        if(this.app.hasProductSimulation()){
            queryAux = "VIEW_PRODUTO_" + this.app.getIdUser();
        } else {
            queryAux = "PRODUTO";
        }
        
        query = "SELECT NOME, QUANTIDADE AS \"QTD PRODUTO\"\n" +
                "    FROM " + queryAux + "\n" +
                "    WHERE QUANTIDADE < 10\n" +
                "    ORDER BY QUANTIDADE DESC";              
        
        try{
            // Executes the Query
            ResultSet rs = this.app.executeQuery(query);
            
            // Creates a String array with the columns description
            String[] cols = {
                "NOME",
                "QTD PRODUTO"
            };
            
            // Adds the Cols Description into ArrayList<String[]>
            report.add(cols);
                        
            // Adds the Data into ArrayList<String[]>
            String[] str;
            while(rs.next()){
                str = new String[cols.length];
                for(int i = 0; i < cols.length; i++){
                    str[i] = rs.getString(cols[i]);
                }
                report.add(str);
            }            
        } 
        // Prints a message if an error occurrs
        catch(Exception e){
            System.out.println(e.getMessage());            
            System.out.println(query);
            return null;
        }
                
        // Returns the Report Data
        return report;
    }    
}
