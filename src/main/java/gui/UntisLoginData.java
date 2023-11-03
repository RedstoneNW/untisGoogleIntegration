/*
 * Created by JFormDesigner on Mon Oct 30 16:40:10 CET 2023
 */

package gui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.github.windpapi4j.WinAPICallFailedException;
import dataHandlement.LoginDataHandler;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * @author redst
 */
public class UntisLoginData extends JPanel {

    static JFrame frame = new JFrame();

    private final String serverTooltip = "Your webuntis server. If you are not sure what that is leave it blank. Default: herakles";

    public static void main(String[] args) {
        FlatDarkLaf.setup();
        frame.add(new UntisLoginData());
        frame.pack();
        frame.setTitle("Untis Google Calendar Integration");
        frame.setMinimumSize(new Dimension(705,400));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        System.out.println("FINISHED");
    }
    public UntisLoginData() {
        initComponents();
    }

    private void next(ActionEvent e) {
        LoginDataHandler loginDataHandler = new LoginDataHandler();
        String[] credentials = new String[4];
        credentials[0] = username.getText();
        credentials[1] = password.getText();
        credentials[2] = school.getText();
        if (server.getText().isEmpty() || server.getText().equals(serverTooltip)) {
            credentials[3] = "https://herakles.webuntis.com";
        } else {
            credentials[3] = server.getText();
        }
        try {
            loginDataHandler.saveCredentials(credentials);
        } catch (WinAPICallFailedException ex) {
            throw new RuntimeException(ex);
        }
        frame.dispose();
        //TODO open next screen
    }

    private void serverFocusGained(FocusEvent e) {
        if (server.getText().equals(serverTooltip)) {
            server.setText("");
            server.setForeground(Color.white);
        }
    }

    private void serverFocusLost(FocusEvent e) {
        if (server.getText().isEmpty()) {
            server.setText(serverTooltip);
            server.setForeground(Color.gray);
        }
    }

    private class MyDocListener implements DocumentListener
    {

        public void changedUpdate(DocumentEvent e)
        {
            checkFieldsFull();
        }

        public void insertUpdate(DocumentEvent e)
        {
            checkFieldsFull();
        }

        public void removeUpdate(DocumentEvent e)
        {
            checkFieldsFull();
        }

    }

    private void checkFieldsFull()
    {
        next.setEnabled(!username.getText().isEmpty() && !password.getText().isEmpty() && !school.getText().isEmpty());
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - N
        heading1 = new JLabel();
        vSpacer1 = new JPanel(null);
        hSpacer1 = new JPanel(null);
        label1 = new JLabel();
        username = new JTextField();
        label2 = new JLabel();
        password = new JPasswordField();
        label3 = new JLabel();
        school = new JTextField();
        label4 = new JLabel();
        server = new JTextField();
        next = new JButton();

        //======== this ========
        setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing. border.
        EmptyBorder( 0, 0, 0, 0) , "", javax. swing. border. TitledBorder. CENTER, javax. swing
        . border. TitledBorder. BOTTOM, new java .awt .Font ("Dialog" ,java .awt .Font .BOLD ,12 ),
        java. awt. Color. red) , getBorder( )) );  addPropertyChangeListener (new java. beans. PropertyChangeListener( )
        { @Override public void propertyChange (java .beans .PropertyChangeEvent e) {if ("border" .equals (e .getPropertyName () ))
        throw new RuntimeException( ); }} );
        setLayout(new MigLayout(
            "hidemode 3",
            // columns
            "[fill]" +
            "[fill]" +
            "[500,fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]",
            // rows
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]"));


        DocumentListener documentListener = new MyDocListener();
        //---- heading1 ----
        heading1.setText("Your Webuntis Login");
        heading1.setHorizontalAlignment(SwingConstants.CENTER);
        heading1.setFont(heading1.getFont().deriveFont(heading1.getFont().getStyle() | Font.BOLD, heading1.getFont().getSize() + 3f));
        add(heading1, "cell 0 1 37 1");
        add(vSpacer1, "cell 2 2");
        add(hSpacer1, "cell 0 3");

        //---- label1 ----
        label1.setText("Username:");
        add(label1, "cell 1 3");

        //---- username ----
        username.setForeground(Color.white);
        add(username, "cell 2 3 27 1");
        username.getDocument().addDocumentListener(documentListener);

        //---- label2 ----
        label2.setText("Password:");
        add(label2, "cell 1 5");

        //---- password ----
        password.setForeground(Color.white);
        add(password, "cell 2 5 27 1");
        password.getDocument().addDocumentListener(documentListener);

        //---- label3 ----
        label3.setText("School");
        add(label3, "cell 1 10");

        //---- school ----
        school.setForeground(Color.white);
        add(school, "cell 2 10 27 1");
        school.getDocument().addDocumentListener(documentListener);

        //---- label4 ----
        label4.setText("Webuntis Server");
        add(label4, "cell 1 12");

        //---- server ----
        server.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        server.setText("Your webuntis server. If you are not sure what that is leave it blank. Default: herakles");
        server.setForeground(Color.gray);
        server.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                serverFocusGained(e);
            }
            @Override
            public void focusLost(FocusEvent e) {
                serverFocusLost(e);
            }
        });
        add(server, "cell 2 12 27 1");

        //---- next ----
        next.setText("Next");
        next.setEnabled(false);
        next.addActionListener(e -> next(e));
        add(next, "cell 22 32 13 1");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - N
    private JLabel heading1;
    private JPanel vSpacer1;
    private JPanel hSpacer1;
    private JLabel label1;
    private JTextField username;
    private JLabel label2;
    private JPasswordField password;
    private JLabel label3;
    private JTextField school;
    private JLabel label4;
    private JTextField server;
    private JButton next;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
