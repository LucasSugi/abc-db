package gui;

import app.Application;
import app.Person;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/*
    StartScreen:
        Renders the start screen that enables the users login the system.
*/
public class StartScreen extends JFrame implements ActionListener {          
    // Application
    private final Application app;
    
    // Java Swing Objects
    private JPanel jp;            
    private JLabel jl_image;
    private JLabel jl_loginArea;
    private JLabel jl_user;
    private JLabel jl_password;
    private JTextField jtf_user;
    private JTextField jtf_password;    
    private JButton jb_login;
    private JButton jb_close;    
    
    // Constructor - Sets the screen settings
    public StartScreen(Application app){
        super();                      
        this.app = app;
        this.setupScreen();
    }
    
    // Sets up the screen settings
    private void setupScreen(){
        // Sets some screen parameters
        this.setTitle("Start Screen");
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
        this.setupLoginArea();
        this.setupWindowArea();
    }
    
    // Configures the Logo Area
    private void setupLogoArea(){
        this.jl_image = Utils.createJComponent(10, 130, 500, 300, 
                new ImageIcon(Utils.getImage("images/logo_big.png")));
        this.jp.add(this.jl_image);
    }
    
    // Sets up the Login Area
    private void setupLoginArea(){
        // Configures the login Area
        this.jl_loginArea = Utils.createJComponent(540, 100, 450, 350, 
                new ImageIcon(Utils.getImage("images/login_area.png")));
        this.jp.add(this.jl_loginArea);
        
        // Configures user label
        this.jl_user = Utils.createJComponent(20, 70, 410, 40, "Usuário");
        this.jl_loginArea.add(this.jl_user);
        
        // Configures user text field
        this.jtf_user = Utils.createJComponent(20, 110, 410, 40);
        this.jl_loginArea.add(this.jtf_user);
        
        // Configures password label
        this.jl_password = Utils.createJComponent(20, 150, 410, 40, "Senha");
        this.jl_loginArea.add(this.jl_password);
        
        // Configures password text field
        this.jtf_password = Utils.createJComponent(20, 190, 410, 40, true);
        this.jl_loginArea.add(this.jtf_password);
        
        // Configures login button
        this.jb_login = Utils.createJComponent(135, 260, 180, 50, 
                new ImageIcon("images/login_button.png"), this, "login");
        this.jl_loginArea.add(this.jb_login);
    }
    
    // Configures the Window Area
    private void setupWindowArea(){        
        // Configures close button
        this.jb_close = Utils.createJComponent(Utils.width - 48, 16, 32, 32, 
                new ImageIcon("images/close_button.png"), this, "close");
        this.jp.add(this.jb_close);
    }        
    
    // Handles the User Loggin
    public void handleLogin(){                
        // Verifies if fields are filled
        if(this.jtf_user.getText().isEmpty() || this.jtf_password.getText().isEmpty() || !Person.validateUserPassword(this.jtf_user.getText(), this.jtf_password.getText(),this.app)){
            JOptionPane.showMessageDialog(null, "Invalid Username and/or Password", "Error", JOptionPane.ERROR_MESSAGE);
        } else if(this.app.getUserType() == 4){
            JOptionPane.showMessageDialog(null, "Not Authorized", "Error", JOptionPane.ERROR_MESSAGE);
        }
        // Values are valid
        else {
            System.out.println("Logging In...");
            
            // Hides this Screen
            this.setVisible(false);
            // Creates and Shows the Home Screen
            HomeScreen h = new HomeScreen(this.app);            
            h.setVisible(true);
            // Destroys this Screen
            this.handleClose();
        } 
    }
    
    // Handles the Window Closing
    public void handleClose(){
        System.out.println("Closing...");
        
        // Destroys the window and cleans it to the operating system
        this.dispose();
    }
    
    // Handles the actions triggered by the buttons
    @Override
    public void actionPerformed(ActionEvent e) {
        if("close".equals(e.getActionCommand())){
            this.handleClose();
        } else if("login".equals(e.getActionCommand())){
            this.handleLogin();
        }
    }
    
    // Main
    public static void main(String[] args){                
        StartScreen s = new StartScreen(new Application());
        s.setVisible(true);
    }
}
