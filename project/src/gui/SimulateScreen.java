package gui;

import app.Application;
import app.Simulate;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/*
    SimulateScreen:
        Renders the screen that allows user to make simulations with the
        data stored in database.
*/
public class SimulateScreen extends JFrame implements ActionListener, ItemListener {
    // Application
    private final Application app;
    private final Simulate simulate;
    
    // Java Swing Objects
    private JPanel jp;      
    private JLabel jl_logoArea;
    private JLabel jl_simulateArea;
    private JComboBox jcb_simulate;
    private JComboBox jcb_simulateObject;
    private JLabel jl_field1;
    private JLabel jl_field2;
    private JTextField jtf_field1;
    private JTextField jtf_field2;
    private JButton jb_simulate;
    private JButton jb_reset;
    private JButton jb_return;
    private JButton jb_close;
    private JButton jb_logout;
    
    // Constructor - Sets the screen settings
    public SimulateScreen(Application app){
        super();                       
        this.app = app;
        this.simulate = new Simulate(this.app);
        this.setupScreen();
    }
    
    // Sets up the screen settings
    private void setupScreen(){
        // Sets some screen parameters
        this.setTitle("Simulate Screen");
        this.setLayout(null);
        this.setUndecorated(true);
        this.setResizable(false);
        this.setSize(Utils.width, Utils.height);        
        
        // Sets up the Screen Components
        this.setupComponents();
        
        // Places the Screen on center
        this.setLocationRelativeTo(null);
    }
    
    // Sets up the screen components settings
    private void setupComponents(){
        // Configures JPanel
        this.jp = (JPanel) this.getContentPane();
        this.jp.setBackground(new Color(195, 214, 219));        
        
        // Configures the components
        this.setupLogoArea();
        this.setupSimulateArea();
        this.setupWindowArea();
    }
    
    // Configures the Logo Area
    private void setupLogoArea(){
        this.jl_logoArea = Utils.createJComponent(Utils.width/2 - 112, 32, 225, 150, 
                new ImageIcon(Utils.getImage("images/logo.png")));
        this.jp.add(this.jl_logoArea);
    }
    
    // Configures the Simulate Area
    private void setupSimulateArea(){
        this.jl_simulateArea = Utils.createJComponent(112, 200, 800, 300, 
                new ImageIcon(Utils.getImage("images/simulate_area.png")));
        this.jp.add(this.jl_simulateArea);
        
        // Configures the Combo Box with the possible simulations to be done
        this.setupSimulateComboBox();
        
        // Configures the fields to perform the simulations
        this.setupSimulateFields();
        
        // Configures the Button to simulate
        this.jb_simulate = Utils.createJComponent(210, 220, 180, 50, 
                new ImageIcon(Utils.getImage("images/simulatedata_button.png")), this, "simulate");
        this.jl_simulateArea.add(this.jb_simulate);
        
        // Configures the Button to reset the database
        this.jb_reset = Utils.createJComponent(410, 220, 180, 50, 
                new ImageIcon(Utils.getImage("images/undodata_button.png")), this, "reset");
        this.jl_simulateArea.add(this.jb_reset);
    }
    
    // Configures the Simulate ComboBox
    private void setupSimulateComboBox(){        
        // Gets the possible simulations
        String[] possibleSimulations = new String[Simulate.possibleSimulations.length];
        for(int i = 0; i < Simulate.possibleSimulations.length; i++){
            possibleSimulations[i] = Simulate.possibleSimulations[i][0];
        }
                
        // Configures the Simulate ComboBox
        this.jcb_simulate = new JComboBox(possibleSimulations);
        this.jcb_simulate.setBounds(50, 60, 200, 30);
        this.jcb_simulate.setFont(new Font("Dialog", Font.PLAIN, 20));
        ((JLabel)this.jcb_simulate.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);        
        this.jcb_simulate.addItemListener(this);       
        this.jcb_simulate.setSelectedIndex(0);
        this.jl_simulateArea.add(this.jcb_simulate);
        
        // Configures the SimulateObject ComboBox
        DefaultComboBoxModel model = new DefaultComboBoxModel(this.simulate.getPossibleProducts());
        this.jcb_simulateObject = new JComboBox();
        this.jcb_simulateObject.setBounds(260, 60, 500, 30);
        this.jcb_simulateObject.setFont(new Font("Dialog", Font.PLAIN, 20));
        ((JLabel)this.jcb_simulateObject.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);        
        this.jcb_simulateObject.setModel(model);
        this.jcb_simulateObject.addItemListener(this);
        this.jcb_simulateObject.setSelectedIndex(0);        
        this.jl_simulateArea.add(this.jcb_simulateObject);
    }
    
