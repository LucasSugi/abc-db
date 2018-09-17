package gui;

import app.Application;
import app.Report;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/*
    ReportScreen:
        Renders the screen that allows user to generate reports.
*/
public class ReportScreen extends JFrame implements ActionListener, ItemListener {
    // Application
    private final Application app;
    private final Report report;
    
    // Java Swing Objects
    private JPanel jp;      
    private JLabel jl_logoArea;
    private JLabel jl_reportArea;
    private JComboBox jcb_report;
    private JLabel jl_filter1;
    private JLabel jl_filter2;
    private JTextField jtf_filter1;
    private JTextField jtf_filter2;
    private JButton jb_return;
    private JButton jb_close;
    private JButton jb_logout;
    private JButton jb_generate;
    
    // Constructor - Sets the screen settings
    public ReportScreen(Application app){
        super();                  
        this.app = app;
        this.report = new Report(this.app);
        this.setupScreen();
    }
    
    // Sets up the screen settings
    private void setupScreen(){
        // Sets some screen parameters
        this.setTitle("Report Screen");
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
        this.setupReportArea();
        this.setupWindowArea();
    }
    
    // Configures the Logo Area
    private void setupLogoArea(){
        this.jl_logoArea = Utils.createJComponent(Utils.width/2 - 112, 32, 225, 150, 
                new ImageIcon(Utils.getImage("images/logo.png")));
        this.jp.add(this.jl_logoArea);
    }
    
    // Configures the Report Area
    private void setupReportArea(){
        this.jl_reportArea = Utils.createJComponent(112, 200, 800, 300, 
                new ImageIcon(Utils.getImage("images/report_area.png")));
        this.jp.add(this.jl_reportArea);
                
        // Configures the Combo Box with the possible reports to be done
        this.setupReportComboBox();
        
        // Configures the Report Filters
        this.setupReportFilters();
        
        // Configures the Button to generate the chosen report
        this.jb_generate = Utils.createJComponent(310, 220, 180, 50, 
                new ImageIcon(Utils.getImage("images/generate_button.png")), this, "generate");
        this.jl_reportArea.add(this.jb_generate);
    }
    
    // Configures the Report ComboBox
    private void setupReportComboBox(){        
        // Gets the possible reports
        String[] possibleReports = new String[Report.possibleReports.length];
        for(int i = 0; i < Report.possibleReports.length; i++){
            possibleReports[i] = Report.possibleReports[i][0];
        }
        
        // Configures the ComboBox
        this.jcb_report = new JComboBox(possibleReports);
        this.jcb_report.setBounds(100, 80, 600, 30);
        this.jcb_report.setFont(new Font("Dialog", Font.PLAIN, 20));
        ((JLabel)this.jcb_report.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);        
        this.jcb_report.addItemListener(this);
        this.jcb_report.setSelectedIndex(0);
        this.jl_reportArea.add(this.jcb_report);
    }
    
    // Configures the Report Filters parameters
    private void setupReportFilters(){
        // Configures the Labels that shows the possible filters to report
        this.jl_filter1 = Utils.createJComponent(100, 125, 200, 35, "");
        this.jl_filter1.setHorizontalAlignment(SwingConstants.CENTER);
        this.jl_filter1.setFont(new Font("dialog", Font.PLAIN, 20));
        this.jl_reportArea.add(this.jl_filter1);
        this.jl_filter2 = Utils.createJComponent(100, 170, 200, 35, "");
        this.jl_filter2.setHorizontalAlignment(SwingConstants.CENTER);
        this.jl_filter2.setFont(new Font("dialog", Font.PLAIN, 20));
        this.jl_reportArea.add(this.jl_filter2);
        
        // Configures the TextFields that enables user to insert filters to report
        this.jtf_filter1 = Utils.createJComponent(310, 125, 390, 35);        
        this.jl_reportArea.add(this.jtf_filter1);
        this.jtf_filter2 = Utils.createJComponent(310, 170, 390, 35);
        this.jl_reportArea.add(this.jtf_filter2);
        
        // Configures the Filters data
        this.setReportFilters(0);
    }
    
    // Configures the ReportFilters Content with data from the Reports[index]
    private void setReportFilters(int index){
        if(Report.possibleReports[index].length > 1){
            this.jl_filter1.setText(Report.possibleReports[index][1]);
            this.jl_filter1.setVisible(true);
            this.jtf_filter1.setVisible(true);
            if(Report.possibleReports[index].length > 2){
                this.jl_filter2.setText(Report.possibleReports[index][2]);
                this.jl_filter2.setVisible(true);
                this.jtf_filter2.setVisible(true);
            } else {
                this.jl_filter2.setVisible(false);
                this.jtf_filter2.setVisible(false);
            }
        } else {
            this.jl_filter1.setVisible(false);
            this.jtf_filter1.setVisible(false);
            this.jl_filter2.setVisible(false);                
            this.jtf_filter2.setVisible(false);
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
    
    // Handles the Report Generation
    private void handleGenerate(){
        System.out.println("Generating report: " + this.jcb_report.getSelectedIndex());
        
        // Gets the report data according to the user choose
        ArrayList<String[]> reportData = this.report.generateReport(this.jcb_report.getSelectedIndex(), this.jtf_filter1.getText(), this.jtf_filter2.getText());
        
        // Verifies if the Report was generated successfully
        if(reportData != null && reportData.size() > 1){
            // Show the dialog that structures the reportData
            DataScreen dataScreen = new DataScreen(reportData);
            dataScreen.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Nenhum registro foi encontrado!", "Erro", JOptionPane.ERROR_MESSAGE);
        }               
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
        } else if("generate".equals(e.getActionCommand())){
            this.handleGenerate();
        }
    }
    
    // Handles the changes on ComboBox
    @Override
    public void itemStateChanged(ItemEvent e) {
        for(int i = 0; i < Report.possibleReports.length; i++){
            if(e.getItem().equals(Report.possibleReports[i][0])){
                this.setReportFilters(i);
                break;
            }
        }        
    }
}
