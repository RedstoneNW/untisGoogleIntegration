/*
 * Created by JFormDesigner on Thu Nov 09 20:54:39 CET 2023
 */

package gui;

import com.formdev.flatlaf.FlatDarkLaf;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;

/**
 * @author redst
 */
public class StartPage extends JPanel {
    private static JFrame frame = new JFrame();
    public static void main(String[] args) {

        FlatDarkLaf.setup();
        frame.add(new StartPage());
        frame.pack();
        frame.setTitle("Untis Google Calendar Integration");
        frame.setMinimumSize(new Dimension(705,400));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        System.out.println("FINISHED");
    }
    public StartPage() {
        initComponents();
        System.setOut(new PrintStream(System.out) {
            public void println(String s) {
                addLog(s);
            }
        });
    }

    private void textField1(ActionEvent e) {
        System.out.println(textField1.getText());
        textField1.setText("");
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - N
        label1 = new JLabel();
        label2 = new JLabel();
        textField1 = new JTextField();
        scrollPane1 = new JScrollPane();
        textArea1 = new JTextArea();

        //======== this ========
        setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder
        (0,0,0,0), "JF\u006frmDesi\u0067ner Ev\u0061luatio\u006e",javax.swing.border.TitledBorder.CENTER,javax.swing.border
        .TitledBorder.BOTTOM,new java.awt.Font("Dialo\u0067",java.awt.Font.BOLD,12),java.awt
        .Color.red), getBorder())); addPropertyChangeListener(new java.beans.PropertyChangeListener(){@Override public void
        propertyChange(java.beans.PropertyChangeEvent e){if("borde\u0072".equals(e.getPropertyName()))throw new RuntimeException()
        ;}});
        setLayout(new MigLayout(
            "hidemode 3",
            // columns
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
            "[30]"));

        //---- label1 ----
        label1.setText("Untis Google Calendar Integration");
        add(label1, "cell 8 0");

        //---- label2 ----
        label2.setText("Last update log");
        add(label2, "cell 0 1");

        //---- textField1 ----
        textField1.addActionListener(e -> textField1(e));
        add(textField1, "cell 11 1 3 1");

        //======== scrollPane1 ========
        {
            scrollPane1.setMinimumSize(new Dimension(16, 500));

            //---- textArea1 ----
            textArea1.setMinimumSize(new Dimension(1, 50));
            textArea1.setEditable(false);
            scrollPane1.setViewportView(textArea1);
        }
        add(scrollPane1, "cell 0 2 15 3");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    public void addLog(String pLogentry) {
        if (textArea1.getText().isEmpty()) {
            textArea1.insert(pLogentry,0);
        } else {
            textArea1.insert(pLogentry + "\n",0);
        }
        textArea1.setCaretPosition(0);
        try
        {
            String filename= "./log.txt";
            FileWriter fw = new FileWriter(filename,true); //the true will append the new data
            fw.write(pLogentry + "\n");//appends the string to the file
            fw.close();
        }
        catch(IOException ioe)
        {
            System.err.println("IOException: " + ioe.getMessage());
        }

    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - N
    private JLabel label1;
    private JLabel label2;
    private JTextField textField1;
    private JScrollPane scrollPane1;
    private JTextArea textArea1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
