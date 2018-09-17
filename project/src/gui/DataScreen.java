package gui;

import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/*
    DataScreen:
        Renders the screen with a table showing some structured data.
 */
public class DataScreen extends JFrame {          
    // Java Swing Object
    private JTable jt_data;
    
    // Table Data
    private String[] t_columns;
    private Object[][] t_data;
    
    // Constructor - Initializes the Dialog
    public DataScreen(ArrayList<String[]> data){
        super();
        this.setupData(data);
        this.setupScreen();
    } 
    
    // Sets up the necessary data to be shown on screen
    private void setupData(ArrayList<String[]> data){        
        this.t_columns = data.get(0);
        this.t_data = new Object[data.size() - 1][this.t_columns.length];        
        for(int i = 0; i < this.t_data.length; i++) this.t_data[i] = data.get(i+1);        
    }
     
    // Sets up the screen settings
    private void setupScreen(){        
        // Creates and adds the Table to Screen
        this.jt_data = new JTable(this.t_data, this.t_columns);        
        this.add(new JScrollPane(this.jt_data));
                
        // Sets some screen parameters
        this.setTitle("Report Screen");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);       
        this.pack();
        
        // Places the Screen on center
        this.setLocationRelativeTo(null);
    }
}
