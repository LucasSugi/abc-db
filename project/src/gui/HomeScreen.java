package gui;

import app.Application;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/*
    HomeScreen:
        Renders the users home screen that enables them to choose the
        operations to be done.
*/
public class HomeScreen extends JFrame implements ActionListener {
    // Application
    private final Application app;
    
    // Java Swing Objects
    private JPanel jp;      
    private JLabel jl_logoArea;
    private JLabel jl_functionalArea;    
    private JButton jb_report;
    private JButton jb_overview;
    private JButton jb_simulate;
    private JButton jb_close;
    private JButton jb_logout;
    
    // Constructor - Sets the screen settings
    public HomeScreen(Application app){
        super();      
        this.app = app;
        this.setupScreen();
    }
    
    // Sets up the screen settings
    private void setupScreen(){
        // Sets some screen parameters
        this.setTitle("Home Screen");
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
        this.setupFunctionalArea();
        this.setupWindowArea();
    }
    
    // Configures the Logo Area
    private void setupLogoArea(){
        this.jl_logoArea = Utils.createJComponent(Utils.width/2 - 112, 32, 225, 150, 
                new ImageIcon(Utils.getImage("images/logo.png")));
        this.jp.add(this.jl_logoArea);
    }        
    
    // Configures the area with the features
    private void setupFunctionalArea(){
        // Configures the Functional Area
        this.jl_functionalArea = Utils.createJComponent(112, 200, 800, 300, 
                new ImageIcon(Utils.getImage("images/function_area.png")));
        this.jp.add(this.jl_functionalArea);        
        
        // Configures the reports button        
        this.jb_report = Utils.createJComponent(300, 100, 200, 150, 
                new ImageIcon(Utils.getImage("images/report_button.png")), this, "report");
        this.jl_functionalArea.add(this.jb_report);
                
        // Configures the overview button
        if(!(this.app.getUserType() == 1) && !(this.app.getUserType() == 2)){
            this.jb_overview = Utils.createJComponent(60, 100, 200, 150, 
                new ImageIcon(Utils.getImage("images/overview_button.png")), this, "overview");
            this.jl_functionalArea.add(this.jb_overview);
        }
                
        // Configures the simulate button
        if(!(this.app.getUserType() == 1)){
            this.jb_simulate = Utils.createJComponent(540, 100, 200, 150, 
                new ImageIcon(Utils.getImage("images/simulate_button.png")), this, "simulate");
            this.jl_functionalArea.add(this.jb_simulate);
        }        
    }
    
    // Configures the Window Area
    private void setupWindowArea(){
        // Configures close button
        this.jb_close = Utils.createJComponent(Utils.width - 48, 16, 32, 32, 
                new ImageIcon("images/close_button.png"), this, "close");
        this.jp.add(this.jb_close);
        
        // Configures logout button
        this.jb_logout = Utils.createJComponent(Utils.width - 90, 16, 32, 32,
                new ImageIcon("images/logout_button.png"), this, "logout");
        this.jp.add(this.jb_logout);
    }                        
    
    // Handles the Report Click
    private void handleReportClick(){
        System.out.println("Report Click...");
        
        // Hides this Screen
        this.setVisible(false);        
        // Creates and Shows the Report Screen
        ReportScreen r = new ReportScreen(this.app);            
        r.setVisible(true);        
        // Destroys this Screen
        this.handleClose();
    }
    
    // Handles the Overview Click
    private void handleOverviewClick(){
        System.out.println("Overview Click...");
        
        // Hides this Screen
        this.setVisible(false);        
        // Creates and Shows the Overview Screen
        OverviewScreen o = new OverviewScreen(this.app);            
        o.setVisible(true);        
        // Destroys this Screen
        this.handleClose();
    }
    
    // Handles the Simulate Click
    private void handleSimulateClick(){
        System.out.println("Simulate Click...");
        
        // Hides this Screen
        this.setVisible(false);        
        // Creates and Shows the Simulate Screen
        SimulateScreen s = new SimulateScreen(this.app);            
        s.setVisible(true);        
        // Destroys this Screen
        this.handleClose();
    }
    
    // Handles the User Logout
    private void handleLogout(){
        System.out.println("Logging out...");
        
        this.app.setHasProductSimulation(false);
        this.app.setHasSubcategorySimulation(false);
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
        } else if("overview".equals(e.getActionCommand())){
            this.handleOverviewClick();
        } else if("report".equals(e.getActionCommand())){
            this.handleReportClick();
        } else if("simulate".equals(e.getActionCommand())){
            this.handleSimulateClick();
        }
    }
}
