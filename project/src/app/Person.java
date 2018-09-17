package app;

import java.sql.ResultSet;
import java.util.ArrayList;

/*
    Person:
        Class responsible to handles the operations involving Person Entity.
*/
public class Person {      
    
    // Validates an user/password according to the database
    public static boolean validateUserPassword(String user, String password, Application app){                
        ArrayList<String> id_pessoa = new ArrayList<>();
        ArrayList<String> email = new ArrayList<>();
        ArrayList<String> senha = new ArrayList<>();
        
        // Gets all person's data
        try {            
            ResultSet rs = app.executeQuery("SELECT ID_PESSOA,EMAIL,SENHA FROM PESSOA");
            while(rs.next()){
                id_pessoa.add(rs.getString("ID_PESSOA"));
                email.add(rs.getString("EMAIL"));
                senha.add(rs.getString("SENHA"));
            }                
        } 
        // Returns null if an error occurs
        catch(Exception e){
            System.out.println(e.getMessage());
            return false;
        }            

        //Verify if user exist
        for(int i=0;i<email.size();i++){
            if(user.equals(email.get(i))){
                if(password.equals(senha.get(i))){                    
                    app.setIdUser(Integer.parseInt(id_pessoa.get(i)));
                    app.setUserType();
                    app.updateLog();
                    return true;
                } else return false;
            }
        }
        return false;
    }
}