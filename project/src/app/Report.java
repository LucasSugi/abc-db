package app;

import java.sql.ResultSet;
import java.util.ArrayList;

/*
    Report:
        Class that interacts with the data and UI related to the Reports.
*/
public class Report {          
    
    // Stores the application
    private final Application app;
    
    // Initializes the Report
    public Report(Application app){
        this.app = app;
    }
    
    // Stores the possible reports (move from here)
    public static final String[][] possibleReports = {
        {"CLIENTES COM CARTÃO DE CRÉDITO VENCIDO", "CLIENTE"},
        {"HISTÓRICO DE DEPARTAMENTO DOS FUNCIONÁRIOS", "FUNCIONÁRIO", "DEPARTAMENTO"},
        {"DADOS DE FRETE", "ANO"},
        {"INFORMAÇÕES DAS VENDAS", "ANO"},
        {"15 PRODUTOS MAIS VENDIDOS", "SEMESTRE", "ANO"},
        {"PARES DE PRODUTOS MAIS VENDIDOS JUNTOS", "QUANTIDADE"},
        {"INFORMAÇOES DAS VENDAS POR PAÍS", "ANO"}
    };
    
    // Generates the reports
    public ArrayList<String[]> generateReport(int reportId, String filter1, String filter2){                
        // Generates and returns the correct report
        if(reportId == 0){
            return this.generateReport0(filter1);
        } else if(reportId == 1){
            return this.generateReport1(filter1, filter2);
        } else if(reportId == 2){
            return this.generateReport2(filter1);
        } else if(reportId == 3){
            return this.generateReport3(filter1);
        } else if(reportId == 4){
            return this.generateReport4(filter1, filter2);
        } else if(reportId == 5){
            return this.generateReport5(filter1);
        } else if(reportId == 6){
            return this.generateReport6(filter1);
        }
        // Returns null if the report doesnt exists
        return null;
    }
    