    // Configures the data fields to simulate
    private void setupSimulateFields(){
        // Configures the Labels that shows the possible filters to report
        this.jl_field1 = Utils.createJComponent(100, 105, 200, 35, "");
        this.jl_field1.setHorizontalAlignment(SwingConstants.CENTER);
        this.jl_field1.setFont(new Font("dialog", Font.PLAIN, 20));
        this.jl_simulateArea.add(this.jl_field1);
        this.jl_field2 = Utils.createJComponent(100, 150, 200, 35, "");
        this.jl_field2.setHorizontalAlignment(SwingConstants.CENTER);
        this.jl_field2.setFont(new Font("dialog", Font.PLAIN, 20));
        this.jl_simulateArea.add(this.jl_field2);
        
        // Configures the TextFields that enables user to insert filters to report
        this.jtf_field1 = Utils.createJComponent(310, 105, 390, 35);        
        this.jl_simulateArea.add(this.jtf_field1);
        this.jtf_field2 = Utils.createJComponent(310, 150, 390, 35);
        this.jl_simulateArea.add(this.jtf_field2);
        
        // Configures the Fields data
        this.setSimulateFields(0, "Adjustable Race");
    }
    
    // Configures the Fields data according to selected
    private void setSimulateFields(int simIndex, String item){        
        // Decides if shows or no the fields
        if(Simulate.possibleSimulations[simIndex].length > 1){
            this.jl_field1.setText(Simulate.possibleSimulations[simIndex][1]);
            this.jl_field1.setVisible(true);
            this.jtf_field1.setVisible(true);
            if(Simulate.possibleSimulations[simIndex].length > 2){
                this.jl_field2.setText(Simulate.possibleSimulations[simIndex][2]);
                this.jl_field2.setVisible(true);
                this.jtf_field2.setVisible(true);
            } else {
                this.jl_field2.setVisible(false);
                this.jtf_field2.setVisible(false);
            }
        } else {
            this.jl_field1.setVisible(false);
            this.jtf_field1.setVisible(false);
            this.jl_field2.setVisible(false);
            this.jtf_field2.setVisible(false);
        }
        
        // Fills the JTextFields according to data
        if(simIndex == 0){
            if(!item.equals("PRODUTO")){
                this.jtf_field1.setText(this.simulate.getValue(simIndex, 1, item));
                this.jtf_field2.setText(this.simulate.getValue(simIndex, 2, item));
            } else {
                this.jtf_field1.setText(this.simulate.getValue(simIndex, 1, "Adjustable Race"));
                this.jtf_field2.setText(this.simulate.getValue(simIndex, 2, "Adjustable Race"));
            }
        } else if(simIndex == 1){
            if(!item.equals("SUBCATEGORIA")){
                this.jtf_field1.setText(this.simulate.getValue(simIndex, 1, item));                
            } else {
                this.jtf_field1.setText(this.simulate.getValue(simIndex, 1, "Mountain Bikes"));
            }
        }           
    }
    
