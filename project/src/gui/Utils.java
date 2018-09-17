package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import javax.imageio.ImageIO;

/*
    Utils:
        Class that provides settings to be used in UI Application.
*/
public class Utils {            
    /****************************************************
     * 
     * Default settings to be used by the GUI.
     * 
     ****************************************************/
    
    // Screen Width and Height
    public static final int width = 1024;
    public static final int height = 560; 
    
    // Font Default
    public static final Font font = new Font("Dialog", Font.PLAIN, 25);
    
    // Foreground Color Default
    public static final Color color = new Color(0, 91, 112);    
    
    /****************************************************
     * 
     * Some methods useful to specific parts of the GUI.
     * 
     ****************************************************/        
    
    // Gets an Image located in a given path
    public static Image getImage(String path){
        Image img = null;
        try { img = ImageIO.read(new File(path)); } 
        catch (Exception e){ System.out.println(e.getMessage()); }
        return img;
    }
    
    /****************************************************
     * 
     * JComponent Generators - Creates suitable JComponents.
     *      - JTextField: Only pass the bounds;
     *      - JLabel for text: Pass the bounds + title;
     *      - JLabel for image: Pass the bounds + image;
     *      - JButton: Pass the bounds + image + 
     *                      actionlistener + actioncommand;
     * 
     ****************************************************/        
    
    // Creates a common JTextField with suitable settings
    public static JTextField createJComponent(int x, int y, int width, int height){
        JTextField jc = new JTextField();
        jc.setFont(Utils.font);
        jc.setForeground(Utils.color);
        jc.setBounds(x, y, width, height);
        return jc;
    }
    
    // Creates a password JTextField with suitable settings
    public static JTextField createJComponent(int x, int y, int width, int height, boolean pass){
        if(pass){
            JPasswordField jc = new JPasswordField();
            jc.setFont(Utils.font);
            jc.setForeground(Utils.color);
            jc.setBounds(x, y, width, height);
            return jc;
        }
        return Utils.createJComponent(x, y, width, height);
    }
    
    // Creates a common JLabel with suitable settings
    public static JLabel createJComponent(int x, int y, int width, int height, String title){
        JLabel jc = new JLabel(title);
        jc.setFont(Utils.font);
        jc.setForeground(Utils.color);
        jc.setBounds(x, y, width, height);
        return jc;
    }
    
    // Creates a JLabel based on icon with suitable settings
    public static JLabel createJComponent(int x, int y, int width, int height, ImageIcon image){
        JLabel jc = new JLabel();
        jc.setIcon(image);
        jc.setBounds(x, y, width, height);
        return jc;
    }
    
    // Creates a JButton with suitable settings
    public static JButton createJComponent(int x, int y, int width, int height, ImageIcon image, ActionListener al, String ac){
        JButton jc = new JButton(image);
        jc.setOpaque(false);
        jc.setContentAreaFilled(false);
        jc.setBorderPainted(false);
        jc.setBounds(x, y, width, height);
        jc.addActionListener(al);
        jc.setActionCommand(ac);
        jc.setToolTipText(ac.toUpperCase());
        return jc;
    }
}