    // Generates the report 0
    private ArrayList<String[]> generateReport0(String filter1){
        ArrayList<String[]> report = new ArrayList<>();
        String query, queryWhere;
        
        // Mounts the suitable where clause        
        if("".equals(filter1)){ // Without filter
            queryWhere = "WHERE ((C.CARTAO_VALIDADE_ANO < (SELECT EXTRACT(YEAR FROM SYSDATE) FROM DUAL)) "
                            + "OR (C.CARTAO_VALIDADE_ANO = (SELECT EXTRACT(YEAR FROM SYSDATE) FROM DUAL) "
                            + "AND C.CARTAO_VALIDADE_MES < (SELECT EXTRACT(MONTH FROM SYSDATE) FROM DUAL)))\n";
        } else { // Filtered
            queryWhere = "WHERE ((C.CARTAO_VALIDADE_ANO < (SELECT EXTRACT(YEAR FROM SYSDATE) FROM DUAL)) "
                            + "OR (C.CARTAO_VALIDADE_ANO = (SELECT EXTRACT(YEAR FROM SYSDATE) FROM DUAL) "
                            + "AND C.CARTAO_VALIDADE_MES < (SELECT EXTRACT(MONTH FROM SYSDATE) FROM DUAL))) "
                            + "AND P.NOME LIKE '%" + filter1 + "%'\n";            
        }
        
        // Mounts the Query
        query = "SELECT P.NOME AS CLIENTE, "
                        + "P.EMAIL AS EMAIL, "
                        + "MAX(V.DATA_VENDA) AS \"DATA ULTIMA VENDA\", "
                        + "C.CARTAO_TIPO AS \"TIPO CARTAO\", "
                        + "C.CARTAO_NUMERO AS \"NUMERO CARTAO\", "
                        + "C.CARTAO_VALIDADE_MES AS \"MES DE VALIDADE CARTAO\", "
                        + "C.CARTAO_VALIDADE_ANO AS \"ANO DE VALIDADE CARTAO\"\n" +
                    "FROM CLIENTE C JOIN PESSOA P ON C.ID_PESSOA = P.ID_PESSOA "
                        + "JOIN VENDA V ON V.ID_CLIENTE = C.ID_CLIENTE\n" +                    
                    queryWhere +
                    "GROUP BY P.NOME, P.EMAIL, C.CARTAO_TIPO, C.CARTAO_NUMERO, C.CARTAO_VALIDADE_MES, C.CARTAO_VALIDADE_ANO";
              
        try{
            // Executes the Query
            ResultSet rs = this.app.executeQuery(query);
            
            // Creates a String array with the columns description
            String[] cols = {
                "CLIENTE",
                "EMAIL",
                "DATA ULTIMA VENDA",
                "TIPO CARTAO",
                "NUMERO CARTAO",
                "MES DE VALIDADE CARTAO",
                "ANO DE VALIDADE CARTAO"
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
        
    // Generates the report 1
    private ArrayList<String[]> generateReport1(String filter1, String filter2){
        ArrayList<String[]> report = new ArrayList<>();
        String query, queryWhere;
        
        // Mounts the suitable where clause
        if( !("".equals(filter1)) && !("".equals(filter2))) { // Without filter
            queryWhere = "WHERE (P.NOME LIKE '%"+filter1+"%' AND D.NOME ='"+filter2+"')";
        } else if(!("".equals(filter1))){ // Filtered    
            queryWhere = "WHERE P.NOME LIKE '%"+filter1+"%'";            
        }
        else if(!("".equals(filter2))){
            queryWhere = "WHERE D.NOME ='"+filter2+"'";
        }
        else{
            queryWhere = "";
        }
             
        // Mounts the Query
        query = "SELECT P.NOME AS EMPREGADO, D.NOME AS DEPARTAMENTO, H.TURNO, H.DATA_ENTRADA AS \"DATA ENTRADA\", "
                + "CASE WHEN TO_CHAR(H.DATA_SAIDA) IS NULL THEN "
                + "'Ativo' "
                + "ELSE TO_CHAR(H.DATA_SAIDA) END AS \"DATA SAIDA\" FROM\n" +
                "    PESSOA P JOIN EMPREGADO E ON P.ID_PESSOA = E.ID_PESSOA \n" +
                "    JOIN HISTORICODEPARTAMENTO H ON H.ID_PESSOA = P.ID_PESSOA\n" +
                "    JOIN DEPARTAMENTO D ON D.ID_DEPARTAMENTO = H.ID_DEPARTAMENTO " + queryWhere;
        
        try{
            // Executes the Query
            ResultSet rs = this.app.executeQuery(query);
            
            // Creates a String array with the columns description
            String[] cols = {
                "EMPREGADO",
                "DEPARTAMENTO",
                "TURNO",
                "DATA ENTRADA",
                "DATA SAIDA"
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
    private ArrayList<String[]> generateReport2(String filter1){
        ArrayList<String[]> report = new ArrayList<>();
        String query, queryWhere;
        
        // Mounts the suitable where clause
        if("".equals(filter1)){ // Without filter
            queryWhere = "";
        } else { // Filtered    
            queryWhere = "WHERE EXTRACT(YEAR FROM DATA_ENVIO) = '"+filter1+"'";            
        }
        
        // Mounts the Query
        query = "SELECT ANO, MAIS_2000, MENOS_2000, MAIS_2000+MENOS_2000 AS \"TOTAL FRETE\" FROM\n" +
            "    (SELECT ANO, CASE WHEN TO_CHAR(MAIS_2000) IS NULL THEN '0' ELSE TO_CHAR(MAIS_2000) END AS MAIS_2000, CASE WHEN TO_CHAR(MENOS_2000) IS NULL THEN '0' ELSE TO_CHAR(MENOS_2000) END AS MENOS_2000  FROM\n" +
            "        (SELECT EXTRACT(YEAR FROM DATA_ENVIO) AS ANO, SUM(VALOR_FRETE) AS FRETE, CASE WHEN TOTAL > 2000 THEN '1' ELSE '0' END AS ID\n" +
            "            FROM VENDA\n" + queryWhere +
            "            GROUP BY EXTRACT(YEAR FROM DATA_ENVIO), TOTAL)\n" +
            "        PIVOT(\n" +
            "            SUM(FRETE)\n" +
            "            FOR ID IN ('1' AS MAIS_2000,'0' AS MENOS_2000)      \n" +
            "        ))\n" +
            "    ORDER BY ANO ASC";
        
        try{
            // Executes the Query
            ResultSet rs = this.app.executeQuery(query);
            
            // Creates a String array with the columns description
            String[] cols = {
                "ANO",
                "MAIS_2000",
                "MENOS_2000",
                "TOTAL FRETE"
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
        if("".equals(filter1)){ // Without filter
            query = "SELECT ANO,JANEIRO,FEVEREIRO,MARCO,ABRIL,MAIO,JUNHO,JULHO,AGOSTO,SETEMBRO,OUTUBRO,NOVEMBRO,DEZEMBRO,TOTAL,TRUNC((((TOTAL-LAG(TOTAL,1,0) OVER(ORDER BY ANO))/TOTAL)*100),2) AS PERCENTUAL FROM\n" +
"    (SELECT ANO,JANEIRO,FEVEREIRO,MARCO,ABRIL,MAIO,JUNHO,JULHO,AGOSTO,SETEMBRO,OUTUBRO,NOVEMBRO,DEZEMBRO, \n" +
"        JANEIRO+FEVEREIRO+MARCO+ABRIL+MAIO+JUNHO+JULHO+AGOSTO+SETEMBRO+OUTUBRO+NOVEMBRO+DEZEMBRO AS TOTAL\n" +
"        FROM\n" +
"        (SELECT ANO, CASE WHEN TO_CHAR(JANEIRO) IS NULL THEN '0' ELSE TO_CHAR(JANEIRO) END AS JANEIRO,\n" +
"            CASE WHEN TO_CHAR(FEVEREIRO) IS NULL THEN '0' ELSE TO_CHAR(FEVEREIRO) END AS FEVEREIRO,\n" +
"            CASE WHEN TO_CHAR(MARCO) IS NULL THEN '0' ELSE TO_CHAR(MARCO) END AS MARCO,\n" +
"            CASE WHEN TO_CHAR(ABRIL) IS NULL THEN '0' ELSE TO_CHAR(ABRIL) END AS ABRIL,\n" +
"            CASE WHEN TO_CHAR(MAIO) IS NULL THEN '0' ELSE TO_CHAR(MAIO) END AS MAIO,\n" +
"            CASE WHEN TO_CHAR(JUNHO) IS NULL THEN '0' ELSE TO_CHAR(JUNHO) END AS JUNHO,\n" +
"            CASE WHEN TO_CHAR(JULHO) IS NULL THEN '0' ELSE TO_CHAR(JULHO) END AS JULHO,\n" +
"            CASE WHEN TO_CHAR(AGOSTO) IS NULL THEN '0' ELSE TO_CHAR(AGOSTO) END AS AGOSTO,\n" +
"            CASE WHEN TO_CHAR(SETEMBRO) IS NULL THEN '0' ELSE TO_CHAR(SETEMBRO) END AS SETEMBRO,\n" +
"            CASE WHEN TO_CHAR(OUTUBRO) IS NULL THEN '0' ELSE TO_CHAR(OUTUBRO) END AS OUTUBRO,\n" +
"            CASE WHEN TO_CHAR(NOVEMBRO) IS NULL THEN '0' ELSE TO_CHAR(NOVEMBRO) END AS NOVEMBRO,\n" +
"            CASE WHEN TO_CHAR(DEZEMBRO) IS NULL THEN '0' ELSE TO_CHAR(DEZEMBRO) END AS DEZEMBRO\n" +
"            FROM\n" +
"            (SELECT EXTRACT(MONTH FROM DATA_VENDA) AS MES, EXTRACT(YEAR FROM DATA_VENDA) AS ANO,SUM(TOTAL) AS TOTAL FROM VENDA\n" +
"                        GROUP BY EXTRACT(YEAR FROM DATA_VENDA),  EXTRACT(MONTH FROM DATA_VENDA)\n" +
"                        ORDER BY EXTRACT(YEAR FROM DATA_VENDA))\n" +
"                PIVOT(\n" +
"                    SUM(TOTAL)\n" +
"                    FOR MES IN('1' AS JANEIRO,'2' AS FEVEREIRO, '3' AS MARCO, '4' AS ABRIL, '5' AS MAIO, '6' AS JUNHO,\n" +
"                    '7' AS JULHO, '8' AS AGOSTO, '9' AS SETEMBRO, '10' AS OUTUBRO, '11' AS NOVEMBRO, '12' AS DEZEMBRO)\n" +
"                )\n" +
"                ORDER BY ANO ASC))";
        } else { // Filtered    
            
            Integer filter2 = Integer.parseInt(filter1)-1;
            
            query = "SELECT * FROM\n" +
"    (SELECT ANO,JANEIRO,FEVEREIRO,MARCO,ABRIL,MAIO,JUNHO,JULHO,AGOSTO,SETEMBRO,OUTUBRO,NOVEMBRO,DEZEMBRO,TOTAL,TRUNC((((TOTAL-LAG(TOTAL,1,0) OVER(ORDER BY ANO))/TOTAL)*100),2) AS PERCENTUAL FROM\n" +
"        (SELECT ANO,JANEIRO,FEVEREIRO,MARCO,ABRIL,MAIO,JUNHO,JULHO,AGOSTO,SETEMBRO,OUTUBRO,NOVEMBRO,DEZEMBRO, \n" +
"            JANEIRO+FEVEREIRO+MARCO+ABRIL+MAIO+JUNHO+JULHO+AGOSTO+SETEMBRO+OUTUBRO+NOVEMBRO+DEZEMBRO AS TOTAL\n" +
"            FROM\n" +
"            (SELECT ANO, CASE WHEN TO_CHAR(JANEIRO) IS NULL THEN '0' ELSE TO_CHAR(JANEIRO) END AS JANEIRO,\n" +
"                CASE WHEN TO_CHAR(FEVEREIRO) IS NULL THEN '0' ELSE TO_CHAR(FEVEREIRO) END AS FEVEREIRO,\n" +
"                CASE WHEN TO_CHAR(MARCO) IS NULL THEN '0' ELSE TO_CHAR(MARCO) END AS MARCO,\n" +
"                CASE WHEN TO_CHAR(ABRIL) IS NULL THEN '0' ELSE TO_CHAR(ABRIL) END AS ABRIL,\n" +
"                CASE WHEN TO_CHAR(MAIO) IS NULL THEN '0' ELSE TO_CHAR(MAIO) END AS MAIO,\n" +
"                CASE WHEN TO_CHAR(JUNHO) IS NULL THEN '0' ELSE TO_CHAR(JUNHO) END AS JUNHO,\n" +
"                CASE WHEN TO_CHAR(JULHO) IS NULL THEN '0' ELSE TO_CHAR(JULHO) END AS JULHO,\n" +
"                CASE WHEN TO_CHAR(AGOSTO) IS NULL THEN '0' ELSE TO_CHAR(AGOSTO) END AS AGOSTO,\n" +
"                CASE WHEN TO_CHAR(SETEMBRO) IS NULL THEN '0' ELSE TO_CHAR(SETEMBRO) END AS SETEMBRO,\n" +
"                CASE WHEN TO_CHAR(OUTUBRO) IS NULL THEN '0' ELSE TO_CHAR(OUTUBRO) END AS OUTUBRO,\n" +
"                CASE WHEN TO_CHAR(NOVEMBRO) IS NULL THEN '0' ELSE TO_CHAR(NOVEMBRO) END AS NOVEMBRO,\n" +
"                CASE WHEN TO_CHAR(DEZEMBRO) IS NULL THEN '0' ELSE TO_CHAR(DEZEMBRO) END AS DEZEMBRO\n" +
"                FROM\n" +
"                (SELECT EXTRACT(MONTH FROM DATA_VENDA) AS MES, EXTRACT(YEAR FROM DATA_VENDA) AS ANO,SUM(TOTAL) AS TOTAL FROM VENDA\n" +
"                            WHERE EXTRACT(YEAR FROM DATA_VENDA) = '"+filter1+"' OR EXTRACT(YEAR FROM DATA_VENDA) = '"+filter2+"'\n" +
"                            GROUP BY EXTRACT(YEAR FROM DATA_VENDA),  EXTRACT(MONTH FROM DATA_VENDA)\n" +
"                            ORDER BY EXTRACT(YEAR FROM DATA_VENDA))\n" +
"                    PIVOT(\n" +
"                        SUM(TOTAL)\n" +
"                        FOR MES IN('1' AS JANEIRO,'2' AS FEVEREIRO, '3' AS MARCO, '4' AS ABRIL, '5' AS MAIO, '6' AS JUNHO,\n" +
"                        '7' AS JULHO, '8' AS AGOSTO, '9' AS SETEMBRO, '10' AS OUTUBRO, '11' AS NOVEMBRO, '12' AS DEZEMBRO)\n" +
"                    )\n" +
"                    ORDER BY ANO ASC)))\n" +
"        WHERE ANO = '"+filter1+"'";            
        }
        
        try{
            // Executes the Query
            ResultSet rs = this.app.executeQuery(query);
            
            // Creates a String array with the columns description
            String[] cols = {
                "ANO",
                "JANEIRO",
                "FEVEREIRO",
                "MARCO",
                "ABRIL",
                "MAIO",
                "JUNHO",
                "JULHO",
                "AGOSTO",
                "SETEMBRO",
                "OUTUBRO",
                "NOVEMBRO",
                "DEZEMBRO",
                "TOTAL",
                "PERCENTUAL"
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
    private ArrayList<String[]> generateReport4(String filter1, String filter2){
        ArrayList<String[]> report = new ArrayList<>();
        String query, queryAux, queryAux2;
        
        if(this.app.hasProductSimulation()){
            queryAux = "VIEW_PRODUTO_" + this.app.getIdUser();
        } else {
            queryAux = "PRODUTO";
        }
        
        if(this.app.hasSubcategorySimularion()){
            queryAux2 = "VIEW_SUBCATEGORIA_" + this.app.getIdUser();
        } else {
            queryAux2 = "SUBCATEGORIA";
        }
        
        // Mounts the suitable where clause
        if("".equals(filter1) && "".equals(filter2)){ // Without filter
            query = "SELECT NOME, PESO, CATEGORIA, SEMESTRE, ANO, TOTAL FROM\n" +
"    (SELECT NOME, PESO, CATEGORIA, SEMESTRE, ANO, SUM(TOTAL) AS TOTAL, ROW_NUMBER() OVER(PARTITION BY ANO,SEMESTRE ORDER BY SUM(TOTAL) DESC) AS ID FROM    \n" +
"            (SELECT P.NOME, P.PRECO, P.PESO, C.NOME AS CATEGORIA, SUM(I.TOTAL_ITEM) AS TOTAL, CASE \n" +
"                WHEN EXTRACT(MONTH FROM V.DATA_VENDA) >=1 AND EXTRACT(MONTH FROM V.DATA_VENDA) <= 6 THEN 1\n" +
"                ELSE 2 END AS SEMESTRE, EXTRACT(YEAR FROM V.DATA_VENDA) AS ANO  FROM\n" +
"                " + queryAux + " P JOIN ITEMVENDA I ON P.ID_PRODUTO = I.ID_PRODUTO\n" +
"                JOIN " + queryAux2 + " S ON P.ID_SUBCATEGORIA = S.ID_SUBCATEGORIA\n" +
"                JOIN CATEGORIA C ON C.ID_CATEGORIA = S.ID_CATEGORIA\n" +
"                JOIN VENDA V ON V.ID_VENDA = I.ID_VENDA\n" +
"                GROUP BY EXTRACT(YEAR FROM V.DATA_VENDA),EXTRACT(MONTH FROM V.DATA_VENDA), P.NOME, P.PRECO, P.PESO, C.NOME\n" +
"                ORDER BY EXTRACT(YEAR FROM V.DATA_VENDA), EXTRACT(MONTH FROM V.DATA_VENDA)\n" +
"            )GROUP BY ANO, SEMESTRE, NOME, PESO, CATEGORIA\n" +
"            ORDER BY ANO, SEMESTRE\n" +
"    )WHERE ID <= 15";
        } else { // Filtered    
            query = "SELECT NOME, PESO, CATEGORIA, SEMESTRE, ANO, TOTAL FROM\n" +
"    (SELECT NOME, PESO, CATEGORIA, SEMESTRE, ANO, SUM(TOTAL) AS TOTAL, ROW_NUMBER() OVER(PARTITION BY ANO,SEMESTRE ORDER BY SUM(TOTAL) DESC) AS ID FROM    \n" +
"            (SELECT P.NOME, P.PRECO, P.PESO, C.NOME AS CATEGORIA, SUM(I.TOTAL_ITEM) AS TOTAL, CASE \n" +
"                WHEN EXTRACT(MONTH FROM V.DATA_VENDA) >=1 AND EXTRACT(MONTH FROM V.DATA_VENDA) <= 6 THEN 1\n" +
"                ELSE 2 END AS SEMESTRE, EXTRACT(YEAR FROM V.DATA_VENDA) AS ANO  FROM\n" +
"                " + queryAux + " P JOIN ITEMVENDA I ON P.ID_PRODUTO = I.ID_PRODUTO\n" +
"                JOIN " + queryAux2 + " S ON P.ID_SUBCATEGORIA = S.ID_SUBCATEGORIA\n" +
"                JOIN CATEGORIA C ON C.ID_CATEGORIA = S.ID_CATEGORIA\n" +
"                JOIN VENDA V ON V.ID_VENDA = I.ID_VENDA\n" +
"                WHERE EXTRACT(YEAR FROM V.DATA_VENDA) = '"+filter2+"'\n" +
"                GROUP BY EXTRACT(YEAR FROM V.DATA_VENDA),EXTRACT(MONTH FROM V.DATA_VENDA), P.NOME, P.PRECO, P.PESO, C.NOME\n" +
"                ORDER BY EXTRACT(YEAR FROM V.DATA_VENDA), EXTRACT(MONTH FROM V.DATA_VENDA)\n" +
"            )\n" +
"            WHERE SEMESTRE = '"+filter1+"'\n" +
"            GROUP BY ANO, SEMESTRE, NOME, PESO, CATEGORIA\n" +
"            ORDER BY ANO, SEMESTRE\n" +
"    )WHERE ID <= 15";            
        }
        
        try{
            // Executes the Query
            ResultSet rs = this.app.executeQuery(query);
            
            // Creates a String array with the columns description
            String[] cols = {
                "NOME",
                "PESO",
                "CATEGORIA",
                "SEMESTRE",
                "ANO",
                "TOTAL"
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
    private ArrayList<String[]> generateReport5(String filter1){
        ArrayList<String[]> report = new ArrayList<>();
        String query, queryAux, queryAux2;
        
        if(this.app.hasProductSimulation()){
            queryAux = "VIEW_PRODUTO_" + this.app.getIdUser();
        } else {
            queryAux = "PRODUTO";
        }
        
        if(this.app.hasSubcategorySimularion()){
            queryAux2 = "VIEW_SUBCATEGORIA_" + this.app.getIdUser();
        } else {
            queryAux2 = "SUBCATEGORIA";
        }
        
        // Mounts the suitable where clause        
        if("".equals(filter1)){ // Without filter
            query = "SELECT PRODUTO_1,CATEGORIA_1,SUBCATEGORIA_1,SUM(TOTAL_1) AS TOTAL_1,PRODUTO_2,CATEGORIA_2,SUBCATEGORIA_2,SUM(TOTAL_2) AS TOTAL_2, COUNT(ID_VENDA) AS QUANTIDADE FROM\n" +
"    (SELECT ID_VENDA, CASE WHEN TO_NUMBER(ID_PRODUTO_1)<TO_NUMBER(ID_PRODUTO_2) THEN ID_PRODUTO_1 ELSE ID_PRODUTO_2 END AS ID_PRODUTO_1,PRODUTO_1,CATEGORIA_1,SUBCATEGORIA_1,TOTAL_1,CASE WHEN TO_NUMBER(ID_PRODUTO_1)<TO_NUMBER(ID_PRODUTO_2) THEN ID_PRODUTO_2 ELSE ID_PRODUTO_1 END AS ID_PRODUTO_2,PRODUTO_2,CATEGORIA_2,SUBCATEGORIA_2,TOTAL_2  FROM\n" +
"        (SELECT ID_VENDA,ID_PRODUTO_1,PRODUTO_1,CATEGORIA_1,SUBCATEGORIA_1,TOTAL_1,ID_PRODUTO_2,PRODUTO_2,CATEGORIA_2,SUBCATEGORIA_2,TOTAL_2 FROM\n" +
"            (SELECT ID_VENDA, SUBSTR(ID_PRODUTO, 1, INSTR(ID_PRODUTO, '-')-1) AS ID_PRODUTO_1, SUBSTR(PRODUTO, 1, INSTR(PRODUTO, '-')-1) AS PRODUTO_1, SUBSTR(CATEGORIA, 1, INSTR(CATEGORIA, '-')-1) AS CATEGORIA_1, SUBSTR(SUBCATEGORIA, 1, INSTR(SUBCATEGORIA, '-')-1) AS SUBCATEGORIA_1, SUBSTR(TOTAL, 1, INSTR(TOTAL, '-')-1) AS TOTAL_1, SUBSTR(ID_PRODUTO, INSTR(ID_PRODUTO, '-')+1) AS ID_PRODUTO_2, SUBSTR(PRODUTO, INSTR(PRODUTO, '-')+1) AS PRODUTO_2, SUBSTR(CATEGORIA, INSTR(CATEGORIA, '-')+1) AS CATEGORIA_2, SUBSTR(SUBCATEGORIA, INSTR(SUBCATEGORIA, '-')+1) AS SUBCATEGORIA_2, SUBSTR(TOTAL, INSTR(TOTAL, '-')+1) AS TOTAL_2 FROM\n" +
"                (SELECT ID_VENDA, LISTAGG(ID_PRODUTO,'-') WITHIN GROUP (ORDER BY ID_PRODUTO) AS ID_PRODUTO, LISTAGG(PRODUTO,'-') WITHIN GROUP (ORDER BY ID_PRODUTO) AS PRODUTO, LISTAGG(CATEGORIA,'-') WITHIN GROUP (ORDER BY ID_PRODUTO) AS CATEGORIA, LISTAGG(SUBCATEGORIA,'-') WITHIN GROUP (ORDER BY ID_PRODUTO) AS SUBCATEGORIA,LISTAGG(TOTAL,'-') WITHIN GROUP (ORDER BY ID_PRODUTO) AS TOTAL FROM\n" +
"                    (SELECT ID_VENDA, ID_PRODUTO, PRODUTO, CATEGORIA, SUBCATEGORIA, TOTAL FROM\n" +
"                            (SELECT V.ID_VENDA, P.ID_PRODUTO,P.NOME AS PRODUTO, C.NOME AS CATEGORIA, S.NOME AS SUBCATEGORIA, I.TOTAL_ITEM AS TOTAL, ROW_NUMBER() OVER (PARTITION BY V.ID_VENDA ORDER BY I.TOTAL_ITEM DESC) AS ID FROM\n" +
"                                " + queryAux + " P JOIN ITEMVENDA I ON I.ID_PRODUTO = P.ID_PRODUTO\n" +
"                                JOIN VENDA V ON V.ID_VENDA = I.ID_VENDA\n" +
"                                JOIN " + queryAux2 + " S ON S.ID_SUBCATEGORIA = P.ID_SUBCATEGORIA\n" +
"                                JOIN CATEGORIA C ON C.ID_CATEGORIA = S.ID_CATEGORIA)\n" +
"                            WHERE ID <= 2)\n" +
"                    GROUP BY ID_VENDA))\n" +
"            WHERE ID_PRODUTO_1 IS NOT NULL AND ID_PRODUTO_2 IS NOT NULL))\n" +
"    GROUP BY ID_PRODUTO_1,ID_PRODUTO_2,PRODUTO_1,PRODUTO_2,CATEGORIA_1,CATEGORIA_2,SUBCATEGORIA_1,SUBCATEGORIA_2";
        } else { // Filtered
            query = "SELECT * FROM\n" +
"    (SELECT PRODUTO_1,CATEGORIA_1,SUBCATEGORIA_1,SUM(TOTAL_1) AS TOTAL_1,PRODUTO_2,CATEGORIA_2,SUBCATEGORIA_2,SUM(TOTAL_2) AS TOTAL_2, COUNT(ID_VENDA) AS QUANTIDADE FROM\n" +
"        (SELECT ID_VENDA, CASE WHEN TO_NUMBER(ID_PRODUTO_1)<TO_NUMBER(ID_PRODUTO_2) THEN ID_PRODUTO_1 ELSE ID_PRODUTO_2 END AS ID_PRODUTO_1,PRODUTO_1,CATEGORIA_1,SUBCATEGORIA_1,TOTAL_1,CASE WHEN TO_NUMBER(ID_PRODUTO_1)<TO_NUMBER(ID_PRODUTO_2) THEN ID_PRODUTO_2 ELSE ID_PRODUTO_1 END AS ID_PRODUTO_2,PRODUTO_2,CATEGORIA_2,SUBCATEGORIA_2,TOTAL_2  FROM\n" +
"            (SELECT ID_VENDA,ID_PRODUTO_1,PRODUTO_1,CATEGORIA_1,SUBCATEGORIA_1,TOTAL_1,ID_PRODUTO_2,PRODUTO_2,CATEGORIA_2,SUBCATEGORIA_2,TOTAL_2 FROM\n" +
"                (SELECT ID_VENDA, SUBSTR(ID_PRODUTO, 1, INSTR(ID_PRODUTO, '-')-1) AS ID_PRODUTO_1, SUBSTR(PRODUTO, 1, INSTR(PRODUTO, '-')-1) AS PRODUTO_1, SUBSTR(CATEGORIA, 1, INSTR(CATEGORIA, '-')-1) AS CATEGORIA_1, SUBSTR(SUBCATEGORIA, 1, INSTR(SUBCATEGORIA, '-')-1) AS SUBCATEGORIA_1, SUBSTR(TOTAL, 1, INSTR(TOTAL, '-')-1) AS TOTAL_1, SUBSTR(ID_PRODUTO, INSTR(ID_PRODUTO, '-')+1) AS ID_PRODUTO_2, SUBSTR(PRODUTO, INSTR(PRODUTO, '-')+1) AS PRODUTO_2, SUBSTR(CATEGORIA, INSTR(CATEGORIA, '-')+1) AS CATEGORIA_2, SUBSTR(SUBCATEGORIA, INSTR(SUBCATEGORIA, '-')+1) AS SUBCATEGORIA_2, SUBSTR(TOTAL, INSTR(TOTAL, '-')+1) AS TOTAL_2 FROM\n" +
"                    (SELECT ID_VENDA, LISTAGG(ID_PRODUTO,'-') WITHIN GROUP (ORDER BY ID_PRODUTO) AS ID_PRODUTO, LISTAGG(PRODUTO,'-') WITHIN GROUP (ORDER BY ID_PRODUTO) AS PRODUTO, LISTAGG(CATEGORIA,'-') WITHIN GROUP (ORDER BY ID_PRODUTO) AS CATEGORIA, LISTAGG(SUBCATEGORIA,'-') WITHIN GROUP (ORDER BY ID_PRODUTO) AS SUBCATEGORIA,LISTAGG(TOTAL,'-') WITHIN GROUP (ORDER BY ID_PRODUTO) AS TOTAL FROM\n" +
"                        (SELECT ID_VENDA, ID_PRODUTO, PRODUTO, CATEGORIA, SUBCATEGORIA, TOTAL FROM\n" +
"                                (SELECT V.ID_VENDA, P.ID_PRODUTO,P.NOME AS PRODUTO, C.NOME AS CATEGORIA, S.NOME AS SUBCATEGORIA, I.TOTAL_ITEM AS TOTAL, ROW_NUMBER() OVER (PARTITION BY V.ID_VENDA ORDER BY I.TOTAL_ITEM DESC) AS ID FROM\n" +
"                                    " + queryAux + " P JOIN ITEMVENDA I ON I.ID_PRODUTO = P.ID_PRODUTO\n" +
"                                    JOIN VENDA V ON V.ID_VENDA = I.ID_VENDA\n" +
"                                    JOIN " + queryAux2 + " S ON S.ID_SUBCATEGORIA = P.ID_SUBCATEGORIA\n" +
"                                    JOIN CATEGORIA C ON C.ID_CATEGORIA = S.ID_CATEGORIA)\n" +
"                                WHERE ID <= 2)\n" +
"                        GROUP BY ID_VENDA))\n" +
"                WHERE ID_PRODUTO_1 IS NOT NULL AND ID_PRODUTO_2 IS NOT NULL))\n" +
"        GROUP BY ID_PRODUTO_1,ID_PRODUTO_2,PRODUTO_1,PRODUTO_2,CATEGORIA_1,CATEGORIA_2,SUBCATEGORIA_1,SUBCATEGORIA_2)\n" +
"        WHERE QUANTIDADE = "+filter1+"";            
        }
        
        try{
            // Executes the Query
            ResultSet rs = this.app.executeQuery(query);
            
            // Creates a String array with the columns description
            String[] cols = {
                "PRODUTO_1",
                "CATEGORIA_1",
                "SUBCATEGORIA_1",
                "TOTAL_1",
                "PRODUTO_2",
                "CATEGORIA_2",
                "SUBCATEGORIA_2",
                "TOTAL_2",
                "QUANTIDADE"
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
    
    // Generates the report 6
    private ArrayList<String[]> generateReport6(String filter1){
        ArrayList<String[]> report = new ArrayList<>();
        String query, queryWhere;
        
        // Mounts the suitable where clause        
        if("".equals(filter1)){ // Without filter
            queryWhere = "";
        } else { // Filtered
            queryWhere = "WHERE EXTRACT(YEAR FROM V.DATA_VENDA) = '"+filter1+"'";            
        }
        
        // Mounts the Query
        query = "SELECT ANO, PAIS, ESTADO, TOTAL_ESTADO AS \"TOTAL ESTADO\", TOTAL_PAIS AS \"TOTAL PAIS\" FROM\n" +
"    (SELECT ANO,PAIS,ESTADO,MAX(TOTAL_ESTADO) AS TOTAL_ESTADO,SUM(TOTAL_ESTADO) AS TOTAL_PAIS, ROW_NUMBER() OVER(PARTITION BY PAIS ORDER BY ANO DESC) AS ID FROM\n" +
"        (SELECT ANO,PAIS,FIRST_VALUE(ESTADO) OVER(PARTITION BY ANO,PAIS ORDER BY ID ASC) AS ESTADO,TOTAL_ESTADO FROM    \n" +
"            (SELECT EXTRACT(YEAR FROM V.DATA_VENDA) AS ANO, E.PAIS, E.ESTADO,SUM(V.TOTAL) AS TOTAL_ESTADO, ROW_NUMBER() OVER(PARTITION BY EXTRACT(YEAR FROM V.DATA_VENDA),PAIS ORDER BY SUM(V.TOTAL) DESC) AS ID FROM\n" +
"                    VENDA V JOIN CLIENTE C ON V.ID_CLIENTE = C.ID_CLIENTE\n" +
"                    JOIN PESSOA P ON P.ID_PESSOA = C.ID_PESSOA\n" +
"                    JOIN ENDERECO E ON E.ID_ENDERECO = P.ID_ENDERECO\n" + queryWhere +
"                    GROUP BY EXTRACT(YEAR FROM V.DATA_VENDA), PAIS,ESTADO\n" +
"                    ORDER BY EXTRACT(YEAR FROM V.DATA_VENDA),PAIS,TOTAL_ESTADO ASC\n" +
"            )\n" +
"        )\n" +
"        GROUP BY ANO,PAIS,ESTADO\n" +
"        ORDER BY ANO,PAIS\n" +
"    )WHERE ID = 1";
              
        try{
            // Executes the Query
            ResultSet rs = this.app.executeQuery(query);
            
            // Creates a String array with the columns description
            String[] cols = {
                "ANO",
                "PAIS",
                "ESTADO",
                "TOTAL ESTADO",
                "TOTAL PAIS"
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