    // Configures the Window Area
    private void setupWindowArea(){
        // Configures return button
        this.jb_return = Utils.createJComponent(16, 16, 32, 32, 
                new ImageIcon("images/return_button.png"), this, "return");
        this.jp.add(this.jb_return);
        
        // Configures close button
        this.jb_close = Utils.createJComponent(Utils.width - 48, 16, 32, 32, 
                new ImageIcon("images/close_button.png"), this, "close");
        this.jp.add(this.jb_close);
        
        // Configures logout button
        this.jb_logout = Utils.createJComponent(Utils.width - 90, 16, 32, 32,
                new ImageIcon("images/logout_button.png"), this, "logout");
        this.jp.add(this.jb_logout);
    }        
    
    // Handles the Product Simulation Screen
    private void handleProduct(String item){
        DefaultComboBoxModel model = new DefaultComboBoxModel(this.simulate.getPossibleProducts());
        this.jcb_simulateObject.setVisible(true);
        this.jcb_simulateObject.setModel(model);
        this.setSimulateFields(0, item);
    }
    
    // Handles the Subcategory Simulation Screen
    private void handleSubcategory(String item){
        DefaultComboBoxModel model = new DefaultComboBoxModel(this.simulate.getPossibleSubcategories());
        this.jcb_simulateObject.setVisible(true);
        this.jcb_simulateObject.setModel(model);
        this.setSimulateFields(1, item);
    }
    
    // Handles the Simulate Click
    private void handleSimulate(){
        System.out.println("Simulating...");
        
        // Sends a message to try to do a simulation into database
        boolean success = this.simulate.simulate(this.jcb_simulate.getSelectedItem().toString(), this.jcb_simulateObject.getSelectedItem().toString(), this.jtf_field1.getText(), this.jtf_field2.getText());
        
        // Shows a message if could not be done
        if(!success)
            JOptionPane.showMessageDialog(null, "Não foi possível fazer a simulação desejada!", "Erro", JOptionPane.ERROR_MESSAGE);        
    }
    
    // Handles the Reset Click
    private void handleReset(){
        System.out.println("Reseting...");                
        
        // Sends a message to try to reset the simulations done over the database
        this.simulate.reset(this.jcb_simulate.getSelectedItem().toString());
        this.handleReturn();
    }
    
    // Handles the Screen Return
    private void handleReturn(){
        System.out.println("Returning...");
        
        // Hides this Screen
        this.setVisible(false);        
        // Creates and Shows the Home Screen
        HomeScreen h = new HomeScreen(this.app);            
        h.setVisible(true);        
        // Destroys this Screen
        this.handleClose();
    }
    
    // Handles the User Logout
    private void handleLogout(){
        System.out.println("Logging out...");
        
        // Hides this Screen
        this.setVisible(false);        
        // Creates and Shows the Start Screen
        StartScreen s = new StartScreen(this.app);            
        s.setVisible(true);        
        // Destroys this Screen
        this.handleClose();
    }
    
    // Handles the Window Closing
    private void handleClose(){
        System.out.println("Closing...");
        
        // Destroys the window and cleans it to the operating system
        this.dispose();
    }
    
    // Handles the actions triggered by the buttons
    @Override
    public void actionPerformed(ActionEvent e) {
        if("close".equals(e.getActionCommand())){
            this.handleClose();
        } else if("logout".equals(e.getActionCommand())){
            this.handleLogout();
        } else if("return".equals(e.getActionCommand())){
            this.handleReturn();
        } else if("simulate".equals(e.getActionCommand())){
            this.handleSimulate();
        } else if("reset".equals(e.getActionCommand())){            
            this.handleReset();
        }
    }
    
    // Handles the changes on ComboBox
    @Override
    public void itemStateChanged(ItemEvent e) {      
        if(e.getStateChange() == ItemEvent.SELECTED){                            
            if("PRODUTO".equals(e.getItem())){
                this.handleProduct(e.getItem().toString());
            } else if("SUBCATEGORIA".equals(e.getItem())){
                this.handleSubcategory(e.getItem().toString());
            } else if(this.jcb_simulate.getSelectedIndex() == 0){
                this.setSimulateFields(0, e.getItem().toString());
            } else if(this.jcb_simulate.getSelectedIndex() == 1){
                this.setSimulateFields(1, e.getItem().toString());
            }
        }
    }
}
